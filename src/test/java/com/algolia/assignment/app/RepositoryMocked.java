package com.algolia.assignment.app;

import com.algolia.assignment.indexing.Repository;
import com.algolia.assignment.model.Query;
import com.algolia.assignment.model.QueryCount;
import com.algolia.assignment.model.TimeRange;
import org.junit.Assert;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class RepositoryMocked {

    private static final CharSequence TAB = "\t";

    private static Query createQuery(String dateTime, String text) {
        return QuerySource.TSV_FILE.feedRecordFromLine(String.join(TAB, dateTime, text));
    }

    private Repository feedQueries() {
        List<Query> queries = Arrays.asList(
                createQuery("2015-08-02 23:59:59", "A"),
                //
                createQuery("2015-08-03 00:00:00", "B"),
                createQuery("2015-08-03 00:00:00", "B"),
                createQuery("2015-08-03 00:00:00", "C"),
                createQuery("2015-08-03 00:00:01", "C"),
                createQuery("2015-08-03 00:00:59", "C"),
                //
                createQuery("2015-08-04 00:01:00", "D"),
                createQuery("2015-08-04 00:02:00", "D"),
                createQuery("2015-08-04 00:03:00", "D"),
                createQuery("2015-08-04 00:04:00", "D"));
        Collections.shuffle(queries);
        return Repository.fromStream(queries.stream());
    }

    @org.junit.Test
    public void distinctByPeriod() {
        Repository queries = feedQueries();
        checkDistinct("2015-08", 4, queries);
        checkDistinct("2015-08-03 00", 2, queries);
        checkDistinct("2015-08-03 00:00", 2, queries);
        checkDistinct("2015-08-03 00:00:00", 2, queries);
        checkDistinct("2015-08-03 00:00:01", 1, queries);

        checkDistinct("2015-08-04", 1, queries);
        checkDistinct("2015-08-04 00", 1, queries);
        checkDistinct("2015-08-04 00:02", 1, queries);

        // 1st entry
        checkDistinct("2015-08-02 23:59:59", 1, queries);
        // last entry
        checkDistinct("2015-08-04 00:04:00", 1, queries);
        // no entry
        checkDistinct("0010-08-02 00:00:00", 0, queries);
        // invalid entry
        try {
            checkDistinct("2015-08-xx", 0, queries);
            Assert.fail();
        } catch (DateTimeParseException exception) {
            // This is the expected behavior
        }
    }

    @org.junit.Test
    public void popularByPeriod() {
        Repository queries = feedQueries();
        List<QueryCount> queryCounts = Arrays.asList(
                new QueryCount("D", 4),
                new QueryCount("C", 3),
                new QueryCount("B", 2),
                new QueryCount("A", 1)
        );
        checkPopular("2015-08", 4, queryCounts, queries);
        checkPopular("2015-08", Integer.MAX_VALUE, queryCounts, queries);
        checkPopular("2015-08", 0, Collections.EMPTY_LIST, queries);
        checkPopular("2015-08", -1, Collections.EMPTY_LIST, queries);

        checkPopular("2015-08-02", 1, Collections.singletonList(new QueryCount("A", 1)), queries);
        checkPopular("2015-08-03", 1, Collections.singletonList(new QueryCount("C", 3)), queries);
        checkPopular("2015-08-04", 1, Collections.singletonList(new QueryCount("D", 4)), queries);
    }


    private void checkDistinct(String d, int expected, Repository queries) {
        TimeRange period = TimeRange.from(d);
        long distinct = queries.getDistinctQueriesCount(period);
        Assert.assertEquals(expected, distinct);
    }

    private void checkPopular(String datePrefix, int top, List<QueryCount> expected, Repository queries) {
        TimeRange range = TimeRange.from(datePrefix);
        List<QueryCount> popularQueries = queries.getPopularQueries(range, top);
        Assert.assertEquals(expected, popularQueries);
    }

}