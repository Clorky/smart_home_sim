package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class RoomsController implements Controller { //TODO: refactor a chytání hmyzu (místnosti - vypnu server - main hub (goback) - klik mistnosti - dialog - crash)

    @FXML
    public void handleMouseClick(MouseEvent arg0) {
        System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem());
    }

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
    public static List<String> roomsToDelete = new ArrayList<>();

    private String currentlyChosenRoomName = null; //TODO: vytvorit tridu Room s těmito atributy

    private double electricityUsage = 0;
    private double lightTime = 0;
    private double currentTemperature = 0;

    public static boolean removingInProcess = false;

    @FXML
    private void initialize() {

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
        Main.serviceManager.addToControllerList(this);
    }

    public boolean update() {
        if (currentlyChosenRoomName == null) return false;
        Platform.runLater(() -> {
            sensorNameLabel.setText(currentlyChosenRoomName + "_sensor");
            temperatureLabel.setText(Main.df.format(currentTemperature) + " °C");
            powerConsumptionAtmLabel.setText(Main.df.format(electricityUsage) + " W");
            lightsOnTimeLabel.setText(Main.df.format(lightTime) + " hodin");
        });
        return true;
    }

    public void requestData() {

        if (currentlyChosenRoomName == null) return;
        try {
            String jsonString = JSONHandler.get("http://localhost:8080/sensors/sensor/" + currentlyChosenRoomName + "_sensor");

            org.json.JSONObject obj = new org.json.JSONObject(jsonString);
            currentTemperature = Double.parseDouble(obj.get("temperature").toString());
            electricityUsage = Double.parseDouble(obj.get("currentConsumption").toString());
            lightTime = Double.parseDouble(obj.get("lightsOnNumberInHours").toString());
        } catch (Exception e) {
            Main.serverOn = false;
            new Warning(Warning.WarningType.SERVER_DOWN);
            return;
        }
    }

    public void addRoom() {

        String roomName = textAddTextField.getText(); //room name se odvodi z text fieldu z UI

        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(roomName);
        boolean b = m.find();

        if (roomName.trim().length() == 0 || b) {
            System.out.println("not added");
            new Warning(Warning.WarningType.INVALID_NAME);
            return;
        }
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
    }

    public void removeRoom() {
        int index = listView.getSelectionModel().getSelectedIndex();
        if (index < 0) return;
        removingInProcess = true;
        String roomName = listView.getSelectionModel().getSelectedItem();
        listView.getItems().remove(index);

        sensorPaneId.setVisible(false);

        currentlyChosenRoomName = null;
        usedRoomNames.remove(roomName);
        roomsToDelete.add(roomName);
    }

    public void changeScreenStatistics(ActionEvent evt) throws IOException {
        if (!serverOn) new Warning(Warning.WarningType.SERVER_DOWN);
        Parent roomsViewParent = FXMLLoader.load(getClass().getResource("statistiky.fxml"));
        Scene roomsView = new Scene(roomsViewParent);

        Stage window = (Stage) ((Node) evt.getSource()).getScene().getWindow();

        window.setScene(roomsView);
        window.show();
    }

    public void goBack(ActionEvent evt) throws IOException {
        if (!serverOn) new Warning(Warning.WarningType.SERVER_DOWN);
        Parent mainViewParent = FXMLLoader.load(getClass().getResource("mainHub.fxml"));
        Scene mainView = new Scene(mainViewParent);

        Stage window = (Stage) ((Node) evt.getSource()).getScene().getWindow();

        window.setScene(mainView);
        window.show();
    }
}

