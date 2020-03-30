package com.company.sensorsAPI;

public class SensorFactory {

    public static void createSensor(Room room){
        SensorSimulation.addSensor(new Sensor(room.getId()));
    }
}
