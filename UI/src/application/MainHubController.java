package application;

import com.jfoenix.controls.JFXSlider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.DecimalFormat;

import static application.Main.serverOn;
import static application.Main.validateServerConnection;

public class MainHubController {

    @FXML
    private JFXSlider slider;
    @FXML
    private HBox toasterBox;
    @FXML
    private Label temp_c ;

    private double lastGlobalTemp = 21.5;

    public void changeScreenRooms(ActionEvent evt) throws IOException {
        update();
        if(!serverOn) return;
        Parent roomsViewParent = FXMLLoader.load(getClass().getResource("mistnosti.fxml"));
        Scene roomsView = new Scene(roomsViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(roomsView);
        window.show();
    }

    public void changeScreenStatistics(ActionEvent evt) throws IOException {
        update();
        if(!serverOn) return;
        Parent roomsViewParent = FXMLLoader.load(getClass().getResource("statistiky.fxml"));
        Scene roomsView = new Scene(roomsViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(roomsView);
        window.show();
    }

    @FXML
    private void initialize(){
        temp_c.setText(new DecimalFormat("#.0").format(slider.getValue()) + " °C");
        if(!validateServerConnection()){
        slider.setDisable(true);
        return;
        }
        update();
        slider.setDisable(false);
    }

    @FXML
    private void increment() {
        String text = String.valueOf(Main.df.format(slider.getValue()));
        String sendText = String.valueOf(slider.getValue());
        temp_c.setText(text + " °C");
        lastGlobalTemp = Double.parseDouble(sendText);
        try {
            JSONHandler.get("http://localhost:8080/sensors/all/updateRequestedTemperature/" + sendText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void checkOnEnteredSlider(){
        if(!validateServerConnection()){
            slider.setDisable(true);
            slider.setValue(lastGlobalTemp);
            temp_c.setText(Main.df.format(slider.getValue()) + " °C");
            showMsg();
            return;
        }else{
            slider.setDisable(false);
            try {
                lastGlobalTemp = Double.parseDouble(JSONHandler.get("http://localhost:8080/sensors/sensor/any"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void update(){
        if(validateServerConnection()){
        try {
            lastGlobalTemp = Double.parseDouble(JSONHandler.get("http://localhost:8080/sensors/sensor/any"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        }else{
            serverOn = false;
            slider.setDisable(true);
            showMsg();
        }
        slider.setValue(lastGlobalTemp);
        temp_c.setText(Main.df.format(lastGlobalTemp) + " °C");
    }
    private void showMsg(){
        Stage stage = (Stage) toasterBox.getScene().getWindow();
        Toast.makeText(stage, "Server neběží.", 2000, 300, 300, toasterBox);
    }
}