package ru.shishmakov.core.state;

import org.junit.Test;
import ru.shishmakov.BaseTest;
import ru.shishmakov.core.Command;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class IdleStateTest extends BaseTest {

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
