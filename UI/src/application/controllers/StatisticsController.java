package application.controllers;

import application.Main;
import application.networking.JSONHandler;
import application.warningSystems.Warning;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static application.Main.serverOn;

public class StatisticsController implements Controller {

    @FXML
    private ListView<String> roomsListView;
    @FXML
    private ListView<String> monthsListView;
    @FXML
    private Label numDaysHeaterOnLabel;
    @FXML
    private Label numAverageTimeLightsOn;
    @FXML
    private Label numFullPowerConsInKwh;

    private String[] months = {"Leden", "Únor", "Březen", "Duben", "Květen", "Červen", "Červenec", "Srpen", "Září", "Říjen", "Listopad", "Prosinec"};
    private Map<String, Integer> roomNamesIDs = new HashMap<>();
    private String currentlyChosenMonth = "Leden";
    private String currentlyChosenRoomName = null;
    private boolean updated;
    private boolean dataRequested;

    HashMap<String, HashMap<String, List<Double>>> formattedStatisticsData = new HashMap<>();

    //statistické hodnoty se vypočítají na serveru a pošlou se zpracované

    @FXML
    private synchronized void initialize() {  //TODO: usedRoomNames z roomsController se naplní až při spuštění metody initialize() dané třídy

        updated = false;
        dataRequested = false;

        for (int i = 0; i < months.length; i++) {
            monthsListView.getItems().add(i, months[i]);
        }

        monthsListView.setOnMouseClicked(mouseEvent -> {
            currentlyChosenMonth = monthsListView.getSelectionModel().getSelectedItem();
        });

        roomsListView.setOnMouseClicked(mouseEvent -> {
            currentlyChosenRoomName = roomsListView.getSelectionModel().getSelectedItem();
        });

        Main.serviceManager.setController(this);
    }

    @Override
    public boolean update() {

        if(!updated) {
            int counter = 0;
            for (String s : roomNamesIDs.keySet()) {
                roomsListView.getItems().add(counter, s);
                counter++;
            }
            updated = true;
        }

        if(currentlyChosenRoomName != null) {
            numAverageTimeLightsOn.setText(formattedStatisticsData.get(currentlyChosenMonth)
                    .get(currentlyChosenRoomName).get(1).toString()); //TODO: dodělat ostatní labely a naplnit databázi + naformátovat data
        }

        return true;
    }

    @Override
    public void requestData() {
        if(!dataRequested) {
            try {
                String roomName = JSONHandler.get("http://localhost:8080/room/all");

                JSONParser parser = new JSONParser();

                Object obj = parser.parse(roomName);
                JSONArray arr = (JSONArray) obj;

                for (int i = 0; i < arr.size(); i++) {
                    JSONObject jsob = (JSONObject) arr.get(i);
                    roomNamesIDs.put(jsob.get("name").toString(), Integer.parseInt(jsob.get("id").toString()));
                }
                dataRequested = true;
            } catch (Exception e) {
                Main.serverOn = false;
                new Warning(Warning.WarningType.SERVER_DOWN);
                dataRequested = false;
            }

            try {
                String statisticsData = JSONHandler.get("http://localhost:8080/statistics_data/all");

                org.json.JSONObject jsonData = new org.json.JSONObject(statisticsData);

                for (String month : months) {

                    org.json.JSONObject obj = new org.json.JSONObject(jsonData.get(month).toString());

                    HashMap<String, List<Double>> values = new HashMap<>();

                    for (String s : roomNamesIDs.keySet()) {

                        ArrayList<Double> sensorValues = new ArrayList<>();

                        org.json.JSONArray arr = new org.json.JSONArray(obj.get(s).toString());

                        for (int i = 0; i < 3; i++) {
                            sensorValues.add(Double.parseDouble(arr.get(i).toString()));
                        }

                        values.put(s, sensorValues);
                    }

                    formattedStatisticsData.put(month, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
                Main.serverOn = false;
                new Warning(Warning.WarningType.SERVER_DOWN);
            }
        }
    }

    public void goBack(ActionEvent evt) throws IOException {
        if (!serverOn) {
            new Warning(Warning.WarningType.SERVER_DOWN);
            return;
        }
        Parent mainViewParent = FXMLLoader.load(getClass().getResource("../UIResources/mainHub.fxml"));
        Scene mainView = new Scene(mainViewParent);
        Stage window = (Stage) ((Node) evt.getSource()).getScene().getWindow();
        window.setScene(mainView);
        window.show();
    }

    public void changeScreenRooms(ActionEvent evt) throws IOException {
        if (!serverOn) {
            new Warning(Warning.WarningType.SERVER_DOWN);
            return;
        }
        Parent roomsViewParent = FXMLLoader.load(getClass().getResource("../UIResources/mistnosti.fxml"));
        Scene roomsView = new Scene(roomsViewParent);

        Stage window = (Stage) ((Node) evt.getSource()).getScene().getWindow();

        window.setScene(roomsView);
        window.show();
    }
}
