package com.company.sensorsAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/room")
public class RoomController {
    @Autowired // This means to get the bean called sensorRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private RoomRepository roomRepository;
    @Autowired
    private SensorRepository sensorRepository;

    @PostMapping(path = "/add") // Map ONLY POST Requests
    public @ResponseBody
    void addNewRoom(@RequestBody Room room) {
        Room room1 = new Room(room.getName());
        roomRepository.save(room1);
        Sensor sensor = new Sensor(10, 0, 0, room.getName() + "_sensor", false, 21.5, room1);
        sensorRepository.save(sensor);
    }

    @PostMapping(path = "/delete") // Map ONLY POST Requests
    public @ResponseBody
    boolean removeRoom(@RequestBody Room room) {

        Iterable<Room> rooms = roomRepository.findAll();
        Iterable<Sensor> sensors = sensorRepository.findAll();
        for (Room room1 : rooms) {
            if(room.getName().equals(room1.getName())){
                for (Sensor sensor : sensors) {
                    if(sensor.getSensorName().equals(room1.getName() + "_sensor")) sensorRepository.delete(sensor);
                }
                roomRepository.delete(room1); //TODO: můžeme mazat místnost a zároveň její senzor a je to správně? (cascade...)
                return true;
            }
        }
        return false;
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Room> getAllRooms() {
        // This returns a JSON or XML with the users
        Iterable<Room> rooms = roomRepository.findAll();
        return rooms;
    }
}