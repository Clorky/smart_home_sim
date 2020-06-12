package application;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private Main mainUnderTest;

    @BeforeEach
    void setUp() {
        mainUnderTest = new Main();
    }

    @Test
    void testCheckServerConnection() {
        assertTrue(Main.checkServerConnection());
    }
}
