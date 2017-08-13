package ru.shishmakov.core;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.shishmakov.BaseTest;
import ru.shishmakov.config.ElevatorConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static ru.shishmakov.core.LifeCycle.IDLE;
import static ru.shishmakov.core.LifeCycle.RUN;

public class ServerTest extends BaseTest {

    @Mock
    private ExecutorService executor;
    @Mock
    private ConsoleService consoleService;
    @Mock
    private ElevatorService elevatorService;

    @InjectMocks
    private Server server;

    @Override
    public void setUp() {
        super.setUp();
        doNothing().when(inbound).validate(any(ElevatorConfig.class));
    }

    @Test
    public void defaultLifeCycleStateIsIdle() {
        assertEquals("Life cycle state should be Idle", IDLE, server.getServiceState().get());
    }

    @Test
    public void startShouldSetRunLifeCycleState() {
        server.start();

        assertEquals("Life cycle state should be Run", RUN, server.getServiceState().get());
    }

    @Test
    public void stopShouldSetIdleLifeCycleStateIfServerIsRun() {
        server.start();
        assertEquals("Life cycle state should be Run", RUN, server.getServiceState().get());

        server.stop();
        assertEquals("Life cycle state should be Idle", IDLE, server.getServiceState().get());
    }

    @Test
    public void awaitShouldWaitIdleLifeCycleStateIfServerRun() throws InterruptedException {
        List<LifeCycle> states = new ArrayList<>(3);
        CountDownLatch startLatch = new CountDownLatch(1);

        new Thread(() -> {
            try {
                startLatch.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", e);
            }

            states.add(server.getServiceState().get()); // 2 step
            server.stop();
            states.add(server.getServiceState().get()); // 3 step
        }, "stop-server-thread").start();

        states.add(server.getServiceState().get()); // 1 step
        server.start();
        startLatch.countDown();

        server.await();

        assertEquals("Should be only Idle state", Lists.newArrayList(IDLE, RUN, IDLE), states);
    }
}
