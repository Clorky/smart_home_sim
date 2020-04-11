package sample;

import com.jfoenix.controls.JFXSlider;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
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

    public void changeScreenRooms(ActionEvent evt) throws IOException {
        Parent roomsViewParent = FXMLLoader.load(getClass().getResource("mistnosti.fxml"));
        Scene roomsView = new Scene(roomsViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(roomsView);
        window.show();
    }
    @FXML
    private JFXSlider slider;
    @FXML
    private void initialize(){
        temp_c.setText(new DecimalFormat("#.00").format(slider.getValue()) + " C");
    }

    @FXML
    private Label temp_c ;

    @FXML
    private void increment() {
        String text = String.valueOf(new DecimalFormat("#.00").format(slider.getValue()));
        temp_c.setText(text + " C");
    }
}