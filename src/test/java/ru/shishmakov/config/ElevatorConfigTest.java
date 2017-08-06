package ru.shishmakov.config;

import org.aeonbits.owner.ConfigFactory;
import org.junit.Test;
import ru.shishmakov.BaseTest;

import static org.junit.Assert.assertTrue;

public class ElevatorConfigTest extends BaseTest {

    @Test
    public void configShouldHasValues() throws Exception {
        ElevatorConfig config = ConfigFactory.create(ElevatorConfig.class);

        assertTrue("Elevator interval value should be positive", config.elevatorIntervalMs() > 0);
        assertTrue("Main interval value should be positive", config.mainIntervalMs() > 0);
        assertTrue("Read delay value should be positive", config.queueReadDelay() > 0);
    }
}
