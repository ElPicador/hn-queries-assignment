package com.algolia.assignment.model;

import com.algolia.assignment.indexing.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Initializes a queries repository from a file.
 */
public enum QuerySource {

    TSV_FILE("\t") {
    },

    CSV_FILE(",") {
    },

    FROM_FIELDS("unused") {
        @Override
        Query feedRecordFromLine(String line) {
            throw new UnsupportedOperationException("Cannot be used for this QuerySource.");
        }
    };

    QuerySource(String separator) {
        this.separator = separator;
    }

    private final String separator;

    public Query feedRecordFromFields(String dateTime, String text) {
        // String.split uses new String(...).
        // The call to String.intern ensures that each String instance will be de-duplicated.
        // This shows as a hot spot in profiling.
        // We still think the memory gain is worth this initial overhead,
        // since the text queries contain many duplicates.
        return new Query(dateTime, text.intern());
    }

    public Repository feedRepositoryFromFile(Path path) throws IOException {
        try (Stream<String> lines = Files.lines(path)) {
            Stream<Query> queryStream = lines.map(this::feedRecordFromLine);

            return Repository.fromStream(queryStream);
        }
    }

    Query feedRecordFromLine(String line) {
        String[] cols = line.split(this.separator);
        String dateTime = cols[0];
        String text = cols[1];

        return feedRecordFromFields(dateTime, text);
    }
}
