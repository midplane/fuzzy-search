package dev.midplane.fuzzysearch.fuzzysearchers;

public class QualityComputer {
    public static double computeJaccardCoefficient(int nofNgramsTerm1, int nofNgramsTerm2, int nofCommonNgrams) {
        return (double) nofCommonNgrams / (nofNgramsTerm1 + nofNgramsTerm2 - nofCommonNgrams);
    }

    public static double computeOverlapMaxCoefficient(int nofNgramsTerm1, int nofNgramsTerm2, int nofCommonNgrams) {
        return (double) nofCommonNgrams / Math.max(nofNgramsTerm1, nofNgramsTerm2);
    }
}