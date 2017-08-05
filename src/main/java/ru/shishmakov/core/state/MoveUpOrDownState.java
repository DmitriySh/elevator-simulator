package ru.shishmakov.core.state;

import ru.shishmakov.core.Command;
import ru.shishmakov.util.TimeUtils;

public class MoveUpOrDownState extends ElevatorState {
    protected int startFloor, goalFloor;
    protected int pastFloor;

    public ElevatorState init(long deadline, int startFloor, int goalFloor) {
        super.init(startFloor > goalFloor ? "Move down" : "Move up", deadline);
        this.pastFloor = startFloor;
        this.startFloor = startFloor;
        this.goalFloor = goalFloor;
        return this;
    }

    /**
     * Try to move to the next state on elapsed timeout
     *
     * @return ElevatorState - current {@link MoveUpOrDownState} or {@link StopOpenState}
     */
    @Override
    public ElevatorState tryGoNext() {
        ElevatorState state = this;
        if (TimeUtils.isTimeExpired(deadline)) {
            state = stopOpenProvider.get().init(0/*define*/, goalFloor);
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
