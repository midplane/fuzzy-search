package dev.midplane.fuzzysearch.interfaces;

import lombok.Data;

import java.util.List;

@Data
public class EntityResult<TEntity> {
    private final List<EntityMatch<TEntity>> matches;
    private final Query query;
}
