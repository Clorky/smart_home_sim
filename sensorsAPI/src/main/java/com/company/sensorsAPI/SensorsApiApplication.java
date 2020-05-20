package com.company.sensorsAPI;

import com.company.sensorsAPI.listeners.MyListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SensorsApiApplication {

    public static void main(String[] args) {
        new Thread(new SensorSimulation()).start();
        SpringApplication app = new SpringApplication();
        app.addListeners(new MyListener());
        app.run(SensorsApiApplication.class, args);
    }

}
