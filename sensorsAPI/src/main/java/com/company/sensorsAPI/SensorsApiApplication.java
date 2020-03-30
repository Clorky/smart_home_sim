package com.company.sensorsAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SensorsApiApplication {

    public static void main(String[] args) {
        new Thread(new SensorSimulation()).start();
        SpringApplication.run(SensorsApiApplication.class, args);
    }

}
