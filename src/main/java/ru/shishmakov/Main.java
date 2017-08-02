package ru.shishmakov;

import com.google.inject.Guice;
import ru.shishmakov.core.GuiceModule;
import ru.shishmakov.core.ServiceController;

/**
 * @author Dmitriy Shishmakov on 31.07.17
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        Guice.createInjector(new GuiceModule(args))
                .getInstance(ServiceController.class)
                .start().await();
    }
}
