package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;

public class RoomsController {

    @FXML
    private ComboBox<String> combo_box;

    @FXML
    private void initialize() throws Exception {
        combo_box.getItems().addAll("hello", "what", "up");
        System.out.println(JSONHandler.get("http://localhost:8080/sensor/sensors"));
    }

    public void goBack(ActionEvent evt) throws IOException {
        Parent mainViewParent = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene mainView = new Scene(mainViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(mainView);
        window.show();
    }

    public void addRoom() throws IOException{
        JSONHandler.post("http://localhost:8080/sensor/add", "{\"name\": \"room\"}");
    }

    //TODO: add JSON parser library
}
