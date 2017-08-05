package ru.shishmakov.core.state;

import ru.shishmakov.core.Command;
import ru.shishmakov.util.TimeUtils;

public class StopOpenState extends ElevatorState {
    private int floor;

    public StopOpenState init(long deadline, int floor) {
        super.init("Stop open", deadline);
        this.floor = floor;
        return this;
    }

    @Override
    public ElevatorState tryGoNext() {
        ElevatorState state = this;
        if (TimeUtils.isTimeExpired(deadline)) {
            state = stopCloseProvider.get().init(floor);
        }
        return state;
    }

    @Override
    public ElevatorState applyCommand(Command cmd) {
        ElevatorState next = this;
        switch (cmd.getType()) {
            default:
                // do nothing
                break;
            case CALL_ELEVATOR:
                // do nothing
                break;
            case PRESS_BUTTON:
                break;
        }
        return next;
    }

    @Override
    public void print() {

    }
}
