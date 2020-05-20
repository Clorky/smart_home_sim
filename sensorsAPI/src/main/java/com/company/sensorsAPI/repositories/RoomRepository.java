package com.company.sensorsAPI.repositories;

import com.company.sensorsAPI.entities.Room;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called sensorRepository

public interface RoomRepository extends CrudRepository<Room, Integer> {
}