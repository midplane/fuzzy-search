package dev.midplane.fuzzysearch.stringsearchers;

import lombok.Data;

@Data
public class Match {
    private final int index;
    private final double quality;
}
