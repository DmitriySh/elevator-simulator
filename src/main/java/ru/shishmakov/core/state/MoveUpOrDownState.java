package ru.shishmakov.core.state;

import ru.shishmakov.core.Command;

public class MoveUpOrDownState extends ElevatorState {
    protected final int startFloor, goalFloor;
    protected int pastFloor;

    public MoveUpOrDownState(String description, long deadline, int startFloor, int goalFloor) {
        super(description, deadline);
        this.pastFloor = startFloor;
        this.startFloor = startFloor;
        this.goalFloor = goalFloor;
    }

    /**
     * Try to move to the next state on elapsed timeout
     *
     * @return ElevatorState - move up/down/stop state
     */
    @Override
    protected ElevatorState nextState() {
        return null;
    }

    /**
     * Move up/down state could be change only by timeout
     *
     * @param cmd command
     * @return ElevatorState - current state
     */
    @Override
    protected ElevatorState applyCommand(Command cmd) {
        // do nothing
        return this;
    }

    @Override
    protected void print() {

    }
}
