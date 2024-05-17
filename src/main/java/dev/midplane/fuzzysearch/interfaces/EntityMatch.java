package dev.midplane.fuzzysearch.interfaces;

import lombok.Data;

@Data
public class EntityMatch<T> {
    private final T entity;
    private final double quality;
    private final String matchedString;
}
