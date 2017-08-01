package ru.shishmakov.core;

import com.beust.jcommander.Parameter;
import com.google.common.collect.Range;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.BoundType.CLOSED;
import static com.google.common.collect.BoundType.OPEN;

/**
 * @author <a href="mailto:d.shishmakov@corp.nekki.ru">Shishmakov Dmitriy</a>
 */
public class Inbound {
    @Parameter(names = {"-n"}, description = "Number of floors", required = true)
    public int number;

    @Parameter(names = {"-h"}, description = "Height of floor", required = true)
    public int height;

    @Parameter(names = {"-v"}, description = "Velocity of elevator", required = true)
    public int velocity;

    @Parameter(names = {"-d"}, description = "Duration of time between the opening and closing elevator door", required = true)
    public int duration;

    public Inbound validate() {
        checkArgument(Range.range(5, CLOSED, 21, OPEN).contains(number), "Number of floors: %s should be in range [5..21)", number);
        checkArgument(Range.range(1, CLOSED, 21, OPEN).contains(height), "Height of floor: %s should be in range [1..21) m", height);
        checkArgument(Range.range(1, CLOSED, 11, OPEN).contains(velocity), "Velocity of elevator: %s should be in range [1..11) m/s", velocity);
        checkArgument(Range.range(1, CLOSED, 21, OPEN).contains(duration),
                "Time: %s between the opening and closing elevator door should be in range [1..21) sec", duration);
        return this;
    }
}
