package ru.shishmakov;

import com.google.inject.Guice;
import ru.shishmakov.core.CustomModule;
import ru.shishmakov.core.ElevatorService;

/**
 * @author Dmitriy Shishmakov on 31.07.17
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        Guice.createInjector(new CustomModule())
                .getInstance(ElevatorService.class)
                .startAsync().await();
    }
}
