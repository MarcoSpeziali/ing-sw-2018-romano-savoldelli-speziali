package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.core.IObjective;
import it.polimi.ingsw.models.ObjectiveCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class ObjectiveCardCLIViewTest {
    private ObjectiveCard objectiveCard;
    private ObjectiveCardCLIView objectiveCardCLIView;

    @BeforeEach
    void setUp(){
        this.objectiveCard = new ObjectiveCard("deep_shades", CardVisibility.PUBLIC,"cards.public_cards.deep_shades.name",
                "cards.public_cards.deep_shades.description", mock(IObjective.class) );
        objectiveCardCLIView = new ObjectiveCardCLIView(objectiveCard);
    }
    @Test
    void render() {
        objectiveCardCLIView.render();
    }
}