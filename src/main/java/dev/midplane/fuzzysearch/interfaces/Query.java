package dev.midplane.fuzzysearch.interfaces;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Query {
    private final String string;
    private final int topN;
    private final double minQuality;

    public Query(final String string) {
        this.string = string;
        this.topN = Integer.MAX_VALUE;
        this.minQuality = 0.3;
    }
}
