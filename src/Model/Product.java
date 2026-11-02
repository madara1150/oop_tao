package Model;

import java.io.Serializable;

/**
 * A drink sold by the machine.
 */
public class Product implements Serializable {

    // keeps database.dat files created by older versions readable
    private static final long serialVersionUID = -8260893728312255825L;

    private int id;
    private String product;
    private double price;
    private String category;
    private String img;
    /** How many times this drink has been sold (used for "Recommend"). */
    private int population;

    public Product() {
        this(0, "", 0, "", "", 0);
    }

    public Product(int id, String product, double price, String category, String img, int population) {
        this.id = id;
        this.product = product;
        this.price = price;
        this.category = category;
        this.img = img;
        this.population = population;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return product;
    }

    public void setProductName(String product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void increasePopulation() {
        population++;
    }
}
