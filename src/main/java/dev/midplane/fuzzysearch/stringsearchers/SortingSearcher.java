package dev.midplane.fuzzysearch.stringsearchers;

import dev.midplane.fuzzysearch.interfaces.Query;
import dev.midplane.fuzzysearch.interfaces.StringSearcher;

import java.util.List;

public class SortingSearcher implements StringSearcher {
    private final StringSearcher stringSearcher;

    public SortingSearcher(final StringSearcher stringSearcher) {
        this.stringSearcher = stringSearcher;
    }

    private static int compareMatch(final Match o1, final Match o2) {
        if (o1.getQuality() == o2.getQuality()) {
            return Integer.compare(o1.getIndex(), o2.getIndex());
        }
        return Double.compare(o2.getQuality(), o1.getQuality());
    }

    @Override
    public void index(final List<String> terms) {
        this.stringSearcher.index(terms);
    }

    @Override
    public Result getMatches(final Query query) {
        final var result = this.stringSearcher.getMatches(query);
        return new Result(result.getMatches().stream().sorted(SortingSearcher::compareMatch).toList(), query);
    }

}
