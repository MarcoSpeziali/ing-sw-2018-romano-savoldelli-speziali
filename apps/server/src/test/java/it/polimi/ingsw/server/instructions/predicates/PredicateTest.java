package it.polimi.ingsw.server.instructions.predicates;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PredicateTest {

    @Test
    void getId() {
        Predicate predicate = mock(Predicate.class);
        when(predicate.getId()).thenReturn("id");

        Assertions.assertEquals("id", predicate.getId());
    }
}