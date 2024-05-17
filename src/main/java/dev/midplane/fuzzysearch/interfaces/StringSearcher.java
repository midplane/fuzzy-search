package dev.midplane.fuzzysearch.interfaces;

import dev.midplane.fuzzysearch.stringsearchers.Result;

import java.util.List;

public interface StringSearcher {
    void index(List<String> terms);

    Result getMatches(Query query);
}
