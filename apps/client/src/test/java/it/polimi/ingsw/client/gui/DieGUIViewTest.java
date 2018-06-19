package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.DieGUIView;
import it.polimi.ingsw.controllers.DieController;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DieGUIViewTest extends Application {

    private DieGUIView dieGUIView;
    private Die die = new Die(GlassColor.BLUE, 5);

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
        Parent root = loader.load();
        dieGUIView = loader.getController();
        dieGUIView.setDie(die);
        Scene scene = new Scene(root, 100, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
