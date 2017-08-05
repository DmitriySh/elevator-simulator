package ru.shishmakov.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shishmakov.core.state.ElevatorState;
import ru.shishmakov.util.QueueUtils;
import ru.shishmakov.util.Threads;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Singleton
public class ElevatorService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Logger fileLogger = LoggerFactory.getLogger("fileLogger");

    private static final String NAME = MethodHandles.lookup().lookupClass().getSimpleName();

    private final AtomicBoolean watcherState = new AtomicBoolean(true);

    /**
     * Current elevator state
     */
    @Inject
    @Named("elevator.startState")
    private ElevatorState state;
    @Inject
    @Named("elevator.executor")
    private ExecutorService executor;
    @Inject
    @Named("console.commands")
    private BlockingQueue<Command> consoleCommands;

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
            Threads.sleepWithInterruptedAfterTimeout(250, MILLISECONDS);// TODO: 05.08.17 use TimeConfig

            if (!consoleCommands.isEmpty()) {
                fileLogger.info("command: {}", consoleCommands.peek().getDescription());
            }
            state = state.tryGoNext();
            state = QueueUtils.poll(consoleCommands, 1, 20, MILLISECONDS)
                    .map(state::applyCommand)
                    .orElse(state)
                    .print();
        }
    }

    private void shutdownConsole() {
        if (watcherState.compareAndSet(true, false)) {
            logger.debug("{} waiting for shutdown...", NAME);
        }
    }
}
