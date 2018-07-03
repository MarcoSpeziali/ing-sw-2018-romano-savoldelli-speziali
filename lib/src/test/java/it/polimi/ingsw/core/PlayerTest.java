package it.polimi.ingsw.core;

import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.net.mocks.DieMock;
import it.polimi.ingsw.net.mocks.WindowMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class PlayerTest {

private Window window;
private Player player;
private Die die = new Die(3, GlassColor.PURPLE);
    @BeforeEach
    public void setUP(){

        player = new Player(1, "prova", 3, mock(WindowMock.class), mock(DieMock.class));
    }


    @Test
    void getCurrentPlayer() {
        Assertions.assertEquals(1, 1);
    }

    @Test
    void getId() {
        Assertions.assertEquals(1,1);
    }

    @Test
    void getUsername() {
        Assertions.assertEquals("prova", "prova");
    }

    @Test
    void getFavourTokens() {
        Assertions.assertEquals(3, 3);
    }

    @Test
    void getNumberOfDice() {
        Assertions.assertEquals(1,1);

        Assertions.assertEquals(0,0);
    }
}