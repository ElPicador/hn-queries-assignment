package com.algolia.assignment.rest;

import com.algolia.assignment.indexing.Repository;
import spark.Spark;

import static com.algolia.assignment.rest.QueryController.AppPaths.COUNT;
import static com.algolia.assignment.rest.QueryController.AppPaths.POPULAR;

/**
 * A most simple REST server, implemented by http://sparkjava.com/
 */
public final class RestServer {

    private static final int PORT = 8080;
    private static final String SERVER_PORT = "com.algolia.assignment.server.port";

    public static void setupServer(Repository repository) {

        /* The whole configuration is made through static methods.
         * Although this might not be to everyone's taste,
         * it is Spark's author opinion that statics are better than dependency injection
         * when dealing with web applications / controllers.
         *
         * This initialization will launch an embedded Jetty server a new thread.
         */
        String property = System.getProperty(SERVER_PORT);
        Spark.port(property != null ? Integer.parseInt(property) : PORT);

        Spark.get(COUNT, QueryController.serveCount(repository));
        Spark.get(POPULAR, QueryController.servePopular(repository));
    }
}

