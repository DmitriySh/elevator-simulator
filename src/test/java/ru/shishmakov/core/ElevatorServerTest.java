package ru.shishmakov.core;

import com.google.common.collect.Sets;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.shishmakov.BaseTest;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import static org.junit.Assert.assertEquals;
import static ru.shishmakov.core.LifeCycle.IDLE;
import static ru.shishmakov.core.LifeCycle.RUN;

public class ElevatorServerTest extends BaseTest {

    @Mock
    private ExecutorService executor;
    @Mock
    private ConsoleService consoleService;
    @Mock
    private ElevatorService elevatorService;

    @InjectMocks
    private ElevatorServer server;

    @Override
    public void setUp() {
        super.setUp();
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
        Set<LifeCycle> states = new HashSet<>();
        CountDownLatch run = new CountDownLatch(1);

        new Thread(() -> {
            try {
                run.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", e);
            }

            server.stop();
        }, "await-thread").start();

        states.add(server.getServiceState().get());
        server.start();
        run.countDown();

        server.await();
        states.add(server.getServiceState().get());

        assertEquals("Should be only Idle state", Sets.newHashSet(IDLE), states);
    }
}
