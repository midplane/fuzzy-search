package dev.midplane.fuzzysearch.stringsearchers;

import dev.midplane.fuzzysearch.interfaces.Normalizer;
import dev.midplane.fuzzysearch.interfaces.Query;
import dev.midplane.fuzzysearch.interfaces.StringSearcher;

import java.util.List;

public class NormalizingSearcher implements StringSearcher {
    private final StringSearcher stringSearcher;
    private final Normalizer normalizer;

    public NormalizingSearcher(final StringSearcher stringSearcher, final Normalizer normalizer) {
        this.stringSearcher = stringSearcher;
        this.normalizer = normalizer;
    }

    @Override
    public void index(final List<String> terms) {
        final var result = this.normalizer.normalizeBulk(terms);
        this.stringSearcher.index(result.getStrings());
    }

    @Override
    public Result getMatches(final Query query) {
        final var normalizedQuery = new Query(this.normalizer.normalize(query.getString()), query.getTopN(),
                                              query.getMinQuality());
        final var result = this.stringSearcher.getMatches(normalizedQuery);
        return new Result(result.getMatches(), query);
    }
}
