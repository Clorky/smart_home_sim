package com.company.sensorsAPI.exceptions;

public class SensorNotFoundException extends RuntimeException{
    public SensorNotFoundException(String name){
        super("Could not find sensor named: " + name);
    }
}
