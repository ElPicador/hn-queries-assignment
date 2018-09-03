package com.algolia.assignment.rest.data;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public final class Count {
    private final long count;

    public Count(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }
}
