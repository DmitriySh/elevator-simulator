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

    @Inject
    @Named("elevator.inbound")
    protected Inbound inbound;
    @Inject
    @Named("elevator.commands")
    protected BlockingQueue<Command> elevatorCommands;
    @Inject
    protected Provider<IdleState> idleProvider;
    @Inject
    protected Provider<MoveUpOrDownState> moveUpOrDownProvider;
    @Inject
    protected Provider<StopCloseState> stopCloseProvider;
    @Inject
    protected Provider<StopOpenState> stopOpenProvider;

    public ElevatorState init(String description, long deadline) {
        this.description = description;
        this.deadline = deadline;
        return this;
    }

    public abstract ElevatorState tryGoNext();

    public abstract ElevatorState applyCommand(Command cmd);

    public abstract void print();
}
