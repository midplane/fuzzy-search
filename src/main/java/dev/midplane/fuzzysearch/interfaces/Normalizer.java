package dev.midplane.fuzzysearch.interfaces;

import dev.midplane.fuzzysearch.normalization.NormalizationResult;

import java.util.List;

public interface Normalizer {
    String normalize(String input);

    NormalizationResult normalizeBulk(List<String> input);
}
