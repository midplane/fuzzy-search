package dev.midplane.fuzzysearch.normalization;

import dev.midplane.fuzzysearch.interfaces.Normalizer;

import java.util.List;

public class SanitizingNormalizer implements Normalizer {
    @Override
    public String normalize(final String input) {
        return input == null ? "" : input;
    }

    @Override
    public NormalizationResult normalizeBulk(final List<String> input) {
        return NormalizationResult.of(input.stream().map(this::normalize).toList());
    }
}
