package com.algolia.assignment.app;

import com.algolia.assignment.indexing.Repository;
import com.algolia.assignment.indexing.test.TestFiles;
import com.algolia.assignment.model.QueryCount;
import com.algolia.assignment.model.TimeRange;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public final class RepositoryHN {

    private static Repository loadQueries() throws IOException {
        return QueryFileReader.feedQueriesFromFile(TestFiles.GLOBAL_FILE_PATH);
    }

    @Test
    public void testTimeRanges() throws IOException {
        Repository queries = loadQueries();
        checkDistinct("2015", 573697, queries);
        checkDistinct("2015-08", 573697, queries);
        checkDistinct("2015-08-03", 198117, queries);
        checkDistinct("2015-08-01 00:04", 617, queries);

        checkPopularByDay(queries);
        checkPopularByYear(queries);
    }

    private void sleep(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkDistinct(String p, int expected, Repository repository) {
        TimeRange period = TimeRange.from(p);


        long distinct = repository.getDistinctQueriesCount(period);
        Assert.assertEquals(expected, distinct);
    }

    private void checkPopularByYear(Repository queries) {
        List<QueryCount> expected = new ArrayList<>();
        addQueryCount("http%3A%2F%2Fwww.getsidekick.com%2Fblog%2Fbody-language-advice", 6675, expected);
        addQueryCount("http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F568045", 4652, expected);
        addQueryCount("http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F379035%3Fsort%3D1", 3100, expected);
        checkPopular("2015", expected, 3, queries);

    }

    private void checkPopularByDay(Repository queries) {
        List<QueryCount> expected = new ArrayList<>();
        addQueryCount("http%3A%2F%2Fwww.getsidekick.com%2Fblog%2Fbody-language-advice", 2283, expected);
        addQueryCount("http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F568045", 1943, expected);
        addQueryCount("http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F379035%3Fsort%3D1", 1358, expected);
        addQueryCount("http%3A%2F%2Fjamonkey.com%2F50-organizing-ideas-for-every-room-in-your-house%2F", 890, expected);
        addQueryCount("http%3A%2F%2Fsharingis.cool%2F1000-musicians-played-foo-fighters-learn-to-fly-and-it-was-epic", 701, expected);
        checkPopular("2015-08-02", expected, 5, queries);
    }

    private void addQueryCount(String s, int i, List<QueryCount> expected) {
        expected.add(new QueryCount(s, i));
    }

    private void checkPopular(String p, List<QueryCount> expected, int top, Repository repository) {
        TimeRange period = TimeRange.from(p);
        List<QueryCount> popularQueries = repository.getPopularQueries(period, top);
        Assert.assertEquals(expected, popularQueries);
    }
}

