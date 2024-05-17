package dev.midplane.fuzzysearch.fuzzysearchers;

import lombok.Data;

import java.util.Arrays;
import java.util.function.Function;

@Data
public class NgramComputerConfig {
    private final int ngramN;
    private Function<String, String> transformNgram;

    public NgramComputerConfig(int ngramN, Function<String, String> transformNgram) {
        this.ngramN = ngramN;
        this.transformNgram = transformNgram;
    }

    public NgramComputerConfig(final int ngramN) {
        this.ngramN = ngramN;
    }

    public static NgramComputerConfig createDefaultConfig() {
        return new NgramComputerConfig(3, ngram -> {
            if (ngram.endsWith("$")) {
                return null;
            } else if (!ngram.contains("$")) {
                char[] chars = ngram.toCharArray();
                Arrays.sort(chars);
                return new String(chars);
            } else {
                return ngram;
            }
        });
    }
}