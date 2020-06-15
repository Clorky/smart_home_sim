package application.controllers;

import application.Main;
import application.networking.JSONHandler;
import application.warningSystem.Warning;
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

import static application.Main.df;
import static application.Main.serverOn;

public class MainHubController implements Controller {
    public static int days = 0;
    @FXML
    private JFXSlider slider;
    @FXML
    private Label temp_c;
    @FXML
    private Label daysHubLabel;
    private double lastGlobalTemp = 21.5;

    @FXML
    private synchronized void initialize() {

        try {
            lastGlobalTemp = Double.parseDouble(JSONHandler.get("http://localhost:8080/sensors/sensor/any"));
            serverOn = true;
        } catch (Exception e) {
            serverOn = false;
        }
        String formattedTemp = df.format(lastGlobalTemp);
        temp_c.setText(formattedTemp + " °C");
        slider.setMajorTickUnit(0.5);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true);
        slider.setShowTickMarks(true);
        slider.setValue(lastGlobalTemp);
        if (!serverOn) slider.setDisable(true);
        slider.setDisable(false);
        Main.serviceManager.setController(this);
    }

    @Override
    public boolean update() {
        if (daysHubLabel != null) {
            String text = String.valueOf(days);

            daysHubLabel.setText("Bylo nutno zapnout\nvytápění " + text + " dny za poslední rok.");
        }
        return true;
    }

    @Override
    public synchronized boolean requestData() {
        try {
            String daysHeatingInYear = JSONHandler.get("http://localhost:8080/statistics_data_cache/heatingInYear");
            System.out.println(daysHeatingInYear.toString());
            MainHubController.days = Integer.parseInt(daysHeatingInYear);
            StatisticsController.daysHeated = Integer.parseInt(daysHeatingInYear);
        } catch (Exception e) {
            serverOn = false;
            new Warning(Warning.WarningType.SERVER_DOWN);
            return false;
        }
        return true;
    }

    @FXML
    private void changeValue() {  //volá se po puštění slideru a posílá novou hodnotu na server
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
    private void increment() {  // volá tlačítko +
        double newGlobalTemp = lastGlobalTemp + 0.5;
        if (newGlobalTemp > slider.getMax()) return;
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
    private void decrement() { // tlačítko -
        double newGlobalTemp = lastGlobalTemp - 0.5;
        if (newGlobalTemp < slider.getMin()) return;
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