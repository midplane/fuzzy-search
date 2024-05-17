package dev.midplane.fuzzysearch.commons;

public class StringUtilities {

    public static boolean isAlphaNumeric(char c) {
        return Character.isLetterOrDigit(c);
    }

    public static boolean isLowerAlpha(char c) {
        return Character.isLowerCase(c);
    }
}
