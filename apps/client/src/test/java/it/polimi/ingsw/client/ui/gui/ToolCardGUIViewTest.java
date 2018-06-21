package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.ToolCardGUIView;
import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.net.mocks.IEffect;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ToolCardGUIViewTest extends Application {
    private IEffect iEffect = new IEffect() {
        @Override
        public void run(String cardId) {

        }

        @Override
        public int getCost() {
            return 0;
        }

        @Override
        public String getDescriptionKey() {
            return null;
        }
    };
    private ToolCard toolCard = new ToolCard("lens_cutter", "finf", "finweif", iEffect );
    private ToolCardGUIView toolCardGUIView;
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Constants.Resources.TOOL_CARD_VIEW.getURL());
        Parent root = fxmlLoader.load();
        toolCardGUIView = fxmlLoader.getController();
        toolCardGUIView.setToolCard(toolCard);
        Scene scene = new Scene(root, 300, 450);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
