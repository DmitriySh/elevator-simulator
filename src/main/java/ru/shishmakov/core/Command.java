package ru.shishmakov.core;

import com.google.common.base.MoreObjects;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.shishmakov.core.Command.CommandType.CALL_ELEVATOR;
import static ru.shishmakov.core.Command.CommandType.PRESS_BUTTON;

public class Command {
    private final CommandType type;
    private final int floor;
    private final String description;

    private Command(CommandType type, int floor, String description) {
        this.floor = floor;
        this.type = type;
        this.description = checkNotNull(description, "description is null");
    }

    public static Command pressButton(int floor) {
        return new Command(PRESS_BUTTON, floor, "press button: " + floor);
    }

    public static Command callElevator() {
        return new Command(CALL_ELEVATOR, 1, "call elevator");
    }

    public int getFloor() {
        return floor;
    }

    public String getDescription() {
        return description;
    }

    public CommandType getType() {
        return type;
    }

    public boolean isCallElevator() {
        return type == CALL_ELEVATOR;
    }

    public boolean isPressButton() {
        return type == PRESS_BUTTON;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("floor", floor)
                .add("description", description)
                .toString();
    }

    public enum CommandType {
        PRESS_BUTTON,
        CALL_ELEVATOR
    }
}
