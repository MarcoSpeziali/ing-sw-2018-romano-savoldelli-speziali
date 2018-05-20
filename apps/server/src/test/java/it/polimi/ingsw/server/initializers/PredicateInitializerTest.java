package it.polimi.ingsw.server.initializers;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.server.compilers.instructions.predicates.CompiledPredicate;
import it.polimi.ingsw.server.compilers.instructions.predicates.PredicateCompiler;
import it.polimi.ingsw.server.compilers.instructions.predicates.directives.PredicateDirective;
import it.polimi.ingsw.server.compilers.instructions.predicates.directives.PredicateDirectivesCompiler;
import it.polimi.ingsw.server.instructions.predicates.DistinctColorPredicate;
import it.polimi.ingsw.server.instructions.predicates.DistinctShadePredicate;
import it.polimi.ingsw.server.instructions.predicates.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

class PredicateInitializerTest {

    Context context;
    List<PredicateDirective> predicateDirectives;

    @BeforeEach
    void setUp() throws ClassNotFoundException, SAXException, ParserConfigurationException, IOException {
        this.context = Context.getSharedInstance();
        this.context.put("row", new Die[] {
                new Die(GlassColor.RED, 2),
                new Die(GlassColor.YELLOW, 2),
                new Die(GlassColor.GREEN, 2),
                new Die(GlassColor.BLUE, 2),
                new Die(GlassColor.PURPLE, 2)
        });

        this.predicateDirectives = PredicateDirectivesCompiler.compile();
    }

    @Test
    void testDistinctColorPredicate() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        CompiledPredicate compiledPredicate = PredicateCompiler.compile(
                "distinct_color $row$",
                this.predicateDirectives
        );

        Predicate predicate = PredicateInitializer.instantiate(compiledPredicate);
        Assertions.assertNotNull(predicate);
        Assertions.assertEquals(DistinctColorPredicate.class, predicate.getClass());
        Assertions.assertTrue(predicate.evaluate(context));
    }

    @Test
    void testDistinctShadePredicate() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        CompiledPredicate compiledPredicate = PredicateCompiler.compile(
                "distinct_shade $row$",
                this.predicateDirectives
        );

        Predicate predicate = PredicateInitializer.instantiate(compiledPredicate);
        Assertions.assertNotNull(predicate);
        Assertions.assertEquals(DistinctShadePredicate.class, predicate.getClass());
        Assertions.assertFalse(predicate.evaluate(context));
    }
}