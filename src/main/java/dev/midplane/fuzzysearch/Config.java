package dev.midplane.fuzzysearch;

import dev.midplane.fuzzysearch.fuzzysearchers.NgramComputerConfig;
import dev.midplane.fuzzysearch.normalization.NormalizerConfig;
import lombok.Data;

@Data
public class Config {
    private final NormalizerConfig normalizerConfig;
    private final NgramComputerConfig ngramComputerConfig;
    private final int maxQueryLength;
    private final double inequalityPenalty;

    public static Config createDefault() {
        return new Config(NormalizerConfig.createDefaultConfig(), NgramComputerConfig.createDefaultConfig(), 150, 0.05);
    }
}
