package ru.shishmakov.core.state;

import ru.shishmakov.core.Command;

public class StopOpenState extends ElevatorState {
    private final int floor;

    public StopOpenState(long deadline, int floor) {
        super("Stop Open", deadline);
        this.floor = floor;
    }

    @Override
    protected ElevatorState nextState() {
        return null;
    }

    @Override
    protected ElevatorState applyCommand(Command cmd) {
        return null;
    }

    @Override
    protected void print() {

    }
}
