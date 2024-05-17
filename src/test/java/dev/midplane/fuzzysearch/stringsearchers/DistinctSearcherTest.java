package dev.midplane.fuzzysearch.stringsearchers;

import dev.midplane.fuzzysearch.interfaces.Query;
import dev.midplane.fuzzysearch.interfaces.StringSearcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class DistinctSearcherTest {
    private static StringSearcher stringSearcher = new LiteralSearcher();
    private static StringSearcher distinctSearcher = new DistinctSearcher(stringSearcher);

    @BeforeAll
    static void setUp() {
        List<String> terms = List.of("Bob", "Carol", "Alice", "Bob", "Charlie", "Alice", "Alice");
        distinctSearcher.index(terms);
    }

    @Test
    void canFindDistinctTerms() {
        Query query = new Query("Carol");
        Result result = distinctSearcher.getMatches(query);

        Assertions.assertEquals(1, result.getMatches().size());
        Assertions.assertEquals(1, result.getMatches().get(0).getIndex());
        Assertions.assertEquals(1, result.getMatches().get(0).getQuality());
    }

    @Test
    void canFindDistinctTermsWithMultipleMatches() {
        Query query = new Query("Bob");
        Result result = distinctSearcher.getMatches(query);

        Assertions.assertEquals(2, result.getMatches().size());
        Assertions.assertEquals(0, result.getMatches().get(0).getIndex());
        Assertions.assertEquals(1, result.getMatches().get(0).getQuality());

        Assertions.assertEquals(3, result.getMatches().get(1).getIndex());
        Assertions.assertEquals(1, result.getMatches().get(1).getQuality());
    }
}