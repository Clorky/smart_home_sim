package com.company.sensorsAPI;

import com.company.sensorsAPI.listeners.MyListener;
import com.company.sensorsAPI.simulation.SensorSimulation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SensorsApiApplication {

    public static void main(String[] args) {
        new Thread(new SensorSimulation()).start();
        SpringApplication app = new SpringApplication();
        app.addListeners(new MyListener());
        SpringApplication.run(SensorsApiApplication.class, args);
    }

}
