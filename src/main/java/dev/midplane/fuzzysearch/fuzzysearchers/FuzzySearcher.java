package dev.midplane.fuzzysearch.fuzzysearchers;

import dev.midplane.fuzzysearch.interfaces.Query;
import dev.midplane.fuzzysearch.interfaces.StringSearcher;
import dev.midplane.fuzzysearch.stringsearchers.Match;
import dev.midplane.fuzzysearch.stringsearchers.Result;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FuzzySearcher implements StringSearcher {

    private final NgramComputer ngramComputer;
    private InvertedIndex invertedIndex;
    private int[] numberOfNgrams;

    public FuzzySearcher(NgramComputer ngramComputer) {
        this.ngramComputer = ngramComputer;
        this.invertedIndex = new InvertedIndex();
        this.numberOfNgrams = new int[0];
    }

    @Override
    public void index(List<String> terms) {
        this.invertedIndex = new InvertedIndex();
        this.numberOfNgrams = new int[terms.size()];

        for (int i = 0; i < terms.size(); i++) {
            String term = terms.get(i);

            if (!isValidTerm(term)) {
                numberOfNgrams[i] = 0;
                continue;
            }

            List<String> ngrams = ngramComputer.computeNgrams(term);
            numberOfNgrams[i] = ngrams.size();

            Map<String, Integer> ngramsToFrequency = getNgramsToFrequency(ngrams);
            for (Map.Entry<String, Integer> entry : ngramsToFrequency.entrySet()) {
                invertedIndex.add(entry.getKey(), i, entry.getValue());
            }
        }
    }

    private boolean isValidTerm(String term) {
        return term != null && !term.trim().isEmpty();
    }

    @Override
    public Result getMatches(Query query) {
        if (invertedIndex.getSize() == 0) {
            return new Result(List.of(), query);
        }

        List<String> ngrams = ngramComputer.computeNgrams(query.getString());
        Map<String, Integer> ngramsToFrequency = getNgramsToFrequency(ngrams);
        int ngramsQuery = ngrams.size();

        final var commonNgramCounts = computeCommonNgramCounts(ngramsToFrequency);
        List<Match> matches = getMatchesFromCommonNgrams(commonNgramCounts, ngramsQuery, query.getMinQuality());
        return new Result(matches, query);
    }

    private int[] computeCommonNgramCounts(Map<String, Integer> queryNgramsToFrequency) {
        int[] commonNgramCounts = new int[this.numberOfNgrams.length];

        queryNgramsToFrequency.forEach((ngram, frequency) -> {
            TermIds termIds = invertedIndex.getIds(ngram);
            if (termIds == null) {
                return;
            }
            for (int i = 0; i < termIds.getLength(); i++) {
                commonNgramCounts[termIds.getId(i)] += Math.min(frequency, termIds.getFrequency(i));
            }
        });
        return commonNgramCounts;
    }

    private Map<String, Integer> getNgramsToFrequency(List<String> ngrams) {
        return ngrams.stream().collect(Collectors.toConcurrentMap(ngram -> ngram, ngram -> 1, Integer::sum));
    }

    private List<Match> getMatchesFromCommonNgrams(final int[] commonNgramCounts, int ngramsQuery, double minQuality) {
        return IntStream.range(0, numberOfNgrams.length).parallel().mapToObj(i -> {
            double quality = QualityComputer.computeJaccardCoefficient(ngramsQuery, numberOfNgrams[i],
                                                                       commonNgramCounts[i]);
            if (quality > minQuality) {
                return new Match(i, quality);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
