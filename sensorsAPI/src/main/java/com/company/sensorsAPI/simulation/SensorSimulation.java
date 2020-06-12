package com.company.sensorsAPI.simulation;

import com.company.sensorsAPI.controllers.StatisticsDataController;
import com.company.sensorsAPI.entities.Sensor;
import com.company.sensorsAPI.listeners.MyListener;
import com.company.sensorsAPI.networking.JSONHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class SensorSimulation implements Runnable {

    public static final int WAIT_TIME = 300000;
    public static List<Sensor> sensors;
    public static boolean deleteFlag = false;
    private final double TEMPERATURE_CONST = 0.5;
    HashMap<String, HashMap<String, List<Double>>> formattedStatisticsData = new HashMap<>();
    boolean databaseFillFlag = false;
    int timer = 0;
    private final Random random = new Random();
    private double elect;
    private boolean light;
    private final String[] months = {"Leden", "Únor", "Březen", "Duben", "Květen", "Červen", "Červenec", "Srpen", "Září", "Říjen", "Listopad", "Prosinec"};
    private final Map<String, Integer> roomNamesIDs = new HashMap<>();
    public SensorSimulation() {
        sensors = new ArrayList<>();
    }

    @Override
    public void run() {

        while (true) {
            while (MyListener.running) {

                if (databaseFillFlag) {
                    try {
                        JSONHandler.get("http://localhost:8080/database/fill");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    databaseFillFlag = false;
                }

                if (!StatisticsDataController.initialized || timer >= 60) { // uloží do cache hodnoty každou hodinu
                    try {
                        JSONHandler.get("http://localhost:8080/statistics_data/init");
                        StatisticsDataController.initialized = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    timer = 0;
                }

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
                            String lightsOnJSON = jsob.get("lightsOn").toString();
                            String sensorNameJSON = jsob.get("sensorName").toString();
                            String isHeatedJSON = jsob.get("heated").toString();
                            String requestedTemp = jsob.get("requestedTemp").toString();
                            Sensor sensorFromJSON = new Sensor(Integer.parseInt(id), Double.parseDouble(temperatureJSON),
                                    Double.parseDouble(currentConsJSON), Boolean.parseBoolean(lightsOnJSON),
                                    sensorNameJSON, Boolean.parseBoolean(isHeatedJSON), Double.parseDouble(requestedTemp));
                            sensors.add(sensorFromJSON);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (Sensor sensor : sensors) {

                        elect = random.nextInt((25 - 15) + 1) + 15;
                        light = random.nextBoolean();

                        sensor.setHeated(sensor.getTemperature() <= sensor.getRequestedTemp());

                        if (sensor.isHeated()) {
                            BigDecimal bd = new BigDecimal(sensor.getTemperature() + TEMPERATURE_CONST).setScale(2, RoundingMode.HALF_UP);
                            sensor.setTemperature(Double.parseDouble(bd.toString()));
                        } else {
                            BigDecimal bd = new BigDecimal(sensor.getTemperature() - TEMPERATURE_CONST).setScale(2, RoundingMode.HALF_UP);
                            sensor.setTemperature(Double.parseDouble(bd.toString()));
                        }

                        sensor.setCurrentConsumption(elect);
                        sensor.setlightsOn(light);

                        try {
                            JSONHandler.post("http://localhost:8080/sensors/update/" + sensor.getId(), "{\"id\"" +
                                    ": " + sensor.getId() + ", \"temperature\": " + sensor.getTemperature() + ", \"currentConsumption\": " + elect +
                                    ", \"lightsOn\": " + light + ", \"heated\": " + sensor.isHeated() +
                                    ", \"requestedTemp\": " + sensor.getRequestedTemp() + "}");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    deleteFlag = true;
                    waitTime();
                    timer += 5;
                }
            }
        }
    }

    private void waitTime() {
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
