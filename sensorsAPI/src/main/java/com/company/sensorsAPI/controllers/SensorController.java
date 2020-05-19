package com.company.sensorsAPI.controllers;

import com.company.sensorsAPI.entities.Sensor;
import com.company.sensorsAPI.entities.StatisticsData;
import com.company.sensorsAPI.exceptions.SensorNotFoundException;
import com.company.sensorsAPI.repositories.SensorRepository;
import com.company.sensorsAPI.repositories.StatisticsDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/sensors")

@Transactional(propagation = Propagation.REQUIRED)
public class SensorController {

    @Autowired
    private StatisticsDataRepository statisticsDataRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @GetMapping("/sensor/any")
    public @ResponseBody
    Double getGlobalTemp() {
        Double defaultTemp = 21.5;
        Iterable<Sensor> sensors = sensorRepository.findAll();
        if(sensors.spliterator().getExactSizeIfKnown() == 0) return defaultTemp;
        for (Sensor sensor : sensors) {
            return sensor.getRequestedTemp();
            }
        return defaultTemp;
        }

    @PostMapping(path = "/add") // Map ONLY POST Requests
    public @ResponseBody
    Sensor addNewSensor(@RequestBody Sensor sensor) {

        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        return sensorRepository.save(sensor);
    }

    @GetMapping("/sensor/{sensor_name}")
    public @ResponseBody
    Sensor getSensor(@PathVariable String sensor_name) {
        Sensor sensorRet = null;
        Iterable<Sensor> sensors = sensorRepository.findAll();
        for (Sensor sensor : sensors) {
            if(sensor.getSensorName().equals(sensor_name)){
                sensorRet = sensor;
            }
        }
        if(sensorRet == null) throw new SensorNotFoundException(sensor_name);
        return sensorRet;
    }


    @PostMapping("/update/{id}")
    public @ResponseBody
    Sensor updateSensor(@RequestBody Sensor updatedSensor, @PathVariable int id) {
        return sensorRepository.findById(id)
                .map(sensor -> {
                    sensor.setTemperature(updatedSensor.getTemperature());
                    sensor.setLightsOnNumberInHours(updatedSensor.getLightsOnNumberInHours());
                    sensor.setCurrentConsumption(updatedSensor.getCurrentConsumption());
                    sensor.setRequestedTemp(updatedSensor.getRequestedTemp());
                    sensor.setHeated(updatedSensor.isHeated());
                    return sensorRepository.save(sensor);
                })
                .orElseGet(() -> {
                    updatedSensor.setId(id);
                    return sensorRepository.save(updatedSensor);
                });
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Sensor> getAllSensors() {
        // This returns a JSON or XML with the users
        Iterable<Sensor> sensors = sensorRepository.findAll();
        for (Sensor s : sensors) {
            statisticsDataRepository.save(new StatisticsData(s.getSensorName(), s.getTemperature(),
                    s.getCurrentConsumption(), s.getLightsOnNumberInHours(), s.isHeated(), s));
        }
        return sensors;
    }

    @GetMapping(path = "/all/updateRequestedTemperature/{value}")
    public @ResponseBody
    void updateAllSensorReqTemp(@PathVariable String value) {
        Iterable<Sensor> sensorsToUpdate = sensorRepository.findAll();
        double requestedTemp = Double.parseDouble(value);

        for (Sensor s : sensorsToUpdate) {
            s.setRequestedTemp(requestedTemp);
        }
        sensorRepository.saveAll(sensorsToUpdate);

        }
}