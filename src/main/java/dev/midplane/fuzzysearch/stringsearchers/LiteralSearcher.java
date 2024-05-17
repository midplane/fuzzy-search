package dev.midplane.fuzzysearch.stringsearchers;

import dev.midplane.fuzzysearch.interfaces.Query;
import dev.midplane.fuzzysearch.interfaces.StringSearcher;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LiteralSearcher implements StringSearcher {
    private List<String> terms;

    public LiteralSearcher() {
        this.terms = List.of();
    }

    @Override
    public void index(final List<String> terms) {
        this.terms = terms;
    }

    @Override
    public Result getMatches(final Query query) {
        final List<Match> matches = new ArrayList<>();
        for (final String term : terms) {
            if (term.equals(query.getString())) {
                matches.add(new Match(terms.indexOf(term), 1));
            }
        }
        return new Result(matches, query);
    }
}
