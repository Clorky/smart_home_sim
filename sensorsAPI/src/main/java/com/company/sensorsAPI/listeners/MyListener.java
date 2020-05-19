package com.company.sensorsAPI.listeners;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class MyListener
        implements ApplicationListener<ApplicationReadyEvent> {

    public static boolean running = false;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println(event.toString());
        running = true;
    }

}