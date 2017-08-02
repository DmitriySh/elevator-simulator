package ru.shishmakov.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shishmakov.util.ElevatorState;
import ru.shishmakov.util.Threads;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static ru.shishmakov.util.ElevatorState.IDLE;

@Singleton
public class ElevatorService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Logger fileLogger = LoggerFactory.getLogger("fileLogger");

    private static final String NAME = MethodHandles.lookup().lookupClass().getSimpleName();

    private final AtomicBoolean watcherState = new AtomicBoolean(true);
    private ElevatorState state = IDLE;
    private int currentFloor, goalFloor = 1;

    @Inject
    @Named("elevator.executor")
    private ExecutorService executor;
    @Inject
    @Named("elevator.commands")
    private BlockingQueue<Command> elevatorCommands;
    @Inject
    @Named("elevator.inbound")
    private Inbound inbound;

    public void start() {
        logger.info("{} starting...", NAME);
        try {
            startElevator();
            logger.info("{} started", NAME);
        } catch (Throwable e) {
            logger.error("{} start failed", NAME, e);
        }
    }

    public void stop() {
        logger.info("{} stopping...", NAME);
        try {
            stopElevator();
            logger.info("{} stopped", NAME);
        } catch (Throwable e) {
            logger.error("{} stop failed", NAME, e);
        }
    }

    private void startElevator() {
        executor.execute(this::process);
    }

    private void stopElevator() {
        shutdownConsole();
    }

    private void process() {
        while (watcherState.get() && !Thread.currentThread().isInterrupted()) {

            Threads.sleepWithInterruptedAfterTimeout(100, MILLISECONDS);
        }
    }

    private void shutdownConsole() {
        if (watcherState.compareAndSet(true, false)) {
            logger.debug("{} waiting for shutdown...", NAME);
        }
    }
}
