package ru.shishmakov.util;

public enum ElevatorState {
    /**
     * The elevator without a passenger with closed door
     */
    IDLE,
    MOVE_UP,
    MOVE_DOWN,
    MOVE_STOP,
    STOP_CLOSE,
    STOP_OPEN
}
