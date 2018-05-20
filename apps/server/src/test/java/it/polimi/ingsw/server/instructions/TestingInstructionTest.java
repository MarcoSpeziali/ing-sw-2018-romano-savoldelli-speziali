package it.polimi.ingsw.server.instructions;

import it.polimi.ingsw.core.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestingInstructionTest {

    private TestingInstruction testingInstruction;

    @BeforeEach
    void setUp() {
        this.testingInstruction = new TestingInstruction();
    }

    @Test
    void setInstruction() {
        this.testingInstruction.setListener(new TestingInstruction.OnTestingInstructionRunEvent() {
            @Override
            public void onTestingInstructionRun(TestingInstruction target, Context context) {
                assertSame(Context.getSharedInstance(), context);
                assertTrue(context.containsKey("testing"));
            }

            @Override
            public Integer getResult(TestingInstruction target, Context context) {
                return 154;
            }
        });

        Context.getSharedInstance().put("testing", true);

        Integer result = this.testingInstruction.run(Context.getSharedInstance());
        assertEquals(154, result.intValue());
    }
}