package db;

import Model.Finance;
import Model.Product;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Stores products and finance data in a serialized file (database.dat).
 */
public class DBModel {

    private static final String DATABASE_FILE = "database.dat";

    private ArrayList<Product> products = new ArrayList<>();
    private Finance finance = new Finance();

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products != null ? products : new ArrayList<>();
    }

    public void addProduct(Product p) {
        if (!products.contains(p)) {
            products.add(p);
        }
    }

    public Product findById(int id) {
        for (Product p : products) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public boolean removeById(int id) {
        return products.removeIf(p -> p.getId() == id);
    }

    /** Next free id (largest id + 1). */
    public int getNewId() {
        int max = 0;
        for (Product p : products) {
            max = Math.max(max, p.getId());
        }
        return max + 1;
    }

    public Finance getFinance() {
        return finance;
    }

    public void setFinance(Finance finance) {
        this.finance = finance;
    }

    @SuppressWarnings("unchecked")
    public void loadFile() {
        try (FileInputStream fin = new FileInputStream(DATABASE_FILE);
             ObjectInputStream in = new ObjectInputStream(fin)) {
            setProducts((ArrayList<Product>) in.readObject());
            finance = (Finance) in.readObject();
            System.out.printf("(LOAD) %d products, Balance : %.2f, Totally : %.2f%n",
                    products.size(), finance.getBalance(), finance.getTotally());
        } catch (FileNotFoundException e) {
            System.out.println("(LOAD) " + DATABASE_FILE + " not found, starting with an empty database");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("(LOAD) Cannot read " + DATABASE_FILE + ": " + e);
        }
    }

    public boolean saveFile() {
        try (FileOutputStream fOut = new FileOutputStream(DATABASE_FILE);
             ObjectOutputStream out = new ObjectOutputStream(fOut)) {
            out.writeObject(products);
            out.writeObject(finance);
            return true;
        } catch (IOException e) {
            System.err.println("(SAVE) Cannot write " + DATABASE_FILE + ": " + e);
            return false;
        }
    }
}
