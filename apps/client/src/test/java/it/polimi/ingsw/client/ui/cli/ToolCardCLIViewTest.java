package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.net.mocks.IEffect;
import it.polimi.ingsw.models.ToolCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class ToolCardCLIViewTest {
    private ToolCardCLIView toolCardCLIView;
    private ToolCard toolCard;

    @BeforeEach
    void setUp() {
        this.toolCard = new ToolCard(
                "grozing_pliers",
                "cards.tool_cards.grozing_pliers.name",
                "cards.tool_cards.grozing_pliers.description",
                mock(IEffect.class)
        );
        this.toolCardCLIView = new ToolCardCLIView(toolCard);
    }

    @Test
    void render() {
        toolCardCLIView.render();
    }
}