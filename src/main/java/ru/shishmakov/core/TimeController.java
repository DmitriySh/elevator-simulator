package ru.shishmakov.core;

import javax.inject.Singleton;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Singleton
public class TimeController {

    public long now() {
        return System.currentTimeMillis();
    }

    public Instant nowInstant() {
        return Instant.ofEpochMilli(now());
    }

    public boolean isTimeExpired(long timeStamp) {
        return isBeforeOrNow(timeStamp);
    }

    public boolean isTimeNotExpired(long timeStamp) {
        return isAfterNow(timeStamp);
    }

    public boolean isAfterNow(long timeStamp) {
        return timeStamp > now();
    }

    public boolean isAfterOrNow(long timeStamp) {
        long now = now();
        return timeStamp > now || now == timeStamp;
    }

    public boolean isBeforeNow(long timeStamp) {
        return now() > timeStamp;
    }

    public boolean isBeforeOrNow(long timeStamp) {
        long now = now();
        return now > timeStamp || now == timeStamp;
    }

    public long nowPlus(long amount, ChronoUnit unit) {
        return nowInstant().plus(amount, unit).toEpochMilli();
    }
}
