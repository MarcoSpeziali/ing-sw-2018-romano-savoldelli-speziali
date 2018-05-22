package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.core.Effect;
import it.polimi.ingsw.models.ToolCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ToolCardCLIViewTest {
    private ToolCardCLIView toolCardCLIView;
    private ToolCard toolCard;
    @BeforeEach
    void setUp() {
        this.toolCard = new ToolCard("grozing_pliers", "cards.tool_cards.grozing_pliers.name",
                1, mock(Effect.class),"cards.tool_cards.grozing_pliers.name" , "cards.tool_cards.grozing_pliers.description");
        this.toolCardCLIView = new ToolCardCLIView(toolCard);
    }
    @Test
    void render() {
        toolCardCLIView.render();
    }
}