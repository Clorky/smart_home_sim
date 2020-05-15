package com.company.sensorsAPI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

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
    private final int waitTime = 1000;
    private Random random = new Random();
    private double elect, light;
    private final double TEMPERATURE_CONST = 0.1;

    @Override
    public void run() {

        while(true) {
            while (MyListener.running) {

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
                        String lightsOnNumberInHoursJSON = jsob.get("lightsOnNumberInHours").toString();
                        String sensorNameJSON = jsob.get("sensorName").toString();
                        String isHeatedJSON = jsob.get("heated").toString();
                        String requestedTemp = jsob.get("requestedTemp").toString();
                        Sensor sensorFromJSON = new Sensor(Integer.parseInt(id), Double.parseDouble(temperatureJSON),
                                Double.parseDouble(currentConsJSON), Double.parseDouble(lightsOnNumberInHoursJSON),
                                sensorNameJSON, Boolean.parseBoolean(isHeatedJSON), Double.parseDouble(requestedTemp));
                        sensors.add(sensorFromJSON);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (Sensor sensor : sensors) {

                    elect = random.nextInt(500-300)+300;
                    light = random.nextInt(24);

                    if (sensor.getTemperature() <= sensor.getRequestedTemp()){
                        sensor.setHeated(true);
                    }else{
                        sensor.setHeated(false);
                    }

                    if(sensor.isHeated()) {
                        BigDecimal bd = new BigDecimal(sensor.getTemperature() + TEMPERATURE_CONST).setScale(2, RoundingMode.HALF_UP);
                        sensor.setTemperature(Double.parseDouble(bd.toString()));
                    }
                    else {
                        BigDecimal bd = new BigDecimal(sensor.getTemperature() - TEMPERATURE_CONST).setScale(2, RoundingMode.HALF_UP);
                        sensor.setTemperature(Double.parseDouble(bd.toString()));
                    }

                    sensor.setCurrentConsumption(elect);
                    sensor.setLightsOnNumberInHours(light);

                    try {
                        JSONHandler.post("http://localhost:8080/sensors/update/" + sensor.getId(), "{\"id\"" +
                                ": " + sensor.getId() +  ", \"temperature\": " + sensor.getTemperature() + ", \"currentConsumption\": " + elect +
                                ", \"lightsOnNumberInHours\": " + light + ", \"isHeated\": " + sensor.isHeated() +
                                ", \"requestedTemp\": " + sensor.getRequestedTemp() + "}");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                waitTime();
            }
        }
    }


    private void waitTime(){
        try{
            Thread.sleep(waitTime);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
