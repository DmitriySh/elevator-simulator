package ru.shishmakov.core;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import ru.shishmakov.core.state.ElevatorState;
import ru.shishmakov.core.state.IdleState;
import ru.vyarus.guice.ext.ExtAnnotationsModule;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.shishmakov.core.Inbound.buildInbound;
import static ru.shishmakov.util.Threads.buildThreadFactory;

/**
 * @author Dmitriy Shishmakov on 31.07.17
 */
public class GuiceModule extends AbstractModule {
    private final Inbound inbound;

    public GuiceModule(String[] args) {
        this.inbound = buildInbound(args);
    }

    @Override
    protected void configure() {
        binder().install(new ExtAnnotationsModule());
        binder().bind(Inbound.class).annotatedWith(Names.named("elevator.inbound")).toInstance(inbound);
        binder().bind(ElevatorState.class).annotatedWith(Names.named("elevator.startState")).to(IdleState.class);
    }

    @Provides
    @Singleton
    @Named("elevator.executor")
    public ExecutorService elevatorExecutor() {
        return Executors.newCachedThreadPool(buildThreadFactory("elevator.executor"));
    }

    @Provides
    @Singleton
    @Named("elevator.commands")
    public BlockingQueue<Command> elevatorCommands() {
        return new ArrayBlockingQueue<>(2048);
    }
}
