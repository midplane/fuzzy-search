package dev.midplane.fuzzysearch.entitySearcher;

import dev.midplane.fuzzysearch.interfaces.Query;
import dev.midplane.fuzzysearch.stringsearchers.LiteralSearcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

class DefaultEntitySearcherTest {
    private static DefaultEntitySearcher<Entity, Integer> entitySearcher;
    private static DefaultEntitySearcher<Person, Integer> personSearcher;

    @BeforeAll
    static void setup() {
        final var literalSearcher = new LiteralSearcher();
        final var entities = List.of(new Entity(23501, "Alice"), new Entity(99234, "Bob"), new Entity(5823, "Carol"),
                                     new Entity(11923, "Charlie"));
        entitySearcher = new DefaultEntitySearcher<>(literalSearcher);
        entitySearcher.indexEntities(entities, Entity::id, entity -> List.of(entity.name()));

        final var people = List.of(new Person(23501, "Alice", "Programmer"), new Person(99234, "Bob", "Teacher"),
                                   new Person(5823, "Carol", "Plumber"), new Person(11923, "Charlie", "Waiter"));
        personSearcher = new DefaultEntitySearcher<>(literalSearcher);
        personSearcher.indexEntities(people, Person::id, person -> List.of(person.name(), person.job()));
    }

    @Test
    void testGetMatches() {
        final var query = new Query("Alice");
        final var result = entitySearcher.getMatches(query);

        Assertions.assertEquals(1, result.getMatches().size());
        Assertions.assertEquals(23501, result.getMatches().get(0).getEntity().id);
    }

    @Test
    void testDoesNotMatch() {
        final var query = new Query("David");
        final var result = entitySearcher.getMatches(query);

        Assertions.assertEquals(0, result.getMatches().size());
    }

    @Test
    void testMatchWithFirstTerm() {
        final var query = new Query("Alice");
        final var result = personSearcher.getMatches(query);

        Assertions.assertEquals(1, result.getMatches().size());
        Assertions.assertEquals(23501, result.getMatches().get(0).getEntity().id);
    }

    @Test
    void testMatchWithSecondTerm() {
        final var query = new Query("Programmer");
        final var result = personSearcher.getMatches(query);

        Assertions.assertEquals(1, result.getMatches().size());
        Assertions.assertEquals(23501, result.getMatches().get(0).getEntity().id);
    }

    record Entity(int id, String name) {}

    record Person(int id, String name, String job) {}
}