package com.company.sensorsAPI.entities;

import org.hibernate.annotations.Cascade;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;

@Entity
public class StatisticsDataCache {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;
    private Integer year;
    private Integer monthNumber;
    private String roomName;
    private Integer daysHeated;
    private long lightOnSeconds;
    private double totalPowerConsumption;
    @Nullable
    private Integer lastUsedDayOfYear;

    public StatisticsDataCache() {
    }

    public StatisticsDataCache(String roomName, Integer year, Integer monthNumber, Integer daysHeated, long lighOnSeconds, double totalPowerConsumption, Sensor sensor) {
        this.roomName = roomName;
        this.year = year;
        this.monthNumber = monthNumber;
        this.daysHeated = daysHeated;
        this.lightOnSeconds = lighOnSeconds;
        this.totalPowerConsumption = totalPowerConsumption;
        this.sensor = sensor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(Integer monthNumber) {
        this.monthNumber = monthNumber;
    }

    public Integer getDaysHeated() {
        return daysHeated;
    }

    public void setDaysHeated(Integer daysHeated) {
        this.daysHeated = daysHeated;
    }

    public long getLightOnSeconds() {
        return lightOnSeconds;
    }

    public void setLightOnSeconds(long lightOnSeconds) {
        this.lightOnSeconds = lightOnSeconds;
    }

    public double getTotalPowerConsumption() {
        return totalPowerConsumption;
    }

    public void setTotalPowerConsumption(double totalPowerConsumption) {
        this.totalPowerConsumption = totalPowerConsumption;
    }

    public Integer getYear() {
        return year;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getLastUsedDayOfYear() {
        return lastUsedDayOfYear;
    }

    public void setLastUsedDayOfYear(Integer lastUsedDayOfYear) {
        this.lastUsedDayOfYear = lastUsedDayOfYear;
    }
}
