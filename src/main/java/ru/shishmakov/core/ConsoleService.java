package ru.shishmakov.core;

import com.google.common.base.Splitter;
import com.google.common.collect.Range;
import com.google.inject.Singleton;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * @author Dmitriy Shishmakov on 31.07.17
 */
@Singleton
public class ConsoleService {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final Logger fileLogger = LoggerFactory.getLogger("fileLogger");

    private static final String NAME = MethodHandles.lookup().lookupClass().getSimpleName();

    @Inject
    @Named("elevator.executor")
    private ExecutorService executor;
    @Inject
    @Named("console.commands")
    private BlockingQueue<Command> consoleCommands;
    @Inject
    private Provider<Server> serverProvider;
    @Inject
    private Inbound inbound;

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
        shutdownConsole();
    }

    private void process() {
        logger.info("{} get ready, choose command: (/h - help)", NAME);
        try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in))) {
            while (watcherState.get() && !Thread.currentThread().isInterrupted()) {
                final String read = input.readLine();
                if (isBlank(read)) continue;

                final Iterator<String> it = Splitter.on(' ').split(read).iterator();
                if (!it.hasNext()) continue;

                final String cmd = trim(lowerCase(it.next()));
                if (isBlank(cmd)) continue;

                fileLogger.info("{} user typed: {}", NAME, cmd);
                switch (cmd) {
                    case "/h":
                    case "/help":
                        printHelp();
                        continue;
                    case "/q":
                    case "/quit":
                        serverProvider.get().stop();
                        break;
                    case "/b":
                    case "/button":
                        pressButton(it);
                        continue;
                    case "/e":
                    case "/elevator":
                        callElevator();
                        continue;
                }
            }
        } catch (Exception e) {
            logger.error("{} error at the time of processing", NAME, e);
        } finally {
            shutdownConsole();
        }
    }

    private void callElevator() {
        consoleCommands.offer(Command.callElevator());
    }

    private void pressButton(Iterator<String> it) {
        final String source = it.hasNext() ? it.next() : EMPTY;
        try {
            if (isValidateNumber(source)) {
                consoleCommands.offer(Command.pressButton(Integer.valueOf(source)));
            } else logger.info("Your floor number is not valid. Please try again...\n");
        } catch (Exception e) {
            logger.error("{} error at the time to send command\n", NAME, e);
        }
    }

    private boolean isValidateNumber(String source) {
        if (isBlank(source) || !NumberUtils.isCreatable(source)) return false;
        else return Range.closed(1, inbound.number).contains(Integer.valueOf(source));
    }

    private void printHelp() {
        logger.info(String.format("\t%s - %s%n\t%s%n", "h", "help", "You see current message"));
        logger.info(String.format("\t%s - %s%n\t%s%n", "b",
                "button [1.." + inbound.number + "]",
                "Press the button to select the floor"));
        logger.info(String.format("\t%s - %s%n\t%s%n", "e",
                "elevator",
                "Invoke the elevator on the 1st floor (main lobby)"));
        logger.info(String.format("\t%s - %s%n\t%s%n", "q", "quit", "End session and quit"));
        logger.info("Start your command with slash symbol '/'\nAuthor: Dmitriy Shishmakov\n");
    }

    private void shutdownConsole() {
        if (watcherState.compareAndSet(true, false)) {
            logger.debug("{} waiting for shutdown...", NAME);
        }
    }
}
