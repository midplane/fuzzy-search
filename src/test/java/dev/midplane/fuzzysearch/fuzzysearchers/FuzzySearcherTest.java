package dev.midplane.fuzzysearch.fuzzysearchers;

import dev.midplane.fuzzysearch.interfaces.Query;
import dev.midplane.fuzzysearch.stringsearchers.Match;
import dev.midplane.fuzzysearch.stringsearchers.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FuzzySearcherTest {
    private FuzzySearcher fuzzySearcher;

    @BeforeEach
    public void setUp() {
        NgramComputerConfig commonNgramComputerConfig = new NgramComputerConfig(3);
        NgramComputer commonNgramComputer = new NgramComputer(commonNgramComputerConfig);
        fuzzySearcher = new FuzzySearcher(commonNgramComputer);
        fuzzySearcher.index(List.of("Alice", "Bob", "Carol", "Charlie"));
    }

    @Test
    public void testFindExactMatchTest1() {
        Query query = new Query("Alice");
        Result result = fuzzySearcher.getMatches(query);
        assertEquals(1, result.getMatches().size());
        assertEquals(new Match(0, 1.0), result.getMatches().get(0));
    }

    @Test
    public void testFindExactMatchTest2() {
        Query query = new Query("Bob");
        Result result = fuzzySearcher.getMatches(query);
        assertEquals(1, result.getMatches().size());
        assertEquals(new Match(1, 1.0), result.getMatches().get(0));
    }

    @Test
    public void testFindApproximateMatchTest1() {
        Query query = new Query("Alic");
        Result result = fuzzySearcher.getMatches(query);
        assertEquals(1, result.getMatches().size());
        assertTrue(result.getMatches().get(0).getQuality() > 0.3 && result.getMatches().get(0).getQuality() < 1);
    }

    @Test
    public void testFindApproximateMatchTest2() {
        Query query = new Query("Bobby", 10, 0.0);
        Result result = fuzzySearcher.getMatches(query);
        assertEquals(1, result.getMatches().size());
        assertTrue(result.getMatches().get(0).getQuality() > 0.3 && result.getMatches().get(0).getQuality() < 1);
    }

    @Test
    public void testFindApproximateMatchTest3() {
        Query query = new Query("Charlei", 10, 0.0);
        Result result = fuzzySearcher.getMatches(query);
        assertEquals(1, result.getMatches().size());
        assertTrue(result.getMatches().get(0).getQuality() > 0.3 && result.getMatches().get(0).getQuality() < 1);
    }

    @Test
    public void testCantFindMatchForUnindexedTerm() {
        Query query = new Query("David");
        Result result = fuzzySearcher.getMatches(query);
        assertEquals(0, result.getMatches().size());
    }

    @Test
    public void testCantFindMatchForEmptyString() {
        Query query = new Query("");
        Result result = fuzzySearcher.getMatches(query);
        assertEquals(0, result.getMatches().size());
    }
}
