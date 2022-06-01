package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This is the logic for the main screen
 * @author Kyler Wilson
 */
public class MainScreen implements Initializable {
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableColumn<Part, Integer> partsIdCol;
    @FXML
    private TableColumn<Part, String> partsNameCol;
    @FXML
    private TableColumn<Part, Integer> partsInvLevelCol;
    @FXML
    private TableColumn<Part, Double> partsCostCol;
    @FXML
    private TableColumn<Product, Integer> productsIdCol;
    @FXML
    private TableColumn<Product, String> productsNameCol;
    @FXML
    private TableColumn<Product, Integer> productsInvLevelCol;
    @FXML
    private TableColumn<Product, Double> productsCostCol;
    @FXML
    private TextField productSearch;
    @FXML
    private TextField partSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsTable.setItems(Inventory.getAllParts());
        productsTable.setItems(Inventory.getAllProducts());

        partsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productsCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * This will take you to the product form when you click add or modify under the products table.
     * @param actionEvent is the on click event
     * @throws IOException throws an error if we cannot load the next page
     */
    public void toProductsForm(ActionEvent actionEvent) throws IOException {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        String buttonId = ((Node)actionEvent.getSource()).getId();
        boolean modifying = !buttonId.equals("productsAddButton");
        if (modifying && selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selected Product");
            alert.setContentText("Please select a product to modify.");
            alert.showAndWait();

            return;
        }

        ProductForm.setInitialParams(modifying, selectedProduct);

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ProductForm.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setTitle("Product Form");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    /**
     * This will take you to the part form when you click add or modify under the parts table.
     * @param actionEvent is the on click event
     * @throws IOException throws an error if we cannot load the next page
     */
    public void toPartsForm(ActionEvent actionEvent) throws IOException {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        String buttonId = ((Node)actionEvent.getSource()).getId();
        boolean modifying = !buttonId.equals("partsAddButton");

        if (modifying && selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selected Part");
            alert.setContentText("Please select a part to modify.");
            alert.showAndWait();

            return;
        }

        PartsForm.setInitialParams(modifying, selectedPart);

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/PartsForm.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setTitle("Parts Form");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    /**
     * This is what closes the application when the exit button is clicked.
     */
    public void closeApplication() {
        Platform.exit();
    }

    /**
     * This will delete a part when the user selects a part then clicks delete under the parts table.
     */
    public void onDeletePart() {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selected Part");
            alert.setContentText("Please select a part to delete.");
            alert.showAndWait();

            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Part Deletion Confirmation");
        alert.setContentText("Are you sure you want to delete " + selectedPart.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() != ButtonType.OK) {
            return;
        }

        boolean partDeleted = Inventory.deletePart(selectedPart);

        if (!partDeleted) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete Part Error");
            alert.setContentText("We could not delete your part. Please try again.");
            alert.showAndWait();
        }
    }

    /**
     * This will delete a product when the user selects a product then clicks delete under the products table.
     * FUTURE ENHANCEMENT - when deleting a product with associated parts, we might want to instead redirect the user
     * to the product form to see the associated parts.
     */
    public void onDeleteProduct() {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selected Product");
            alert.setContentText("Please select a product to delete.");
            alert.showAndWait();

            return;
        }

        if (!selectedProduct.getAllAssociatedParts().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Associated Parts");
            alert.setContentText("Please make sure you remove the associated parts with this product before deleting it.");
            alert.showAndWait();

            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Product Deletion Confirmation");
        alert.setContentText("Are you sure you want to delete " + selectedProduct.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() != ButtonType.OK) {
            return;
        }

        boolean productDeleted = Inventory.deleteProduct(selectedProduct);

        if (!productDeleted) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete Product Error");
            alert.setContentText("We could not delete your product. Please try again.");
            alert.showAndWait();
        }
    }

    /**
     * This will search for a product in the product table when the user enters a string.
     * @param partialName the name we will search for in our table
     * @return a list of products that we will display in our table
     */
    private ObservableList<Product> searchForProductsName(String partialName) {
        ObservableList<Product> namedProducts = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(partialName.toLowerCase())) {
                namedProducts.add(product);
            }
        }

        return namedProducts;
    }

    /**
     * This will search for a product in the product table based on an id that was given.
     * @param id the id we want to find in our product table
     * @return a product that will display in our table
     */
    private Product searchForProductId(int id) {
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (Product product : allProducts) {
            if (product.getId() == id) {
                return product;
            }
        }

        return null;
    }

    /**
     * This will search for a part in the part table when the user enters a string.
     * @param partialName the name we will search for in our table
     * @return a list of parts that we will display in our table
     */
    private ObservableList<Part> searchForPartsName(String partialName) {
        ObservableList<Part> namedParts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(partialName.toLowerCase())) {
                namedParts.add(part);
            }
        }

        return namedParts;
    }

    /**
     * This will search for a part in the part table based on an id that was given.
     * @param id the id we want to find in our part table
     * @return a part that will display in our table
     */
    private Part searchForPartId(int id) {
        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part part : allParts) {
            if (part.getId() == id) {
                return part;
            }
        }

        return null;
    }

    /**
     * This is what runs when a search query is entered for a product.
     */
    public void onProductSearch() {
        String input = this.productSearch.getText();

        ObservableList<Product> updatedSearch = this.searchForProductsName(input);

        if (updatedSearch.isEmpty()) {
            try {
                int numberInput = Integer.parseInt(input);
                Product idSearch = searchForProductId(numberInput);

                if (idSearch == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("No Products");
                    alert.setContentText("There are no products with that name or id.");
                    alert.showAndWait();

                    return;
                }

                updatedSearch.add(idSearch);
            } catch (NumberFormatException ignored) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Products");
                alert.setContentText("There are no products with that name or id.");
                alert.showAndWait();

                return;
            }
        }

        this.productsTable.setItems(updatedSearch);
    }

    /**
     * This is what runs when a search query is entered for a part.
     */
    public void onPartSearch() {
        String input = this.partSearch.getText();

        ObservableList<Part> updatedSearch = this.searchForPartsName(input);

        if (updatedSearch.isEmpty()) {
            try {
                int numberInput = Integer.parseInt(input);
                Part idSearch = searchForPartId(numberInput);

                if (idSearch == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("No Parts");
                    alert.setContentText("There are no parts with that name or id.");
                    alert.showAndWait();

                    return;
                }

                updatedSearch.add(idSearch);
            } catch (NumberFormatException ignored) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Parts");
                alert.setContentText("There are no parts with that name or id.");
                alert.showAndWait();

                return;
            }
        }

        this.partsTable.setItems(updatedSearch);
    }
}
