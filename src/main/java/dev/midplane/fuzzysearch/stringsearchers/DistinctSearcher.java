package dev.midplane.fuzzysearch.stringsearchers;

import dev.midplane.fuzzysearch.interfaces.Query;
import dev.midplane.fuzzysearch.interfaces.StringSearcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DistinctSearcher implements StringSearcher {
    private final StringSearcher stringSearcher;
    private int[] distinctMapping;
    private int[] sortMapping;

    public DistinctSearcher(StringSearcher stringSearcher) {
        this.stringSearcher = stringSearcher;
        this.distinctMapping = new int[0];
        this.sortMapping = new int[0];
    }

    @Override
    public void index(List<String> terms) {
        TermIndex[] termsSorted = new TermIndex[terms.size()];
        for (int i = 0; i < terms.size(); i++) {
            termsSorted[i] = new TermIndex(terms.get(i), i);
        }
        Arrays.sort(termsSorted, Comparator.comparing(t -> t.term));

        this.sortMapping = new int[termsSorted.length];
        for (int i = 0; i < termsSorted.length; i++) {
            this.sortMapping[i] = termsSorted[i].index;
        }

        List<String> distinctTerms = new ArrayList<>();
        List<Integer> distinctMappingNumbers = new ArrayList<>();
        int j = 0;

        String previousTerm = null;

        for (int i = 0; i < termsSorted.length; i++) {
            String term = termsSorted[i].term;

            if (!term.equals(previousTerm)) {
                distinctTerms.add(term);
                distinctMappingNumbers.add(i);
                j++;
            }

            previousTerm = term;
        }

        distinctMappingNumbers.add(termsSorted.length);
        this.stringSearcher.index(distinctTerms);
        this.distinctMapping = distinctMappingNumbers.stream().mapToInt(Integer::intValue).toArray();
    }

    @Override
    public Result getMatches(Query query) {
        Result result = this.stringSearcher.getMatches(query);
        List<Match> newMatches = new ArrayList<>();

        for (Match m : result.getMatches()) {
            int start = this.distinctMapping[m.getIndex()];
            int end = this.distinctMapping[m.getIndex() + 1];

            for (int i = start; i < end; i++) {
                newMatches.add(new Match(this.sortMapping[i], m.getQuality()));
            }
        }

        return new Result(newMatches, query);
    }

    private static class TermIndex {
        String term;
        int index;

        public TermIndex(String term, int index) {
            this.term = term;
            this.index = index;
        }
    }
}
