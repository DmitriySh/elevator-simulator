package ru.shishmakov.core;

import com.beust.jcommander.Parameter;

/**
 * @author <a href="mailto:d.shishmakov@corp.nekki.ru">Shishmakov Dmitriy</a>
 */
public class Inbound {
    @Parameter(names = {"-n"}, description = "Number of floors", required = true)
    public int number;

    @Parameter(names = {"-h"}, description = "Height of floor", required = true)
    public int heightFloor;

    @Parameter(names = {"-v"}, description = "Velocity of elevator", required = true)
    public int velocity;

    @Parameter(names = {"-d"}, description = "Duration of time between the closing and opening elevator door", required = true)
    public int duration;
}
