package ru.shishmakov.core.state;

import org.assertj.core.util.Lists;
import org.junit.Test;
import ru.shishmakov.BaseTest;
import ru.shishmakov.core.Command;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MoveUpOrDownStateTest extends BaseTest {

    @Test
    public void tryGoNextShouldReturnStopOpenStateIfTimeExpired() {
        moveUpOrDownState.floor = 1;
        moveUpOrDownState.goalFloor = 1;
        moveUpOrDownState.startFloor = 5;

        ElevatorState state = moveUpOrDownState.tryGoNext();

        assertThat("Should be StopOpen state", state, instanceOf(StopOpenState.class));
        assertEquals("Floor should be the same", moveUpOrDownState.floor, state.floor);
        verify(timeController, times(1)).isTimeExpired(anyLong());
    }

    @Test
    public void tryGoNextShouldReturnMoveUpOrDownStateIfTimeNotExpired() {
        moveUpOrDownState.floor = 1;
        moveUpOrDownState.goalFloor = 1;
        moveUpOrDownState.startFloor = 5;
        moveUpOrDownState.deadline = currentTime + 1;

        ElevatorState state = moveUpOrDownState.tryGoNext();

        assertThat("Should be MoveUpOrDown state", state, instanceOf(MoveUpOrDownState.class));
        assertEquals("Floor should be the same", moveUpOrDownState.floor, state.floor);
        assertEquals("Goal floor should be the same", moveUpOrDownState.goalFloor, ((MoveUpOrDownState) state).goalFloor);
        verify(timeController, times(1)).isTimeExpired(anyLong());
    }

    @Test
    public void applyCommandShouldReturnMoveUpOrDownStateForAllCommands() {
        moveUpOrDownState.floor = 1;
        moveUpOrDownState.goalFloor = 1;
        moveUpOrDownState.startFloor = 5;
        List<Command> commands = Lists.newArrayList(Command.callElevator(), Command.pressButton(5));

        commands.forEach(cmd -> {
            ElevatorState state = moveUpOrDownState.applyCommand(cmd);
            assertThat("Should be MoveUpOrDown state", state, instanceOf(MoveUpOrDownState.class));
            assertEquals("Floor should be the same", moveUpOrDownState.floor, state.floor);
            assertEquals("Goal floor should be the same", moveUpOrDownState.goalFloor, ((MoveUpOrDownState) state).goalFloor);
        });
    }
}
