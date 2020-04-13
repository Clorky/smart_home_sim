package com.company.sensorsAPI;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StatisticsData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer roomId;
    private double temperature;
    private double currentConsumption;
    private double lightsOnNumberInHours;

    public StatisticsData() {
    }

    public StatisticsData(Integer roomId, double temperature, double currentConsumption, double lightsOnNumberInHours){
        this.roomId = roomId;
        this.temperature = temperature;
        this.currentConsumption = currentConsumption;
        this.lightsOnNumberInHours = lightsOnNumberInHours;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
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
}
