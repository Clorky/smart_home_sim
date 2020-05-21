package com.company.sensorsAPI.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class StatisticsData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statistics_id;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="sensor_fk")
    private Sensor sensor;
    private String sensorName;
    private double temperature;
    private double currentConsumption;
    private double lightsOnNumberInSeconds;
    private boolean wasHeated;
    @CreationTimestamp
    private Timestamp timestamp;

    public StatisticsData() {
    }

    public StatisticsData(String sensorName, double temperature, double currentConsumption, double lightsOnNumberInSeconds, boolean wasHeated, Sensor sensor){
        this.sensorName = sensorName;
        this.temperature = temperature;
        this.currentConsumption = currentConsumption;
        this.lightsOnNumberInSeconds = lightsOnNumberInSeconds;
        this.wasHeated = wasHeated;
        this.sensor = sensor;

    }

    public Integer getStatistics_id() {
        return statistics_id;
    }

    public void setStatistics_id(Integer id) {
        this.statistics_id = id;
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

    public double getLightsOnNumberInSeconds() {
        return lightsOnNumberInSeconds;
    }

    public void setLightsOnNumberInSeconds(double lightsOnNumberInSeconds) {
        this.lightsOnNumberInSeconds = lightsOnNumberInSeconds;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String roomName) {
        this.sensorName = roomName;
    }

    public boolean isWasHeated() {
        return wasHeated;
    }

    public void setWasHeated(boolean wasHeated) {
        this.wasHeated = wasHeated;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

