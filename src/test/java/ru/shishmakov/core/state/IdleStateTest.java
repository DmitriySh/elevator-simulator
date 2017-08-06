package ru.shishmakov.core.state;

import org.junit.Before;
import org.junit.Test;
import ru.shishmakov.BaseTest;
import ru.shishmakov.core.Command;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

public class IdleStateTest extends BaseTest {

    @Before
    @Override
    public void setUp() {
        super.setUp();

        doReturn(currentTime).when(timeController).now();
        doReturn(idleState).when(idleProvider).get();
        doReturn(moveUpOrDownState).when(moveUpOrDownProvider).get();
        doReturn(stopOpenState).when(stopOpenProvider).get();
        doReturn(stopCloseState).when(stopCloseProvider).get();
    }

    @Test
    public void tryGoNextShouldReturnIdleState() throws Exception {
        ElevatorState state = idleState.tryGoNext();

        assertThat("Should be Idle state", state, instanceOf(IdleState.class));
    }

    @Test
    public void applyCommandShouldReturnStopOpenStateIfCommandElevatorAndFloorIsFirst() throws Exception {
        idleState.floor = 1;
        ElevatorState state = idleState.applyCommand(Command.callElevator());

        assertThat("Should be StopOpen state", state, instanceOf(StopOpenState.class));
        assertEquals("Floor should be the first", idleState.floor, state.floor);
    }

    @Test
    public void applyCommandShouldReturnMoveUpOrDownStateIfCommandElevatorAndFloorAboveFirst() throws Exception {
        idleState.floor = 5;
        ElevatorState state = idleState.applyCommand(Command.callElevator());

        assertThat("Should be MoveUpOrDownState state", state, instanceOf(MoveUpOrDownState.class));
        assertEquals("Floor should be the same", idleState.floor, state.floor);
        assertEquals("Goal floor should be the first", 1, ((MoveUpOrDownState) state).goalFloor);
    }

    @Test
    public void applyCommandShouldReturnStopOpenStateIfCommandPressButtonAndFloorTheSame() throws Exception {
        idleState.floor = 1;
        ElevatorState state = idleState.applyCommand(Command.pressButton(1));

        assertThat("Should be StopOpen state", state, instanceOf(StopOpenState.class));
        assertEquals("Floor should be the same", idleState.floor, state.floor);
    }

    @Test
    public void applyCommandShouldReturnMoveUpOrDownStateIfCommandPressButtonAndFloorIsDifferent() throws Exception {
        idleState.floor = 1;
        ElevatorState state = idleState.applyCommand(Command.pressButton(5));

        assertThat("Should be MoveUpOrDownState state", state, instanceOf(MoveUpOrDownState.class));
        assertEquals("Floor should be the same", idleState.floor, state.floor);
    }
}
