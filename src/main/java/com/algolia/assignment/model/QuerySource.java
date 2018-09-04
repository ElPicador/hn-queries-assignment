package com.algolia.assignment.model;

import com.algolia.assignment.indexing.Repository;
import com.algolia.assignment.model.Query;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * This class initializes a queries repository from a file.
 */
public enum QuerySource {

     TSV_FILE {
         private static final String TAB = "\t";

         final Query feedRecordFromLine(String line) {
             String[] cols = line.split(TAB);
             String dateTime = cols[0];
             String text = cols[1];

             // String.split uses new String(...).
             // The call to String.intern ensures that each String instance will be de-duplicated.
             // This shows as a hot spot in profiling.
             // We still think the memory gain is worth this initial overhead,
             // since the text queries contain many duplicates.
             return new Query(dateTime, text.intern());
         }};


    public Repository feedRepositoryFromFile(Path path) throws IOException {
        try (Stream<String> lines = Files.lines(path)) {
            Stream<Query> queryStream = lines.map(this::feedRecordFromLine);

            return Repository.fromStream(queryStream);
        }
    }

    abstract Query feedRecordFromLine(String line) ;
}
