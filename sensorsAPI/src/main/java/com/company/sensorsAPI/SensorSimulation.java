package com.company.sensorsAPI;

import com.company.sensorsAPI.entities.Sensor;
import com.company.sensorsAPI.listeners.MyListener;
import com.company.sensorsAPI.networking.JSONHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SensorSimulation implements Runnable{

    public static List<Sensor> sensors;
    public SensorSimulation() {
        sensors = new ArrayList<>();
    }
    public static final int WAIT_TIME = 5000;
    private Random random = new Random();
    private double elect, light;
    private final double TEMPERATURE_CONST = 0.1;
    public static boolean deleteFlag = false;

    @Override
    public void run() {

        while(true) {
                while (MyListener.running) {
                    synchronized (this) {
                    deleteFlag = false;
                    System.out.println("simulation working");

                    sensors.clear();
                    JSONParser parser = new JSONParser();
                    try {
                        String sensorsJSON = JSONHandler.get("http://localhost:8080/sensors/all");
                        System.out.println(sensorsJSON);
                        Object obj = parser.parse(sensorsJSON);
                        JSONArray arr = (JSONArray) obj;

                        for (int i = 0; i < arr.size(); i++) {
                            JSONObject jsob = (JSONObject) arr.get(i);
                            String id = jsob.get("id").toString();
                            String temperatureJSON = jsob.get("temperature").toString();
                            String currentConsJSON = jsob.get("currentConsumption").toString();
                            String lightsOnNumberInSecondsJSON = jsob.get("lightsOnNumberInSeconds").toString();
                            String sensorNameJSON = jsob.get("sensorName").toString();
                            String isHeatedJSON = jsob.get("heated").toString();
                            String requestedTemp = jsob.get("requestedTemp").toString();
                            Sensor sensorFromJSON = new Sensor(Integer.parseInt(id), Double.parseDouble(temperatureJSON),
                                    Double.parseDouble(currentConsJSON), Double.parseDouble(lightsOnNumberInSecondsJSON),
                                    sensorNameJSON, Boolean.parseBoolean(isHeatedJSON), Double.parseDouble(requestedTemp));
                            sensors.add(sensorFromJSON);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (Sensor sensor : sensors) {

                        elect = 0.0 + (2.25347222 - 0.0) * random.nextDouble();
                        light = random.nextInt(WAIT_TIME/1000 + 1);

                        if (sensor.getTemperature() <= sensor.getRequestedTemp()) {
                            sensor.setHeated(true);
                        } else {
                            sensor.setHeated(false);
                        }

                        if (sensor.isHeated()) {
                            BigDecimal bd = new BigDecimal(sensor.getTemperature() + TEMPERATURE_CONST).setScale(2, RoundingMode.HALF_UP);
                            sensor.setTemperature(Double.parseDouble(bd.toString()));
                        } else {
                            BigDecimal bd = new BigDecimal(sensor.getTemperature() - TEMPERATURE_CONST).setScale(2, RoundingMode.HALF_UP);
                            sensor.setTemperature(Double.parseDouble(bd.toString()));
                        }

                        sensor.setCurrentConsumption(elect);
                        sensor.setLightsOnNumberInSeconds(light);

                        try {
                            JSONHandler.post("http://localhost:8080/sensors/update/" + sensor.getId(), "{\"id\"" +
                                    ": " + sensor.getId() + ", \"temperature\": " + sensor.getTemperature() + ", \"currentConsumption\": " + elect +
                                    ", \"lightsOnNumberInSeconds\": " + light + ", \"heated\": " + sensor.isHeated() +
                                    ", \"requestedTemp\": " + sensor.getRequestedTemp() + "}");
                        } catch (IOException e) {
                            System.out.println("problem is here");
                            e.printStackTrace();
                        }
                    }
                    deleteFlag = true;
                    waitTime();
                }
            }
        }
    }

    private void waitTime(){
        try{
            Thread.sleep(WAIT_TIME);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
