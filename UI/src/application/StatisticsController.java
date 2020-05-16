package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static application.Main.serverOn;

public class StatisticsController {

    public void goBack(ActionEvent evt) throws IOException {
        Parent mainViewParent = FXMLLoader.load(getClass().getResource("mainHub.fxml"));
        Scene mainView = new Scene(mainViewParent);
        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();
        window.setScene(mainView);

        window.show();
    }
    public void changeScreenRooms(ActionEvent evt) throws IOException {
        if(!serverOn) new Warning(Warning.WarningType.SERVER_DOWN);
        Parent roomsViewParent = FXMLLoader.load(getClass().getResource("mistnosti.fxml"));
        Scene roomsView = new Scene(roomsViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(roomsView);
        window.show();
    }
}
