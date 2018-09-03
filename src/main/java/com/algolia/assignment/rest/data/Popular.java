package com.algolia.assignment.rest.data;

import com.algolia.assignment.model.QueryCount;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.List;

@Immutable
public final class Popular {
    private final List<QueryCount> queries;

    public Popular(List<QueryCount> list) {
        this.queries = list;
    }

    public List<QueryCount> getQueries() {
        return queries;
    }

}


