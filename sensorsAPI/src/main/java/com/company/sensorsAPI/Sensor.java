package com.company.sensorsAPI;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String sensorName;
    private boolean isHeated;
    private double requestedTemp;
    private double temperature;
    private double currentConsumption;
    private double lightsOnNumberInHours;

    public Sensor(){

    }

    public Sensor(double temperature, double currentConsumption, double lightsOnNumberInHours, String sensorName, boolean isHeated, double requestedTemp) {
        this.requestedTemp = requestedTemp;
        this.isHeated = isHeated;
        this.temperature = temperature;
        this.currentConsumption = currentConsumption;
        this.lightsOnNumberInHours = lightsOnNumberInHours;
        this.sensorName = sensorName;
    }

    public Sensor(Integer id, double temperature, double currentConsumption, double lightsOnNumberInHours, String sensorName, boolean isHeated, double requestedTemp) {
        this.id = id;
        this.isHeated = isHeated;
        this.requestedTemp = requestedTemp;
        this.temperature = temperature;
        this.currentConsumption = currentConsumption;
        this.lightsOnNumberInHours = lightsOnNumberInHours;
        this.sensorName = sensorName;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setCurrentConsumption(double currentConsumption) {
        this.currentConsumption = currentConsumption;
    }

    public void setLightsOnNumberInHours(double lightsOnNumberInHours) {
        this.lightsOnNumberInHours = lightsOnNumberInHours;
    }
    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String roomName) {
        this.sensorName = roomName;
    }

    public double getRequestedTemp() {
        return requestedTemp;
    }

    public void setRequestedTemp(double requestedTemp) {
        this.requestedTemp = requestedTemp;
    }

    public boolean isHeated() {
        return isHeated;
    }

    public void setHeated(boolean heated) {
        isHeated = heated;
    }
}
