package com.company.sensorsAPI.simulation;

import com.company.sensorsAPI.entities.Sensor;
import com.company.sensorsAPI.entities.StatisticsData;
import com.company.sensorsAPI.repositories.RoomRepository;
import com.company.sensorsAPI.repositories.SensorRepository;
import com.company.sensorsAPI.repositories.StatisticsDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class DataGeneratorTest {

    @Mock
    private StatisticsDataRepository mockStatisticsDataRepository;
    @Mock
    private SensorRepository mockSensorRepository;
    @Mock
    private RoomRepository mockRoomRepository;

    @InjectMocks
    private DataGenerator dataGeneratorUnderTest;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    void testControlledRandom() {

        boolean[] result = new boolean[100];
        result = DataGenerator.controlledRandom(100, 28);

        int expectedNumber = 28;
        int resultNumber = 0;
        for (boolean b : result) {
            if(b) resultNumber++;
        }

        assertEquals(expectedNumber, resultNumber);
    }
}
