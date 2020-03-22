package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {

    public void changeScreenKitchen(ActionEvent evt) throws IOException {
        Parent kitchenViewParent = FXMLLoader.load(getClass().getResource("KitchenWindow.fxml"));
        Scene kitchenView = new Scene(kitchenViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(kitchenView);
        window.show();
    }
    public void changeScreenBathroom(ActionEvent evt) throws IOException {
        Parent bathroomViewParent = FXMLLoader.load(getClass().getResource("BathroomWindow.fxml"));
        Scene bathroomView = new Scene(bathroomViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(bathroomView);
        window.show();
    }
    public void changeScreenLivingRoom(ActionEvent evt) throws IOException {
        Parent livingViewParent = FXMLLoader.load(getClass().getResource("LivingWindow.fxml"));
        Scene livingView = new Scene(livingViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(livingView);
        window.show();
    }
    public void changeScreenBedroom(ActionEvent evt) throws IOException {
        Parent bedroomViewParent = FXMLLoader.load(getClass().getResource("BedroomWindow.fxml"));
        Scene bedroomView = new Scene(bedroomViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(bedroomView);
        window.show();
    }
}