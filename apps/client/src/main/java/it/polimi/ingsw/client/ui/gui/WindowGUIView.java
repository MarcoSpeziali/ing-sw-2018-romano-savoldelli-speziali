package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.views.WindowView;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

public class WindowGUIView extends WindowView implements GUIView{

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
                "-fx-border-radius: 5;"+
                "-fx-padding: 15 15 15 15;");
        for(int i=0; i<window.getDifficulty(); i++) {
            grid.add(new Circle(5), 0,1);
        }
        for(int i = 0; i<cellViews.length; i++) {
            for (int j = 0; j<cellViews[0].length; j++) {
                grid.add(cellViews[i][j].render(), 0, i);
            }
        }
        return grid;
    }


}
