# fuzzy-search

Java port of https://github.com/m31coding/fuzzy-search/

## Installation
Include as a dependency in your `pom.xml`:
```xml
<dependency>
    <groupId>dev.midplane</groupId>
    <artifactId>fuzzy-search</artifactId>
    <version>1.0.0-1</version>
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
