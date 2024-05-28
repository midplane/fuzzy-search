package dev.midplane.fuzzysearch.fuzzysearchers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InvertedIndex {
    private final Map<String, TermIds> data;

    public InvertedIndex() {
        this.data = new ConcurrentHashMap<>();
    }

    public void add(String ngram, int termIndex, int frequency) {
        TermIds termIds = data.get(ngram);

        if (termIds == null) {
            termIds = new TermIds();
            data.put(ngram, termIds);
        }

        termIds.addId(termIndex, frequency);
    }


    public TermIds getIds(String ngram) {
        return data.get(ngram);
    }

    public int getSize() {
        return data.size();
    }
}

