package com.algolia.assignment.app;

import com.algolia.assignment.indexing.Repository;
import com.algolia.assignment.rest.RestServer;

import java.io.IOException;
import java.nio.file.Paths;

final class Application {

    /**
     * The entry point into the application.
     *
     * @param args the location of the data file. ex: /home/mary/hn.tsv
     */
    public static void main(String[] args) throws IOException {
        if (args == null || args.length == 0) {
            throwUsage();
        }

        /* This method first loads the repository data into memory,
         * then starts the server.
         * No requests will be served until the data is fully and uniquely loaded.
         * This frees us from any concurrency considerations,
         * at the expense of a few seconds' startup.
         */
        String path = args[0];
        Repository repository = QueryFileReader.TSV.feedQueriesFromFile(Paths.get(path));
        setupServer(repository);
    }

    private static void throwUsage() {
        throw new IllegalArgumentException("An argument must provide the location of the queries file.");
    }

    private static void setupServer(Repository queries) {
        RestServer.setupServer(queries);
    }
}
