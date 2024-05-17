package dev.midplane.fuzzysearch.fuzzysearchers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class NgramComputer {
    private final int ngramN;
    private final Function<String, String> transformNgram;

    public NgramComputer(NgramComputerConfig ngramComputerConfig) {
        this.ngramN = ngramComputerConfig.getNgramN();
        this.transformNgram = ngramComputerConfig.getTransformNgram() != null ? ngramComputerConfig.getTransformNgram()
                                                                              : (ngram -> ngram);
    }

    public List<String> computeNgrams(String input) {
        if (input.isEmpty()) {
            return new ArrayList<>();
        }

        if (input.length() <= ngramN) {
            String transformed = transformNgram.apply(input);
            return transformed != null ? List.of(transformed) : new ArrayList<>();
        }

        List<String> ngrams = new ArrayList<>();
        for (int i = 0; i < maximumNumberOfNgrams(input); i++) {
            String transformed = transformNgram.apply(input.substring(i, i + ngramN));
            if (transformed != null) {
                ngrams.add(transformed);
            }
        }
        return ngrams;
    }

    private int maximumNumberOfNgrams(String input) {
        return Math.max(input.length() - ngramN + 1, !input.isEmpty() ? 1 : 0);
    }
}
