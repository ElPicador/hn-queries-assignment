package com.algolia.assignment.model;

import com.algolia.assignment.model.date.DateConverter;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.function.UnaryOperator;

/**
 * A time interval for which we will examine queries.
 */
@Immutable
public final class TimeRange {

    /**
     * Start date is inclusive.
     */
    private final LocalDateTime start;
    /**
     * End date is exclusive.
     */
    private final LocalDateTime end;

    private TimeRange(LocalDateTime aStart, LocalDateTime anEnd) {
        this.start = aStart;
        this.end = anEnd;
    }

    public static TimeRange from(String datePrefix) {
        return getResolution(datePrefix).asTimeRange(datePrefix);
    }

    private static Resolution getResolution(String datePrefix) {
        // We are not going out of our way to accommodate incomplete formats.
        // Anything outside the specified format will fail.
        switch (datePrefix.length()) {
            case 4:
                return Resolution.YEAR;
            case 7:
                return Resolution.MONTH;
            case 10:
                return Resolution.DAY;
            case 13:
                return Resolution.HOUR;
            case 16:
                return Resolution.MINUTE;
            case 19:
                return Resolution.SECOND;
            default:
                throw new DateTimeException("Not an accepted date prefix.");
        }
    }

    @Override
    public String toString() {
        return "TimeRange{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Transforms the date prefix
     * into an actual time range.
     *
     * @see Resolution#asTimeRange(String)
     */
    private enum Resolution {
        YEAR(year -> year + "-01-01 00:00:00",
                dt -> dt.plusYears(1)),

        MONTH(month -> month + "-01 00:00:00",
                dt -> dt.plusMonths(1)),

        DAY(day -> day + " 00:00:00",
                dt -> dt.plusDays(1)),

        HOUR(hour -> hour + ":00:00",
                dt -> dt.plusHours(1)),

        MINUTE(minute -> minute + ":00",
                dt -> dt.plusMinutes(1)),

        SECOND(second -> second,
                dt -> dt.plusSeconds(1));

        /**
         *
         */
        private final UnaryOperator<String> formatStart;
        private final UnaryOperator<LocalDateTime> offsetToEnd;

        Resolution(UnaryOperator<String> formatStartOp, UnaryOperator<LocalDateTime> offsetOp) {
            this.formatStart = formatStartOp;
            this.offsetToEnd = offsetOp;
        }

        TimeRange asTimeRange(String datePrefix) {
            LocalDateTime start = DateConverter.parse(this.formatStart.apply(datePrefix));
            LocalDateTime end = this.offsetToEnd.apply(start);
            return new TimeRange(start, end);
        }
    }
}