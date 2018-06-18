package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.CellGUIView;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Cell;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        Scene scene = new Scene(root, 100, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
