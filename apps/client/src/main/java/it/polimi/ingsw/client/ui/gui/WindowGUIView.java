package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.views.WindowView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class WindowGUIView extends WindowView {

    @FXML
    public GridPane gridPane;

    @FXML
    public Label nameLabel;

    @FXML
    public HBox difficultyHbox;

    public WindowGUIView(){
    }



    public void setWindow(Window window) throws IOException {
        super.setWindow(window);

        nameLabel.setText(window.getId());
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        for (int i = 0; i < window.getRows(); i++) {
            for (int j = 0; j < window.getColumns(); j++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Constants.Resources.CELL_VIEW_FXML.getURL());
                Node root = loader.load();
                CellGUIView controller = loader.getController();
                controller.setCell(window.getCells()[i][j]);
                gridPane.add(root, j, i);

            }
        }


        for (int i = 0; i < window.getDifficulty(); i++) {
            Circle circle = (Circle) difficultyHbox.getChildren().get(i);
            circle.setFill(Paint.valueOf("#2c3e50"));
        }



    }


}
