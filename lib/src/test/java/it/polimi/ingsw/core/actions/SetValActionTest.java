package it.polimi.ingsw.core.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.Die;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SetValActionTest {

    private RedrawAction action;
    private Context context = new Context();
    private ActionData testData = new ActionData("test", null, null, null);
    private Die die;

    @BeforeEach
    void setUp() {
    }

    @Test
    void run() {
    }
}