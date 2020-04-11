package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StatisticsController {

    public void goBack(ActionEvent evt) throws IOException {
        Parent mainViewParent = FXMLLoader.load(getClass().getResource("mainHub.fxml"));
        Scene mainView = new Scene(mainViewParent);

        Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        window.setScene(mainView);
        window.show();
    }
}
