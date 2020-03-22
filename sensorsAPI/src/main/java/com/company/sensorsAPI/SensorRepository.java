package com.company.sensorsAPI;

import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called sensorRepository

public interface SensorRepository extends CrudRepository<Sensor, Integer> {
}