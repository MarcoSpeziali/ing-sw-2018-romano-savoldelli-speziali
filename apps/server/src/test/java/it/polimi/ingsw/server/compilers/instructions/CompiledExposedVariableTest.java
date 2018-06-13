package it.polimi.ingsw.server.compilers.instructions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

class CompiledExposedVariableTest {

    @Test
    void testGetters() {
        CompiledExposedVariable compiledExposedVariable = new CompiledExposedVariable(
                "default_name",
                "override_value",
                Serializable.class
        );

        Assertions.assertEquals("default_name", compiledExposedVariable.getDefaultName());
        Assertions.assertEquals("override_value", compiledExposedVariable.getOverrideValue());
        Assertions.assertEquals(Serializable.class, compiledExposedVariable.getExposedVariableType());
    }
}