package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class is responsible for keeping track of all the inventory.
 * @author Kyler Wilson
 */
public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * This will add a part to the allParts list.
     * @param newPart the part to add
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * This will add a product to the allProducts list.
     * @param newProduct the product to add
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * This will update an existing part in the allParts list.
     * @param index the index for the updated part
     * @param selectedPart the updated part
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * This will update an existing product in the allProducts list.
     * @param index the index for the updated product
     * @param selectedProduct the updated product
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * This will remove an existing part from the allParts list.
     * @param selectedPart part to remove
     * @return whether the remove was successful
     */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * This will remove an existing products from the allProducts list.
     * @param selectedProduct product to remove
     * @return whether the remove was successful
     */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * This returns a list of all the parts in the inventory
     * @return all the parts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * This returns a list of all the products in the inventory
     * @return all the products
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}