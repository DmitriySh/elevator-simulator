package ru.shishmakov.core.state;

import ru.shishmakov.core.Command;
import ru.shishmakov.util.QueueUtils;

public class StopCloseState extends ElevatorState {
    private boolean notify;

    /**
     * Try to move to the next state on elapsed timeout
     *
     * @return ElevatorState - current {@link StopCloseState} or {@link IdleState}, or {@link MoveUpOrDownState}
     */
    @Override
    public ElevatorState tryGoNext() {
        ElevatorState state = this;
        if (timeController.isTimeExpired(deadline)) {
            state = QueueUtils.poll(elevatorCommands)
                    .map(cmd -> buildMoveUpOrDownState(floor, cmd.getFloor()))
                    .orElse(buildIdleState(floor));
        }
        return state;
    }

    /**
     * Current {@link StopCloseState} could be change only by timeout
     *
     * @param cmd command
     * @return ElevatorState - current {@link StopCloseState}
     */
    @Override
    public ElevatorState applyCommand(Command cmd) {
        switch (cmd.getType()) {
            default:
                // do nothing
                break;
            case CALL_ELEVATOR:
                // do nothing
                break;
            case PRESS_BUTTON:
                if (elevatorCommands.isEmpty() && cmd.getFloor() != floor) {
                    QueueUtils.offer(elevatorCommands, cmd);
                }
                break;
        }
        return this;
    }

    @Override
    public void print() {
        if (!notify) {
            notify = true;
            logger.info("\t{} floor, elevator close doors", floor);
        }

    }
}
