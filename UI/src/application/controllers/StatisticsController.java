package application.controllers;

import application.Main;
import application.ServiceManager;
import application.networking.JSONHandler;
import application.warningSystems.Warning;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static application.Main.serverOn;

public class StatisticsController implements Controller {

    @FXML
    private ListView<String> roomsListView;
    @FXML
    private ListView<String> monthsListView;

    private String[] months = {"Leden", "Únor", "Březen", "Duben", "Květen", "Červen", "Červenec", "Srpen", "Září", "Říjen", "Listopad", "Prosinec"};
    private List<String> roomNames = new ArrayList<>();
    private String currentlyChosenMonth = "Leden";
    private String currentlyChosenRoomName = null;
    private boolean listViewSet;
    private boolean dataRequested;

    //statistické hodnoty se vypočítají na serveru a pošlou se zpracované

    @FXML
    private synchronized void initialize() {  //TODO: usedRoomNames z roomsController se naplní až při spuštění metody initialize() dané třídy

        listViewSet = false;
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

        if(!listViewSet) {
            for (int i = 0; i < roomNames.size(); i++) {
                roomsListView.getItems().add(i, roomNames.get(i));
            }
            listViewSet = true;
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
                    roomNames.add(jsob.get("name").toString());
                }
                dataRequested = true;
            } catch (Exception e) {
                Main.serverOn = false;
                new Warning(Warning.WarningType.SERVER_DOWN);
                dataRequested = false;
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
