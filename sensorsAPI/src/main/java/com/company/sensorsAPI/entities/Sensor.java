package com.company.sensorsAPI.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Sensor {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sensor", orphanRemoval = true)
    List<StatisticsData> statistics = new ArrayList<StatisticsData>();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sensor_id")
    private Integer id;
    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    private Room room;
    private String sensorName;
    private boolean heated;
    private double requestedTemp;
    private double temperature;
    private double currentConsumption;
    private boolean lightsOn;

    public Sensor() {

    }

    public Sensor(double temperature, double currentConsumption, boolean lightsOn, String sensorName, boolean heated, double requestedTemp, Room room) {
        this.requestedTemp = requestedTemp;
        this.heated = heated;
        this.temperature = temperature;
        this.currentConsumption = currentConsumption;
        this.lightsOn = lightsOn;
        this.sensorName = sensorName;
        this.room = room;
    }

    public Sensor(Integer id, double temperature, double currentConsumption, boolean lightsOn, String sensorName, boolean heated, double requestedTemp) {
        this.id = id;
        this.heated = heated;
        this.requestedTemp = requestedTemp;
        this.temperature = temperature;
        this.currentConsumption = currentConsumption;
        this.lightsOn = lightsOn;
        this.sensorName = sensorName;
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

    public boolean getlightsOn() {
        return lightsOn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setlightsOn(boolean lightsOn) {
        this.lightsOn = lightsOn;
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
        return this.heated;
    }

    public void setHeated(boolean heated) {
        this.heated = heated;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
