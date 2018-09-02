package com.algolia.assignment.indexing;

import com.algolia.assignment.model.Query;
import com.algolia.assignment.model.QueryCount;
import com.algolia.assignment.model.TimeRange;
import com.algolia.assignment.todo.AppLogger;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class holds the content data.
 * It implements the search methods used in the application.
 */
@Immutable
public final class Repository {
    private static final Comparator<Query> DATE_COMPARATOR = Query.getDateComparator();
    /**
     * The list of queries, indexed by date.
     */
    private final ArrayList<Query> queries;

    private Repository(ArrayList<Query> list) {
        this.queries = list;
    }

    /**
     * Feeds this index from a stream.
     */
    public static Repository fromStream(Stream<Query> stream) {
        long start = System.currentTimeMillis();
        ArrayList<Query> queries = stream.sorted(DATE_COMPARATOR).collect(Collectors.toCollection(ArrayList::new));
        Repository repository = new Repository(queries);
        long end = System.currentTimeMillis();
        AppLogger.log(String.format("Indexed queries in %d ms.", end - start));
        return repository;
    }

    /**
     * Returns the top-n most popular queries in a time range.
     */
    public List<QueryCount> getPopularQueries(TimeRange range, int top) {
        Stream<Query> slice = sliceRange(range);
        Map<String, Long> countsByText = slice.collect(Collectors.groupingBy(Query::getText, Collectors.counting()));

        return sortTopK(top, countsByText);
    }

    /**
     * Since we only want a small fraction of the result sorted,
     * it is cheaper to use a priority queue.
     */
    private List<QueryCount> sortTopK(int top, Map<String, Long> countsByText) {

        Queue<QueryCount> queue = new PriorityQueue<>(countsByText.entrySet().size(), QueryCount.getCountComparator());
        for (Map.Entry<String, Long> entry : countsByText.entrySet()) {
            queue.add(new QueryCount(entry.getKey(), entry.getValue().intValue()));
        }

        List<QueryCount> popular = new ArrayList<>();
        for (int i = 0, m = Math.min(top, queue.size()); i < m; i++) {
            popular.add(queue.remove());
        }
        return popular;
    }

    /**
     * Returns the number of queries in a time range.
     */
    public long getDistinctQueriesCount(TimeRange range) {
        Stream<Query> slice = sliceRange(range);
        return slice.map(Query::getText).distinct().count();
    }

    private Stream<Query> sliceRange(TimeRange range) {
        int startIndex = positionAtFirstQueryWithDate(range.getStart());
        int endIndex = positionAtFirstQueryWithDate(range.getEnd());
        return queries.stream().skip(startIndex).limit(endIndex - startIndex);
    }

    private int positionAtFirstQueryWithDate(LocalDateTime target) {
        int pos = binarySearch(target);
        if (pos < 0)
            return -pos - 1; // as by contract of binary search.

        // We have located an entry at the right time, but it may be non-unique.
        // => we need to rewind to the first entry at this time.
        Query current = queries.get(pos);
        while (pos > 0) {
            Query previous = queries.get(pos - 1);
            if (previous.compareDateTo(current) < 0)
                break;
            pos--;
            current = previous;
        }
        return pos;
    }

    /**
     * @see Arrays#binarySearch(Object[], Object, Comparator)
     */
    private int binarySearch(LocalDateTime targetDate) {
        int low = 0;
        int high = queries.size() - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = queries.get(mid).compareDateTo(targetDate);
            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.
    }

    @Override
    public String toString() {
        int size = queries.size();
        String s = "Repository{" +
                "size=" + size;
        if (size < 10)
            s += ", queries=" + queries;
        return s + '}';
    }
}