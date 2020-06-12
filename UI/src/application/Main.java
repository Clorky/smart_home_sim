package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class Main extends Application {

    public static boolean serverOn = true;
    public static boolean running = true;
    public static ServiceManager serviceManager = new ServiceManager();
    public static DecimalFormat df = new DecimalFormat("#0.0");
    public static DecimalFormat dfW = new DecimalFormat("#0");

    public static void main(String[] args) {
        Thread thread = new Thread(serviceManager, "service manager thread from main");
        thread.start();
        launch(args);
        running = false;
    }

    public static boolean checkServerConnection() {

        try {
            URL url = new URL("http://localhost:8080/sensors");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(100);
            if (conn.getResponseCode() < 300) return true;

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("UIResources/mainHub.fxml"));
        primaryStage.setTitle("Smart Home Simulation");
        primaryStage.setScene(new Scene(root, 900, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
