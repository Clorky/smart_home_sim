package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class RoomsController {

    @FXML
    private ListView<String> listView;

    @FXML
    private void initialize() throws Exception {
        listView.getItems().addAll("Bathroom", "Livingroom", "Kitchen");

//        System.out.println(JSONHandler.get("http://localhost:8080/sensor/sensors"));
    }

    public void goBack(ActionEvent evt) throws IOException {
        Parent mainViewParent = FXMLLoader.load(getClass().getResource("mainHub.fxml"));
        Scene mainView = new Scene(mainViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(mainView);
        window.show();
    }

    public void addRoom() throws IOException{
        JSONHandler.post("http://localhost:8080/sensor/add", "{\"name\": \"test room278\"}");
    }

    public void deleteRoom() throws  IOException{
        JSONHandler.post("http://localhost:8080/sensor/delete", "{\"name\": \"bedroom231\"}");
    }
}
