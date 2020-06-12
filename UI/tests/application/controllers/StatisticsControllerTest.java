package application.controllers;

import javafx.event.ActionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatisticsControllerTest {

    private StatisticsController statisticsControllerUnderTest;

    @BeforeEach
    void setUp() {
        statisticsControllerUnderTest = new StatisticsController();
    }

    @Test
    void atestUpdate() {
        // Setup
        StatisticsController.threadFinished = true;


        // Run the test
        final boolean result = statisticsControllerUnderTest.update();

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testRequestData() {
        // Setup

        // Run the test

        final boolean result = statisticsControllerUnderTest.requestData();

        // Verify the results
        assertTrue(result);
    }

}
