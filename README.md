## How to build the application
The `maven package` goal will create a fat jar, packaging the application with its dependencies. 
 
## How to launch the application
A mandatory argument must specify the location of the queries file.

The server will listen to connections on port 8080. This number may be changed through the optional property: `com.algolia.assignment.server.port`

```bash
java -jar hn-query-mestelan-1.0.jar ~/hn/hn.tsv [-Dcom.algolia.assignment.server.port=4567]
```
