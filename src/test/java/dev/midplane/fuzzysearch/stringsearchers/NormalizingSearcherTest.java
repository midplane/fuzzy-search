package dev.midplane.fuzzysearch.stringsearchers;

import dev.midplane.fuzzysearch.interfaces.Query;
import dev.midplane.fuzzysearch.interfaces.StringSearcher;
import dev.midplane.fuzzysearch.normalization.NgramNormalizer;
import dev.midplane.fuzzysearch.normalization.NormalizerConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class NormalizingSearcherTest {
    private static StringSearcher stringSearcher = new LiteralSearcher();
    private static StringSearcher normalizingSearcher = new NormalizingSearcher(stringSearcher, new NgramNormalizer(
            NormalizerConfig.createDefaultConfig()));

    @BeforeAll
    static void setUp() {
        normalizingSearcher.index(List.of("Hello world!"));
    }

    @Test
    void canFindNormalizedTerms1() {
        var result = normalizingSearcher.getMatches(new Query("HELLO-WORLD"));
        Assertions.assertEquals(1, result.getMatches().size());
        Assertions.assertEquals(0, result.getMatches().get(0).getIndex());
        Assertions.assertEquals(1, result.getMatches().get(0).getQuality());
    }

    @Test
    void canFindNormalizedTerms2() {
        var result = normalizingSearcher.getMatches(new Query("!hello! world!!!!"));
        Assertions.assertEquals(1, result.getMatches().size());
        Assertions.assertEquals(0, result.getMatches().get(0).getIndex());
        Assertions.assertEquals(1, result.getMatches().get(0).getQuality());
    }
}