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
import java.math.BigDecimal;
import java.math.RoundingMode;

import static application.Main.serverOn;

public class MainHubController {
    @FXML
    private JFXSlider slider;
    @FXML
    private Label temp_c ;

    private double lastGlobalTemp = 21.5;

    @FXML
    private void initialize(){

        try {
            lastGlobalTemp = Double.parseDouble(JSONHandler.get("http://localhost:8080/sensors/sensor/any"));
            serverOn = true;
        } catch (Exception e) {
            serverOn = false;
        }
        String formattedTemp = Main.df.format(lastGlobalTemp);
        temp_c.setText(formattedTemp + " °C");
        if(!serverOn) slider.setDisable(true);
        slider.setDisable(false);
    }

    public void changeScreenRooms(ActionEvent evt) throws IOException {
        if(!serverOn) new Warning(Warning.WarningType.SERVER_DOWN);
        Parent roomsViewParent = FXMLLoader.load(getClass().getResource("mistnosti.fxml"));
        Scene roomsView = new Scene(roomsViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(roomsView);
        window.show();
    }

    public void changeScreenStatistics(ActionEvent evt) throws IOException {
        if(!serverOn) new Warning(Warning.WarningType.SERVER_DOWN);
        Parent roomsViewParent = FXMLLoader.load(getClass().getResource("statistiky.fxml"));
        Scene roomsView = new Scene(roomsViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(roomsView);
        window.show();
    }

    @FXML
    private void increment() {
        if(serverOn) {
            BigDecimal bd = new BigDecimal(slider.getValue()).setScale(2, RoundingMode.HALF_UP);
            String formattedText = bd.toString();
            try {
                JSONHandler.get("http://localhost:8080/sensors/all/updateRequestedTemperature/" + formattedText);
                lastGlobalTemp = Double.parseDouble(formattedText);
                temp_c.setText(lastGlobalTemp + " °C");
                slider.setValue(lastGlobalTemp);
            } catch (Exception e) {
                serverOn = false;
                slider.setDisable(true);
            }
        }
        else {
            slider.setDisable(true);
            temp_c.setText(lastGlobalTemp + " °C");
            slider.setValue(lastGlobalTemp);
            new Warning(Warning.WarningType.SERVER_DOWN);
            slider.setDisable(false);
        }
    }

}