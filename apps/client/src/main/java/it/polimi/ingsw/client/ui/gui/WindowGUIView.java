package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.controllers.DieInteractionException;
import it.polimi.ingsw.controllers.WindowController;
import it.polimi.ingsw.core.Player;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.rmi.RemoteException;

public class WindowGUIView extends GUIView<WindowController> {

    @FXML
    public GridPane gridPane;

    @FXML
    public Label nameLabel;

    @FXML
    public HBox difficultyHbox;

    public WindowGUIView() {
    }

    public void setController(WindowController windowController) throws IOException {

        super.setController(windowController);

        IWindow iWindow = windowController.getWindow();

        nameLabel.setText(iWindow.getId());
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        for (int i = 0; i < iWindow.getRows(); i++) {
            for (int j = 0; j < iWindow.getColumns(); j++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Constants.Resources.CELL_VIEW_FXML.getURL());
                Node cell = loader.load();
                CellGUIView guiView = loader.getController();
                guiView.setController(windowController.getCellController(i, j));

                int finalI = i;
                int finalJ = j;
                cell.setOnDragDropped(event -> {

                    //TODO: opterei per una versione di questo tipo:

                    IDie heldDie = Player.getCurrentPlayer().getHeldDie();

                    if (heldDie == null) {
                        return; // do nothing
                    }

                    try {
                        cell.setCursor(new ImageCursor(new Image(Constants.Resources.DICE_CURSOR.getRelativePath())));
                        this.controller.tryToPut(heldDie, 2 * finalI + finalJ);
                    }
                    catch (DieInteractionException e) {
                        cell.setCursor(new ImageCursor(new Image(Constants.Resources.STOP_CURSOR.getRelativePath())));
                        cell.setDisable(true);
                    }
                    catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });

                cell.setOnDragDetected(event -> {

                });

                gridPane.add(cell, j, i);
            }
        }

        for (int i = 0; i < 6 - iWindow.getDifficulty(); i++) {
            Circle circle = (Circle) difficultyHbox.getChildren().get(i);
            circle.setFill(Paint.valueOf("#2c3e50"));
        }
    }

    @Override
    public void init() {

    }
}
