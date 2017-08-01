package ru.shishmakov;

import com.beust.jcommander.JCommander;
import com.google.inject.Guice;
import ru.shishmakov.core.CustomModule;
import ru.shishmakov.core.Inbound;
import ru.shishmakov.core.ServiceController;

/**
 * @author Dmitriy Shishmakov on 31.07.17
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        Guice.createInjector(new CustomModule())
                .getInstance(ServiceController.class)
                .start(parseInboundParameters(args))
                .await();
    }

    private static Inbound parseInboundParameters(String[] args) {
        Inbound inbound = new Inbound();
        JCommander.newBuilder().addObject(inbound).build().parse(args);
        return inbound.validate();
    }
}
