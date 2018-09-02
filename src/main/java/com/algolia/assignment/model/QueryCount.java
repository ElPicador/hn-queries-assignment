package com.algolia.assignment.model;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.Comparator;
import java.util.Objects;

/**
 * A simple data class grouping the text of a query
 * with the count of its occurrences.
 */
@Immutable
public final class QueryCount {
    private final String query;
    private final int count;

    public QueryCount(String query, int count) {
        this.query = query;
        this.count = count;
    }

    public static Comparator<QueryCount> getCountComparator() {
        return Comparator.comparing(QueryCount::getCount).reversed();
    }

    public String getQuery() {
        return query;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryCount that = (QueryCount) o;
        return count == that.count &&
                Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, count);
    }

    @Override
    public String toString() {
        return "QueryCount{" +
                "query='" + query + '\'' +
                ", count=" + count +
                '}';
    }
}
