package application.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoomsControllerTest {

    private RoomsController roomsControllerUnderTest;

    @BeforeEach
    void setUp() {
        roomsControllerUnderTest = new RoomsController();
    }

    @Test
    void testUpdate() {
        // Setup
        // Run the test
        final boolean result = roomsControllerUnderTest.update();

        // Verify the results
        assertTrue(result);
    }

    @Test
    void testRequestData() {
        // Setup

        // Run the test
        final boolean result = roomsControllerUnderTest.requestData();

        // Verify the results
        assertTrue(result);
    }
}
