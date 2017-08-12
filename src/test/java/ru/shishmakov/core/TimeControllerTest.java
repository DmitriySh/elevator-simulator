package ru.shishmakov.core;

import org.junit.Test;
import ru.shishmakov.BaseTest;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.*;

public class TimeControllerTest extends BaseTest {

    @Test
    public void nowInstantShouldReturnCurrentTime() {
        Instant now = timeController.nowInstant();

        assertEquals("Time should be current", currentTime, now.toEpochMilli());
    }

    @Test
    public void isBeforeOrNowShouldReturnTrueIfTimeIsCurrent() {
        boolean timestamp = timeController.isBeforeOrNow(currentTime);

        assertTrue("Check time should return true", timestamp);
    }

    @Test
    public void isBeforeOrNowShouldReturnTrueIfTimeIsPast() {
        boolean timestamp = timeController.isBeforeOrNow(currentTime - 1);

        assertTrue("Check time should return true", timestamp);
    }

    @Test
    public void isBeforeOrNowShouldReturnFalseIfTimeIsFuture() {
        boolean timestamp = timeController.isBeforeOrNow(currentTime + 1);

        assertFalse("Check time should return false", timestamp);
    }

    @Test
    public void isAfterOrNowShouldReturnTrueIfTimeIsCurrent() {
        boolean timestamp = timeController.isAfterOrNow(currentTime);

        assertTrue("Check time should return true", timestamp);
    }

    @Test
    public void isAfterOrNowShouldReturnTrueIfTimeIsFuture() {
        boolean timestamp = timeController.isAfterOrNow(currentTime + 1);

        assertTrue("Check time should return true", timestamp);
    }

    @Test
    public void isAfterOrNowShouldReturnFalseIfTimeIsPast() {
        boolean timestamp = timeController.isAfterOrNow(currentTime - 1);

        assertFalse("Check time should return false", timestamp);
    }

    @Test
    public void isTimeExpiredShouldReturnTrueIfTimeIsCurrent() {
        boolean expired = timeController.isTimeExpired(currentTime);

        assertTrue("Time should be expired", expired);
    }

    @Test
    public void isTimeExpiredShouldReturnTrueIfTimeIsPast() {
        boolean expired = timeController.isTimeExpired(currentTime - 1);

        assertTrue("Time should be expired", expired);
    }

    @Test
    public void isTimeExpiredShouldReturnFalseIfTimeIsFuture() {
        boolean expired = timeController.isTimeExpired(currentTime + 1);

        assertFalse("Time should not be expired", expired);
    }

    @Test
    public void nowPlusShouldReturnLegalFutureTimeIfShiftTimePositive() {
        int seconds = 11;
        long timestamp = timeController.nowPlus(seconds, SECONDS);

        assertTrue("Time should be in future", timestamp > currentTime);
        assertEquals("Time shift should be legal", seconds, MILLISECONDS.toSeconds(timestamp - currentTime));
    }

    @Test
    public void nowPlusShouldReturnLegalPastTimeIfShiftTimeNegative() {
        int seconds = -11;
        long timestamp = timeController.nowPlus(seconds, SECONDS);

        assertTrue("Time should be in past", timestamp < currentTime);
        assertEquals("Time shift should be legal", seconds, MILLISECONDS.toSeconds(timestamp - currentTime));
    }
}
