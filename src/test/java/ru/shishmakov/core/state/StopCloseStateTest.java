package ru.shishmakov.core.state;

import org.junit.Test;
import ru.shishmakov.BaseTest;
import ru.shishmakov.core.Command;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StopCloseStateTest extends BaseTest {

    @Test
    public void tryGoNextShouldReturnStopCloseStateIfTimeNotExpired() {
        stopCloseState.floor = 1;
        stopCloseState.deadline = currentTime + 1;

        ElevatorState state = stopCloseState.tryGoNext();

        assertThat("Should be StopClose state", state, instanceOf(StopCloseState.class));
        assertEquals("Floor should be the same", stopCloseState.floor, state.floor);
        verify(timeController, times(1)).isTimeExpired(anyLong());
    }

    @Test
    public void tryGoNextShouldReturnIdleStateIfTimeExpiredAndCommandsQueueIsEmpty() {
        stopCloseState.floor = 1;

        ElevatorState state = stopCloseState.tryGoNext();

        assertThat("Should be Idle state", state, instanceOf(IdleState.class));
        assertEquals("Floor should be the same", stopCloseState.floor, state.floor);
        verify(timeController, times(1)).isTimeExpired(anyLong());
    }

    @Test
    public void tryGoNextShouldReturnMoveUpOrDownStateIfTimeExpiredAndQueueHasPressButtonCommand() {
        stopCloseState.floor = 1;

        elevatorCommands.offer(Command.pressButton(5));

        ElevatorState state = stopCloseState.tryGoNext();

        assertThat("Should be MoveUpOrDown state", state, instanceOf(MoveUpOrDownState.class));
        assertEquals("Floor should be the same", stopCloseState.floor, state.floor);
        assertEquals("Goal floor should be the five", 5, ((MoveUpOrDownState) state).goalFloor);
        verify(timeController, times(1)).isTimeExpired(anyLong());
    }

    @Test
    public void tryGoNextShouldReturnIdleStateIfTimeExpiredAndQueueHasCallElevatorCommand() {
        stopCloseState.floor = 1;

        elevatorCommands.offer(Command.callElevator());

        ElevatorState state = stopCloseState.tryGoNext();

        assertThat("Should be Idle state", state, instanceOf(IdleState.class));
        assertEquals("Floor should be the same", stopCloseState.floor, state.floor);
        verify(timeController, times(1)).isTimeExpired(anyLong());
    }

    @Test
    public void applyCommandShouldReturnStopCloseStateIfCommandElevator() {
        stopCloseState.floor = 1;

        ElevatorState state = stopCloseState.applyCommand(Command.callElevator());

        assertThat("Should be StopClose state", state, instanceOf(StopCloseState.class));
        assertEquals("Floor should be the first", stopCloseState.floor, state.floor);
    }

    @Test
    public void applyCommandShouldReturnStopCloseStateIfCommandPressButtonAndQueueIsNotEmpty() {
        stopCloseState.floor = 1;
        elevatorCommands.offer(Command.pressButton(5));

        ElevatorState state = stopCloseState.applyCommand(Command.pressButton(1));

        assertThat("Should be StopClose state", state, instanceOf(StopCloseState.class));
        assertEquals("Floor should be the same", stopCloseState.floor, state.floor);
    }

    @Test
    public void applyCommandShouldReturnStopOpenStateIfCommandPressButtonAndQueueIsEmptyAndCommandFloorTheSame() {
        stopCloseState.floor = 1;

        ElevatorState state = stopCloseState.applyCommand(Command.pressButton(1));

        assertThat("Should be StopOpen state", state, instanceOf(StopOpenState.class));
        assertEquals("Floor should be the same", stopCloseState.floor, state.floor);
    }

    @Test
    public void applyCommandShouldReturnStopCloseStateIfCommandPressButtonAndQueueIsEmptyAndCommandFloorIsDifferent() {
        stopCloseState.floor = 1;

        ElevatorState state = stopCloseState.applyCommand(Command.pressButton(5));

        assertThat("Should be StopClose state", state, instanceOf(StopCloseState.class));
        assertEquals("Floor should be the same", stopCloseState.floor, state.floor);
    }

    @Test
    public void applyCommandShouldAddCommandPressButtonIfCommandPressButtonAndCommandsQueueIsEmptyAndCommandFloorIsDifferent() {
        stopCloseState.floor = 1;

        stopCloseState.applyCommand(Command.pressButton(5));

        assertFalse("Command queue is not empty", elevatorCommands.isEmpty());
        assertTrue("Should be StopClose state", elevatorCommands.peek().isPressButton());
    }
}
