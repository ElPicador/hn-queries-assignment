# Technical test

The goal of this test is to evaluate your ability to parse & process a big amount of data and to use the appropriate data structures to solve a simple challenge.

The provided [data file](https://www.dropbox.com/s/duv704waqjp3tu1/hn_logs.tsv.gz?dl=0) is a TSV file listing all queries performed on [HN Search](https://hn.algolia.com) during a few days.

The goal is to extract the popular (most frequent) queries that have been done during a specific time range.

## Instructions

Build a small application (whatever the language) exposing the following endpoints through a REST API:
 * `GET /1/queries/count/<DATE_PREFIX>`: returns a JSON object specifying the number of distinct queries that have been done during a specific time range
 * `GET /1/queries/popular/<DATE_PREFIX>?size=<SIZE>`: returns a JSON object listing the top `<SIZE>` popular queries that have been done during a specific time range

### Examples

 * Distinct queries done in 2015: `GET /1/queries/count/2015`: returns `{ count: 573697 }`
 * Distinct queries done in Aug: `GET /1/queries/count/2015-08`: returns `{ count: 573697 }`
 * Distinct queries done on Aug 3rd: `GET /1/queries/count/2015-08-03`: returns `{ count: 198117 }`
 * Distinct queries done on Aug 1st between 00:04:00 and 00:04:59: `GET /1/queries/count/2015-08-01 00:04`: returns `{ count: 617 }`

 * Top 3 popular queries done in 2015: `GET /1/queries/popular/2015?size=3`: returns

```js
    {
      queries: [
        { query: "http%3A%2F%2Fwww.getsidekick.com%2Fblog%2Fbody-language-advice", count: 6675 },
        { query: "http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F568045", count: 4652 },
        { query: "http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F379035%3Fsort%3D1", count: 3100 }
      ]
    }
```

 * Top 5 popular queries done on Aug 2nd: `GET /1/queries/popular/2015-08-02?size=5`: returns

```js
    {
      queries: [
        { query: "http%3A%2F%2Fwww.getsidekick.com%2Fblog%2Fbody-language-advice", count: 2283 },
        { query: "http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F568045", count: 1943 },
        { query: "http%3A%2F%2Fwebboard.yenta4.com%2Ftopic%2F379035%3Fsort%3D1", count: 1358 },
        { query: "http%3A%2F%2Fjamonkey.com%2F50-organizing-ideas-for-every-room-in-your-house%2F", count: 890 },
        { query: "http%3A%2F%2Fsharingis.cool%2F1000-musicians-played-foo-fighters-learn-to-fly-and-it-was-epic", count: 701 }
      ]
    }
```

You can use third party libraries if you know how to re-implement the features they're providing. Your application cannot depend on any database or external software (the goal is to evaluate your ability to choose the right data structures).

## Evaluation Criteria

Please push your code to a public GitHub repository. We'll evaluate:

 * the complexity & scalability of your algorithm,
 * the quality of your code.

No time limit. Good luck!
