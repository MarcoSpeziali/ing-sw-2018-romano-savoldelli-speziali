package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.DieGUIView;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;


public class DieGUIViewTest extends Application {

    private DieGUIView dieGUIView;
    private Die die = new Die(2, GlassColor.GREEN);

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
        Parent root = loader.load();
        dieGUIView = loader.getController();
        dieGUIView.setDie(die);
        Scene scene = new Scene(root, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
