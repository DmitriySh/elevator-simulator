package ru.shishmakov.core;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ru.vyarus.guice.ext.ExtAnnotationsModule;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.shishmakov.util.Threads.buildThreadFactory;

/**
 * @author Dmitriy Shishmakov on 31.07.17
 */
public class CustomModule extends AbstractModule {
    @Override
    protected void configure() {
        binder().install(new ExtAnnotationsModule());
    }

    @Provides
    @Singleton
    @Named("elevator.executor")
    public ExecutorService starterExecutor() {
        return Executors.newCachedThreadPool(buildThreadFactory("elevator.executor"));
    }
}
