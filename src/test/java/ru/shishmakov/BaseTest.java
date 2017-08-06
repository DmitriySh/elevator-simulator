package ru.shishmakov;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shishmakov.core.Command;
import ru.shishmakov.core.Inbound;
import ru.shishmakov.core.TimeController;
import ru.shishmakov.core.state.IdleState;
import ru.shishmakov.core.state.MoveUpOrDownState;
import ru.shishmakov.core.state.StopCloseState;
import ru.shishmakov.core.state.StopOpenState;

import javax.inject.Provider;
import java.util.concurrent.BlockingQueue;

import static org.mockito.Mockito.doReturn;

/**
 * Base class for JUnit test classes.
 *
 * @author Dmitriy Shishmakov on 06.08.17
 */
public abstract class BaseTest {
    /**
     * Logger used by test.
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    protected static final long currentTime = System.currentTimeMillis();

    @Spy
    @InjectMocks
    protected IdleState idleState;
    @Spy
    @InjectMocks
    protected MoveUpOrDownState moveUpOrDownState;
    @Spy
    @InjectMocks
    protected StopOpenState stopOpenState;
    @Spy
    @InjectMocks
    protected StopCloseState stopCloseState;

    @Spy
    protected Inbound inbound = Inbound.buildInbound(new String[]{"-n", "15", "-h", "4", "-v", "2", "-d", "10"});
    @Spy
    protected TimeController timeController;
    @Mock(name = "idleProvider")
    protected Provider<IdleState> idleProvider;
    @Mock(name = "moveUpOrDownProvider")
    protected Provider<MoveUpOrDownState> moveUpOrDownProvider;
    @Mock(name = "stopCloseProvider")
    protected Provider<StopCloseState> stopCloseProvider;
    @Mock(name = "stopOpenProvider")
    protected Provider<StopOpenState> stopOpenProvider;
    @Mock
    protected BlockingQueue<Command> elevatorCommands;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        logTestStart();
        MockitoAnnotations.initMocks(this);

        doReturn(currentTime).when(timeController).now();
        doReturn(idleState).when(idleProvider).get();
        doReturn(moveUpOrDownState).when(moveUpOrDownProvider).get();
        doReturn(stopOpenState).when(stopOpenProvider).get();
        doReturn(stopCloseState).when(stopCloseProvider).get();
    }

    private void logTestStart() {
        logger.info("Running test \"{}\"", testName.getMethodName());
    }

}
