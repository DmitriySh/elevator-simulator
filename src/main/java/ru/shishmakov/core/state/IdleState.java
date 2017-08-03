package ru.shishmakov.core.state;

import ru.shishmakov.core.Command;

/**
 * The elevator without a passenger with closed door
 */
public class IdleState extends ElevatorState {
    private final int floor;

    public IdleState(int floor) {
        super("Idle", Long.MAX_VALUE);
        this.floor = floor;
    }

    /**
     * Idle state could be change only by command
     *
     * @return ElevatorState - current state
     */
    @Override
    protected ElevatorState nextState() {
        // do nothing
        return this;
    }

    /**
     * Try to move to the next state
     *
     * @param cmd command
     * @return ElevatorState - idle or move up/down state
     */
    @Override
    protected ElevatorState applyCommand(Command cmd) {
        ElevatorState next = this;
        switch (cmd.getType()) {
            default:
            case BLANK:
                // do nothing
                break;
            case CALL_ELEVATOR:
                if (floor > 1) {
                    next = new MoveUpOrDownState("Move Down", 0/*define*/, floor, cmd.getFloor());
                }
                if (floor == 1) {
                    next = new StopOpenState(0/*define*/, floor);
                }
                break;
            case PRESS_BUTTON:
                if (floor != cmd.getFloor()) {
                    String description = floor > cmd.getFloor() ? "Move Down state" : "Move Up";
                    next = new MoveUpOrDownState(description, 0/*define*/, floor, cmd.getFloor());
                }
                break;
        }
        return next;
    }

    @Override
    protected void print() {
        fileLogger.info("Idle state, timestamp: {}", System.currentTimeMillis());
    }
}
