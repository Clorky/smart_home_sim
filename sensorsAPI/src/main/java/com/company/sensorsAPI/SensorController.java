package com.company.sensorsAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/test")
public class SensorController {

    @Autowired
    private SensorRepository sensorRepository;

    @PostMapping(path = "/add") // Map ONLY POST Requests
    public @ResponseBody
    Sensor addNewSensor(@RequestBody Sensor sensor) {

        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        return sensorRepository.save(sensor);
    }
}
