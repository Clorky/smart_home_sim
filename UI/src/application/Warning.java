package application;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Optional;

public class Warning{ //TODO: zamknou okno, vykutit tři tečky :)

    private Alert alert;
    private Stage window;
    private Label label;
    int counter = 0;
    ButtonType appOffButton = new ButtonType("Vypnout aplikaci", ButtonBar.ButtonData.LEFT);

    public static StringProperty labelProperty = new SimpleStringProperty();

    public Warning(WarningType warningType) {
        if(warningType == WarningType.SERVER_DOWN) {

            label = new Label();
            label.setWrapText(true);
            label.setAlignment(Pos.CENTER);
            label.textProperty().bindBidirectional(labelProperty);

            alert = new Alert(Alert.AlertType.WARNING, "Server down", appOffButton);
            alert.getButtonTypes().set(0, appOffButton);
            alert.getDialogPane().setMinWidth(200);
            alert.getDialogPane().setContent(label);

            window = (Stage) alert.getDialogPane().getScene().getWindow();

            initServerDown();

            window.setOnCloseRequest(windowEvent -> { //konzumuje event na zavreni okna - okno se neda zavrit
                if(!Main.serverOn) windowEvent.consume();
            });

            Optional a = alert.showAndWait();
            if(a.isPresent() && a.get() == appOffButton) Platform.exit();

        }
        if(warningType == WarningType.INVALID_NAME) {

        }
    }

    private void initServerDown() {
        Task task = new Task<Void>() {
            @Override public Void call() {
                while(!Main.serverOn && Main.running) {
                    counter++;
                    Platform.runLater(() -> {
                        labelProperty.setValue("Retrying connection (" + counter + ")");
                    });
                    Main.serverOn = checkServerConnection();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (Main.serverOn && alert.isShowing()) {
                    Platform.runLater(() -> {
                        alert.close();
                    });
                }
                return null;
            }
        };
        new Thread(task).start();
}
    private boolean checkServerConnection(){

        try {
            URL url = new URL("http://localhost:8080/sensors");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(100);
            if(conn.getResponseCode() < 300)
                return true;
        } catch(Exception e) {
            System.out.println("fail");
            return false;
        }
        return true;
    }

    public enum WarningType {
        SERVER_DOWN, INVALID_NAME, ALREADY_USED
    }
}




