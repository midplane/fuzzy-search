package dev.midplane.fuzzysearch.normalization;

import dev.midplane.fuzzysearch.interfaces.Normalizer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class NgramNormalizer implements Normalizer {
    private final String paddingLeft;
    private final String paddingRight;
    private final String paddingMiddle;
    private final Set<Character> paddingCharacters;
    private final Predicate<Character> treatCharacterAsSpace;
    private final Predicate<Character> allowCharacter;

    public NgramNormalizer(NormalizerConfig normalizerConfig) {
        this.paddingLeft = normalizerConfig.getPaddingLeft();
        this.paddingRight = normalizerConfig.getPaddingRight();
        this.paddingMiddle = normalizerConfig.getPaddingMiddle();
        this.paddingCharacters = new HashSet<>();
        Arrays.asList(normalizerConfig.getPaddingLeft().toCharArray(),
                      normalizerConfig.getPaddingRight().toCharArray(),
                      normalizerConfig.getPaddingMiddle().toCharArray())
                .forEach(array -> {
                    for (char c : array) {
                        paddingCharacters.add(c);
                    }
                });
        this.treatCharacterAsSpace = normalizerConfig.getTreatCharacterAsSpace();
        this.allowCharacter = normalizerConfig.getAllowCharacter();
    }

    public String normalize(String input) {
        StringBuilder normalized = new StringBuilder(input.length() + 2);
        normalized.append(this.paddingLeft);
        boolean previousIsPadding = true;
        boolean previousIsSkippedEmptyChar = false;
        boolean properCharacterAdded = false;

        for (int i = 0, l = input.length(); i < l; i++) {
            char currentChar = input.charAt(i);
            String normalizedChar = getNormalizedCharacter(currentChar);

            if (normalizedChar.equals("")) {
                continue;
            }

            if (normalizedChar.equals(" ")) {
                previousIsSkippedEmptyChar = true;
            } else {
                if (previousIsSkippedEmptyChar && !previousIsPadding) {
                    normalized.append(this.paddingMiddle);
                }

                normalized.append(normalizedChar);
                properCharacterAdded = true;
                previousIsPadding = false;
                previousIsSkippedEmptyChar = false;
            }
        }

        normalized.append(this.paddingRight);

        if (!properCharacterAdded) {
            return "";
        }

        return normalized.toString();
    }

    private String getNormalizedCharacter(char character) {
        if (character == ' ' || this.treatCharacterAsSpace.test(character)) {
            return " ";
        }

        if (isSurrogate(character) || this.paddingCharacters.contains(character) || !this.allowCharacter.test(
                character)) {
            return "";
        }

        return Character.toString(Character.toLowerCase(character));
    }

    private boolean isSurrogate(char character) {
        return Character.isSurrogate(character);
    }

    public NormalizationResult normalizeBulk(List<String> input) {
        return NormalizationResult.of(input.stream().map(this::normalize).toList());
    }
}
