package dev.midplane.fuzzysearch.stringsearchers;

import dev.midplane.fuzzysearch.interfaces.Query;
import dev.midplane.fuzzysearch.interfaces.StringSearcher;

import java.util.ArrayList;
import java.util.List;

public class InequalityPenalizingSearcher implements StringSearcher {
    private final StringSearcher stringSearcher;
    private final double penaltyFactor;
    private List<Integer> hashCodes;

    public InequalityPenalizingSearcher(StringSearcher stringSearcher, double penalty) {
        this.stringSearcher = stringSearcher;
        this.penaltyFactor = 1 - penalty;
        this.hashCodes = new ArrayList<>();
    }

    @Override
    public void index(final List<String> terms) {
        this.stringSearcher.index(terms);

        this.hashCodes = new ArrayList<>();
        for (String term : terms) {
            this.hashCodes.add(term.hashCode());
        }
    }

    @Override
    public Result getMatches(final Query query) {
        final var result = this.stringSearcher.getMatches(query);
        final var hashCodeQuery = query.getString().hashCode();
        final var newMatches = result.getMatches().stream().map(match -> {
            final var hashCodeMatch = this.hashCodes.get(match.getIndex());
            final var penalty = hashCodeMatch == hashCodeQuery ? 1 : this.penaltyFactor;
            return new Match(match.getIndex(), match.getQuality() * penalty);
        }).filter(match -> match.getQuality() >= query.getMinQuality()).toList();
        return new Result(newMatches, query);
    }
}
