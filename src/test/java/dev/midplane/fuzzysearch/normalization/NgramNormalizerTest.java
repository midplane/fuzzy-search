package dev.midplane.fuzzysearch.normalization;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NgramNormalizerTest {
    private final static NormalizerConfig normalizerConfig = NormalizerConfig.createDefaultConfig();
    private final NgramNormalizer ngramNormalizer = new NgramNormalizer(normalizerConfig);

    @BeforeAll
    static void setUp() {
        normalizerConfig.setPaddingLeft("$$");
        normalizerConfig.setPaddingRight("!!");
        normalizerConfig.setPaddingMiddle("%%");
    }

    @Test
    void canNormalizeEmptyString() {
        assertEquals("", ngramNormalizer.normalize(""));
    }

    @Test
    void canNormalizeSingleLowerCaseLetter() {
        assertEquals("$$a!!", ngramNormalizer.normalize("a"));
    }

    @Test
    void canNormalizeSingleUpperCaseLetter() {
        assertEquals("$$a!!", ngramNormalizer.normalize("A"));
    }

    @Test
    void canNormalizeTwoLetters() {
        assertEquals("$$he!!", ngramNormalizer.normalize("He"));
    }

    @Test
    void canNormalizeThreeLetters() {
        assertEquals("$$hel!!", ngramNormalizer.normalize("Hel"));
    }

    @Test
    void canNormalizeSingleWord() {
        assertEquals("$$hello!!", ngramNormalizer.normalize("HELLO"));
    }

    @Test
    void canNormalizeTwoWords() {
        assertEquals("$$hello%%world!!", ngramNormalizer.normalize("Hello World"));
    }

    @Test
    void canNormalizeSingleWordWithSpace() {
        assertEquals("$$hello!!", ngramNormalizer.normalize("    hello  "));
    }

    @Test
    void canNormalizeTwoWordWithSpace() {
        assertEquals("$$hello%%world!!", ngramNormalizer.normalize("   Hello   World   "));
    }

    @Test
    void canNormalizeWithPaddingCharacters() {
        assertEquals("$$helloworld!!", ngramNormalizer.normalize("%hello!world$"));
    }

    @Test
    void canNormalizeWithSpaceEquivalentCharacters() {
        assertEquals("$$lorem%%ipsum%%dolor%%sit%%amet!!", ngramNormalizer.normalize("Lorem-ipsum_dolor,sit amet"));
    }
}