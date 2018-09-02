package com.algolia.assignment.app;

import com.algolia.assignment.indexing.Repository;
import com.algolia.assignment.model.Query;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * This class initializes a queries repository from a TSV file.
 */
final class QueryFileReader {

    private static final String TAB = "\t";

    static Repository feedQueriesFromFile(Path path) throws IOException {

        try (Stream<String> lines = Files.lines(path)) {
            Stream<Query> queryStream = lines.map(QueryFileReader::feedRecordFromTsvLine);

            return Repository.fromStream(queryStream);
        }
    }

    private static Query feedRecordFromTsvLine(String line) {
        String[] cols = line.split(TAB);
        String dateTime = cols[0];
        String text = cols[1];

        // String.split uses new String(...).
        // The call to String.intern ensures that each String instance will be de-duplicated.
        // This shows as a hot spot in profiling.
        // We still think the memory gain is worth this initial overhead,
        // since the text queries contain many duplicates.
        return new Query(dateTime, text.intern());
    }
}
