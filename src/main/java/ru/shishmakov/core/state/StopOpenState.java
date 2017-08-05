package ru.shishmakov.core.state;

import ru.shishmakov.core.Command;
import ru.shishmakov.util.QueueUtils;

public class StopOpenState extends ElevatorState {

    /**
     * Try to move to the next state on elapsed timeout
     *
     * @return ElevatorState - current {@link StopOpenState} or {@link StopCloseState}
     */
    @Override
    public ElevatorState tryGoNext() {
        ElevatorState state = this;
        if (timeController.isTimeExpired(deadline)) {
            state = buildStopClose(0/*define*/, floor);
        }
        return state;
    }


    /**
     * Try to move to the next state
     *
     * @param cmd command
     * @return ElevatorState - current {@link StopOpenState} or {@link StopCloseState}
     */
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
                if (elevatorCommands.isEmpty()) {
                    QueueUtils.offer(elevatorCommands, cmd);
                }
                next = buildStopClose(0/*define*/, floor);
                break;
        }
        return next;
    }

    @Override
    public void print() {

    }
}
