package application.warningSystem;

import application.Main;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Optional;

import static application.Main.checkServerConnection;

public class Warning {

    public static StringProperty labelProperty = new SimpleStringProperty();
    private int counter = 0;
    private ButtonType appOffButton = new ButtonType("Vypnout aplikaci", ButtonBar.ButtonData.LEFT);
    private Alert alert;
    private Stage window;
    private Label label;

    public Warning(WarningType warningType) {
        if (warningType == WarningType.SERVER_DOWN) {

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
                if (!Main.serverOn) windowEvent.consume();
            });

            Optional a = alert.showAndWait();
            if (a.isPresent() && a.get() == appOffButton) {
                Main.running = false;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Platform.exit();
                    }
                });
            }

        }
        if (warningType == WarningType.INVALID_NAME) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Zadejte jiné jméno místnosti", ButtonType.OK);
            alert.getDialogPane().setMinWidth(200);
            alert.showAndWait();
        }
        if (warningType == WarningType.ALREADY_USED) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Místnost nebyla přidána, protože její název už existuje", ButtonType.OK);
            alert.getDialogPane().setMinWidth(200);
            alert.showAndWait();
        }
    }

    private synchronized void initServerDown() {
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                while (!Main.serverOn && Main.running) {
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




