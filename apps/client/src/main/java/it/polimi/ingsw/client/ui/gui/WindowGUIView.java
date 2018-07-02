package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;

import it.polimi.ingsw.core.Match;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IWindow;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class WindowGUIView extends GUIView<IWindow> {


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
                Node target = loader.load();
                CellGUIView cellGUIView = loader.getController();
                cellGUIView.setModel(model.getCells()[i][j]);

                target.setOnDragOver(new EventHandler<DragEvent>() {
                    public void handle(DragEvent event) {
                        if (property == Property.OWNED) {
                            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        }
                        event.consume();
                    }
                });

                int finalJ = j, finalI = i;
                target.setOnDragDropped(new EventHandler <DragEvent>() {
                    public void handle(DragEvent event) {
                        Dragboard db = event.getDragboard();

                        try {
                            Match.getMatchController().tryToMove(Move.getCurrentMove()
                                    .end(finalI *iWindow.getColumns()+ finalJ));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        target.setCursor(Cursor.CROSSHAIR);
                        System.out.println();
                        cellGUIView.onUpdateReceived((IDie) db.getContent(Constants.iDieFormat));

                        event.setDropCompleted(true);

                        event.consume();
                    }
                });

                if (property == Property.SELECTION) {
                    target.setOnMousePressed(event -> {
                        selectedLocation = finalI*iWindow.getColumns()+finalJ;
                    });
                }


                gridPane.add(target, j, i);
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
