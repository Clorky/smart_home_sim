package com.company.sensorsAPI.repositories;

import com.company.sensorsAPI.entities.StatisticsData;
import org.springframework.data.repository.CrudRepository;

public interface StatisticsDataRepository extends CrudRepository<StatisticsData, Integer> {
}
