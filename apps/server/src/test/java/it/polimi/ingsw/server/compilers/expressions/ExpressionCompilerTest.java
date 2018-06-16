package it.polimi.ingsw.server.compilers.expressions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExpressionCompilerTest {

    private Die die;

    @BeforeEach
    void setUp() {
        this.die = new Die(GlassColor.RED, 3);

        Context.getSharedInstance().put("DIE", this.die);
        Context.getSharedInstance().put("test_class", new TestClass(19));
    }

    @Test
    void testSimple() {
        String predicate = "$DIE$";
        Die die = (Die) ExpressionCompiler.compile(predicate).get(Context.getSharedInstance());

        Assertions.assertSame(this.die, die);
    }

    @Test
    void testSimpleFunction() {
        String predicate = "$DIE::getShade()$";
        Integer shade = (Integer) ExpressionCompiler.compile(predicate).get(Context.getSharedInstance());

        Assertions.assertEquals(3, shade.intValue());
    }

    @Test
    void testSimpleFunction2() {
        String predicate = "$DIE::getShade()::intValue()$";
        int shade = (int) ExpressionCompiler.compile(predicate).get(Context.getSharedInstance());

        Assertions.assertEquals(3, shade);
    }

    @Test
    void testSimpleFunctionWithParameters() {
        String predicate = "$DIE::getShade()::compareTo(4)$";
        int res = (int) ExpressionCompiler.compile(predicate).get(Context.getSharedInstance());

        Assertions.assertEquals(-1, res);
    }

    @Test
    void testFieldAccess() {
        String predicate = "$test_class::a$";

        int res = (int) ExpressionCompiler.compile(predicate).get(Context.getSharedInstance());
        Assertions.assertEquals(19, res);
    }

    @Test
    void testSemiComplexFunction() {
        String predicate = "$test_class::useDie($DIE$, 4, \"test\")$";

        boolean res = (boolean) ExpressionCompiler.compile(predicate).get(Context.getSharedInstance());
        Assertions.assertFalse(res);
    }

    @Test
    void testComplexFunction() {
        String predicate = "$test_class::useDie($DIE$, $test_class::getA()$, \"test\")$";

        boolean res = (boolean) ExpressionCompiler.compile(predicate).get(Context.getSharedInstance());
        Assertions.assertFalse(res);
    }

    @Test
    void testStringWithDollarSignFunction() {
        String predicate = "$test_class::useDie($DIE$, 12, \"15\\$\")$";

        boolean res = (boolean) ExpressionCompiler.compile(predicate).get(Context.getSharedInstance());
        Assertions.assertFalse(res);
    }

    @Test
    void testNoSuchFieldException() {
        String predicate = "$test_class::non_existing$";

        Assertions.assertThrows(MalformedExpressionException.class, () -> {
            ExpressionCompiler.compile(predicate).get(Context.getSharedInstance());
        });
    }

    @Test
    void testMalformedException() {
        String predicate = "$test_class::non_existing(;$";

        Assertions.assertThrows(MalformedExpressionException.class, () -> {
            ExpressionCompiler.compile(predicate).get(Context.getSharedInstance());
        });
    }

    @Test
    void testIllegalAccessExceptionForField() {
        String predicate = "$test_class::b$";

        Assertions.assertThrows(MalformedExpressionException.class, () -> {
            ExpressionCompiler.compile(predicate).get(Context.getSharedInstance());
        });
    }

    @Test
    void testIllegalAccessExceptionForMethod() {
        String predicate = "$test_class::x()$";

        Assertions.assertThrows(MalformedExpressionException.class, () -> {
            ExpressionCompiler.compile(predicate).get(Context.getSharedInstance());
        });
    }

    @Test
    void testNoSuchMethodException() {
        String predicate = "$test_class::b()$";

        Assertions.assertThrows(MalformedExpressionException.class, () -> {
            ExpressionCompiler.compile(predicate).get(Context.getSharedInstance());
        });
    }

    @Test
    void testConstant() {
        String predicate = "true";

        boolean res = (boolean) ExpressionCompiler.compile(predicate).get(Context.getSharedInstance());
        Assertions.assertTrue(res);
    }

    @SuppressWarnings("ALL")
    class TestClass {
        public Integer a;
        private Integer b;

        public TestClass(int a) {
            this.a = a;
        }

        private Integer x() {
            return 2;
        }

        public Integer getA() {
            return a;
        }

        public boolean useDie(Die die, Integer a, String s) {
            return s.equals("test") && die.getColor() == GlassColor.GREEN;
        }
    }
}