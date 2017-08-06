package ru.shishmakov.core;

import com.google.common.util.concurrent.MoreExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shishmakov.config.ElevatorConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static ru.shishmakov.core.LifeCycle.*;
import static ru.shishmakov.util.Threads.*;

/**
 * @author Dmitriy Shishmakov on 31.07.17
 */
@Singleton
public class ServiceController {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Logger fileLogger = LoggerFactory.getLogger("fileLogger");

    private static final String NAME = MethodHandles.lookup().lookupClass().getSimpleName();
    private static final AtomicReference<LifeCycle> SERVICE_STATE = new AtomicReference<>(IDLE);

    @Inject
    @Named("elevator.executor")
    private ExecutorService executor;
    @Inject
    private ConsoleService consoleService;
    @Inject
    private ElevatorService elevatorService;
    @Inject
    private ElevatorConfig config;

    @PostConstruct
    public void setUp() {
        logger.info("----- // -----    {} START {}    ----- // -----", NAME, LocalDateTime.now());
    }

    @PreDestroy
    public void tearDown() {
        logger.info("----- // -----    {} STOP {}    ----- // -----\nBuy!", NAME, LocalDateTime.now());
    }

    public ServiceController start() {
        logger.info("{} starting...", NAME);
        Thread.currentThread().setName("service-main");

        final LifeCycle state = SERVICE_STATE.get();
        if (LifeCycle.isNotIdle(state)) {
            logger.warn("Warning! {} already started, state: {}", NAME, state);
            return this;
        }
        SERVICE_STATE.set(INIT);
        consoleService.start();
        elevatorService.start();
        assignThreadHook(this::stop, "service-main-hook-thread");

        SERVICE_STATE.set(RUN);
        logger.info("{} started, state: {}", NAME, SERVICE_STATE.get());
        return this;
    }

    public void stop() {
        logger.info("{} stopping...", NAME);
        final LifeCycle state = SERVICE_STATE.get();
        if (LifeCycle.isNotRun(state)) {
            logger.warn("Warning! {} already stopped, state: {}", NAME, state);
            return;
        }

        try {
            SERVICE_STATE.set(STOPPING);
            consoleService.stop();
            elevatorService.stop();
            stopExecutors();
        } finally {
            SERVICE_STATE.set(IDLE);
            logger.info("{} stopped, state: {}", NAME, SERVICE_STATE.get());
        }
    }

    public void await() throws InterruptedException {
        fileLogger.info("{} thread: {} await the state: {} to stop itself", NAME, Thread.currentThread(), IDLE);
        for (long count = 0; LifeCycle.isNotIdle(SERVICE_STATE.get()); count++) {
            if (count % 100 == 0) fileLogger.debug("Thread: {} is alive", Thread.currentThread());
            sleepWithoutInterruptedAfterTimeout(config.mainIntervalMs(), MILLISECONDS);
        }
    }

    private void stopExecutors() {
        logger.info("Executor service stopping...");
        try {
            MoreExecutors.shutdownAndAwaitTermination(executor, STOP_TIMEOUT_SEC, SECONDS);
            logger.info("Executor service stopped");
        } catch (Exception e) {
            logger.error("{} exception occurred during stopping executor services", NAME, e);
        }
    }
}
