package ru.shishmakov.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.common.util.concurrent.Uninterruptibles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * @author Dmitriy Shishmakov on 31.07.17
 */
public final class Threads {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final long STOP_TIMEOUT_SEC = 3;

    /**
     * Modified version of the method {@link Uninterruptibles#sleepUninterruptibly(long, TimeUnit)}
     * suppresses {@link InterruptedException} occurred at the time of timeout
     */
    public static void sleepWithoutInterruptedAfterTimeout(long timeout, TimeUnit unit) {
        long remainingNanos = unit.toNanos(timeout);
        final long end = System.nanoTime() + remainingNanos;
        while (true) {
            try {
                // TimeUnit.sleep() treats negative timeouts just like zero.
                NANOSECONDS.sleep(remainingNanos);
                return;
            } catch (InterruptedException e) {
                remainingNanos = end - System.nanoTime();
            }
        }
    }

    /**
     * Version of the method {@link Uninterruptibles#sleepUninterruptibly(long, TimeUnit)}
     * interrupts the thread after timeout if {@link InterruptedException} happened
     */
    public static void sleepWithInterruptedAfterTimeout(long timeout, TimeUnit unit) {
        Uninterruptibles.sleepUninterruptibly(timeout, unit);
    }

    /**
     * The thread could interrupted at the time of timeout
     */
    public static void sleepInterrupted(long timeout, TimeUnit unit) {
        try {
            Thread.sleep(unit.toMillis(timeout));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void assignThreadHook(Runnable task, String name) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.debug("{} was interrupted by hook", Thread.currentThread());
            task.run();
        }, name));
    }

    public static ThreadFactory buildThreadFactory(String name) {
        ThreadFactoryBuilder factory = new ThreadFactoryBuilder();
        factory.setNameFormat(name + "-%d");
        factory.setUncaughtExceptionHandler((t, e) -> logger.warn("thread pool: " + name + " has unhandled exception", e));
        return factory.build();
    }
}
