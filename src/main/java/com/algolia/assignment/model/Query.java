package com.algolia.assignment.model;

import com.algolia.assignment.indexing.Repository;
import com.algolia.assignment.model.date.DateConverter;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * The representation of a HN query. This class is tailored for compactness,
 * since a large number of instances will be held by {@link Repository}.
 */
@Immutable
public final class Query {
    /**
     * A datetime, stored in Epoch seconds.
     * Note: We could have chosen an int format (which can code for 68 years at second-resolution),
     * but the corresponding Java memory layout would bring no memory gain (see jol-memory-occupation.txt).
     * // TODO: unless we also compact the text String?
     * <p>
     * Because storing the date as a primitive type is a compromise on object-orientation,
     * this class has added methods accepting other types for comparison.
     * This saves us from leaking the primitive format.
     */
    private final long date;

    /**
     * The query text.
     */
    private final String text;

    private Query(long date, String text) {
        this.text = text;
        this.date = date;
    }

    /**
     * Public constructor
     * @param dateTime a String representation conforming to the format specified in the assignment
     * @see DateConverter#FORMATTER
     * @param text the text of the query
     */
    public Query(String dateTime, String text) {
        this(DateConverter.toEpochSecond(dateTime), text);
    }

    public static Comparator<Query> getDateComparator() {
        return Comparator.comparing(Query::getDate);
    }

    private long getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public int compareDateTo(Query anotherQuery) {
        return compareDateTo(anotherQuery.date);
    }

    public int compareDateTo(LocalDateTime targetDate) {
        return compareDateTo(DateConverter.toEpochSecond(targetDate));
    }

    private int compareDateTo(long anotherTime) {
        return Long.compare(date, anotherTime);
    }

    @Override
    public String toString() {
        return "Query{" +
                "date=" + DateConverter.ofEpochSecond(date) +
                ", text='" + text + '\'' +
                '}';
    }
}

