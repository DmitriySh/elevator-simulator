package ru.shishmakov.core;

import com.google.common.base.MoreObjects;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class Command {
    public static final Command BLANK = new Command();

    private boolean indoor;
    private int floor;
    private String description = EMPTY;

    private Command() {
    }

    private Command(boolean indoor, int floor, String description) {
        super();
        checkArgument(floor > 0, "Command should have positive floor number: %s", floor);
        this.indoor = indoor;
        this.floor = floor;
        this.description = description;
    }

    public static Command pressButton(int floor) {
        return new Command(true, floor, "press button");
    }

    public static Command callElevator() {
        return new Command(false, 1, "call elevator");
    }

    public boolean isIndoor() {
        return indoor;
    }

    public int getFloor() {
        return floor;
    }

    public String getDescription() {
        return description;
    }

    public boolean isBlank() {
        return !indoor && floor == 0 && EMPTY.equals(description);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("indoor", indoor)
                .add("floor", floor)
                .add("description", description)
                .toString();
    }
}
