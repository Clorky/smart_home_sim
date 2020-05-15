package application;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import org.json.JSONObject;

public class DataReceiver extends Task {

    private final int sleepTime = 1000;

    private RoomsController roomsController;
    private String roomName;
    private double temp;
    private double elect;
    private double light;
    private String sensorName;
    private String jsonString;

    public DataReceiver(RoomsController roomsController) {
        this.roomsController = roomsController;
    }

    @Override
    public void run() {

        while(Main.running || RoomsController.removingInProcess){ //background worker
            if(this.isCancelled()) break;
            roomsController.updateRemoval();
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(roomName == null) continue;
            try {
                jsonString = JSONHandler.get("http://localhost:8080/sensors/sensor/" + roomName + "_sensor");
            } catch (Exception e) {
                Main.serverOn = false;
                Platform.runLater(() -> {
                    new Warning(Warning.WarningType.SERVER_DOWN);
                });
                continue;
            }

            JSONObject obj = new JSONObject(jsonString);
            temp = Double.parseDouble(obj.get("temperature").toString());
            elect = Double.parseDouble(obj.get("currentConsumption").toString());
            light = Double.parseDouble(obj.get("lightsOnNumberInHours").toString());
            sensorName = obj.get("sensorName").toString();


            roomsController.setCurrentTemperature(temp);
            roomsController.setElectricityUsage(elect);
            roomsController.setLightTime(light);
            roomsController.setRoomName(roomName);
            roomsController.update();
        }
    }

    @Override
    public boolean cancel(boolean b) {
        return super.cancel(b);
    }



    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    protected Object call() throws Exception {
        return null;
    }
}
