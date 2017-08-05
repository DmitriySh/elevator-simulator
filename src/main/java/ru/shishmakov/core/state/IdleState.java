package ru.shishmakov.core.state;

import ru.shishmakov.core.Command;

/**
 * The elevator without a passenger with closed door
 */
public class IdleState extends ElevatorState {
    private int floor = 1;

    public IdleState init(int floor) {
        super.init("Idle", Long.MAX_VALUE);
        this.floor = floor;
        return this;
    }

    /**
     * Idle state could be change only by command
     *
     * @return ElevatorState - current state
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
     * @return ElevatorState - idle or move up/down state
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
                    next = moveUpOrDownProvider.get().init("Move down", 0/*define*/, floor, cmd.getFloor());
                } else {
                    next = stopOpenProvider.get().init(0/*define*/, floor);
                }
                break;
            case PRESS_BUTTON:
                if (floor != cmd.getFloor()) {
                    String description = floor > cmd.getFloor() ? "Move down" : "Move up";
                    next = moveUpOrDownProvider.get().init(description, 0/*define*/, floor, cmd.getFloor());
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
