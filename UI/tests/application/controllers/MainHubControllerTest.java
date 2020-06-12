package application.controllers;

import javafx.event.ActionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainHubControllerTest {

    private MainHubController mainHubControllerUnderTest;

    @BeforeEach
    void setUp() {
        mainHubControllerUnderTest = new MainHubController();
    }

    @Test
    void testUpdate() {
        // Setup

        // Run the test
        final boolean result = mainHubControllerUnderTest.update();

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testRequestData() {
        // Setup

        // Run the test
        final boolean result = mainHubControllerUnderTest.requestData();

        // Verify the results
        assertTrue(result);
    }
}
