package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;

import java.util.Objects;

// The javadoc folder will be labeled javadoc and located in the root of the zip.
/**
 * The main class loads the initial screen for the application.
 * @author Kyler Wilson
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Outsourced rim = new Outsourced(1, "Rim", 3.66, 15, 15, 15, "Mongoose");
        Outsourced wheel = new Outsourced(2, "Wheel", 5.66, 18, 15, 30, "Mongoose");
        InHouse frame = new InHouse(3, "Frame", 15, 7, 5, 15, 155);
        InHouse handle = new InHouse(4, "Handle", 4.66, 6, 3, 25, 177);

        Product mountainBike = new Product(1, "Mountain Bike", 149.99, 3, 0, 10);
        Product bmxBike = new Product(2, "BMX Bike", 169.99, 2, 2, 15);
        Product streetBike = new Product(3, "Street Bike", 249.99, 5, 1, 20);

        Inventory.addProduct(mountainBike);
        Inventory.addProduct(bmxBike);
        Inventory.addProduct(streetBike);

        Inventory.addPart(rim);
        Inventory.addPart(wheel);
        Inventory.addPart(frame);
        Inventory.addPart(handle);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
        stage.setTitle("Performance Assessment");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    /**
     * This is what starts the application.
     * @param args is an array of strings that we can pass in through the command line
     */
    public static void main(String[] args) {
        launch(args);
    }
}
