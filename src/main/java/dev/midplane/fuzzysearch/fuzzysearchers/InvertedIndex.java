package dev.midplane.fuzzysearch.fuzzysearchers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InvertedIndex {
    private final Map<String, TermIds> ngramToTermIds;

    public InvertedIndex() {
        this.ngramToTermIds = new ConcurrentHashMap<>();
    }

    public void add(String ngram, int termIndex, int frequency) {
        TermIds termIds = ngramToTermIds.get(ngram);

        if (termIds == null) {
            termIds = new TermIds();
            ngramToTermIds.put(ngram, termIds);
        }

        termIds.addId(termIndex, frequency);
    }


    public TermIds getIds(String ngram) {
        return ngramToTermIds.get(ngram);
    }

    public int getSize() {
        return ngramToTermIds.size();
    }
}

