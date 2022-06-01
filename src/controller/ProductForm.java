package controller;

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
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This is the logic for the part form
 * @author Kyler Wilson
 */
public class ProductForm implements Initializable {
    @FXML
    private Label productsFormTitle;
    @FXML
    private TextField productsFormIdInput;
    @FXML
    private TextField productsFormNameInput;
    @FXML
    private TextField productsFormInvInput;
    @FXML
    private TextField productsFormPriceInput;
    @FXML
    private TextField productsFormMaxInput;
    @FXML
    private TextField productsFormMinInput;
    @FXML
    private TableView<Part> productsFormAllPartTable;
    @FXML
    private TableView<Part> productsFormRelatedPartsTable;
    @FXML
    private TableColumn<Part, Integer> productFormAllPartsId;
    @FXML
    private TableColumn<Part, String> productFormAllPartsName;
    @FXML
    private TableColumn<Part, Integer> productFormAllPartsInvLevel;
    @FXML
    private TableColumn<Part, Double> productsFormAllPartsCost;
    @FXML
    private TableColumn<Part, Integer> productsFormRelatedId;
    @FXML
    private TableColumn<Part, String> productsFormRelatedName;
    @FXML
    private TableColumn<Part, Integer> productsFormRelatedInvLevel;
    @FXML
    private TableColumn<Part, Double> productsFormRelatedCost;
    @FXML
    private TextField productFormPartsSearch;

    private static boolean isModifying;
    private static Product product = null;
    private final ObservableList<Part> tempRelatedParts = FXCollections.observableArrayList();

    /**
     * This will be called before we switch screens
     * @param modifying whether to add or modify
     * @param selectedPart the part that was selected
     */
    public static void setInitialParams(boolean modifying, Product selectedPart) {
        isModifying = modifying;
        if (modifying) {
            product = selectedPart;
        } else {
            product = new Product(11, "", 0, 0, 0, 0);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        productsFormAllPartTable.setItems(allParts);
        productsFormRelatedPartsTable.setItems(product.getAllAssociatedParts());

        String productTitle = "Add Product";
        if (isModifying) {
            productTitle = "Modify Product";

            this.productsFormIdInput.setPromptText(String.valueOf(product.getId()));
            this.productsFormNameInput.setText(product.getName());
            this.productsFormInvInput.setText(String.valueOf(product.getStock()));
            this.productsFormPriceInput.setText(String.valueOf(product.getPrice()));
            this.productsFormMinInput.setText(String.valueOf(product.getMin()));
            this.productsFormMaxInput.setText(String.valueOf(product.getMax()));
        }

        productsFormTitle.setText(productTitle);

        productFormAllPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productFormAllPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productFormAllPartsInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productsFormAllPartsCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        productsFormRelatedId.setCellValueFactory(new PropertyValueFactory<>("id"));
        productsFormRelatedName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsFormRelatedInvLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productsFormRelatedCost.setCellValueFactory(new PropertyValueFactory<>("price"));
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
     * This is what runs when a search query is entered for a part.
     */
    public void onPartSearch() {
        String input = this.productFormPartsSearch.getText();

        ObservableList<Part> updatedSearch = this.searchForPartsName(input);

        if (updatedSearch.isEmpty()) {
            try {
                int numberInput = Integer.parseInt(input);
                Part idSearch = searchForPartId(numberInput);
                updatedSearch.add(idSearch);
            } catch (NumberFormatException ignored) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Parts");
                alert.setContentText("There are no parts with that name or id.");
                alert.showAndWait();

                return;
            }
        }

        this.productsFormAllPartTable.setItems(updatedSearch);
    }

    /**
     * Called when an associated part is added
     */
    public void onAddPartClick() {
        Part part = productsFormAllPartTable.getSelectionModel().getSelectedItem();
        product.addAssociatedPart(part);
        tempRelatedParts.add(part);
    }

    /**
     * Called when an associated part is removed
     */
    public void onRemovePartClick() {
        Part partToRemove = productsFormRelatedPartsTable.getSelectionModel().getSelectedItem();
        boolean deleted = product.deleteAssociatedPart(partToRemove);

        if (!deleted) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Deleting Part");
            alert.setContentText("Could not delete associated part. Please try again.");
            alert.showAndWait();
        }
    }

    /**
     * RUNTIME ERROR - At first I was using the onCancelClick to help close the application, this was fine at first until
     * I added the logic to remove associated parts when canceling a modification. To fix this, I wrote the redirection again
     * @param actionEvent on click button action to save a modified or new product
     * @throws IOException throws an exception if we cannot load the main screen after a save
     */
    public void onSaveClick(ActionEvent actionEvent) throws IOException {
        boolean isEmpty = false;
        String nameText  = productsFormNameInput.getText();
        String stockText = productsFormInvInput.getText();
        String priceText = productsFormPriceInput.getText();
        String minText   = productsFormMinInput.getText();
        String maxText   = productsFormMaxInput.getText();

        double price;
        int stock;
        int min;
        int max;

        if (nameText.isEmpty()) {
            isEmpty = true;
        } else {
            try {
                product.setName(nameText);
            } catch (Exception error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Name Value Error");
                alert.setContentText("Please be sure the name you entered was a valid string.");
                alert.showAndWait();

                return;
            }
        }

        if (stockText.isEmpty()) {
            isEmpty = true;
        } else {
            try {
                stock = Integer.parseInt(stockText);
                product.setStock(stock);
            } catch (Exception error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Stock Value Error");
                alert.setContentText("Please be sure the machine id you entered was a whole number.");
                alert.showAndWait();

                return;
            }
        }

        if (priceText.isEmpty()) {
            isEmpty = true;
        } else {
            try {
                price = Double.parseDouble(priceText);
                product.setPrice(price);
            } catch (NumberFormatException error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Price Value Error");
                alert.setContentText("Please be sure the price you entered was an number.");
                alert.showAndWait();

                return;
            }
        }

        if (minText.isEmpty()) {
            isEmpty = true;
        } else {
            try {
                min = Integer.parseInt(minText);
                product.setMin(min);
            } catch (NumberFormatException error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Minimum Value Error");
                alert.setContentText("Please be sure the machine id you entered was a whole number.");
                alert.showAndWait();

                return;
            }
        }

        if (maxText.isEmpty()) {
            isEmpty = true;
        } else {
            try {
                max = Integer.parseInt(maxText);
                product.setMax(max);
            } catch (NumberFormatException error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Maximum Value Error");
                alert.setContentText("Please be sure the machine id you entered was a whole number.");
                alert.showAndWait();

                return;
            }
        }

        if (isEmpty) {
            return;
        }

        if (!isModifying) {
            int newId = 1;
            ObservableList<Product> allProducts = Inventory.getAllProducts();
            if (allProducts.size() > 0) {
                newId += allProducts.get(allProducts.size() - 1).getId();
            }
            product.setId(newId);
            Inventory.addProduct(product);
        } else {
            int index = 0;
            ObservableList<Product> allProducts = Inventory.getAllProducts();
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getId() == product.getId()) {
                    index = i;
                    break;
                }
            }
            Inventory.updateProduct(index, product);
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setTitle("Performance Assessment");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    /**
     *
     * @param actionEvent on click button action to cancel adding or modifying a product
     * @throws IOException throws an exception if we cannot load the main screen after a save
     */
    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        if (isModifying) {
            for (Part part : tempRelatedParts) {
                product.deleteAssociatedPart(part);
            }
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setTitle("Performance Assessment");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }
}