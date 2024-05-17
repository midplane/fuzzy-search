# fuzzy-search

Java port of https://github.com/m31coding/fuzzy-search/

## Installation
Include as a dependency in your `pom.xml`:
```xml
<dependency>
    <groupId>dev.midplane</groupId>
    <artifactId>fuzzy-search</artifactId>
    <version>1.0.0-2</version>
</dependency>
```

## Usage

```java
import dev.midplane.fuzzysearch.Config;
import dev.midplane.fuzzysearch.entitySearcher.EntitySearcherFactory;
import dev.midplane.fuzzysearch.interfaces.Query;

record Entity(int id, String name) {}

final var entities = List.of(
        new Entity(23501, "Alice"),
        new Entity(99234, "Bob"),
        new Entity(5823, "Carol"),
        new Entity(11923, "Charlie")
                            );

final var searcher = EntitySearcherFactory.createEntitySearcher(Config.createDefault());
searcher.indexEntities(entities, Entity::id, entity ->List.of(entity.name()));

final var result = entitySearcher.getMatches(new Query("Alice"));
System.out.println(result);
```

## Benchmark
```md
Benchmark                           Mode  Cnt    Score   Error   Units
FuzzySearchBenchmark.searcher1k    thrpt       61.320          ops/ms
FuzzySearchBenchmark.searcher10k   thrpt       18.756          ops/ms
FuzzySearchBenchmark.searcher100k  thrpt        5.774          ops/ms
FuzzySearchBenchmark.searcher1m    thrpt        1.110          ops/ms
FuzzySearchBenchmark.searcher1k     avgt        0.017           ms/op
FuzzySearchBenchmark.searcher10k    avgt        0.038           ms/op
FuzzySearchBenchmark.searcher100k   avgt        0.171           ms/op
FuzzySearchBenchmark.searcher1m     avgt        1.184           ms/op
```