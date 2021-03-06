package ru.shishmakov.core.state;

import ru.shishmakov.core.Command;

import javax.annotation.PostConstruct;

/**
 * The elevator without a passenger with closed door
 */
public class IdleState extends ElevatorState {
    private boolean notify;

    @PostConstruct
    public void setUp() {
        this.floor = 1;
    }

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
                    next = buildMoveUpOrDownState(floor, cmd.getFloor());
                } else {
                    next = buildStopOpenState(floor);
                }
                break;
            case PRESS_BUTTON:
                if (floor == cmd.getFloor()) {
                    next = buildStopOpenState(floor);
                } else {
                    next = buildMoveUpOrDownState(floor, cmd.getFloor());
                }
                break;
        }
        return next;
    }

    @Override
    public ElevatorState print() {
        if (!notify) {
            notify = true;
            fileLogger.info("Idle state, utc: {}", timeController.nowLocalDateTime());
        }
        return this;
    }
}
