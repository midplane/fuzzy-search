package dev.midplane.fuzzysearch.stringsearchers;

import dev.midplane.fuzzysearch.interfaces.Query;
import dev.midplane.fuzzysearch.interfaces.StringSearcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SortingSearcherTest {
    private static StringSearcher sortingSearcher;

    @BeforeAll
    static void setUp() {
        sortingSearcher = new SortingSearcher(new StringSearcher() {
            @Override
            public void index(final List<String> terms) {
            }

            @Override
            public Result getMatches(final Query query) {
                final var matches = List.of(new Match(7, 0.2), new Match(16, 1.0), new Match(10, 0.5),
                                            new Match(4, 0.5), new Match(8, 0.6));
                return new Result(matches, query);
            }
        });
    }

    @Test
    void canSortMatches() {
        final var query = new Query("some test");
        final var result = sortingSearcher.getMatches(query);
        final var matches = result.getMatches();
        assertEquals(16, matches.get(0).getIndex());
        assertEquals(8, matches.get(1).getIndex());
        assertEquals(4, matches.get(2).getIndex());
        assertEquals(10, matches.get(3).getIndex());
        assertEquals(7, matches.get(4).getIndex());
    }
}