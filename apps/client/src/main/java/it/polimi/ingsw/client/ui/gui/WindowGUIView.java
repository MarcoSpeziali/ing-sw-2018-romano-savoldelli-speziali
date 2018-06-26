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
import javafx.scene.Node;
import javafx.scene.control.Label;
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
                int finalI = i;
                int finalJ = j;
                cell.setOnDragDropped(event -> {
                    /*
                    TODO: possible positionS for die ok ma che die? Probabilmente Player.getCurrentPlayer().getHeldDie();
                    if (iWindow.getPossiblePositionForDie().contains(2 * finalI + finalJ)) {
                        //cell.putDie() TODO !!!
                    }
                    else cell.setCursor(Cursor.WAIT);

                    TODO: opterei per una versione di questo tipo:
                    */

                    IDie heldDie = Player.getCurrentPlayer().getHeldDie();

                    if (heldDie == null) {
                        return; // do nothing
                    }

                    try {
                        this.controller.tryToPut(heldDie, 2 * finalI + finalJ);
                    }
                    catch (DieInteractionException e) {
                        cell.setCursor(Cursor.WAIT);
                    }
                    catch (RemoteException e) {
                        // CIAONE PROPRIO
                    }
                });

                CellGUIView guiView = loader.getController();
                guiView.setController(windowController.getCellController(i, j));
                gridPane.add(cell, j, i);
            }
        }

        // for (int i = 0; i < 6- iWindow.getDifficulty(); i++) {  // FIXME molto oscuro il perchè funzioni solo così... @marco: semplicemente (basta vederlo da scenebuilder) i pallini sono in ordine inverso, vanno da sx a dx
        for (int i = iWindow.getDifficulty() - 1; i > 0; i--) {
            Circle circle = (Circle) difficultyHbox.getChildren().get(i);
            circle.setFill(Paint.valueOf("#2c3e50"));
        }
    }

    @Override
    public void init() {

    }
}
