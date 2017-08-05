package ru.shishmakov.core.state;

import ru.shishmakov.core.Command;
import ru.shishmakov.util.QueueUtils;
import ru.shishmakov.util.TimeUtils;

import static java.time.temporal.ChronoUnit.SECONDS;

public class StopCloseState extends ElevatorState {
    private int floor;

    public ElevatorState init(int floor) {
        super.init("Stop close", TimeUtils.nowPlus(1, SECONDS));
        this.floor = floor;
        return this;
    }

    /**
     * Try to move to the next state on elapsed timeout
     *
     * @return ElevatorState - current {@link StopCloseState} or {@link IdleState}, or {@link MoveUpOrDownState}
     */
    @Override
    public ElevatorState tryGoNext() {
        ElevatorState state = this;
        if (TimeUtils.isTimeExpired(deadline)) {
            state = QueueUtils.poll(elevatorCommands)
                    .map(cmd -> moveUpOrDownProvider.get().init(0/*define*/, floor, cmd.getFloor()))
                    .orElse(idleProvider.get().init(floor));
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
                if (elevatorCommands.isEmpty()) {
                    QueueUtils.offer(elevatorCommands, cmd);
                }
                break;
        }
        return this;
    }

    @Override
    public void print() {

    }
}
