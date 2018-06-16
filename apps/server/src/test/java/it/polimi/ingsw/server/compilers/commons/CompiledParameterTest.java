package it.polimi.ingsw.server.compilers.commons;

import it.polimi.ingsw.server.utils.VariableSupplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

class CompiledParameterTest {

    @Test
    void testOptionalCompiledParameter() {
        VariableSupplier<String> variableSupplier = context -> "testValue";

        CompiledParameter compiledParameter = new CompiledParameter(
                Serializable.class,
                12,
                variableSupplier,
                "optional_name",
                "default string value"
        );

        Assertions.assertEquals(Serializable.class, compiledParameter.getType());
        Assertions.assertEquals(12, compiledParameter.getPosition().intValue());
        Assertions.assertSame(variableSupplier, compiledParameter.getParameterValue());
        Assertions.assertEquals("optional_name", compiledParameter.getOptionalName());
        Assertions.assertEquals("default string value", compiledParameter.getDefaultValue());
        Assertions.assertTrue(compiledParameter.isOptional());
    }

    @Test
    void testMandatoryCompiledParameter() {
        VariableSupplier<String> variableSupplier = context -> "testValue";

        CompiledParameter compiledParameter = new CompiledParameter(
                Serializable.class,
                12,
                variableSupplier,
                null,
                null
        );

        Assertions.assertEquals(Serializable.class, compiledParameter.getType());
        Assertions.assertEquals(12, compiledParameter.getPosition().intValue());
        Assertions.assertSame(variableSupplier, compiledParameter.getParameterValue());
        Assertions.assertNull(compiledParameter.getOptionalName());
        Assertions.assertNull(compiledParameter.getDefaultValue());
        Assertions.assertFalse(compiledParameter.isOptional());
    }
}