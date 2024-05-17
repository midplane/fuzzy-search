package dev.midplane.fuzzysearch.normalization;

import lombok.Data;

import java.util.List;

@Data
public class NormalizationResult {
    private final List<String> strings;

    public NormalizationResult(final List<String> strings) {
        this.strings = strings;
    }

    public static NormalizationResult of(final List<String> strings) {
        return new NormalizationResult(strings);
    }
}
