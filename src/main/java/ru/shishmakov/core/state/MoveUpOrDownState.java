package ru.shishmakov.core.state;

import ru.shishmakov.core.Command;
import ru.shishmakov.util.TimeUtils;

public class MoveUpOrDownState extends ElevatorState {
    protected int startFloor, goalFloor;
    protected int pastFloor;

    public MoveUpOrDownState init(String description, long deadline, int startFloor, int goalFloor) {
        super.init(description, deadline);
        this.pastFloor = startFloor;
        this.startFloor = startFloor;
        this.goalFloor = goalFloor;
        return this;
    }

    /**
     * Try to move to the next state on elapsed timeout
     *
     * @return ElevatorState - move up/down state or stop open state
     */
    @Override
    protected ElevatorState tryGoNext() {
        ElevatorState state = this;
        if (TimeUtils.isTimeExpired(deadline)) {
            state = stopOpenProvider.get().init(0/*define*/, goalFloor);
        }
        return state;
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
