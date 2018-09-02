package com.algolia.assignment.model.date;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class DateConverter {
    /**
     * A formatter corresponding to the syntax specified in the assignment.
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * A standard ZoneOffset
     */
    private static final ZoneOffset ZONEOFFSET = ZoneOffset.UTC;

    public static LocalDateTime parse(String dateTime) {
        return LocalDateTime.parse(dateTime, FORMATTER);
    }

    public static long toEpochSecond(String dateTime) {
        return toEpochSecond(parse(dateTime));
    }

    public static long toEpochSecond(LocalDateTime dateTime) {
        return dateTime.toEpochSecond(ZONEOFFSET);
    }

    public static LocalDateTime ofEpochSecond(long dateAsLong) {
        return LocalDateTime.ofEpochSecond(dateAsLong, 0, ZONEOFFSET);
    }
}
