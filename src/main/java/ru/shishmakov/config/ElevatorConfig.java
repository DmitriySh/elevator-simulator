package ru.shishmakov.config;

import org.aeonbits.owner.Config;

/**
 * @author Dmitriy Shishmakov on 11.03.17
 */
@Config.Sources({"file:config/config.properties", "classpath:config/config.properties"})
public interface ElevatorConfig extends Config {

    @DefaultValue("250")
    @Key("elevator.intervalMs")
    long elevatorIntervalMs();

    @DefaultValue("100")
    @Key("main.intervalMs")
    long mainIntervalMs();

    @DefaultValue("20")
    @Key("queue.readDelay")
    long queueReadDelay();

    @DefaultValue("5, 20")
    @Key("floor.minMax")
    int[] floorMinMax();

    @DefaultValue("1, 10")
    @Key("floor.heights")
    int[] floorHeights();

    @DefaultValue("1, 10")
    @Key("elevator.velocities")
    int[] elevatorVelocities();

    @DefaultValue("2, 20")
    @Key("door.times")
    int[] doorTimes();
}
