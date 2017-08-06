package ru.shishmakov.core.state;

import org.junit.Test;
import ru.shishmakov.BaseTest;
import ru.shishmakov.core.Command;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class StopOpenStateTest extends BaseTest {

    @Test
    public void tryGoNextShouldReturnStopCloseStateIfTimeExpired() throws Exception {
        stopOpenState.floor = 1;

        ElevatorState state = stopOpenState.tryGoNext();

        assertThat("Should be StopClose state", state, instanceOf(StopCloseState.class));
        assertEquals("Floor should be the same", stopOpenState.floor, state.floor);
        verify(timeController, times(1)).isTimeExpired(anyLong());
    }

    @Test
    public void tryGoNextShouldReturnStopOpenStateIfTimeNotExpired() throws Exception {
        stopOpenState.floor = 1;
        stopOpenState.deadline = currentTime + 1;

        ElevatorState state = stopOpenState.tryGoNext();

        assertThat("Should be StopOpen state", state, instanceOf(StopOpenState.class));
        assertEquals("Floor should be the same", stopOpenState.floor, state.floor);
        verify(timeController, times(1)).isTimeExpired(anyLong());
    }

    @Test
    public void applyCommandShouldReturnStopOpenStateIfCommandElevator() throws Exception {
        stopOpenState.floor = 1;

        ElevatorState state = stopOpenState.applyCommand(Command.callElevator());

        assertThat("Should be StopOpen state", state, instanceOf(StopOpenState.class));
        assertEquals("Floor should be the first", stopOpenState.floor, state.floor);
    }

    @Test
    public void applyCommandShouldReturnStopOpenStateIfCommandPressButtonAndFloorTheSame() throws Exception {
        stopOpenState.floor = 1;

        ElevatorState state = stopOpenState.applyCommand(Command.pressButton(1));

        assertThat("Should be StopOpen state", state, instanceOf(StopOpenState.class));
        assertEquals("Floor should be the same", stopOpenState.floor, state.floor);
    }

    @Test
    public void applyCommandShouldReturnStopCloseStateIfCommandPressButtonAndFloorIsDifferentAndQueueCommandsIsEmpty() throws Exception {
        stopOpenState.floor = 1;
        doReturn(true).when(elevatorCommands).isEmpty();

        ElevatorState state = stopOpenState.applyCommand(Command.pressButton(5));

        assertThat("Should be StopClose state", state, instanceOf(StopCloseState.class));
        assertEquals("Floor should be the same", stopOpenState.floor, state.floor);
    }

    @Test
    public void applyCommandShouldReturnStopOpenStateIfCommandPressButtonAndFloorIsDifferentAndQueueCommandsIsNotEmpty() throws Exception {
        stopOpenState.floor = 1;

        ElevatorState state = stopOpenState.applyCommand(Command.pressButton(5));

        assertThat("Should be StopOpen state", state, instanceOf(StopOpenState.class));
        assertEquals("Floor should be the same", stopOpenState.floor, state.floor);
    }
}
