package ru.shishmakov.core;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import ru.shishmakov.BaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InboundTest extends BaseTest {

    @Test
    public void buildInboundShouldThrowExceptionIfFloorsNumberIllegal() throws Exception {
        String[] args = {"-n", "1", "-h", "4", "-v", "2", "-d", "10"};

        Assertions.assertThatExceptionThrownBy(() -> {
            Inbound.buildInbound(args);
            return null;
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("Number of floors: 1 should be in range [5..20]");
    }

    @Test
    public void buildInboundShouldThrowExceptionIfFloorHeightIllegal() throws Exception {
        String[] args = {"-n", "15", "-h", "40", "-v", "2", "-d", "10"};

        Assertions.assertThatExceptionThrownBy(() -> {
            Inbound.buildInbound(args);
            return null;
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("Height of floor: 40 should be in range [1..10] m");
    }

    @Test
    public void buildInboundShouldThrowExceptionIfElevatorVelocityIllegal() throws Exception {
        String[] args = {"-n", "15", "-h", "4", "-v", "20", "-d", "10"};

        Assertions.assertThatExceptionThrownBy(() -> {
            Inbound.buildInbound(args);
            return null;
        }).isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("Velocity of elevator: 20 should be in range [1..10] m/s");
    }

    @Test
    public void buildInboundShouldThrowExceptionIfTimeBetweenOpenAndCloseElevatorDoorIllegal() throws Exception {
        String[] args = {"-n", "15", "-h", "4", "-v", "2", "-d", "50"};

        Assertions.assertThatExceptionThrownBy(() -> {
            Inbound.buildInbound(args);
            return null;
        }).isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Time: 50 between the opening and closing elevator door should be in range [2..20] sec");
    }

    @Test
    public void buildInboundShouldBuildValidObject() throws Exception {
        int number = 15;
        int floorHeight = 4;
        int velocity = 2;
        int doorTime = 5;
        String[] args = {"-n", String.valueOf(number), "-h", String.valueOf(floorHeight),
                "-v", String.valueOf(velocity), "-d", String.valueOf(doorTime)};

        Inbound inbound = Inbound.buildInbound(args);

        assertNotNull("Inbound object should not be null", inbound);
        assertEquals("Number is not equal", number, inbound.number);
        assertEquals("Height is not equal", floorHeight, inbound.height);
        assertEquals("Velocity is not equal", velocity, inbound.velocity);
        assertEquals("Door time is not equal", doorTime, inbound.door);

    }
}
