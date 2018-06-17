package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.views.WindowView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.layout.GridPane;

public class WindowGUIView extends WindowView implements GUIView {

    private CellGUIView[][] cellViews;

    public WindowGUIView(Window window) {
        super(window);

        this.cellViews = new CellGUIView[window.getRows()][window.getColumns()];

        for (int i = 0; i < window.getRows(); i++) {
            for (int j = 0; j < window.getColumns(); j++) {
                cellViews[i][j] = new CellGUIView(window.getCells()[i][j]);
            }
        }
    }


    @Override
    public Node render() {
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #34495e;" +
                "-fx-padding: 20 20 20 20");
        grid.setHgap(10);
        grid.setVgap(10);
        for (int i = 0; i < window.getRows(); i++) {
            for (int j = 0; j < window.getColumns(); j++) {
                Node root = cellViews[i][j].render();
                grid.add(root, j, i);
            }
        }
        grid.setAlignment(Pos.CENTER);
        return grid;
    }


}
