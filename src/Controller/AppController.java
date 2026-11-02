package Controller;

import GUI.AdminUI.AdminGUI;
import GUI.AdminUI.AdminStock;
import GUI.Components.AdminProducts;
import GUI.Components.Table;
import GUI.Components.pnlBorder;
import GUI.EachDrink;
import GUI.MainWindow;
import GUI.Menu;
import GUI.MenuComponent.DrinkGUI;
import GUI.MenuComponent.ToppingsButton;
import GUI.MenuComponent.TypeOfDrinkButton;
import GUI.MenuComponent.SweetnessLevelButton;
import GUI.PaymentGUI.PaymentChangeWindow;
import GUI.PaymentGUI.PaymentInsertWindow;
import GUI.PaymentGUI.PaymentMainWindow;
import GUI.PaymentGUI.PaymentQrWindow;
import GUI.PaymentGUI.PaymentSuccess;
import Model.Card;
import Model.Finance;
import Model.Product;
import Model.Stock;
import db.DBModel;
import internal.Login;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import util.ProductImages;

/**
 * Main controller of the vending machine.
 *
 * Wires every screen together (CardLayout inside MainWindow), handles the
 * customer flow (category -> drink -> options -> payment) and the admin flow
 * (login -> dashboard -> stock management).
 */
public class AppController implements ActionListener {

    // Demo admin account (see README)
    private static final String ADMIN_USERNAME = "madara";
    private static final String ADMIN_PASSWORD = "55555";

    private static final String RECOMMEND = "Recommend";
    private static final int RECOMMEND_LIMIT = 6;
    private static final int TOPPING_PRICE = 5;
    private static final int CHANGE_COUNTDOWN_SECONDS = 5;
    private static final int SUCCESS_COUNTDOWN_SECONDS = 10;
    private static final int IDLE_TIMEOUT_MS = 60_000;

    // Colors
    private static final Color CATEGORY_IDLE = Color.decode("#F6E6E6");
    private static final Color CATEGORY_ACTIVE = Color.decode("#CC8484");
    private static final Color OPTION_IDLE = Color.decode("#906F76");
    private static final Color OPTION_ACTIVE = Color.decode("#F6E6E6");
    private static final Color DRINK_CARD = Color.decode("#C59E7E");
    private static final Color DRINK_CARD_HOVER = Color.decode("#B09393");
    private static final Color OPTION_HOVER = Color.decode("#A88A90");
    private static final Color BACK_HOVER = Color.decode("#E8B4B4");

    private final DBModel db = new DBModel();

    private final MainWindow mw = new MainWindow();
    private final Login login = new Login();
    private final Menu menu = new Menu();
    private final EachDrink eachdrink = new EachDrink();
    private final PaymentMainWindow paymentmain = new PaymentMainWindow();
    private final PaymentChangeWindow paymentchange = new PaymentChangeWindow();
    private final PaymentInsertWindow paymentinsert = new PaymentInsertWindow();
    private final PaymentQrWindow paymentqr = new PaymentQrWindow();
    private final PaymentSuccess paymentsuccess = new PaymentSuccess();
    private final AdminGUI adminGUI = new AdminGUI();
    private AdminStock stock;

    private final ImageIcon balanceIcon = new ImageIcon(getClass().getResource("/GUI/icons/coins-solid 3.png"));
    private final ImageIcon totalIcon = new ImageIcon(getClass().getResource("/GUI/icons/Vector3.png"));

    // Drink being ordered (a copy) and the stored product it came from
    private Product choose;
    private Product chosenProduct;
    private int typeOfDrinkPrice = 0;
    private int toppingsPrice = 0;
    private pnlBorder selectedType;
    private pnlBorder selectedSweetness;
    private String paymentMethod = "";
    private double receivedCash;

    // Option buttons -> their text label (and price for drink types)
    private final Map<pnlBorder, JLabel> typeButtons = new LinkedHashMap<>();
    private final Map<pnlBorder, Integer> typePrices = new LinkedHashMap<>();
    private final Map<pnlBorder, JLabel> sweetnessButtons = new LinkedHashMap<>();
    private final Map<pnlBorder, JLabel> toppingButtons = new LinkedHashMap<>();
    private final Map<pnlBorder, Boolean> toppingSelected = new LinkedHashMap<>();

    private Timer changeTimer;
    private Timer successTimer;
    private Timer idleTimer;

    // Image chosen in the admin stock screen (not yet saved)
    private File sourceFile;
    private String fName = "";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppController::new);
    }

    public AppController() {
        db.loadFile();

        registerOptionButtons();
        registerNavigation();
        registerHoverEffects();

        mw.setLayout(new CardLayout());
        mw.add(adminGUI);
        mw.getPnlDesktop().add(login);
        mw.add(menu);
        mw.add(eachdrink);
        mw.add(paymentmain);
        mw.add(paymentchange);
        mw.add(paymentinsert);
        mw.add(paymentqr);
        mw.add(paymentsuccess);

        menu.setVisible(false);
        eachdrink.setVisible(false);
        paymentmain.setVisible(false);
        paymentchange.setVisible(false);
        paymentinsert.setVisible(false);
        paymentqr.setVisible(false);
        paymentsuccess.setVisible(false);
        adminGUI.setVisible(false);

        mw.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // "Balance" is today's income, it starts from 0 on every run
                db.getFinance().setBalance(0);
                db.saveFile();
            }
        });

        adminGUI.getAdminControl().getBalance().setData(new Card(balanceIcon, "  Today.", "", "description"));
        adminGUI.getAdminControl().getTotally().setData(new Card(totalIcon, "  All time.", "", "description"));
        adminGUI.getAdminControl().getTable().setNumericColumns(0);
        refreshAdminDashboard();
        startIdleWatcher();

        mw.setTitle("TAO NGOI - Drink Vending Machine");
        mw.setVisible(true);
    }

    // ------------------------------------------------------------------
    // Wiring
    // ------------------------------------------------------------------

    private void registerOptionButtons() {
        TypeOfDrinkButton type = eachdrink.getTypeOfDrink().getTypeOfDrinkButton();
        addType(type.getHotButton(), type.getHotText(), 0);
        addType(type.getIcedButton(), type.getIcedText(), 0);
        addType(type.getIced5Button(), type.getIced5Text(), 5);
        addType(type.getSmoothie5Button(), type.getSmoothie5Text(), 5);
        addType(type.getSmoothie10Button(), type.getSmoothie10Text(), 10);

        SweetnessLevelButton sweet = eachdrink.getSweetnessLevel().getSweetnessLevelButton();
        sweetnessButtons.put(sweet.getButton0(), sweet.getText0());
        sweetnessButtons.put(sweet.getButton25(), sweet.getText25());
        sweetnessButtons.put(sweet.getButton50(), sweet.getText50());
        sweetnessButtons.put(sweet.getButton75(), sweet.getText75());
        sweetnessButtons.put(sweet.getButton100(), sweet.getText100());

        ToppingsButton top = eachdrink.getToppings().getToppingsButton();
        toppingButtons.put(top.getBubbleButton(), top.getBubbleText());
        toppingButtons.put(top.getWhipCreamButton(), top.getWhipCreamText());
        toppingButtons.put(top.getMilkFoamButton(), top.getMilkFoamText());
        toppingButtons.put(top.getOreoButton(), top.getOreoText());
        toppingButtons.put(top.getJellyButton(), top.getJellyText());
        toppingButtons.put(top.getYoghurtButton(), top.getYoghurtText());
        toppingButtons.put(top.getCookieButton(), top.getCookieText());
        toppingButtons.put(top.getSnackButton(), top.getSnackText());
        toppingButtons.put(top.getChocolateSauceButton(), top.getChocolateSauceText());

        typeButtons.keySet().forEach(btn -> onClick(btn, () -> chooseTypeOfDrink(btn)));
        sweetnessButtons.keySet().forEach(btn -> onClick(btn, () -> chooseSweetnessLevel(btn)));
        toppingButtons.keySet().forEach(btn -> onClick(btn, () -> toggleTopping(btn)));

        // light up option buttons that are not selected when the mouse is over them
        for (Map<pnlBorder, JLabel> group : List.of(typeButtons, sweetnessButtons, toppingButtons)) {
            for (pnlBorder btn : group.keySet()) {
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (btn.getBackground().equals(OPTION_IDLE)) {
                            btn.setBackground(OPTION_HOVER);
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (btn.getBackground().equals(OPTION_HOVER)) {
                            btn.setBackground(OPTION_IDLE);
                        }
                    }
                });
            }
        }
    }

    private void addType(pnlBorder btn, JLabel text, int price) {
        typeButtons.put(btn, text);
        typePrices.put(btn, price);
    }

    private void registerNavigation() {
        // Home screen
        onClick(mw.getPnlLogin(), () -> {
            login.setVisible(true);
            login.getTfUsername().requestFocusInWindow();
        });
        mw.getPnlLogin().setToolTipText("Admin login");
        onClick(mw.getPnlRecommend(), () -> chooseProduct(RECOMMEND));
        onClick(mw.getPnlCoffee(), () -> chooseProduct("Coffee"));
        onClick(mw.getPnlTea(), () -> chooseProduct("Tea"));
        onClick(mw.getPnlMilk(), () -> chooseProduct("Milk & Coco"));
        onClick(mw.getPnlJuice(), () -> chooseProduct("Juice"));
        onClick(mw.getPnlSoda(), () -> chooseProduct("Soda"));
        mw.getPnlContainer().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                login.setVisible(false);
            }
        });
        login.getBtnLogin().addActionListener(this);
        // press Enter in either field to log in
        login.getTfUsername().addActionListener(e -> login.getBtnLogin().doClick());
        login.getjPasswordField1().addActionListener(e -> login.getBtnLogin().doClick());

        // Menu
        onClick(menu.getBackButton(), () -> show(mw.getPnlContainer()));
        onClick(menu.getCatagoryGUI().getRecommendButton(), () -> chooseProduct(RECOMMEND));
        onClick(menu.getCatagoryGUI().getCoffeeButton(), () -> chooseProduct("Coffee"));
        onClick(menu.getCatagoryGUI().getTeaButton(), () -> chooseProduct("Tea"));
        onClick(menu.getCatagoryGUI().getMilkAndCocoaButton(), () -> chooseProduct("Milk & Coco"));
        onClick(menu.getCatagoryGUI().getJuiceButton(), () -> chooseProduct("Juice"));
        onClick(menu.getCatagoryGUI().getSodaButton(), () -> chooseProduct("Soda"));

        // Drink detail
        onClick(eachdrink.getBackButton(), () -> show(menu));
        onClick(eachdrink.getPurchasingButton(), this::goToPayment);

        // Payment method
        onClick(paymentmain.getBackButton(), () -> show(eachdrink));
        onClick(paymentmain.getButtonCancel(), () -> show(eachdrink));
        onClick(paymentmain.getButtonCash(), () -> {
            paymentMethod = "Cash";
            paymentinsert.getTotal().setText(String.format("Total : %.2f ฿", currentTotal()));
            show(paymentinsert);
        });
        onClick(paymentmain.getButtonQrCode(), () -> showQr("QR Code"));
        onClick(paymentmain.getButtonTrue(), () -> showQr("TrueMoney Wallet"));

        // QR code / TrueMoney
        onClick(paymentqr.getBackButton(), () -> show(paymentmain));
        onClick(paymentqr.getButtonCancel(), () -> show(paymentmain));
        onClick(paymentqr.getButtonConfirm(), () -> {
            receivedCash = 0;
            recordSale();
            showSuccess();
        });

        // Cash
        onClick(paymentinsert.getBackButton(), () -> show(paymentmain));
        onClick(paymentinsert.getButtonCancel(), () -> show(paymentmain));
        onClick(paymentinsert.getButtonConfirm(), this::confirmCashPayment);
        for (JComponent note : paymentinsert.getQuickAmountButtons()) {
            int amount = (int) note.getClientProperty("amount");
            onClick(note, () -> insertCash(amount));
            hover(note, "#E8C9C9", "#F6E6E6");
        }
        onClick(paymentinsert.getClearButton(), () -> paymentinsert.getTextInsert().setText(""));
        hover(paymentinsert.getClearButton(), "#D0D0D0", "#E6E6E6");
        paymentinsert.getTextInsert().addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                    paymentinsert.getToolkit().beep();
                    e.consume();
                }
            }
        });
        paymentinsert.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                paymentinsert.getTextInsert().setText("");
            }
        });

        // Change
        paymentchange.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                startChangeCountdown();
            }
        });
        onClick(paymentchange.getButtonConfirm(), () -> {
            // the button is a panel, so "disabled" does not block mouse events
            if (paymentchange.getButtonConfirm().isEnabled()) {
                recordSale();
                showSuccess();
            }
        });

        // Success
        onClick(paymentsuccess.getButtonConfirm(), () -> show(mw.getPnlContainer()));
        paymentsuccess.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                if (successTimer != null) {
                    successTimer.stop();
                }
            }
        });

        // Admin
        onClick(adminGUI.getNavbar().getBackButton(), () -> show(mw.getPnlContainer()));
        onClick(adminGUI.getAdminControl().getStock(), this::openStockWindow);
    }

    private void registerHoverEffects() {
        hover(mw.getPnlLogin(), "#FFFFFF", "#F6E6E6");
        for (JComponent c : Arrays.asList(mw.getPnlRecommend(), mw.getPnlCoffee(), mw.getPnlTea(),
                mw.getPnlMilk(), mw.getPnlJuice(), mw.getPnlSoda())) {
            hover(c, "#F6E6E6", "#C59E7E");
        }
        hover(paymentmain.getButtonCash(), "#7B6565", "#564747");
        hover(paymentmain.getButtonQrCode(), "#7B6565", "#564747");
        hover(paymentmain.getButtonTrue(), "#7B6565", "#564747");
        hover(paymentmain.getButtonCancel(), "#9F5151", "#A32B2B");
        hover(paymentqr.getButtonCancel(), "#9F5151", "#A32B2B");
        hover(paymentqr.getButtonConfirm(), "#DAC1AC", "#C59E7E");
        hover(paymentinsert.getButtonCancel(), "#9F5151", "#A32B2B");
        hover(paymentinsert.getButtonConfirm(), "#DAC1AC", "#C59E7E");
        hover(paymentsuccess.getButtonConfirm(), "#DAC1AC", "#C59E7E");
        for (JLabel back : Arrays.asList(menu.getBackButton(), eachdrink.getBackButton(), paymentmain.getBackButton(),
                paymentqr.getBackButton(), paymentinsert.getBackButton(), adminGUI.getNavbar().getBackButton())) {
            Color normal = back.getForeground();
            back.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    back.setForeground(BACK_HOVER);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    back.setForeground(normal);
                }
            });
        }
        paymentchange.getButtonConfirm().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (paymentchange.getButtonConfirm().isEnabled()) {
                    paymentchange.getButtonConfirm().setBackground(Color.decode("#DAC1AC"));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (paymentchange.getButtonConfirm().isEnabled()) {
                    paymentchange.getButtonConfirm().setBackground(Color.decode("#C59E7E"));
                }
            }
        });
    }

    /** Runs {@code action} when the component is clicked and shows a hand cursor on it. */
    private static void onClick(JComponent c, Runnable action) {
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                action.run();
            }
        });
    }

    private static void hover(JComponent c, String hoverColor, String normalColor) {
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                c.setBackground(Color.decode(hoverColor));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                c.setBackground(Color.decode(normalColor));
            }
        });
    }

    /** Hides every screen and shows only {@code screen}. */
    private void show(JComponent screen) {
        for (JComponent c : Arrays.asList(mw.getPnlContainer(), adminGUI, menu, eachdrink, paymentmain,
                paymentchange, paymentinsert, paymentqr, paymentsuccess)) {
            c.setVisible(c == screen);
        }
    }

    // ------------------------------------------------------------------
    // Customer flow
    // ------------------------------------------------------------------

    public void chooseProduct(String category) {
        Map<String, pnlBorder> categoryButtons = new LinkedHashMap<>();
        categoryButtons.put(RECOMMEND, menu.getCatagoryGUI().getRecommendButton());
        categoryButtons.put("Coffee", menu.getCatagoryGUI().getCoffeeButton());
        categoryButtons.put("Tea", menu.getCatagoryGUI().getTeaButton());
        categoryButtons.put("Milk & Coco", menu.getCatagoryGUI().getMilkAndCocoaButton());
        categoryButtons.put("Juice", menu.getCatagoryGUI().getJuiceButton());
        categoryButtons.put("Soda", menu.getCatagoryGUI().getSodaButton());
        categoryButtons.forEach((name, btn) -> btn.setBackground(name.equals(category) ? CATEGORY_ACTIVE : CATEGORY_IDLE));

        List<Product> drinks;
        if (category.equals(RECOMMEND)) {
            // best sellers first
            drinks = db.getProducts().stream()
                    .filter(p -> p.getPopulation() > 0)
                    .sorted(Comparator.comparingInt(Product::getPopulation).reversed())
                    .limit(RECOMMEND_LIMIT)
                    .collect(Collectors.toList());
        } else {
            drinks = db.getProducts().stream()
                    .filter(p -> p.getCategory().equals(category))
                    .collect(Collectors.toList());
        }

        menu.getPnlMenu().removeAll();
        for (Product product : drinks) {
            DrinkGUI card = new DrinkGUI(product.getProductName(), String.format("%.0f ฿", product.getPrice()),
                    ProductImages.load(product.getImg()));
            onClick(card, () -> openDrink(product));
            card.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    card.setBackground(DRINK_CARD_HOVER);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    card.setBackground(DRINK_CARD);
                }
            });
            menu.getPnlMenu().add(card);
        }
        if (drinks.isEmpty()) {
            JLabel empty = new JLabel(category.equals(RECOMMEND)
                    ? "<html><center>No best sellers yet.<br>Pick a category and be the first!</center></html>"
                    : "No drinks in this category yet.", JLabel.CENTER);
            empty.setFont(empty.getFont().deriveFont(java.awt.Font.BOLD, 24f));
            empty.setForeground(Color.WHITE);
            empty.setPreferredSize(new Dimension(680, 300));
            menu.getPnlMenu().add(empty);
        }
        menu.getPnlMenu().setPreferredSize(new Dimension(720, (int) (Math.max(1, Math.ceil(drinks.size() / 3.0)) * 280)));
        menu.getPnlMenu().revalidate();
        menu.getPnlMenu().repaint();
        menu.getjScrollPane().getVerticalScrollBar().setValue(0);

        show(menu);
    }

    private void openDrink(Product product) {
        chosenProduct = product;
        // copy, so the options never change the stored product
        choose = new Product(product.getId(), product.getProductName(), product.getPrice(),
                product.getCategory(), product.getImg(), 0);
        typeOfDrinkPrice = 0;
        toppingsPrice = 0;

        eachdrink.set(choose.getImg(), choose.getProductName(), choose.getCategory(), choose.getPrice());
        showOptionsFor(choose.getCategory());
        show(eachdrink);
    }

    /** Each category offers different drink types and toppings. */
    private void showOptionsFor(String category) {
        TypeOfDrinkButton type = eachdrink.getTypeOfDrink().getTypeOfDrinkButton();
        ToppingsButton top = eachdrink.getToppings().getToppingsButton();

        List<pnlBorder> types;
        List<pnlBorder> toppings;
        switch (category) {
            case "Coffee":
                types = List.of(type.getHotButton(), type.getIced5Button(), type.getSmoothie10Button());
                toppings = List.of(top.getWhipCreamButton(), top.getMilkFoamButton(), top.getChocolateSauceButton());
                break;
            case "Milk & Coco":
                types = List.of(type.getHotButton(), type.getIced5Button(), type.getSmoothie10Button());
                toppings = List.of(top.getBubbleButton(), top.getWhipCreamButton(), top.getOreoButton(), top.getCookieButton());
                break;
            case "Tea":
                types = List.of(type.getIcedButton(), type.getSmoothie5Button());
                toppings = List.of(top.getBubbleButton(), top.getWhipCreamButton(), top.getJellyButton(), top.getYoghurtButton());
                break;
            case "Juice":
                types = List.of(type.getIcedButton(), type.getSmoothie5Button());
                toppings = List.of(top.getBubbleButton(), top.getWhipCreamButton(), top.getSnackButton());
                break;
            default: // Soda
                types = List.of(type.getIcedButton());
                toppings = List.of(top.getBubbleButton(), top.getWhipCreamButton(), top.getJellyButton());
                break;
        }

        type.removeAll();
        types.forEach(type::add);
        top.removeAll();
        toppings.forEach(top::add);

        toppingButtons.forEach((btn, text) -> {
            toppingSelected.put(btn, false);
            paintOption(btn, text, false);
        });
        chooseTypeOfDrink(types.get(0));
        chooseSweetnessLevel(eachdrink.getSweetnessLevel().getSweetnessLevelButton().getButton50());
    }

    private void chooseTypeOfDrink(pnlBorder selected) {
        selectedType = selected;
        typeOfDrinkPrice = typePrices.get(selected);
        typeButtons.forEach((btn, text) -> paintOption(btn, text, btn == selected));
        updatePriceLabel();
    }

    private void chooseSweetnessLevel(pnlBorder selected) {
        selectedSweetness = selected;
        sweetnessButtons.forEach((btn, text) -> paintOption(btn, text, btn == selected));
        eachdrink.repaint();
    }

    private void toggleTopping(pnlBorder btn) {
        boolean selected = !toppingSelected.getOrDefault(btn, false);
        toppingSelected.put(btn, selected);
        toppingsPrice += selected ? TOPPING_PRICE : -TOPPING_PRICE;
        paintOption(btn, toppingButtons.get(btn), selected);
        updatePriceLabel();
    }

    private void paintOption(pnlBorder btn, JLabel text, boolean selected) {
        btn.setBackground(selected ? OPTION_ACTIVE : OPTION_IDLE);
        text.setForeground(selected ? Color.BLACK : Color.WHITE);
    }

    private double currentTotal() {
        return choose.getPrice() + typeOfDrinkPrice + toppingsPrice;
    }

    private void updatePriceLabel() {
        eachdrink.getPurchasingButton().getPriceText().setText(String.format("Purchasing | %.2f ฿", currentTotal()));
        eachdrink.revalidate();
        eachdrink.repaint();
    }

    private void goToPayment() {
        paymentmain.getProductImg().setIcon(ProductImages.load(choose.getImg()));
        paymentmain.setOrder(choose.getProductName(), orderDetails(), currentTotal());
        show(paymentmain);
    }

    /** e.g. "Iced · Sweetness 50% · Whip cream, Milk Foam" */
    private String orderDetails() {
        StringBuilder sb = new StringBuilder();
        if (selectedType != null) {
            // "Iced(+฿5)" -> "Iced"
            sb.append(typeButtons.get(selectedType).getText().replaceAll("\\(.*\\)", "").trim());
        }
        if (selectedSweetness != null) {
            sb.append(" · Sweetness ").append(sweetnessButtons.get(selectedSweetness).getText().trim());
        }
        String toppings = toppingSelected.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(e -> toppingButtons.get(e.getKey()).getText().trim())
                .collect(Collectors.joining(", "));
        if (!toppings.isEmpty()) {
            sb.append(" · ").append(toppings);
        }
        return sb.toString();
    }

    private void showQr(String method) {
        paymentMethod = method;
        paymentqr.setTotal(currentTotal());
        show(paymentqr);
    }

    /** Adds a banknote to the amount typed in the cash screen. */
    private void insertCash(int amount) {
        long current = 0;
        try {
            current = Long.parseLong(paymentinsert.getTextInsert().getText().trim());
        } catch (NumberFormatException ignored) {
        }
        paymentinsert.getTextInsert().setText(String.valueOf(current + amount));
    }

    /** Shows the receipt and goes back home by itself after a few seconds. */
    private void showSuccess() {
        double total = currentTotal();
        List<String[]> lines = new ArrayList<>();
        lines.add(new String[]{choose.getProductName(), String.format("%.2f ฿", choose.getPrice())});
        String details = orderDetails();
        if (typeOfDrinkPrice + toppingsPrice > 0) {
            lines.add(new String[]{"Extras", String.format("+%d ฿", typeOfDrinkPrice + toppingsPrice)});
        }
        lines.add(new String[]{"<font color='#8C787D'>" + details + "</font>", ""});
        lines.add(new String[]{"Total", String.format("%.2f ฿", total)});
        lines.add(new String[]{"Paid by", paymentMethod});
        if (receivedCash > 0) {
            lines.add(new String[]{"Change", String.format("%.2f ฿", receivedCash - total)});
        }
        paymentsuccess.setReceipt(lines.toArray(new String[0][]));
        show(paymentsuccess);

        if (successTimer != null) {
            successTimer.stop();
        }
        int[] secondsLeft = {SUCCESS_COUNTDOWN_SECONDS};
        paymentsuccess.getCountdown().setText("Returning to home in " + secondsLeft[0] + " s");
        successTimer = new Timer(1000, e -> {
            secondsLeft[0]--;
            if (secondsLeft[0] > 0) {
                paymentsuccess.getCountdown().setText("Returning to home in " + secondsLeft[0] + " s");
            } else {
                successTimer.stop();
                show(mw.getPnlContainer());
            }
        });
        successTimer.start();
    }

    /**
     * Goes back to the home screen when a customer walks away in the middle of
     * an order. Not used on the change/receipt screens (money is involved) or
     * in the admin pages.
     */
    private void startIdleWatcher() {
        idleTimer = new Timer(IDLE_TIMEOUT_MS, e -> {
            for (JComponent screen : Arrays.asList(menu, eachdrink, paymentmain, paymentinsert, paymentqr)) {
                if (screen.isVisible()) {
                    show(mw.getPnlContainer());
                    return;
                }
            }
        });
        idleTimer.setRepeats(false);
        java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(e -> idleTimer.restart(),
                java.awt.AWTEvent.MOUSE_EVENT_MASK | java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK | java.awt.AWTEvent.KEY_EVENT_MASK);
        idleTimer.start();
    }

    private void confirmCashPayment() {
        String text = paymentinsert.getTextInsert().getText().trim();
        double received;
        try {
            received = Double.parseDouble(text);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mw, "Please insert money.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double total = currentTotal();
        if (received < total) {
            JOptionPane.showMessageDialog(mw, String.format("Not enough money. Total is %.2f ฿", total),
                    "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        receivedCash = received;
        paymentchange.getReceived().setText(String.format("Received amount : %.2f ฿", received));
        paymentchange.getChange().setText(String.format("Change : %.2f ฿", received - total));
        show(paymentchange);
    }

    /** Counts down without blocking the UI thread, then enables "continue". */
    private void startChangeCountdown() {
        if (changeTimer != null) {
            changeTimer.stop();
        }
        paymentchange.getButtonConfirm().setEnabled(false);
        paymentchange.getButtonConfirm().setVisible(false);
        paymentchange.getButtonConfirm().setBackground(new Color(246, 230, 230));

        int[] secondsLeft = {CHANGE_COUNTDOWN_SECONDS};
        paymentchange.getLabel3().setText("The machine is about to change money in " + secondsLeft[0]);
        changeTimer = new Timer(1000, e -> {
            secondsLeft[0]--;
            if (secondsLeft[0] > 0) {
                paymentchange.getLabel3().setText("The machine is about to change money in " + secondsLeft[0]);
            } else {
                changeTimer.stop();
                paymentchange.getLabel3().setText("Please click continue...");
                paymentchange.getButtonConfirm().setBackground(new Color(197, 158, 126));
                paymentchange.getButtonConfirm().setEnabled(true);
                paymentchange.getButtonConfirm().setVisible(true);
            }
        });
        changeTimer.start();
    }

    /** Adds the sale to today's balance and the overall total, then saves. */
    private void recordSale() {
        double total = currentTotal();
        Finance finance = db.getFinance();
        finance.setBalance(finance.getBalance() + total);
        finance.setTotally(finance.getTotally() + total);

        chosenProduct.increasePopulation();
        System.out.printf("[Purchased!] %s %.2f ฿ | today %.2f ฿, total %.2f ฿%n",
                choose.getProductName(), total, finance.getBalance(), finance.getTotally());

        db.saveFile();
        refreshAdminDashboard();
    }

    // ------------------------------------------------------------------
    // Admin
    // ------------------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent ae) {
        Object src = ae.getSource();
        if (src == login.getBtnLogin()) {
            login();
        } else if (stock != null) {
            AdminProducts form = stock.getAdminProducts();
            if (src == form.getBtnAdd()) {
                addProduct(form);
            } else if (src == form.getBtnUpdate()) {
                updateProduct(form);
            } else if (src == form.getBtnDelete()) {
                deleteProduct(form);
            } else if (src == form.getBtnImage()) {
                chooseImage(form);
            }
        }
    }

    private void login() {
        String username = login.getTfUsername().getText();
        String password = new String(login.getjPasswordField1().getPassword());

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            login.getTfUsername().setText("");
            login.getjPasswordField1().setText("");
            login.setVisible(false);
            refreshAdminDashboard();
            show(adminGUI);
        } else if (username.equals(ADMIN_USERNAME)) {
            JOptionPane.showMessageDialog(mw, "Wrong Password", "Wrong Password", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mw, "Wrong Username", "Wrong Username", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Refreshes balance, total, number of products and the selling table. */
    private void refreshAdminDashboard() {
        Finance finance = db.getFinance();
        adminGUI.getAdminControl().getBalance().getLblValue().setText(String.format("      %.2f ฿", finance.getBalance()));
        adminGUI.getAdminControl().getTotally().getLblValue().setText(String.format("      %.2f ฿", finance.getTotally()));
        adminGUI.getAdminControl().getStock().sendData(new Stock(db.getProducts().size()));

        Table table = adminGUI.getAdminControl().getTable();
        table.getTableModel().setRowCount(0);
        for (Product p : db.getProducts()) {
            table.getTableModel().addRow(new Object[]{p.getPopulation(), p.getImg(), p.getProductName(), p.getCategory()});
        }
        // best sellers on top
        if (table.getRowSorter() != null) {
            table.getRowSorter().setSortKeys(List.of(new javax.swing.RowSorter.SortKey(0, javax.swing.SortOrder.DESCENDING)));
        }
    }

    private void openStockWindow() {
        if (stock != null) {
            stock.dispose();
        }
        stock = new AdminStock();
        stock.setTitle("TAO NGOI - Products");
        AdminProducts form = stock.getAdminProducts();
        form.getTable().setNumericColumns(0, 3);
        form.getBtnAdd().addActionListener(this);
        form.getBtnUpdate().addActionListener(this);
        form.getBtnDelete().addActionListener(this);
        form.getBtnImage().addActionListener(this);
        clearImageSelection(form);

        for (Product p : db.getProducts()) {
            form.getTable().getTableModel().addRow(productRow(p));
        }

        form.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = form.getTable().getSelectedRow();
                if (row < 0) {
                    return;
                }
                form.getTfName().setText(form.getTable().getValueAt(row, 2).toString());
                form.getTfPrice().setText(form.getTable().getValueAt(row, 3).toString());
                form.getCategory().setSelectedItem(form.getTable().getValueAt(row, 4));
                sourceFile = null;
                fName = "";
                form.getLblFile().setText(shorten(form.getTable().getValueAt(row, 1).toString(), 20));
            }
        });
    }

    private static Object[] productRow(Product p) {
        return new Object[]{p.getId(), p.getImg(), p.getProductName(), p.getPrice(), p.getCategory(), new JButton("Delete")};
    }

    /** Reads name/price from the form, or shows an error and returns null. */
    private Double readPrice(AdminProducts form) {
        if (form.getTfName().getText().isBlank()) {
            JOptionPane.showMessageDialog(stock, "Please enter a product name.", "Error", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
            double price = Double.parseDouble(form.getTfPrice().getText().trim());
            if (price > 0) {
                return price;
            }
        } catch (NumberFormatException ignored) {
        }
        JOptionPane.showMessageDialog(stock, "Price must be a number greater than 0.", "Error", JOptionPane.WARNING_MESSAGE);
        return null;
    }

    private void addProduct(AdminProducts form) {
        Double price = readPrice(form);
        if (price == null) {
            return;
        }
        if (sourceFile == null) {
            JOptionPane.showMessageDialog(stock, "Please choose an image.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        copyImage();

        Product p = new Product(db.getNewId(), form.getTfName().getText().trim(), price,
                form.getCategory().getSelectedItem().toString(), fName, 0);
        db.addProduct(p);
        db.saveFile();

        form.getTable().getTableModel().addRow(productRow(p));
        clearForm(form);
        refreshAdminDashboard();
    }

    private void updateProduct(AdminProducts form) {
        Table table = form.getTable();
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(stock, "Please select a product.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Double price = readPrice(form);
        if (price == null) {
            return;
        }
        // the table can be sorted, so convert to the model row first
        int modelRow = table.convertRowIndexToModel(row);
        Product p = db.findById((int) table.getTableModel().getValueAt(modelRow, 0));
        if (p == null) {
            return;
        }
        p.setProductName(form.getTfName().getText().trim());
        p.setPrice(price);
        p.setCategory(form.getCategory().getSelectedItem().toString());
        if (sourceFile != null) {
            copyImage();
            p.setImg(fName);
        }
        db.saveFile();

        Object[] values = productRow(p);
        for (int col = 0; col < values.length - 1; col++) {
            table.getTableModel().setValueAt(values[col], modelRow, col);
        }
        clearImageSelection(form);
        refreshAdminDashboard();
    }

    private void deleteProduct(AdminProducts form) {
        Table table = form.getTable();
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(stock, "Please select a product.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int id = (int) table.getTableModel().getValueAt(modelRow, 0);
        String name = table.getTableModel().getValueAt(modelRow, 2).toString();
        int answer = JOptionPane.showConfirmDialog(stock, "Delete \"" + name + "\" ?", "Delete", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        db.removeById(id);
        db.saveFile();
        table.getTableModel().removeRow(modelRow);
        clearForm(form);
        refreshAdminDashboard();
    }

    private void chooseImage(AdminProducts form) {
        JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
        fc.setAcceptAllFileFilterUsed(false);
        if (fc.showOpenDialog(stock) == JFileChooser.APPROVE_OPTION) {
            sourceFile = fc.getSelectedFile();
            fName = sourceFile.getName();
            form.getLblFile().setText(shorten(fName, 20));
        }
    }

    private void clearImageSelection(AdminProducts form) {
        sourceFile = null;
        fName = "";
        form.getLblFile().setText("");
    }

    private void clearForm(AdminProducts form) {
        form.getTfName().setText("");
        form.getTfPrice().setText("");
        form.getTable().clearSelection();
        clearImageSelection(form);
    }

    /** Copies the chosen image into src/products so the menu can show it. */
    private void copyImage() {
        File directory = new File(ProductImages.UPLOAD_DIR);
        directory.mkdirs();
        File destination = new File(directory, fName);
        if (destination.exists()) {
            return;
        }
        try {
            Files.copy(sourceFile.toPath(), destination.toPath());
        } catch (IOException e) {
            System.err.println("Cannot copy image: " + e);
        }
    }

    private static String shorten(String text, int max) {
        if (text == null) {
            return "";
        }
        if (text.length() <= max) {
            return text;
        }
        if (max <= 5) {
            return text.substring(0, max);
        }
        return text.substring(0, max - 2) + "...";
    }
}
