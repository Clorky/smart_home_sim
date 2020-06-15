package com.company.sensorsAPI.controllers;

import com.company.sensorsAPI.repositories.RoomRepository;
import com.company.sensorsAPI.repositories.SensorRepository;
import com.company.sensorsAPI.repositories.StatisticsDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.mockito.Mockito.*;

class StatisticsDataControllerTest {
    @Mock
    StatisticsDataRepository statisticsDataRepository;
    @Mock
    StatisticsDataCacheController statisticsDataCacheRepository;
    @Mock
    SensorRepository sensorRepository;
    @Mock
    RoomRepository roomRepository;
    @Mock
    Map<String, Map<Integer, Integer>> lightData;
    @Mock
    Map<Integer, Double> totalEnergyConsumption;
    @InjectMocks
    StatisticsDataController statisticsDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testInit() {
        statisticsDataCacheRepository.initialize();
    }
}