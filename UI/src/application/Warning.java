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

import static application.Main.checkServerConnection;

public class Warning{ //TODO: zkontrolovat vsechny warningy jestli jsou ok (to co se po nich spusti)

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
            if(a.isPresent() && a.get() == appOffButton) {
                Main.running = false;
                Platform.runLater(() -> {
                    Platform.exit();
                });
            }

        }
        if(warningType == WarningType.INVALID_NAME) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Zadejte jiné jméno místnosti", ButtonType.OK);
            alert.getDialogPane().setMinWidth(200);
            alert.showAndWait();
        }
        if(warningType == WarningType.ALREADY_USED) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Místnost nebyla přidána, protože její název už existuje", ButtonType.OK);
            alert.getDialogPane().setMinWidth(200);
            alert.showAndWait();
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
        new Thread(task, "init server down thread from Warning").start();
}

    public enum WarningType {
        SERVER_DOWN, INVALID_NAME, ALREADY_USED
    }
}




