package ru.shishmakov.core;

import com.google.common.util.concurrent.MoreExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shishmakov.concurrent.LifeCycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static ru.shishmakov.concurrent.LifeCycle.*;
import static ru.shishmakov.concurrent.Threads.*;

/**
 * @author Dmitriy Shishmakov on 31.07.17
 */
@Singleton
public class ElevatorService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Logger ucLogger = LoggerFactory.getLogger("userConsole");

    private static final String NAME = MethodHandles.lookup().lookupClass().getSimpleName();
    private static final AtomicReference<LifeCycle> CLIENT_STATE = new AtomicReference<>(IDLE);
    private static final CountDownLatch awaitStart = new CountDownLatch(1);

    @Inject
    @Named("elevator.executor")
    private ExecutorService executor;
    @Inject
    private ConsoleClient consoleClient;

    @PostConstruct
    public void setUp() {
        logger.info("----- // -----    {} START {}    ----- // -----", NAME, LocalDateTime.now());
        ucLogger.info("{} START {}", NAME, LocalDateTime.now());
    }

    @PreDestroy
    public void tearDown() {
        logger.info("----- // -----    {} STOP {}    ----- // -----", NAME, LocalDateTime.now());
        ucLogger.info("{} STOP {}\nBuy!", NAME, LocalDateTime.now());
    }

    public ElevatorService startAsync() {
        executor.execute(this::start);
        return this;
    }

    public ElevatorService start() {
        logger.info("{} starting...", NAME);
        Thread.currentThread().setName("service-elevator");

        final LifeCycle state = CLIENT_STATE.get();
        if (LifeCycle.isNotIdle(state)) {
            logger.warn("Warning! {} already started, state: {}", NAME, state);
            return this;
        }
        CLIENT_STATE.set(INIT);
        awaitStart.countDown();
        consoleClient.start();
        assignThreadHook(this::stop, "elevator-service-hook-thread");

        CLIENT_STATE.set(RUN);
        logger.info("{} started, state: {}", NAME, CLIENT_STATE.get());
        return this;
    }

    public void stop() {
        logger.info("{} stopping...", NAME);
        final LifeCycle state = CLIENT_STATE.get();
        if (LifeCycle.isNotRun(state)) {
            logger.warn("Warning! {} already stopped, state: {}", NAME, state);
            return;
        }

        try {
            CLIENT_STATE.set(STOPPING);
            consoleClient.stop();
            stopExecutors();
        } finally {
            CLIENT_STATE.set(IDLE);
            logger.info("{} stopped, state: {}", NAME, CLIENT_STATE.get());
            ucLogger.info("{} stopped, state: {}", NAME, CLIENT_STATE.get());
        }
    }

    public void await() throws InterruptedException {
        awaitStart.await();
        Thread.currentThread().setName("service-main");
        logger.info("{} thread: {} await the state: {} to stop itself", NAME, Thread.currentThread(), IDLE);
        for (long count = 0; LifeCycle.isNotIdle(CLIENT_STATE.get()); count++) {
            if (count % 100 == 0) logger.debug("Thread: {} is alive", Thread.currentThread());
            sleepWithoutInterruptedAfterTimeout(100, MILLISECONDS);
        }
    }

    private void stopExecutors() {
        logger.info("{} executor services stopping...", NAME);
        try {
            MoreExecutors.shutdownAndAwaitTermination(executor, STOP_TIMEOUT_SEC, SECONDS);
            logger.info("Executor services stopped");
        } catch (Exception e) {
            logger.error("{} exception occurred during stopping executor services", NAME, e);
        }
    }
}
