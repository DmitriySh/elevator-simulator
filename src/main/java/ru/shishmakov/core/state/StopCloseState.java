package ru.shishmakov.core.state;

import ru.shishmakov.core.Command;

public class StopCloseState extends ElevatorState {
    private int floor;

    public StopCloseState init(int floor) {
        super.init("Stop close", Long.MAX_VALUE);
        this.floor = floor;
        return this;
    }

    @Override
    protected ElevatorState tryGoNext() {
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
