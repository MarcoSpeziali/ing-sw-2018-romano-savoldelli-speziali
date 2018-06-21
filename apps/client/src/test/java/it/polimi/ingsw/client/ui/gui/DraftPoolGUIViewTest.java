package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.DraftPoolGUIView;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.DraftPool;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// FIXME: 20/06/18 WTF is this?
public class DraftPoolGUIViewTest extends Application {

    private DraftPoolGUIView draftPoolGUIView;
    private DraftPool draftPool = new DraftPool(5);


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Constants.Resources.DRAFTPOOL_VIEW_FXML.getURL());
        Parent root = loader.load();
        draftPoolGUIView =  loader.getController();
        for (int i = 0; i < 5; i++) {
            draftPool.putDie(new Die(6, GlassColor.RED));
        }
        draftPoolGUIView.setDraftPool(draftPool);
        Scene scene = new Scene(root, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }
}
