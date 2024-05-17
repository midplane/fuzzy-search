package dev.midplane.fuzzysearch.normalization;

import dev.midplane.fuzzysearch.interfaces.Normalizer;

import java.util.List;

public class DefaultNormalizer {
    public static Normalizer create(NormalizerConfig normalizerConfig) {
        final var sanitizingNormalizer = new SanitizingNormalizer();
        final var ngramNormalizer = new NgramNormalizer(normalizerConfig);

        return new MultiNormalizer(List.of(sanitizingNormalizer, ngramNormalizer));
    }
}
