package dev.midplane.fuzzysearch.fuzzysearchers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NgramComputerTest {
    private NgramComputer commonNgramComputer;
    private NgramComputer sortedNgramComputer;
    private NgramComputer defaultNgramComputer;

    @BeforeEach
    public void setUp() {
        NgramComputerConfig commonConfig = new NgramComputerConfig(3);
        commonNgramComputer = new NgramComputer(commonConfig);

        NgramComputerConfig sortedConfig = new NgramComputerConfig(3, ngram -> {
            char[] chars = ngram.toCharArray();
            Arrays.sort(chars);
            return new String(chars);
        });
        sortedNgramComputer = new NgramComputer(sortedConfig);

        NgramComputerConfig defaultConfig = new NgramComputerConfig(3, ngram -> {
            if (ngram.endsWith("$")) {
                return null;
            } else if (!ngram.contains("$")) {
                char[] chars = ngram.toCharArray();
                Arrays.sort(chars);
                return new String(chars);
            } else {
                return ngram;
            }
        });
        defaultNgramComputer = new NgramComputer(defaultConfig);
    }

    @Test
    public void testEmptyString() {
        assertTrue(commonNgramComputer.computeNgrams("").isEmpty());
    }

    @Test
    public void testSingleCharacter() {
        assertEquals(Arrays.asList("a"), commonNgramComputer.computeNgrams("a"));
    }

    @Test
    public void testTwoCharacters() {
        assertEquals(Arrays.asList("ab"), commonNgramComputer.computeNgrams("ab"));
    }

    @Test
    public void testSortedTwoCharacters() {
        assertEquals(Arrays.asList("ab"), sortedNgramComputer.computeNgrams("ab"));
    }

    @Test
    public void testSortedTwoUnsortedCharacters() {
        assertEquals(Arrays.asList("ab"), sortedNgramComputer.computeNgrams("ba"));
    }

    @Test
    public void testThreeCharacters() {
        assertEquals(Arrays.asList("abc"), commonNgramComputer.computeNgrams("abc"));
    }

    @Test
    public void testSortedThreeCharacters() {
        assertEquals(Arrays.asList("abc"), sortedNgramComputer.computeNgrams("abc"));
    }

    @Test
    public void testSortedThreeUnsortedCharacters() {
        assertEquals(Arrays.asList("abc"), sortedNgramComputer.computeNgrams("cba"));
    }

    @Test
    public void testFourCharacters() {
        assertEquals(Arrays.asList("abc", "bcd"), commonNgramComputer.computeNgrams("abcd"));
    }

    @Test
    public void testSortedFourCharacters() {
        assertEquals(Arrays.asList("abc", "bcd"), sortedNgramComputer.computeNgrams("abcd"));
    }

    @Test
    public void testSortedFourUnsortedCharacters() {
        assertEquals(Arrays.asList("bcd", "acd"), sortedNgramComputer.computeNgrams("bdca"));
    }

    @Test
    public void testWord() {
        assertEquals(Arrays.asList("ali", "lic", "ice"), commonNgramComputer.computeNgrams("alice"));
    }

    @Test
    public void testSortedWord() {
        assertEquals(Arrays.asList("ail", "cil", "cei"), sortedNgramComputer.computeNgrams("alice"));
    }

    @Test
    public void testPaddedWord() {
        assertEquals(Arrays.asList("$$a", "$al", "ali", "lic", "ice", "ce!", "e!!"),
                     commonNgramComputer.computeNgrams("$$alice!!"));
    }

    @Test
    public void testSortedPaddedWord() {
        assertEquals(Arrays.asList("$$a", "$al", "ail", "cil", "cei", "!ce", "!!e"),
                     sortedNgramComputer.computeNgrams("$$alice!!"));
    }

    @Test
    public void testTwoPaddedWords() {
        assertEquals(Arrays.asList("$$a", "$al", "ali", "lic", "ice", "ce%", "e%%", "%%k", "%ki", "kin", "ing", "ng!",
                                   "g!!"), commonNgramComputer.computeNgrams("$$alice%%king!!"));
    }

    @Test
    public void testSortedTwoPaddedWords() {
        assertEquals(Arrays.asList("$$a", "$al", "ail", "cil", "cei", "%ce", "%%e", "%%k", "%ik", "ikn", "gin", "!gn",
                                   "!!g"), sortedNgramComputer.computeNgrams("$$alice%%king!!"));
    }

    @Test
    public void testDefaultNgramsOfPaddedWord1() {
        assertEquals(Arrays.asList("$$h", "$he", "ehl", "ell", "llo"), defaultNgramComputer.computeNgrams("$$hello"));
    }

    @Test
    public void testDefaultNgramsOfPaddedWord2() {
        assertEquals(Arrays.asList("$$a", "$al", "ail", "cil", "cei"), defaultNgramComputer.computeNgrams("$$alice"));
    }

    @Test
    public void testDefaultNgramsOfTwoPaddedWords() {
        assertEquals(Arrays.asList("$$a", "$al", "ail", "cil", "cei", "$$k", "$ki", "ikn", "gin"),
                     defaultNgramComputer.computeNgrams("$$alice$$king"));
    }
}
