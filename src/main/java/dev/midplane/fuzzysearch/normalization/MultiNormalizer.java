package dev.midplane.fuzzysearch.normalization;

import dev.midplane.fuzzysearch.interfaces.Normalizer;

import java.util.List;

public class MultiNormalizer implements Normalizer {
    private final List<Normalizer> normalizers;

    public MultiNormalizer(final List<Normalizer> normalizers) {
        this.normalizers = normalizers;
    }

    @Override
    public String normalize(final String input) {
        var result = input;

        for (final Normalizer normalizer : normalizers) {
            result = normalizer.normalize(result);
        }

        return result;
    }

    @Override
    public NormalizationResult normalizeBulk(final List<String> input) {
        return NormalizationResult.of(input.stream().map(this::normalize).toList());
    }
}
