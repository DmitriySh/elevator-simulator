package ru.shishmakov.core;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Range;
import ru.shishmakov.config.ElevatorConfig;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author <a href="mailto:d.shishmakov@corp.nekki.ru">Shishmakov Dmitriy</a>
 */
public class Inbound {
    @Parameter(names = {"-n"}, description = "Number of floors", required = true)
    public int number;

    @Parameter(names = {"-h"}, description = "Height of floor", required = true)
    public int height;

    @Parameter(names = {"-v"}, description = "Velocity of elevator in m/sec", required = true)
    public int velocity;

    @Parameter(names = {"-d"}, description = "Duration of time sec between the opening and closing elevator door", required = true)
    public int door;

    private Inbound() {
    }

    public static Inbound buildInbound(String[] args) {
        Inbound inbound = new Inbound();
        JCommander.newBuilder().addObject(inbound).build().parse(args);
        return inbound;
    }

    public void validate(ElevatorConfig config) {
        Range<Integer> numbers = Range.closed(config.floorMinMax()[0], config.floorMinMax()[1]);
        Range<Integer> heights = Range.closed(config.floorHeights()[0], config.floorHeights()[1]);
        Range<Integer> velocities = Range.closed(config.elevatorVelocities()[0], config.elevatorVelocities()[1]);
        Range<Integer> doorTimes = Range.closed(config.doorTimes()[0], config.doorTimes()[1]);

        checkArgument(numbers.contains(number), "Number of floors: %s should be in range %s", number, numbers);
        checkArgument(heights.contains(height), "Height of floor: %s should be in range %s m", height, heights);
        checkArgument(velocities.contains(velocity), "Velocity of elevator: %s should be in range %s m/s", velocity, velocities);
        checkArgument(doorTimes.contains(door),
                "Time: %s between the opening and closing elevator door should be in range %s sec", door, doorTimes);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("number", number)
                .add("height", height)
                .add("velocity", velocity)
                .add("door", door)
                .toString();
    }
}
