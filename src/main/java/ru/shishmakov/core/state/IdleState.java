package ru.shishmakov.core.state;

import ru.shishmakov.core.Command;

/**
 * The elevator without a passenger with closed door
 */
public class IdleState extends ElevatorState {

    /**
     * Current {@link IdleState} could be change only by command
     *
     * @return ElevatorState - current {@link IdleState}
     */
    @Override
    public ElevatorState tryGoNext() {
        // do nothing
        return this;
    }

    /**
     * Try to move to the next state
     *
     * @param cmd command
     * @return ElevatorState - {@link MoveUpOrDownState} or {@link StopOpenState}
     */
    @Override
    public ElevatorState applyCommand(Command cmd) {
        ElevatorState next = this;
        switch (cmd.getType()) {
            default:
                // do nothing
                break;
            case CALL_ELEVATOR:
                if (floor > 1) {
                    next = buildMoveUpOrDownState(0/*define*/, floor, cmd.getFloor());
                } else {
                    next = buildStopOpenState(0/*define*/, floor);
                }
                break;
            case PRESS_BUTTON:
                if (floor != cmd.getFloor()) {
                    next = buildMoveUpOrDownState(0/*define*/, floor, cmd.getFloor());
                }
                break;
        }
        return next;
    }

    @Override
    public void print() {
        fileLogger.info("Idle state, timestamp: {}", System.currentTimeMillis());
    }
}
