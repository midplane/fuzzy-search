package dev.midplane.fuzzysearch.commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilitiesTest {

    @Test
    void testIsAlphaNumeric() {
        assertTrue(StringUtilities.isAlphaNumeric('a'));
        assertTrue(StringUtilities.isAlphaNumeric('1'));
        assertFalse(StringUtilities.isAlphaNumeric(' '));
    }

    @Test
    void testIsLowerAlpha() {
        assertTrue(StringUtilities.isLowerAlpha('a'));
        assertFalse(StringUtilities.isLowerAlpha('A'));
        assertFalse(StringUtilities.isLowerAlpha('1'));
    }
}