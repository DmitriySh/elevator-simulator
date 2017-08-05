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
    public ElevatorState tryGoNext() {
        return null;
    }

    @Override
    public ElevatorState applyCommand(Command cmd) {
        return null;
    }

    @Override
    public void print() {

    }
}
