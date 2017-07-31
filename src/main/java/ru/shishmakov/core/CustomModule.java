package ru.shishmakov.core;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ru.vyarus.guice.ext.ExtAnnotationsModule;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;

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
    @Named("client.executor")
    public ExecutorService starterExecutor() {
        return ThreadPoolBuilder.pool("client.executor")
                .withThreads(1)
                .build();
    }
}
