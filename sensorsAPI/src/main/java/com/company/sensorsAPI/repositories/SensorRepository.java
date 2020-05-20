package com.company.sensorsAPI.repositories;

import com.company.sensorsAPI.entities.Sensor;
import org.springframework.data.repository.CrudRepository;

public interface SensorRepository extends CrudRepository<Sensor, Integer> {
}
