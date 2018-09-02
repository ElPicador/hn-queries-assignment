package com.algolia.assignment.model.date;

import com.algolia.assignment.model.TimeRange;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class TimeRangeTest {

    @Test
    public void yearRange() {
        String d = "2015";
        String expectedStart = "2015-01-01T00:00:00";
        String expectedEnd = "2016-01-01T00:00:00";
        checkRange(expectedStart, expectedEnd, d);

    }

    @Test
    public void monthRange() {
        String d = "2015-08";
        String expectedStart = "2015-08-01T00:00:00";
        String expectedEnd = "2015-09-01T00:00:00";
        checkRange(expectedStart, expectedEnd, d);

    }

    @Test
    public void dayRange() {
        String d = "2015-08-03";
        String expectedStart = "2015-08-03T00:00:00";
        String expectedEnd = "2015-08-04T00:00:00";
        checkRange(expectedStart, expectedEnd, d);
    }

    @Test
    public void hourRange() {
        String d = "2015-08-03 04";
        String expectedStart = "2015-08-03T04:00:00";
        String expectedEnd = "2015-08-03T05:00:00";
        checkRange(expectedStart, expectedEnd, d);
    }

    @Test
    public void minuteRange() {
        String d = "2015-08-03 00:04";
        String expectedStart = "2015-08-03T00:04:00";
        String expectedEnd = "2015-08-03T00:05:00";
        checkRange(expectedStart, expectedEnd, d);
    }

    @Test
    public void secondRange() {
        String d = "2015-08-03 01:02:03";
        String expectedStart = "2015-08-03T01:02:03";
        String expectedEnd = "2015-08-03T01:02:04";
        checkRange(expectedStart, expectedEnd, d);
    }

    private void checkRange(String expectedStart, String expectedEnd, String d) {
        TimeRange period = TimeRange.from(d);
        assertEqualsDate(expectedStart, period.getStart());
        assertEqualsDate(expectedEnd, period.getEnd());

    }

    private void assertEqualsDate(String expected, LocalDateTime dateTime) {
        Assert.assertEquals(expected, dateTime.format(DateTimeFormatter.ISO_DATE_TIME));
    }

    @Test
    public void replace() {
        String d = "2015-08-03 04";
        String r = d.replace(" ", "T");
        Assert.assertEquals("2015-08-03T04", r);
    }
}