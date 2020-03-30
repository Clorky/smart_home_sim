package com.company.sensorsAPI;

public class Sensor {

    private Integer roomId;
    private double temperature;
    private double currentConsumption;
    private double lightsOnNumberInHours;

    public Sensor(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getCurrentConsumption() {
        return currentConsumption;
    }

    public double getLightsOnNumberInHours() {
        return lightsOnNumberInHours;
    }
}
