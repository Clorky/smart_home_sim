package com.company.sensorsAPI;

public class SensorNotFoundException extends RuntimeException{
    SensorNotFoundException(String name){
        super("Could not find sensor named: " + name);
    }
}
