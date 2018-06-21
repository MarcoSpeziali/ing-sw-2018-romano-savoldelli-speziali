package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.WindowGUIView;
import it.polimi.ingsw.controllers.WindowController;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Window;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowGUIViewTest extends Application {

    private WindowGUIView windowGUIView;

    private Cell[][] cells = new Cell[][]{
            {
                    new Cell(6, null), new Cell(5, null), new Cell(4, null), new Cell(0, GlassColor.GREEN)
            },
            {
                    new Cell(0, null), new Cell(1, null), new Cell(3, null), new Cell(0, GlassColor.PURPLE)
            },
            {
                    new Cell(0, GlassColor.BLUE), new Cell(2, null), new Cell(0, GlassColor.RED), new Cell(0, GlassColor.YELLOW)
            }
    };
    private Window window = new Window(4, 3,4,"MadonnaGay", null, cells);

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Constants.Resources.WINDOW_VIEW_FXML.getURL());
        Parent root = loader.load();
        windowGUIView = loader.getController();
        windowGUIView.setWindow(window);
        Scene scene = new Scene(root, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

}
