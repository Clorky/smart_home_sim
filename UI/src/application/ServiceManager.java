package application;

import application.controllers.Controller;
import application.controllers.RoomsController;
import application.networking.JSONHandler;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.IOException;

public class ServiceManager extends Task {

    private boolean removalInProcess;
    private Controller controller = null;

    private final int SLEEP_TIME = 1000;

    @Override
    protected Object call() throws Exception {

        while (Main.running || removalInProcess) {
            synchronized (this) {

                Thread.sleep(SLEEP_TIME);

                if (!Main.serverOn) continue;

                if (controller != null) {
                    Platform.runLater(() -> {
                        controller.requestData();
                        controller.update();
                    });
                }

                if (!RoomsController.roomsToDelete.isEmpty()) {
                    removalInProcess = true;
                    try {
                        JSONHandler.post("http://localhost:8080/room/delete", "{\"name\": " + "\"" + RoomsController.roomsToDelete.get(0) + "\"" + "}");
                        System.out.println(RoomsController.roomsToDelete.get(0) + " removed");
                        RoomsController.roomsToDelete.remove(0);

                    } catch (IOException e) {
                        Main.serverOn = false;
                        removalInProcess = false;
                        continue;
                    }
                }
                if (RoomsController.roomsToDelete.isEmpty()) {
                    removalInProcess = false;
                }

            }

        }
        return null;
    }

    public void setController(Controller controller) {

        this.controller = controller;
    }
}
