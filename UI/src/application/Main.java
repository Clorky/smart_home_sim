package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javafx.stage.Stage;

import java.text.DecimalFormat;

public class Main extends Application { //TODO: Statistiky, testy, diagramy :)

    public static boolean serverOn = false;
    public static boolean running = true;
    public static ServiceManager serviceManager = new ServiceManager();
    public static DecimalFormat df = new DecimalFormat("#.0");

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainHub.fxml"));
        primaryStage.setTitle("Smart Home Simulation");
        primaryStage.setScene(new Scene(root, 900, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Thread thread = new Thread(serviceManager);
        thread.start();
        launch(args);
        running = false;
        serviceManager.cancel();
    }


    public static void showMsgWarn(String message){ //TODO: nahradit vlastními warningy
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }
}
