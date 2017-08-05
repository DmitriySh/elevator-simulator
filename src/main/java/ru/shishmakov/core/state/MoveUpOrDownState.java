package ru.shishmakov.core.state;

import com.google.common.math.DoubleMath;
import ru.shishmakov.core.Command;

import java.util.concurrent.TimeUnit;

import static java.math.RoundingMode.HALF_UP;

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
            state = buildStopOpenState(goalFloor);
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
        double deltaTimeSec = TimeUnit.MILLISECONDS.toSeconds(deadline - timeController.now());
        double remainFloors = deltaTimeSec * inbound.velocity / inbound.height;
        int currentFloor = DoubleMath.roundToInt(Math.abs(startFloor > goalFloor
                ? goalFloor + remainFloors
                : goalFloor - remainFloors), HALF_UP);
        if (currentFloor != floor) {
            floor = currentFloor;
            logger.info("\t{} floor", floor);
        }
    }
}
