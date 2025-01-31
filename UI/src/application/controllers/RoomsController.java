package application.controllers;

import application.Main;
import application.networking.JSONHandler;
import application.warningSystem.Warning;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static application.Main.serverOn;

public class RoomsController implements Controller {

    public static List<String> roomsToDelete = new ArrayList<>();
    public static boolean removingInProcess = false;
    @FXML
    private Pane sensorPaneId;
    @FXML
    private ListView<String> listView;
    @FXML
    private TextField textAddTextField;
    @FXML
    private Label sensorNameLabel;
    @FXML
    private Label lightsOnTimeLabel;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label powerConsumptionAtmLabel;
    private List<String> usedRoomNames = new ArrayList<>();
    private String currentlyChosenRoomName = null;

    private double electricityUsage = 0;
    private boolean lightTime = false;
    private double currentTemperature = 0;

    @FXML
    public void handleMouseClick(MouseEvent arg0) {
        System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem());
    }

    @FXML
    private synchronized void initialize() {
        textAddTextField.textProperty().addListener( //max počet znaků v texfield je 20
                (observable, oldValue, newValue) -> {
                    if (newValue.length() > 20) textAddTextField.setText(oldValue);
                }
        );
        listView.setOnMouseClicked(mouseEvent -> {
            currentlyChosenRoomName = listView.getSelectionModel().getSelectedItem();
            sensorPaneId.setVisible(true);
            update();
            if (listView.getSelectionModel().getSelectedItem() == null) {
                sensorPaneId.setVisible(false);
            }
        });

        usedRoomNames = new ArrayList<>();

        try {
            String roomName = JSONHandler.get("http://localhost:8080/room/all");

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(roomName);
            JSONArray arr = (JSONArray) obj;

            for (int i = 0; i < arr.size(); i++) {
                JSONObject jsob = (JSONObject) arr.get(i);
                listView.getItems().add(jsob.get("name").toString());
                usedRoomNames.add(jsob.get("name").toString());
            }
        } catch (Exception e) {
            Main.serverOn = false;
            new Warning(Warning.WarningType.SERVER_DOWN);
        }
        Main.serviceManager.setController(this);
    }

    @Override
    public boolean update() {
        if (currentlyChosenRoomName != null) {
            Platform.runLater(() -> {
                sensorNameLabel.setText(currentlyChosenRoomName + "_sensor");
                temperatureLabel.setText(Main.df.format(currentTemperature) + " °C");
                powerConsumptionAtmLabel.setText(Main.df.format(electricityUsage) + " W");
                lightsOnTimeLabel.setText(lightTime ? "svítí" : "nesvítí");
            });
        }
        return true;
    }

    @Override
    public synchronized boolean requestData() {
        if (currentlyChosenRoomName != null) {
            try {

                String jsonString = JSONHandler.get("http://localhost:8080/sensors/sensor/" + currentlyChosenRoomName + "_sensor");
                org.json.JSONObject obj = new org.json.JSONObject(jsonString);
                currentTemperature = Double.parseDouble(obj.get("temperature").toString());
                electricityUsage = Double.parseDouble(obj.get("currentConsumption").toString());
                lightTime = Boolean.parseBoolean(obj.get("lightsOn").toString());
            } catch (Exception e) {
                Main.serverOn = false;
                new Warning(Warning.WarningType.SERVER_DOWN);
                return false;
            }
        }
        return true;
    }

    public synchronized void addRoom() {

        String roomName = textAddTextField.getText(); //room name se odvodi z text fieldu z UI

        Pattern p = Pattern.compile("^[^ _]", Pattern.CASE_INSENSITIVE); //není to ideální ale funguje to
        Pattern p2 = Pattern.compile("[^ _]$", Pattern.CASE_INSENSITIVE);
        Pattern p3 = Pattern.compile("[^a-zA-Z0-9 _]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(roomName);
        Matcher m2 = p2.matcher(roomName);
        Matcher m3 = p3.matcher(roomName);
        boolean b = m.find();
        boolean b2 = m2.find();
        boolean b3 = m3.find();

        if (!b || !b2 || b3) {
            System.out.println("not added");
            new Warning(Warning.WarningType.INVALID_NAME);
            return;
        }
        roomName = roomName.replaceAll("\\s", "_");
        for (String usedRoomName : usedRoomNames) {
            if (roomName.equals(usedRoomName)) {
                new Warning(Warning.WarningType.ALREADY_USED);
                return;
            }
        }
        listView.getItems().add(roomName);
        try {
            JSONHandler.post("http://localhost:8080/room/add", "{\"name\": " + "\"" + roomName + "\"" + "}");
        } catch (IOException e) {
            Main.serverOn = false;
            new Warning(Warning.WarningType.SERVER_DOWN);
            return;
        }
        usedRoomNames.add(roomName);
        System.out.println(roomName);
    }

    public void removeRoom() {
        int index = listView.getSelectionModel().getSelectedIndex();
        if (index < 0) return;
        removingInProcess = true;
        String roomName = listView.getSelectionModel().getSelectedItem();
        listView.getItems().remove(index);

        sensorPaneId.setVisible(false);
        StatisticsController.roomNamesIDs.remove(roomName);
        currentlyChosenRoomName = null;
        usedRoomNames.remove(roomName);
        roomsToDelete.add(roomName);
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

}

