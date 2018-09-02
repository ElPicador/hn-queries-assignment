package com.algolia.assignment.rest;

import com.algolia.assignment.indexing.Repository;
import com.algolia.assignment.model.QueryCount;
import com.algolia.assignment.model.TimeRange;
import com.algolia.assignment.rest.dao.Count;
import com.algolia.assignment.rest.dao.Popular;
import com.algolia.assignment.rest.util.JsonUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

final class QueryController {

    private static final String DATE_PREFIX = ":dateprefix";
    private static final String SIZE = "size";

    public static Route serveCount(Repository repository) {

        return (Request request, Response response) -> {
            TimeRange range = getTimeRange(request);
            long count = repository.getDistinctQueriesCount(range);

            return JsonUtil.dataToJson(new Count(count));
        };
    }

    public static Route servePopular(Repository repository) {

        return (Request request, Response response) -> {
            TimeRange range = getTimeRange(request);
            int size = getSizeParam(request);
            List<QueryCount> popular = repository.getPopularQueries(range, size);
            return JsonUtil.dataToJson(new Popular(popular));
        };
    }

    /**
     * We should probably limit to a maximum size, e.g. 128?
     */
    private static int getSizeParam(Request request) {
        String s = request.queryParams(SIZE);
        if (s == null)
            throw new IllegalArgumentException("A 'size' parameter has to be specified.");

        return Integer.parseInt(s);
    }

    private static TimeRange getTimeRange(Request request) {
        String datePrefix = request.params(DATE_PREFIX);
        return TimeRange.from(datePrefix);
    }

    public class AppPaths {
        private static final String VERSION = "1";
        public static final String COUNT = "/" + VERSION + "/queries/count/" + DATE_PREFIX;
        public static final String POPULAR = "/" + VERSION + "/queries/popular/" + DATE_PREFIX;
    }
}
