package dev.midplane.fuzzysearch.normalization;

import dev.midplane.fuzzysearch.commons.StringUtilities;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@Data
@AllArgsConstructor
public class NormalizerConfig {
    private String paddingLeft;
    private String paddingRight;
    private String paddingMiddle;
    private Map<String, List<String>> replacements;
    private Predicate<Character> treatCharacterAsSpace;
    private Predicate<Character> allowCharacter;

    public static NormalizerConfig createDefaultConfig() {
        final var spaceEquivalentCharacters = Set.of('_', '-', '–', '/', ',', '\t');
        final var treatCharacterAsSpace = (Predicate<Character>) spaceEquivalentCharacters::contains;
        final var allowCharacter = (Predicate<Character>) StringUtilities::isAlphaNumeric;

        return new NormalizerConfig("$$", "!", "%", Map.of(), treatCharacterAsSpace, allowCharacter);
    }
}
