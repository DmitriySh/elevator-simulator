package ru.shishmakov.core.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shishmakov.core.Command;
import ru.shishmakov.core.Inbound;
import ru.shishmakov.core.TimeController;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.concurrent.BlockingQueue;

import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;

public abstract class ElevatorState {
    protected static final Logger fileLogger = LoggerFactory.getLogger("fileLogger");
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String description;
    protected long deadline;
    protected int floor;

    @Inject
    @Named("elevator.inbound")
    protected Inbound inbound;
    @Inject
    @Named("elevator.commands")
    protected BlockingQueue<Command> elevatorCommands;
    @Inject
    protected TimeController timeController;
    @Inject
    private Provider<IdleState> idleProvider;
    @Inject
    private Provider<MoveUpOrDownState> moveUpOrDownProvider;
    @Inject
    private Provider<StopCloseState> stopCloseProvider;
    @Inject
    private Provider<StopOpenState> stopOpenProvider;

    public abstract ElevatorState tryGoNext();

    public abstract ElevatorState applyCommand(Command cmd);

    public abstract void print();

    public ElevatorState init(String description, long deadline, int floor) {
        this.description = description;
        this.deadline = deadline;
        this.floor = floor;
        return this;
    }

    public ElevatorState buildIdleState(int floor) {
        IdleState state = idleProvider.get();
        return state.init("Idle", Long.MAX_VALUE, floor);
    }

    public ElevatorState buildMoveUpOrDownState(int startFloor, int goalFloor) {
        int deltaFloor = Math.abs(goalFloor - startFloor);
        long deadline = timeController.nowPlus((deltaFloor * inbound.height) / inbound.velocity, SECONDS);
        MoveUpOrDownState state = moveUpOrDownProvider.get();
        state.startFloor = startFloor;
        state.goalFloor = goalFloor;
        return state.init(startFloor > goalFloor ? "Move down" : "Move up", deadline, startFloor);
    }

    public ElevatorState buildStopOpenState(int floor) {
        long doorMillis = inbound.door * 1000;
        long deadline = timeController.nowPlus(doorMillis - (doorMillis / 3), MILLIS);
        StopOpenState state = stopOpenProvider.get();
        return state.init("Stop open", deadline, floor);
    }

    public ElevatorState buildStopClose(int floor) {
        long deadline = timeController.nowPlus((inbound.door * 1000) / 3, MILLIS);
        StopCloseState state = stopCloseProvider.get();
        return state.init("Stop close", deadline, floor);
    }
}
