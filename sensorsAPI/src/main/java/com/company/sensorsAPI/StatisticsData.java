package com.company.sensorsAPI;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StatisticsData { //odkud ty data dostanu? jak?

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String roomName;
    private double temperature;
    private double currentConsumption;
    private double lightsOnNumberInHours;
    private boolean wasHeated;

    public StatisticsData() {
    }

    public StatisticsData(String roomName, double temperature, double currentConsumption, double lightsOnNumberInHours, boolean wasHeated){
        this.roomName = roomName;
        this.temperature = temperature;
        this.currentConsumption = currentConsumption;
        this.lightsOnNumberInHours = lightsOnNumberInHours;
        this.wasHeated = wasHeated;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getCurrentConsumption() {
        return currentConsumption;
    }

    public void setCurrentConsumption(double currentConsumption) {
        this.currentConsumption = currentConsumption;
    }

    public double getLightsOnNumberInHours() {
        return lightsOnNumberInHours;
    }

    public void setLightsOnNumberInHours(double lightsOnNumberInHours) {
        this.lightsOnNumberInHours = lightsOnNumberInHours;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public boolean isWasHeated() {
        return wasHeated;
    }

    public void setWasHeated(boolean wasHeated) {
        this.wasHeated = wasHeated;
    }
}
