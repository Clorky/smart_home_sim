package com.company.sensorsAPI.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Sensor{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //TODO: primary key a foreign key z room (Room room)
    @Column(name = "sensor_id")
    private Integer id;
    @OneToOne(optional = false, cascade = CascadeType.ALL, orphanRemoval = true)
    private Room room;
    private String sensorName;
    private boolean heated;
    private double requestedTemp;
    private double temperature;
    private double currentConsumption;
    private double lightsOnTotalPerWeek;
    private double lightsOnNumberInSeconds;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="sensor", orphanRemoval = true)
    List<StatisticsData> statistics = new ArrayList<StatisticsData>();

    private void test(){
    }

    public Sensor(){

    }

    public Sensor(double temperature, double currentConsumption, double lightsOnNumberInSeconds, String sensorName, boolean heated, double requestedTemp, Room room) {
        this.requestedTemp = requestedTemp;
        this.heated = heated;
        this.temperature = temperature;
        this.currentConsumption = currentConsumption;
        this.lightsOnNumberInSeconds = lightsOnNumberInSeconds;
        this.sensorName = sensorName;
        this.room = room;
    }

    public Sensor(Integer id, double temperature, double currentConsumption, double lightsOnNumberInSeconds, String sensorName, boolean heated, double requestedTemp) {
        this.id = id;
        this.heated = heated;
        this.requestedTemp = requestedTemp;
        this.temperature = temperature;
        this.currentConsumption = currentConsumption;
        this.lightsOnNumberInSeconds = lightsOnNumberInSeconds;
        this.sensorName = sensorName;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getCurrentConsumption() {
        return currentConsumption;
    }

    public double getLightsOnNumberInSeconds() {
        return lightsOnNumberInSeconds;
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

    public void setLightsOnNumberInSeconds(double lightsOnNumberInSeconds) {
        this.lightsOnNumberInSeconds = lightsOnNumberInSeconds;
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

    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
    }

    public void setHeated(boolean heated) {
        this.heated = heated;
    }

    public double getLightsOnTotalPerWeek() {
        return lightsOnTotalPerWeek;
    }

    public void setLightsOnTotalPerWeek(double lightsOnTotalPerWeek) {
        this.lightsOnTotalPerWeek = lightsOnTotalPerWeek;
    }
}
