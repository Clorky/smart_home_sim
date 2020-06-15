package application.controllers;

import application.Main;
import application.networking.JSONHandler;
import application.warningSystem.Warning;
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
import java.util.HashMap;
import java.util.Map;

import static application.Main.dfW;
import static application.Main.serverOn;

public class StatisticsController implements Controller {

    public static final String[] MONTHS = {"Leden", "Únor", "Březen", "Duben", "Květen", "Červen", "Červenec", "Srpen", "Září", "Říjen", "Listopad", "Prosinec"};
    public static Map<String, Integer> roomNamesIDs = new HashMap<>();
    public static boolean threadFinished = false;
    public static int daysHeated;
    public static Map<String, Map<Integer, Integer>> lightData;
    public static Map<Integer, Double> totalEnergyConsumption;
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

    private String currentlyChosenMonth = "Leden";
    private int currentlyChosenMonthNumber = 0;
    private String currentlyChosenRoomName = null;
    private boolean updated;

    //statistické hodnoty se vypočítají na serveru a pošlou se zpracované z cache

    @FXML
    private synchronized void initialize() {

        updated = false;

        for (int i = 0; i < MONTHS.length; i++) {
            monthsListView.getItems().add(i, MONTHS[i]);
        }

        monthsListView.setOnMouseClicked(mouseEvent -> {
            currentlyChosenMonth = monthsListView.getSelectionModel().getSelectedItem();
            for (int i = 0; i < MONTHS.length; i++) {
                if (currentlyChosenMonth.equals(MONTHS[i])) {
                    currentlyChosenMonthNumber = i;
                    break;
                }
            }
        });

        roomsListView.setOnMouseClicked(mouseEvent -> {
            currentlyChosenRoomName = roomsListView.getSelectionModel().getSelectedItem();
        });

        monthsListView.getSelectionModel().select(0);
        Main.serviceManager.setController(this);
    }

    @Override
    public boolean update() {

        if (!threadFinished) return false;

        if (!updated) {
            int counter = 0;
            for (String s : roomNamesIDs.keySet()) {
                roomsListView.getItems().add(counter, s);
                counter++;
            }
            updated = true;
        }

        if (currentlyChosenRoomName != null) {

            int numHeating = daysHeated;
            String textHeating = String.valueOf(numHeating);
            numDaysHeaterOnLabel.setText(textHeating + " dnů");

            int numLightning = lightData.get(currentlyChosenRoomName).get(currentlyChosenMonthNumber);
            numLightning /= 3600;
            String textLightning = String.valueOf(numLightning);
            numAverageTimeLightsOn.setText(textLightning + " hodin");

            double numPower = totalEnergyConsumption.get(currentlyChosenMonthNumber);
            double temp = numPower / 1000;
            String textPower = dfW.format(temp);
            numFullPowerConsInKwh.setText(textPower + " kW");
        }

        return true;
    }

    @Override
    public boolean requestData() {
        try {
            String roomName = JSONHandler.get("http://localhost:8080/room/all");

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(roomName);
            JSONArray arr = (JSONArray) obj;

            for (int i = 0; i < arr.size(); i++) {
                JSONObject jsob = (JSONObject) arr.get(i);
                StatisticsController.roomNamesIDs.put(jsob.get("name").toString(), Integer.parseInt(jsob.get("id").toString()));
            }
        } catch (Exception e) {
            Main.serverOn = false;
            new Warning(Warning.WarningType.SERVER_DOWN);
            return false;
        }

        try {
            String lightDataJson = JSONHandler.get("http://localhost:8080/statistics_data_cache/lightTimeInMonths");
            org.json.JSONObject lightData = new org.json.JSONObject(lightDataJson);
            Map<String, Map<Integer, Integer>> lightMap = new HashMap<>();

            for (String s : StatisticsController.roomNamesIDs.keySet()) {
                Map<Integer, Integer> monthsLight = new HashMap<>();

                org.json.JSONObject months = new org.json.JSONObject(lightData.get(s).toString());
                System.out.println(months.toString());
                for (int i = 0; i < StatisticsController.MONTHS.length; i++) {

                    monthsLight.put(i, Integer.valueOf(months.get(String.valueOf(i)).toString()));
                }

                lightMap.put(s, monthsLight);
            }
            StatisticsController.lightData = lightMap;


            String energyConsumptionDataJson = JSONHandler.get("http://localhost:8080/statistics_data_cache/totalConsumption");
            org.json.JSONObject consumptionData = new org.json.JSONObject(energyConsumptionDataJson);

            Map<Integer, Double> consumptionMap = new HashMap<>();

            for (int i = 0; i < StatisticsController.MONTHS.length; i++) {
                consumptionMap.put(i, Double.parseDouble(consumptionData.get(String.valueOf(i)).toString()));
            }

            StatisticsController.totalEnergyConsumption = consumptionMap;

        } catch (Exception e) {
            e.printStackTrace();
            Main.serverOn = false;
            new Warning(Warning.WarningType.SERVER_DOWN);
            return false;
        }
        StatisticsController.threadFinished = true;
        return true;
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
