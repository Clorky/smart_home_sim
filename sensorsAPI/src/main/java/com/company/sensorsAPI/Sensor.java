package com.company.sensorsAPI;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //TODO: primary key a foreign key z room (Room room)
    @Column(name = "sensor_id")
    private Integer id;
    @OneToOne(optional = false)
    private Room room;
    private String sensorName;
    private boolean isHeated;
    private double requestedTemp;
    private double temperature;
    private double currentConsumption;
    private double lightsOnNumberInHours;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="sensor")
    List<StatisticsData> statistics = new ArrayList<StatisticsData>();

    public Sensor(){

    }

    public Sensor(double temperature, double currentConsumption, double lightsOnNumberInHours, String sensorName, boolean isHeated, double requestedTemp, Room room) {
        this.requestedTemp = requestedTemp;
        this.isHeated = isHeated;
        this.temperature = temperature;
        this.currentConsumption = currentConsumption;
        this.lightsOnNumberInHours = lightsOnNumberInHours;
        this.sensorName = sensorName;
        this.room = room;
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

    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
}
