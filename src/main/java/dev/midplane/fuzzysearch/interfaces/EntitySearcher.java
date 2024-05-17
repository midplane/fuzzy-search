package dev.midplane.fuzzysearch.interfaces;

import java.util.List;
import java.util.function.Function;

public interface EntitySearcher<Entity, Id> {
    void indexEntities(List<Entity> entities, Function<Entity, Id> idExtractor,
                       Function<Entity, List<String>> termExtractor);

    EntityResult<Entity> getMatches(Query query);

    List<String> getTerms();
}
