package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The Product class will keep track of an individual product.
 * @author Kyler Wilson
 */
public class Product {
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * This is the constructor for when a new product is created.
     * @param id the id that is automatically generated when a product is created
     * @param name the name of the product
     * @param price the price for the product
     * @param stock how much of the product is in the inventory
     * @param min the minimum amount of stock that can be in the inventory
     * @param max the maximum amount of stock that can be in the inventory
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * This will set the id of the product.
     * @param id the id of the product
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * This will set the name of the product.
     * @param name the products name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This will set the price for the product.
     * @param price the products price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * This will set the stock for the product.
     * @param stock how much of the product we have
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * This will set the minimum amount of stock the inventory can hold.
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * This will set the maximum amount of stock the inventory can hold.
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * This will get the id of the product.
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * This will get the name of the product.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * This will get the price of the product.
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * This will get the amount of stock the inventory has of this product.
     * @return how much stock there is
     */
    public int getStock() {
        return stock;
    }

    /**
     * This will get the minimum amount of stock the inventory can hold.
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * This will get the maximum amount of stock the inventory can hold.
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * This associate a part with this product.
     * @param part a part that is associated with the product
     */
    public void addAssociatedPart(Part part) {
        this.associatedParts.add(part);
    }

    /**
 * This will delete an associated part from this product.
     * @param selectedAssociatedPart the part to delete
     * @return whether it was deleted
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return this.associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * This will get all associated parts for this product.
     * @return the list of parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return this.associatedParts;
    }
}
