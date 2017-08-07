package ru.shishmakov.core.state;

import org.junit.Test;
import ru.shishmakov.BaseTest;
import ru.shishmakov.core.Command;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StopOpenStateTest extends BaseTest {

    @Test
    public void tryGoNextShouldReturnStopCloseStateIfTimeExpired() {
        stopOpenState.floor = 1;

        ElevatorState state = stopOpenState.tryGoNext();

        assertThat("Should be StopClose state", state, instanceOf(StopCloseState.class));
        assertEquals("Floor should be the same", stopOpenState.floor, state.floor);
        verify(timeController, times(1)).isTimeExpired(anyLong());
    }

    @Test
    public void tryGoNextShouldReturnStopOpenStateIfTimeNotExpired() {
        stopOpenState.floor = 1;
        stopOpenState.deadline = currentTime + 1;

        ElevatorState state = stopOpenState.tryGoNext();

        assertThat("Should be StopOpen state", state, instanceOf(StopOpenState.class));
        assertEquals("Floor should be the same", stopOpenState.floor, state.floor);
        verify(timeController, times(1)).isTimeExpired(anyLong());
    }

    @Test
    public void applyCommandShouldReturnStopOpenStateIfCommandElevator() {
        stopOpenState.floor = 1;

        ElevatorState state = stopOpenState.applyCommand(Command.callElevator());

        assertThat("Should be StopOpen state", state, instanceOf(StopOpenState.class));
        assertEquals("Floor should be the first", stopOpenState.floor, state.floor);
    }

    @Test
    public void applyCommandShouldReturnStopOpenStateIfCommandPressButtonAndFloorTheSame() {
        stopOpenState.floor = 1;

        ElevatorState state = stopOpenState.applyCommand(Command.pressButton(1));

        assertThat("Should be StopOpen state", state, instanceOf(StopOpenState.class));
        assertEquals("Floor should be the same", stopOpenState.floor, state.floor);
    }

    @Test
    public void applyCommandShouldReturnStopCloseStateIfCommandPressButtonAndFloorIsDifferentAndCommandsQueueIsEmpty() {
        stopOpenState.floor = 1;

        ElevatorState state = stopOpenState.applyCommand(Command.pressButton(5));

        assertThat("Should be StopClose state", state, instanceOf(StopCloseState.class));
        assertEquals("Floor should be the same", stopOpenState.floor, state.floor);
    }

    @Test
    public void applyCommandShouldReturnStopOpenStateIfCommandPressButtonAndFloorIsDifferentAndCommandsQueueIsNotEmpty() {
        stopOpenState.floor = 1;
        elevatorCommands.offer(Command.callElevator());

        ElevatorState state = stopOpenState.applyCommand(Command.pressButton(5));

        assertThat("Should be StopOpen state", state, instanceOf(StopOpenState.class));
        assertEquals("Floor should be the same", stopOpenState.floor, state.floor);
    }
}
