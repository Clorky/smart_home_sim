package com.company.sensorsAPI.controllers;

import com.company.sensorsAPI.entities.Room;
import com.company.sensorsAPI.entities.Sensor;
import com.company.sensorsAPI.repositories.RoomRepository;
import com.company.sensorsAPI.repositories.SensorRepository;
import com.company.sensorsAPI.simulation.SensorSimulation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/room")
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private SensorRepository sensorRepository;

    @PostMapping(path = "/add")
    public @ResponseBody
    void addNewRoom(@RequestBody Room room) {
        Room room1 = new Room(room.getName());
        roomRepository.save(room1);
        Sensor sensor = new Sensor(10, 0, false, room.getName() + "_sensor", false, 21.5, room1);
        StatisticsDataController.initNewRoom(room);
        sensorRepository.save(sensor);
    }

    @PostMapping(path = "/delete")
    public @ResponseBody
    boolean removeRoom(@RequestBody Room room) {
        if (SensorSimulation.deleteFlag) {
            Iterable<Room> rooms = roomRepository.findAll();
            Iterable<Sensor> sensors = sensorRepository.findAll();
            for (Room room1 : rooms) {
                if (room.getName().equals(room1.getName())) {
                    for (Sensor sensor : sensors) {
                        if (sensor.getSensorName().equals(room1.getName() + "_sensor")) {
                            sensorRepository.delete(sensor);
                        }
                    }
                    roomRepository.delete(room1);
                    StatisticsDataController.lightData.remove(room1.getName());
                    return true;
                }
            }
        } else {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            removeRoom(room);
        }
        return false;
    }


    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Room> getAllRooms() {
        Iterable<Room> rooms = roomRepository.findAll();
        return rooms;
    }
}