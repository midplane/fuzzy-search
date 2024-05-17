package dev.midplane.fuzzysearch.stringsearchers;

import dev.midplane.fuzzysearch.interfaces.Query;
import lombok.Data;

import java.util.List;

@Data
public class Result {
    private final List<Match> matches;
    private final Query query;
}
