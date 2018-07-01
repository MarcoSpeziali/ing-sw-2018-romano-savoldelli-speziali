package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.IOException;

import static it.polimi.ingsw.client.ui.gui.WindowGUIView.Property.*;

public class WindowGUIView extends GUIView<IWindow> {

    public Property getProperty() {
        return property;
    }

    public enum Property {
        OWNED, OPPONENT, NONE, SELECTION
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    private Property property;

    @FXML
    public GridPane gridPane;

    @FXML
    public Label nameLabel;

    @FXML
    public HBox difficultyHbox;

    public int getSelectedLocation() {
        return selectedLocation;
    }

    private int selectedLocation;

    public WindowGUIView() {
    }

    public void setModel(IWindow iWindow) throws IOException {

        super.setModel(iWindow);

        nameLabel.setText(iWindow.getId());
        gridPane.setVgap(5);
        gridPane.setHgap(5);

        for (int i = 0; i < iWindow.getRows(); i++) {
            for (int j = 0; j < iWindow.getColumns(); j++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Constants.Resources.CELL_VIEW_FXML.getURL());
                Node cell = loader.load();
                CellGUIView guiView = loader.getController();
                guiView.setModel(model.getCells()[i][j]);

                cell.setOnDragDropped(event -> {

                    if (this.property == OWNED) {
                        IDie heldDie = Player.getCurrentPlayer().getHeldDie();

                        if (heldDie == null) {
                            return; // do nothing
                        }

                        //try
                        {
                            cell.setCursor(new ImageCursor(new Image(Constants.Resources.DICE_CURSOR.getRelativePath())));

                            //this.model.tryToPut(heldDie, Move.getCurrentMove().getDraftPoolPickPosition());
                        }
                        /*catch (DieInteractionException e) {
                        cell.setCursor(new ImageCursor(new Image(Constants.Resources.STOP_CURSOR.getRelativePath())));
                        cell.setDisable(true);
                        }
                        catch (RemoteException e) {
                            e.printStackTrace();
                        }*/
                    }
                    else {
                        cell.setCursor(new ImageCursor(new Image(Constants.Resources.STOP_CURSOR.getRelativePath())));
                    }
                });


                cell.setOnMouseEntered(event -> {

                    if (property == OWNED) {
                        cell.setCursor(Cursor.CLOSED_HAND);
                    }
                    if (property == OPPONENT) {
                        cell.setCursor(new ImageCursor(new Image(Constants.Resources.STOP_CURSOR.getRelativePath())));
                    }
                    if (property == SELECTION) {
                        cell.setCursor(Cursor.HAND);
                    }
                });

                int finalI = i, finalJ = j;
                cell.setOnMouseClicked(event -> {
                    if (property == SELECTION) {
                        selectedLocation = finalI*iWindow.getColumns() + finalJ;
                    }
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
