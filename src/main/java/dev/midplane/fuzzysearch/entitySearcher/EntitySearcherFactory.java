package dev.midplane.fuzzysearch.entitySearcher;

import dev.midplane.fuzzysearch.Config;
import dev.midplane.fuzzysearch.fuzzysearchers.FuzzySearcher;
import dev.midplane.fuzzysearch.fuzzysearchers.NgramComputer;
import dev.midplane.fuzzysearch.interfaces.StringSearcher;
import dev.midplane.fuzzysearch.normalization.DefaultNormalizer;
import dev.midplane.fuzzysearch.stringsearchers.DistinctSearcher;
import dev.midplane.fuzzysearch.stringsearchers.InequalityPenalizingSearcher;
import dev.midplane.fuzzysearch.stringsearchers.NormalizingSearcher;
import dev.midplane.fuzzysearch.stringsearchers.SortingSearcher;

public class EntitySearcherFactory {
    public static <Entity, Id> DefaultEntitySearcher<Entity, Id> createSearcher(Config config) {
        final var ngramComputer = new NgramComputer(config.getNgramComputerConfig());
        final var normalizer = DefaultNormalizer.create(config.getNormalizerConfig());

        StringSearcher stringSearcher = new FuzzySearcher(ngramComputer);
        stringSearcher = new InequalityPenalizingSearcher(stringSearcher, config.getInequalityPenalty());
        stringSearcher = new DistinctSearcher(stringSearcher);
        stringSearcher = new SortingSearcher(stringSearcher);
        stringSearcher = new NormalizingSearcher(stringSearcher, normalizer);

        return new DefaultEntitySearcher<>(stringSearcher);
    }
}
