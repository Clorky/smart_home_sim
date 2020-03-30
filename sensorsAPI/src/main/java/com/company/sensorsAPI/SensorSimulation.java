package com.company.sensorsAPI;

import java.util.ArrayList;

public class SensorSimulation implements Runnable{

    public static ArrayList<Sensor> sensors;

    public SensorSimulation() {
        sensors = new ArrayList<>();
    }

    @Override
    public void run() {



        while(true){

            if(!sensors.isEmpty()) System.out.println(sensors.get(0).getRoomId());

            waitDay();
        }

    }

    public static void addSensor(Sensor sensor){
        sensors.add(sensor);
    }

    private void waitDay(){
        try{
            Thread.sleep(10000);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
