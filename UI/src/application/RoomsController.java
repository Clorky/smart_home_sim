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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoomsController { //TODO: refactor a chytání hmyzu (místnosti - vypnu server - main hub (goback) - klik mistnosti - dialog - crash)
    @FXML public void handleMouseClick(MouseEvent arg0) {
        System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem());
    }
    @FXML
    private Pane sensorPaneId;
    @FXML
    private ListView<String> listView;
    @FXML
    private TextField textAdd;
    @FXML
    private Label sensorName;
    @FXML
    private Label lightsOnTime;
    @FXML
    private Label temperature;
    @FXML
    private Label powerConsumptionAtm;

    private List<String> usedRoomNames;
    private String roomName;
    private double electricityUsage, lightTime, currentTemperature;
    private DataReceiver dr;

    private List<String> roomsToDelete;

    public static boolean removingInProcess = false;

    @FXML
    private void initialize() {
        if(!Main.serverOn)
            return;
        roomsToDelete = new ArrayList<>();
        dr = new DataReceiver(this);
        Thread thread = new Thread(dr);
        thread.start();

        listView.setOnMouseClicked(mouseEvent -> {
            dr.setRoomName(listView.getSelectionModel().getSelectedItem());
            sensorPaneId.setVisible(true);
            if(listView.getSelectionModel().getSelectedItem() == null){
                sensorPaneId.setVisible(false);
            }
        });

        usedRoomNames = new ArrayList<>();

        String roomName = null;

        try {
            roomName = JSONHandler.get("http://localhost:8080/room/all");
        } catch (Exception e) {
            Main.serverOn = false;
            new Warning(Warning.WarningType.SERVER_DOWN);
        }

        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(roomName);
            JSONArray arr = (JSONArray) obj;

            for (int i = 0; i < arr.size(); i++) {
                JSONObject jsob = (JSONObject) arr.get(i);
                listView.getItems().add(jsob.get("name").toString());
                usedRoomNames.add(jsob.get("name").toString());
            }
        }catch(ParseException e){
            System.out.println("Chyba parsování.");
        }
    }

    public void update(){
        Platform.runLater(()->{
            sensorName.setText(roomName + "_sensor");
            temperature.setText(Main.df.format(currentTemperature) + " °C");
            powerConsumptionAtm.setText(Main.df.format(electricityUsage) + " W");
            lightsOnTime.setText(Main.df.format(lightTime) + " hodin");
        });
    }
    public void updateRemoval(){
        if(roomsToDelete.isEmpty()) {
            removingInProcess = false;
            return;
        }
        else removingInProcess = true;

        try {
            JSONHandler.post("http://localhost:8080/room/delete", "{\"name\": " + "\"" + roomsToDelete.get(0) + "\"" + "}");
            System.out.println("removed");
            roomsToDelete.remove(0);
        } catch (IOException e) {
            new Warning(Warning.WarningType.SERVER_DOWN);
        }
    }

    public void goBack(ActionEvent evt) throws IOException {
        dr.cancel();
        Parent mainViewParent = FXMLLoader.load(getClass().getResource("mainHub.fxml"));
        Scene mainView = new Scene(mainViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(mainView);
        window.show();
    }

    public void addButtonRoom(){

        String roomName = textAdd.getText();

        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(roomName);
        boolean b = m.find();

        if(roomName.trim().length() == 0 || b){
            System.out.println("not added");
            Main.showMsgWarn("Místnost nebyla přidána, zvolte jiný název.");
            return;
        }
        for (String usedRoomName : usedRoomNames) {
            if(roomName.equals(usedRoomName)){
                Main.showMsgWarn("Místnost nebyla přidána, protože její název už existuje.");
                return;
            }
        }
        listView.getItems().add(roomName);
        try {
            JSONHandler.post("http://localhost:8080/room/add", "{\"name\": " + "\"" + roomName + "\"" + "}");
        } catch (IOException e) {
            Main.serverOn = false;
            new Warning(Warning.WarningType.SERVER_DOWN);
        }
        usedRoomNames.add(roomName);
    }

    public void removeRoom(){
        removingInProcess = true;
        int index = listView.getSelectionModel().getSelectedIndex();
        String roomName = listView.getSelectionModel().getSelectedItem();
        if (index >= 0) {
            listView.getItems().remove(index);
        }
            sensorPaneId.setVisible(false);

        dr.setRoomName(null);
        usedRoomNames.remove(roomName);
        roomsToDelete.add(roomName);
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }
    public double getElectricityUsage() {
        return electricityUsage;
    }

    public void setElectricityUsage(double electricityUsage) {
        this.electricityUsage = electricityUsage;
    }

    public double getLightTime() {
        return lightTime;
    }

    public void setLightTime(double lightTime) {
        this.lightTime = lightTime;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;


    }
}

