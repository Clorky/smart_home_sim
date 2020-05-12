package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.text.DecimalFormat;

public class Main extends Application { //TODO: historická data, generované hodnoty vylepšit, toast, validateserverconnection jestli je všude jak má být, ošetření když jsi v místnostech a padne server,

    public static boolean serverOn = false;
    public static boolean running = true;
    public static DecimalFormat df = new DecimalFormat("#.0");
    public static int counter = 0;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainHub.fxml"));
        primaryStage.setTitle("Smart Home Simulation");
        primaryStage.setScene(new Scene(root, 900, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        running = false;
    }

    public static boolean validateServerConnection(){
        String s = null;
        try {
            s = JSONHandler.get("http://localhost:8080/sensors/all");
        } catch (Exception e) {
            serverOn = false;
            System.out.println("Server neběží." + counter++);
            return false;
        }
        System.out.println("validating " + counter++);
        serverOn = true;
    return true;
    }
}
