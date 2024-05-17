package dev.midplane.fuzzysearch.entitySearcher;

import dev.midplane.fuzzysearch.interfaces.EntityMatch;
import dev.midplane.fuzzysearch.interfaces.EntityResult;
import dev.midplane.fuzzysearch.interfaces.EntitySearcher;
import dev.midplane.fuzzysearch.interfaces.Query;
import dev.midplane.fuzzysearch.interfaces.StringSearcher;
import dev.midplane.fuzzysearch.stringsearchers.Match;
import dev.midplane.fuzzysearch.stringsearchers.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

public class DefaultEntitySearcher<Entity, Id> implements EntitySearcher<Entity, Id> {
    private final StringSearcher stringSearcher;

    private List<Entity> entities = Collections.emptyList();
    private List<String> terms = Collections.emptyList();

    private List<Integer> termIndexToEntityIndex = Collections.emptyList();

    public DefaultEntitySearcher(final StringSearcher stringSearcher) {
        this.stringSearcher = stringSearcher;
    }

    @Override
    public synchronized void indexEntities(final List<Entity> entities, final Function<Entity, Id> idExtractor,
                                           final Function<Entity, List<String>> termExtractor) {
        this.entities = entities;
        this.terms = new ArrayList<>();
        this.termIndexToEntityIndex = new ArrayList<>();

        for (int i = 0; i < entities.size(); i++) {
            final Entity entity = entities.get(i);
            final Id id = idExtractor.apply(entity);
            final List<String> entityTerms = termExtractor.apply(entity);
            this.terms.addAll(entityTerms);

            for (final String ignored : entityTerms) {
                this.termIndexToEntityIndex.add(i);
            }
        }

        this.stringSearcher.index(this.terms);
    }

    @Override
    public EntityResult<Entity> getMatches(final Query query) {
        final var stringSearcherQuery = new Query(query.getString(), Integer.MAX_VALUE, query.getMinQuality());
        final var stringSearcherResult = this.stringSearcher.getMatches(stringSearcherQuery);
        return new EntityResult<>(this.getMatchesFromResult(stringSearcherResult, query.getTopN()), query);
    }

    private List<EntityMatch<Entity>> getMatchesFromResult(Result result, int topN) {
        if (topN < 0) {
            return List.of();
        }
        final var matchedIndexes = new HashSet<>();
        final var matches = new ArrayList<EntityMatch<Entity>>();

        for (Match match : result.getMatches()) {
            final var entityIndex = this.termIndexToEntityIndex.get(match.getIndex());

            if (matchedIndexes.contains(entityIndex)) {
                continue;
            }

            matchedIndexes.add(entityIndex);
            final var entity = this.entities.get(entityIndex);
            final var matchQuality = match.getQuality();
            matches.add(new EntityMatch<>(entity, matchQuality, this.terms.get(match.getIndex())));

            if (matches.size() == topN) {
                break;
            }
        }

        return matches;
    }

    @Override
    public List<String> getTerms() {
        return this.terms;
    }
}
