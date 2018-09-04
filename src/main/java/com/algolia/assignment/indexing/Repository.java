package com.algolia.assignment.indexing;

import com.algolia.assignment.model.Query;
import com.algolia.assignment.model.QueryCount;
import com.algolia.assignment.model.TimeRange;
import com.algolia.assignment.model.util.MutableInt;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class holds the content data.
 * It implements the search methods used in the application.
 */
@Immutable
public final class Repository {
    private static final Comparator<Query> DATE_COMPARATOR = Query.getDateComparator();
    private static final Logger LOGGER = LoggerFactory.getLogger(Repository.class);
    /**
     * The list of indexedQueries, indexed by date.
     */
    private final ArrayList<Query> indexedQueries;


    private Repository(ArrayList<Query> list) {
        this.indexedQueries = list;
    }

    /**
     * Factory method: creates a new instance,
     * feeding its underlying array index from a stream.
     */
    public static Repository fromStream(Stream<Query> stream) {
        long start = System.currentTimeMillis();
        ArrayList<Query> queries = stream.sorted(DATE_COMPARATOR).collect(Collectors.toCollection(ArrayList::new));
        long end = System.currentTimeMillis();
        LOGGER.info("Indexed indexedQueries in {} ms.", end - start);

        return new Repository(queries);
    }

    /**
     * Returns the top-n most popular indexedQueries in a time range.
     */
    public List<QueryCount> getPopularQueries(TimeRange range, int top) {
        List<Query> interval = restrictToRange(range);
        Map<String, MutableInt> countsByText = getCountsByText(interval);

        return sortTopK(top, countsByText);
    }

    private Map<String, MutableInt> getCountsByText(List<Query> queries) {
        // We use ConcurrentHashMap for its performance, not for its concurrency features.
        Map<String, MutableInt> countsByText = new ConcurrentHashMap<>(queries.size());
        queries.forEach(q -> countsByText.computeIfAbsent(q.getText(), x -> new MutableInt()).
                increment());
        return countsByText;
    }

    /**
     * Since we probably want to sort a small fraction of the result,
     * it makes sense to use a heap.
     */
    private List<QueryCount> sortTopK(int top, Map<String, MutableInt> countsByText) {

        Queue<QueryCount> queue = new PriorityQueue<>(countsByText.entrySet().size(), QueryCount.getCountComparator());
        for (Map.Entry<String, MutableInt> entry : countsByText.entrySet()) {
            queue.add(new QueryCount(entry.getKey(), entry.getValue().intValue()));
        }

        List<QueryCount> popular = new ArrayList<>();
        for (int i = 0, m = Math.min(top, queue.size()); i < m; i++) {
            popular.add(queue.remove());
        }
        return popular;
    }

    /**
     * Returns the number of indexedQueries in a time range.
     */
    public long getDistinctQueriesCount(TimeRange range) {
        List<Query> queries = restrictToRange(range);
        Set<String> uniqueTexts = new HashSet<>(queries.size());
        queries.forEach(q -> uniqueTexts.add(q.getText()));
        return uniqueTexts.size();
    }

    private List<Query> restrictToRange(TimeRange range) {
        int startIndex = positionAtFirstQueryWithDate(range.getStart());
        int endIndex = positionAtFirstQueryWithDate(range.getEnd());
        return indexedQueries.subList(startIndex, endIndex);
    }

    private int positionAtFirstQueryWithDate(LocalDateTime target) {
        int pos = binarySearch(target);
        if (pos < 0)
            return -pos - 1; // as by contract of binary search.

        // We have located an entry at the right time, but it may be non-unique.
        // => we need to rewind to the first entry at this time.
        Query current = indexedQueries.get(pos);
        while (pos > 0) {
            Query previous = indexedQueries.get(pos - 1);
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
        int high = indexedQueries.size() - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = indexedQueries.get(mid).compareDateTo(targetDate);
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
        int size = indexedQueries.size();
        String s = "Repository{" +
                "size=" + size;
        if (size < 10)
            s += ", indexedQueries=" + indexedQueries;
        return s + '}';
    }
}
