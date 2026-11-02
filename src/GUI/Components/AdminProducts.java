package GUI.Components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

public class AdminProducts extends javax.swing.JPanel implements ActionListener {
    public AdminProducts() {
        initComponents();
        setOpaque(false);
        setBackground(Color.WHITE);

        // some look and feels (e.g. macOS) ignore the button background, which
        // makes the white text invisible, so paint the background ourselves
        for (javax.swing.JButton b : new javax.swing.JButton[]{btnAdd, btnUpdate, btnDelete, btnImage}) {
            b.setOpaque(true);
            b.setBorderPainted(false);
            b.setFocusPainted(false);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.LEFT);
        
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
//        for(int i = 0; i < db.getProducts().size(); i++){
//            int id = ((Product)(db.getProducts().get(i))).getId();
//            String img = ((Product)(db.getProducts().get(i))).getImg();
//            String productName = ((Product)(db.getProducts().get(i))).getProductName();
//            double price = ((Product)(db.getProducts().get(i))).getPrice();
//            String category = ((Product)(db.getProducts().get(i))).getCategory();
//            JButton action = new JButton("Delete");
//            
//            Object[] data = {id,img,productName,price,category,action};
//            System.out.println("si");
//            table.getTableModel().addRow(data);
//        }
//        
//        btnAdd.addActionListener(this);
//        btnUpdate.addActionListener(this);
//        btnDelete.addActionListener(this);
//        table.addMouseListener(new MouseAdapter(){
//            public void mouseClicked(MouseEvent arg0){
//                int i = table.getSelectedRow();
//                tfName.setText(table.getValueAt(i, 2).toString());
//                tfPrice.setText(table.getValueAt(i, 3).toString());
//                category.setSelectedItem(table.getValueAt(i, 4));
//                view.setfName(table.getValueAt(i, 1).toString());
//                lblFile.setText(shorten(table.getValueAt(i, 1).toString(), 20));
//                
//                System.out.println(table.getValueAt(i, 1).toString());
//            }
//        });
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spTable = new javax.swing.JScrollPane();
        table = new GUI.Components.Table();
        lblProduct = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();
        lblPrice = new javax.swing.JLabel();
        tfPrice = new javax.swing.JTextField();
        lblCategory = new javax.swing.JLabel();
        category = new javax.swing.JComboBox<>();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        choosepic = new javax.swing.JPanel();
        lblPictures = new javax.swing.JLabel();
        lblFile = new javax.swing.JLabel();
        btnImage = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        spTable.setBackground(new java.awt.Color(255, 255, 255));
        spTable.setBorder(null);
        spTable.setForeground(new java.awt.Color(255, 255, 255));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Image", "Product", "Price", "Category"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        spTable.setViewportView(table);

        lblProduct.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        lblProduct.setForeground(new java.awt.Color(102, 102, 102));
        lblProduct.setText("Products");

        lblName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblName.setForeground(new java.awt.Color(51, 51, 51));
        lblName.setText("Product");

        lblPrice.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblPrice.setForeground(new java.awt.Color(51, 51, 51));
        lblPrice.setText("Price");

        lblCategory.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblCategory.setForeground(new java.awt.Color(51, 51, 51));
        lblCategory.setText("Category");

        category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Coffee", "Milk & Coco", "Tea", "Soda", "Juice" }));

        btnAdd.setBackground(new java.awt.Color(197, 158, 126));
        btnAdd.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(197, 158, 126));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setText("Update");

        btnDelete.setBackground(new java.awt.Color(197, 158, 126));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Delete");

        choosepic.setBackground(new java.awt.Color(255, 255, 255));

        lblPictures.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        lblPictures.setForeground(new java.awt.Color(51, 51, 51));
        lblPictures.setText("Pictures");
        choosepic.add(lblPictures);

        lblFile.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblFile.setForeground(new java.awt.Color(102, 102, 102));
        choosepic.add(lblFile);

        btnImage.setBackground(new java.awt.Color(197, 158, 126));
        btnImage.setForeground(new java.awt.Color(255, 255, 255));
        btnImage.setText("Browse File.");
        btnImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImageActionPerformed(evt);
            }
        });
        choosepic.add(btnImage);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(1220, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(spTable, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(173, 173, 173)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblCategory)
                                    .addComponent(choosepic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblPrice)
                                        .addComponent(tfPrice)
                                        .addComponent(lblName)
                                        .addComponent(tfName)
                                        .addComponent(category, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(77, 77, 77))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(spTable, javax.swing.GroupLayout.PREFERRED_SIZE, 559, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblPrice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(lblCategory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(category, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(choosepic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAdd)
                            .addComponent(btnUpdate)
                            .addComponent(btnDelete))
                        .addGap(58, 58, 58))))
        );
    }// </editor-fold>//GEN-END:initComponents
   
    
    
//    private String fName = "";
//    File sourceFile = null;
//    File destinationFile = null;
    private void btnImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImageActionPerformed
//        if(evt.getSource().equals(btnImage)){
//            JFileChooser fc = new JFileChooser();
//            int result = fc.showOpenDialog(null);
//            if (result == JFileChooser.APPROVE_OPTION) {
//                File f = fc.getSelectedFile();
//                Path p = Paths.get(f.getAbsolutePath());
//                    lblFile.setText(shorten(p.getFileName().toString(), 20));
//                    fName = p.getFileName().toString();
//                    view.setfName(fName);
//                
//                String fileName = p.toString();
//                String fileNameOutput = p.getFileName().toString().replaceFirst("[.][^.]+$", "");
//                String newPath = "public/products";
//                File directory = new File(newPath);
//                if(!directory.exists()){
//                    directory.mkdirs();
//                }
//                String  extension = fileName.substring(fileName.lastIndexOf('.') + 1);
//                    sourceFile = new File(fileName);
//                    destinationFile = new File(newPath+"/"+fileNameOutput+"."+extension);
//
//            }
//        }
    }//GEN-LAST:event_btnImageActionPerformed
    public void saveFile(File sourceFile,File destinationFile){
//        try{
//            Files.copy(sourceFile.toPath(), destinationFile.toPath());
//        } catch (IOException e){
//        }
    }
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed
//    private String shorten(String text,int max){
//       if(text == null){
//           return "";
//       }
//       if(text.length() <= max){
//           return text;
//       }
//       if(max <= 5){
//           return text.substring(0,max);
//       }
//       return text.substring(0,max-2) + "...";
//    }
//    private DBModel db = new DBModel();
//    Product p;
//    int index = db.getIndex();
    @Override
    public void actionPerformed(ActionEvent ae) {
//        if(ae.getSource().equals(btnAdd)){
//            try{
//                index++;
//                System.out.println(index);
//                p = new Product(index, tfName.getText(), Double.parseDouble(tfPrice.getText()), category.getSelectedItem().toString(), fName, 0);
//                
//                db.addProduct(p);
//                Object[] data = {index,fName,tfName.getText(),Double.parseDouble(tfPrice.getText()),category.getSelectedItem().toString(),new JButton("Delete")};
//                
//                
//                table.getTableModel().addRow(data);
//                table.getTableModel().fireTableDataChanged();
//                table.repaint();
//                
//                saveFile(sourceFile,destinationFile);
//                
//                
//            } catch (NumberFormatException e){
//                JOptionPane.showMessageDialog(null,"Please Fill.", "Error", JOptionPane.PLAIN_MESSAGE);
//            }
//        } else if (ae.getSource().equals(btnDelete)){
//            int i = table.getSelectedRow();
//            if(i>=0){
//                db.removeProducts(table.getSelectedRow());
//                table.getTableModel().removeRow(i);
//                JOptionPane.showMessageDialog(null,"Remove Data.", "Removing", JOptionPane.PLAIN_MESSAGE);
//            }
//        } else if (ae.getSource().equals(btnUpdate)){
//            int i = table.getSelectedRow();
//            table.setValueAt(tfName.getText(),i,2);
//            table.setValueAt(tfPrice.getText(), i,3);
//            table.setValueAt(category.getSelectedItem().toString(), i,4);
//            table.setValueAt(lblFile.getText(), i,1);
//            
//            JOptionPane.showMessageDialog(null,"Update Data.", "Updating", JOptionPane.PLAIN_MESSAGE);
//
//        }

    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.fillRect(0, getWidth()-20, getHeight(), getHeight());
        super.paintChildren(g);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnImage;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> category;
    private javax.swing.JPanel choosepic;
    private javax.swing.JLabel lblCategory;
    private javax.swing.JLabel lblFile;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPictures;
    private javax.swing.JLabel lblPrice;
    private javax.swing.JLabel lblProduct;
    private javax.swing.JScrollPane spTable;
    private GUI.Components.Table table;
    private javax.swing.JTextField tfName;
    private javax.swing.JTextField tfPrice;
    // End of variables declaration//GEN-END:variables
    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JButton getBtnImage() {
        return btnImage;
    }

    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public Table getTable() {
        return table;
    }

    public JComboBox<String> getCategory() {
        return category;
    }

    public JLabel getLblFile() {
        return lblFile;
    }

    public void setCategory(JComboBox<String> category) {
        this.category = category;
    }

    public void setLblFile(JLabel lblFile) {
        this.lblFile = lblFile;
    }

    

    public void setTfName(JTextField tfName) {
        this.tfName = tfName;
    }

    public void setTfPrice(JTextField tfPrice) {
        this.tfPrice = tfPrice;
    }

    public JTextField getTfName() {
        return tfName;
    }

    public JTextField getTfPrice() {
        return tfPrice;
    }
}
