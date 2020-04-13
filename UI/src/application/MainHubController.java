package application;

import com.jfoenix.controls.JFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.DecimalFormat;

public class MainHubController {

    public void changeScreenRooms(ActionEvent evt) throws IOException {
        Parent roomsViewParent = FXMLLoader.load(getClass().getResource("mistnosti.fxml"));
        Scene roomsView = new Scene(roomsViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(roomsView);
        window.show();
    }

    public void changeScreenStatistics(ActionEvent evt) throws IOException {
        Parent roomsViewParent = FXMLLoader.load(getClass().getResource("statistiky.fxml"));
        Scene roomsView = new Scene(roomsViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(roomsView);
        window.show();
    }

    @FXML
    private JFXSlider slider;
    @FXML
    private void initialize(){
        temp_c.setText(new DecimalFormat("#.0").format(slider.getValue()) + " C");
    }

    @FXML
    private Label temp_c ;

    @FXML
    private void increment() {
        String text = String.valueOf(new DecimalFormat("#.0").format(slider.getValue()));
        temp_c.setText(text + " C");
    }
}