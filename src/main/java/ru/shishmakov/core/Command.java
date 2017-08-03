package ru.shishmakov.core;

import com.google.common.base.MoreObjects;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static ru.shishmakov.core.Command.CommandType.CALL_ELEVATOR;
import static ru.shishmakov.core.Command.CommandType.PRESS_BUTTON;

public class Command {
    public static final Command BLANK = new Command(CommandType.BLANK);

    private final CommandType type;
    private int floor;
    private String description = EMPTY;

    private Command(CommandType type) {
        this.type = type;
    }

    private Command(CommandType type, int floor, String description) {
        this(type);
        checkArgument(floor > 0, "Command should have positive floor number: %s", floor);
        this.floor = floor;
        this.description = description;
    }

    public static Command pressButton(int floor) {
        return new Command(PRESS_BUTTON, floor, "press button");
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

    public boolean isBlank() {
        return type == CommandType.BLANK;
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
        BLANK,
        PRESS_BUTTON,
        CALL_ELEVATOR
    }
}
