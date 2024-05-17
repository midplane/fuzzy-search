package dev.midplane.fuzzysearch.stringsearchers;

import dev.midplane.fuzzysearch.interfaces.Query;
import dev.midplane.fuzzysearch.interfaces.StringSearcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class InequalityPenalizingSearcherTest {
    private static StringSearcher stringSearcher;
    private static StringSearcher inequalityPenalizingSearcher;

    @BeforeAll
    static void setUp() {
        stringSearcher = new StringSearcher() {
            @Override
            public void index(final List<String> terms) {
            }

            @Override
            public Result getMatches(final Query query) {
                final var matches = List.of(new Match(0, 1.0), new Match(1, 0.6));
                return new Result(matches, query);
            }
        };

        inequalityPenalizingSearcher = new InequalityPenalizingSearcher(stringSearcher, 0.05);
        inequalityPenalizingSearcher.index(List.of("hello", "yellow"));
    }

    @Test
    void canPenalizeInequality() {
        final var query = new Query("hello");
        final var result = inequalityPenalizingSearcher.getMatches(query);
        final var matches = result.getMatches();
        Assertions.assertEquals(1.0, matches.get(0).getQuality());
        Assertions.assertEquals(0.6 * (1 - 0.05), matches.get(1).getQuality());
    }
}