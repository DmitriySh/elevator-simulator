package ru.shishmakov.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

    public static long now() {
        return System.currentTimeMillis();
    }

    public static Instant nowInstant() {
        return Instant.ofEpochMilli(now());
    }

    public static boolean isTimeExpired(long timeStamp) {
        return isBeforeOrNow(timeStamp);
    }

    public static boolean isTimeNotExpired(long timeStamp) {
        return isAfterNow(timeStamp);
    }

    public static boolean isAfterNow(long timeStamp) {
        return timeStamp > now();
    }

    public static boolean isAfterOrNow(long timeStamp) {
        long now = now();
        return timeStamp > now || now == timeStamp;
    }

    public static boolean isBeforeNow(long timeStamp) {
        return now() > timeStamp;
    }

    public static boolean isBeforeOrNow(long timeStamp) {
        long now = now();
        return now > timeStamp || now == timeStamp;
    }

    public static long nowPlus(long amount, ChronoUnit unit) {
        return nowInstant().plus(amount, unit).toEpochMilli();
    }
}
