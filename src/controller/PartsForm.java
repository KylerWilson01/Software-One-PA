package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This is the logic for the part form
 * @author Kyler Wilson
 */
public class PartsForm implements Initializable {
    @FXML
    private RadioButton partsInHouseRadio;
    @FXML
    private RadioButton partsOutsourcedRadio;
    @FXML
    private TextField partsFormIdInput;
    @FXML
    private TextField partsFormNameInput;
    @FXML
    private TextField partsFormInvInput;
    @FXML
    private TextField partsFormPriceInput;
    @FXML
    private TextField partsFormMaxInput;
    @FXML
    private TextField partsFormMachineCompanyInput;
    @FXML
    private TextField partsFormMinInput;
    @FXML
    private Label partsFormTitle;
    @FXML
    private Label partsMachineCompanyLabel;

    private static boolean isModifying;
    private boolean isOutsourced = false;
    private static Part part;

    /**
     * This will run before the screen is initialized.
     * @param modifying tells the program if we are modifying an existing part or adding a new part
     * @param selectedPart this will be an existing part or null
     */
    public static void setInitialParams(boolean modifying, Part selectedPart) {
        isModifying = modifying;
        part = selectedPart;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (isOutsourced) {
            this.partsMachineCompanyLabel.setText("Company Name");
            this.partsOutsourcedRadio.setSelected(true);
        } else {
            this.partsMachineCompanyLabel.setText("Machine ID");
            this.partsInHouseRadio.setSelected(true);
        }

        if (isModifying) {
            this.partsFormTitle.setText("Modify Part");
            this.partsFormIdInput.setPromptText(String.valueOf(part.getId()));
            this.partsFormNameInput.setText(part.getName());
            this.partsFormInvInput.setText(String.valueOf(part.getStock()));
            this.partsFormPriceInput.setText(String.valueOf(part.getPrice()));
            this.partsFormMinInput.setText(String.valueOf(part.getMin()));
            this.partsFormMaxInput.setText(String.valueOf(part.getMax()));

            if (part instanceof InHouse) {
                this.partsMachineCompanyLabel.setText("Machine ID");
                this.partsInHouseRadio.setSelected(true);
                this.isOutsourced = false;

                this.partsFormMachineCompanyInput.setText(String.valueOf(((InHouse) part).getMachineId()));
            } else {
                this.partsMachineCompanyLabel.setText("Company Name");
                this.partsOutsourcedRadio.setSelected(true);
                this.isOutsourced = true;

                this.partsFormMachineCompanyInput.setText(String.valueOf(((Outsourced) part).getCompanyName()));
            }
        }
    }

    /**
     * This will check to make sure all inputs are valid.
     * If the inputs are not valid it will pop up a dialog box telling the user what is wrong.
     * @param actionEvent button action to save a modified or new part
     * @throws IOException throws an exception if we cannot load the main screen after a save
     */
    public void onSaveClick(ActionEvent actionEvent) throws IOException {
        boolean isEmpty = false;

        String nameText  = partsFormNameInput.getText();
        String stockText = partsFormInvInput.getText();
        String priceText = partsFormPriceInput.getText();
        String minText   = partsFormMinInput.getText();
        String maxText   = partsFormMaxInput.getText();
        String machComp  = partsFormMachineCompanyInput.getText();
        int partId = 0;

        if (isModifying) {
            partId = part.getId();
        }

        if (isOutsourced) {
            part = new Outsourced(partId, "", 0, 0, 0, 0, "");
        } else {
            part = new InHouse(partId, "", 0, 0, 0, 0, 0);
        }

        int stock = 0;
        double price;
        int min = 0;
        int max = 0;

        if (nameText.isEmpty()) {
            isEmpty = true;
        } else {
            try {
                part.setName(nameText);
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
                part.setStock(stock);
            } catch (Exception error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Stock Value Error");
                alert.setContentText("Please be sure the inventory stock you entered was a whole number.");
                alert.showAndWait();

                return;
            }
        }

        if (priceText.isEmpty()) {
            isEmpty = true;
        } else {
            try {
                price = Double.parseDouble(priceText);
                part.setPrice(price);
            } catch (NumberFormatException error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Price Value Error");
                alert.setContentText("Please be sure the price you entered was a number.");
                alert.showAndWait();

                return;
            }
        }

        if (minText.isEmpty()) {
            isEmpty = true;
        } else {
            try {
                min = Integer.parseInt(minText);
                part.setMin(min);
            } catch (NumberFormatException error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Minimum Value Error");
                alert.setContentText("Please be sure the minimum value you entered was a whole number.");
                alert.showAndWait();

                return;
            }
        }

        if (maxText.isEmpty()) {
            isEmpty = true;
        } else {
            try {
                max = Integer.parseInt(maxText);
                part.setMax(max);
            } catch (NumberFormatException error) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Maximum Value Error");
                alert.setContentText("Please be sure the maximum value you entered was a whole number.");
                alert.showAndWait();

                return;
            }
        }

        if (machComp.isEmpty()) {
            isEmpty = true;
        } else {
            if (isOutsourced) {
                assert part instanceof Outsourced;
                ((Outsourced)part).setCompanyName(machComp);
            } else {
                try {
                    int machineId = Integer.parseInt(machComp);
                    ((InHouse)part).setMachineId(machineId);
                } catch (NumberFormatException error) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Machine ID Error");
                    alert.setContentText("Please be sure the machine id you entered was a whole number.");
                    alert.showAndWait();

                    return;
                }
            }
        }

        if (isEmpty) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Empty Field Error");
            alert.setContentText("Please be sure to fill all fields.");
            alert.showAndWait();

            return;
        }

        if (min > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Minimum or Maximum Value Error");
            alert.setContentText("The minimum value cannot be larger than the maximum value.");
            alert.showAndWait();

            return;
        } else if (min > stock || max < stock) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Stock Value Error");
            alert.setContentText("The stock value must be between the value of minimum and maximum.");
            alert.showAndWait();

            return;
        }

        if (isModifying) {
            int index = 0;
            ObservableList<Part> allParts = Inventory.getAllParts();
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getId() == part.getId()) {
                    index = i;
                    break;
                }
            }
            Inventory.updatePart(index, part);
        } else {
            int partInventorySize = Inventory.getAllParts().size() - 1;

            if (partInventorySize > 0) {
                partId = Inventory.getAllParts().get(partInventorySize).getId() + 1;
            } else {
                partId = 1;
            }

            part.setId(partId);
            Inventory.addPart(part);
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setTitle("Performance Assessment");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    /**
     * Will be called when the cancel button is clicked
     * @param actionEvent button action to cancel modifying or adding a part
     * @throws IOException throws an exception if we cannot load the main screen after a save
     */
    public void onCancelClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        stage.setTitle("Performance Assessment");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    /**
     * When the user wants to switch from an outsourced part to an in-house part.
     * This will also switch from a company name to a machine id for the last input.
     */
    public void onInHouseClick() {
        this.partsMachineCompanyLabel.setText("Machine ID");
        this.partsInHouseRadio.setSelected(true);
        isOutsourced = false;
    }

    /**
     * When the user wants to switch from an in-house part to an outsourced part.
     * This will also switch from a machine id to a company name for the last input.
     */
    public void onOutsourcedClick() {
        this.partsMachineCompanyLabel.setText("Company Name");
        this.partsOutsourcedRadio.setSelected(true);
        isOutsourced = true;
    }
}