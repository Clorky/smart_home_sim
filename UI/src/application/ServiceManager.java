package application;

import javafx.application.Platform;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static application.Main.checkServerConnection;

public class ServiceManager extends Task {

    private boolean removalInProcess;
    private List<Controller> controllerList = new ArrayList<>();

    private int sleepTime = 1000;

    @Override
    protected Object call() throws Exception {  //TODO: mistnosti -> oznaceni mistnosti -> vypnuti serveru -> updatovani hodnot - neco spatne

        while(Main.running || removalInProcess){

            Thread.sleep(sleepTime);

            Main.serverOn = checkServerConnection();

            for (Controller controller : controllerList) {
                controller.requestData();
                controller.update();
            }

            if(!Main.serverOn) continue;

            if(!RoomsController.roomsToDelete.isEmpty()) {
                removalInProcess = true;
                try {
                    JSONHandler.post("http://localhost:8080/room/delete", "{\"name\": " + "\"" + RoomsController.roomsToDelete.get(0) + "\"" + "}");
                    System.out.println(RoomsController.roomsToDelete.get(0) + " removed");
                    RoomsController.roomsToDelete.remove(0);

                } catch (IOException e) {
                    Main.serverOn = false;
                    continue;
                }
            }
            if(RoomsController.roomsToDelete.isEmpty()) {
                removalInProcess = false;
            }

            System.out.println("working");

        }

        return null;
    }

    public void addToControllerList(Controller controller) {
        controllerList.add(controller);
    }

}
