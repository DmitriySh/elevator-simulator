package ru.shishmakov.core;

import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * @author Dmitriy Shishmakov on 31.07.17
 */
public class ConsoleClient {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Logger ucLogger = LoggerFactory.getLogger("userConsole");

    private static final String NAME = MethodHandles.lookup().lookupClass().getSimpleName();
    @Inject
    @Named("elevator.executor")
    private ExecutorService executor;
    //    @Inject
//    private TimeConfig timeConfig;
    @Inject
    private Provider<ElevatorService> service;

    private final AtomicBoolean watcherState = new AtomicBoolean(true);

    protected void start() {
        logger.info("{} starting...", NAME);
        try {
            startConsole();
            logger.info("{} started", NAME);
        } catch (Throwable e) {
            logger.error("{} start failed", NAME, e);
        }
    }

    protected void stop() {
        logger.info("{} stopping...", NAME);
        try {
            stopConsole();
            logger.info("{} stopped", NAME);
        } catch (Throwable e) {
            logger.error("{} stop failed", NAME, e);
        }
    }

    private void startConsole() {
        executor.execute(this::process);
    }

    private void stopConsole() {
        shutdownClient();
    }

    private void process() {
        logger.info("{} listening user typing...", NAME);
        ucLogger.info("{} get ready, choose command... (/h - help)", NAME);
        try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in))) {
            while (watcherState.get() && !Thread.currentThread().isInterrupted()) {
                final String read = input.readLine();
                if (isBlank(read)) continue;

                final Iterator<String> it = Splitter.on(' ').split(read).iterator();
                if (!it.hasNext()) continue;

                final String cmd = trim(lowerCase(it.next()));
                if (isBlank(cmd)) continue;

                logger.debug("{} user typed: {}", NAME, cmd);
                switch (cmd) {
                    case "/h":
                    case "/help":
                        printHelp();
                        continue;
                    case "/q":
                    case "/quit":
                        service.get().stop();
                        break;
                    case "/b":
                    case "/button":
                        // todo
                        ucLogger.info("Nothing todo");
                        continue;
                    case "/e":
                    case "/elevator":
                        // todo
                        ucLogger.info("Nothing todo");
                        continue;
                }
            }
        } catch (Exception e) {
            logger.error("{} error in time of processing", NAME, e);
        } finally {
            shutdownClient();
        }
    }

    private static void printHelp() {
        ucLogger.info(String.format("\t%s - %s%n\t%s%n", "h", "help", "You see current message"));
        ucLogger.info(String.format("\t%s - %s%n\t%s%n", "b",
                "button <number>",
                "Press the button to select the floor"));
        ucLogger.info(String.format("\t%s - %s%n\t%s%n", "e",
                "elevator",
                "Invoke the elevator on the 1st floor (main lobby)"));
        ucLogger.info(String.format("\t%s - %s%n\t%s%n", "q", "quit", "End session and quit"));
        ucLogger.info("Start your command with slash symbol '/'\nAuthor: Dmitriy Shishmakov\n");
    }

    private void shutdownClient() {
        if (watcherState.compareAndSet(true, false)) {
            logger.debug("{} waiting for shutdown the client...", NAME);
        }
    }
}
