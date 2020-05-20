package application.controllers;

import application.networking.JSONHandler;
import application.Main;
import application.warningSystems.Warning;
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

import static application.Main.serverOn;

public class MainHubController implements Controller {
    @FXML
    private JFXSlider slider;
    @FXML
    private Label temp_c;

    private double lastGlobalTemp = 21.5;

    @FXML
    private synchronized void initialize() {

        try {
            lastGlobalTemp = Double.parseDouble(JSONHandler.get("http://localhost:8080/sensors/sensor/any"));
            serverOn = true;
        } catch (Exception e) {
            serverOn = false;
        }
        String formattedTemp = Main.df.format(lastGlobalTemp);
        temp_c.setText(formattedTemp + " °C");
        slider.setMajorTickUnit(0.5);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true);
        slider.setShowTickMarks(true);
        slider.setValue(lastGlobalTemp);
        if (!serverOn) slider.setDisable(true);
        slider.setDisable(false);
    }

    @Override
    public boolean update() {  //TODO: topeni bylo treba zapnout % dni v roce - bude v update a request data
        return false;
    }

    @Override
    public void requestData() {

    }

    @FXML
    private void changeValue() {
        String formattedText = String.valueOf(slider.getValue());
        try {
            JSONHandler.get("http://localhost:8080/sensors/all/updateRequestedTemperature/" + formattedText);
            lastGlobalTemp = Double.parseDouble(formattedText);
            temp_c.setText(lastGlobalTemp + " °C");
            slider.setValue(lastGlobalTemp);
        } catch (Exception e) {
            serverOn = false;
            slider.setDisable(true);
            slider.setValue(lastGlobalTemp);
            new Warning(Warning.WarningType.SERVER_DOWN);
            slider.setDisable(false);
        }
    }
    @FXML
    private void increment(){
        double newGlobalTemp = lastGlobalTemp + 0.5;
        if(newGlobalTemp > slider.getMax()) return;
        try {
            JSONHandler.get("http://localhost:8080/sensors/all/updateRequestedTemperature/" + newGlobalTemp);
            lastGlobalTemp = newGlobalTemp;
            temp_c.setText(lastGlobalTemp + " °C");
            slider.setValue(lastGlobalTemp);
        } catch (Exception e) {
            serverOn = false;
            slider.setDisable(true);
            new Warning(Warning.WarningType.SERVER_DOWN);
            slider.setDisable(false);
        }
    }
    @FXML
    private void decrement(){
        double newGlobalTemp = lastGlobalTemp - 0.5;
        if(newGlobalTemp < slider.getMin()) return;
        try {
            JSONHandler.get("http://localhost:8080/sensors/all/updateRequestedTemperature/" + newGlobalTemp);
            lastGlobalTemp = newGlobalTemp;
            temp_c.setText(lastGlobalTemp + " °C");
            slider.setValue(lastGlobalTemp);
        } catch (Exception e) {
            serverOn = false;
            slider.setDisable(true);
            new Warning(Warning.WarningType.SERVER_DOWN);
            slider.setDisable(false);
        }
    }

    public void changeScreenRooms(ActionEvent evt) throws IOException {
        if (!serverOn) {
            new Warning(Warning.WarningType.SERVER_DOWN);
            return;
        }
        Parent roomsViewParent = FXMLLoader.load(getClass().getResource("../uiresources/mistnosti.fxml"));
        Scene roomsView = new Scene(roomsViewParent);

        Stage window = (Stage) ((Node) evt.getSource()).getScene().getWindow();

        window.setScene(roomsView);
        window.show();
    }

    public void changeScreenStatistics(ActionEvent evt) throws IOException {
        if (!serverOn) {
            new Warning(Warning.WarningType.SERVER_DOWN);
            return;
        }
        Parent roomsViewParent = FXMLLoader.load(getClass().getResource("../UIResources/statistiky.fxml"));
        Scene roomsView = new Scene(roomsViewParent);

        Stage window = (Stage) ((Node) evt.getSource()).getScene().getWindow();

        window.setScene(roomsView);
        window.show();
    }
}