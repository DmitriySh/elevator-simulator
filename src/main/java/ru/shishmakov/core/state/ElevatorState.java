package ru.shishmakov.core.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shishmakov.core.Command;
import ru.shishmakov.core.Inbound;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.concurrent.BlockingQueue;

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
        IdleState idle = idleProvider.get();
        idle.init("Idle", Long.MAX_VALUE, floor);
        return idle;
    }

    public ElevatorState buildMoveUpOrDownState(long deadline, int startFloor, int goalFloor) {
        MoveUpOrDownState moveUpOrDown = moveUpOrDownProvider.get();
        moveUpOrDown.init(startFloor > goalFloor ? "Move down" : "Move up", deadline, startFloor);
        moveUpOrDown.startFloor = startFloor;
        moveUpOrDown.goalFloor = goalFloor;
        return this;
    }

    public ElevatorState buildStopOpenState(long deadline, int floor) {
        StopOpenState stopOpen = stopOpenProvider.get();
        stopOpen.init("Stop open", deadline, floor);
        return this;
    }

    public ElevatorState buildStopClose(long deadline, int floor) {
        StopCloseState stopClose = stopCloseProvider.get();
        stopClose.init("Stop close", deadline, floor);
        return this;
    }
}
