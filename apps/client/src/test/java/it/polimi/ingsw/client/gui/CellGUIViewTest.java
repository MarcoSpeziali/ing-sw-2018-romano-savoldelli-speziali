package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.CellGUIView;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Die;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

public class CellGUIViewTest extends Application {

    private CellGUIView cellGUIView;
    private Cell cell = new Cell(5, GlassColor.BLUE);

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Constants.Resources.CELL_VIEW_FXML.getURL());
        Parent root = loader.load();
        cellGUIView = loader.getController();
        cellGUIView.setCell(cell);
        cell.putDie(new Die(5, GlassColor.RED));
        cellGUIView.onUpdateReceived(cell);
        Scene scene = new Scene(root, Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
