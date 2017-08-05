package ru.shishmakov.core.state;

import ru.shishmakov.core.Command;

public class MoveUpOrDownState extends ElevatorState {
    int startFloor, goalFloor;

    /**
     * Try to move to the next state on elapsed timeout
     *
     * @return ElevatorState - current {@link MoveUpOrDownState} or {@link StopOpenState}
     */
    @Override
    public ElevatorState tryGoNext() {
        ElevatorState state = this;
        if (timeController.isTimeExpired(deadline)) {
            state = buildStopOpenState(0/*define*/, goalFloor);
        }
        return state;
    }

    /**
     * Current {@link MoveUpOrDownState} could be change only by timeout
     *
     * @param cmd command
     * @return ElevatorState - current {@link MoveUpOrDownState}
     */
    @Override
    public ElevatorState applyCommand(Command cmd) {
        // do nothing
        return this;
    }

    @Override
    public void print() {

    }
}
