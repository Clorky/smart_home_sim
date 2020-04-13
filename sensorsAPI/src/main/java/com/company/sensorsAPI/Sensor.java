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
    private Integer roomId;
    private double temperature;
    private double currentConsumption;
    private double lightsOnNumberInHours;

    public Sensor(){

    }

    public Sensor(Integer id, double temperature, double currentConsumption, double lightsOnNumberInHours) {
        this.id = id;
        this.temperature = temperature;
        this.currentConsumption = currentConsumption;
        this.lightsOnNumberInHours = lightsOnNumberInHours;
    }

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
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
}
