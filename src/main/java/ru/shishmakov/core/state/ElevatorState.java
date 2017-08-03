package ru.shishmakov.core.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shishmakov.core.Command;

public abstract class ElevatorState {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final Logger fileLogger = LoggerFactory.getLogger("fileLogger");

    protected final String description;
    protected final long deadline;


    public ElevatorState(String description, long deadline) {
        this.description = description;
        this.deadline = deadline;
    }

    protected abstract ElevatorState nextState();

    protected abstract ElevatorState applyCommand(Command cmd);

    protected abstract void print();
}
