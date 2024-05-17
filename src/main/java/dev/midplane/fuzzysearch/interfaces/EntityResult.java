package dev.midplane.fuzzysearch.interfaces;

import lombok.Data;

import java.util.List;

@Data
public class EntityResult<Entity> {
    private final List<EntityMatch<Entity>> matches;
    private final Query query;
}
