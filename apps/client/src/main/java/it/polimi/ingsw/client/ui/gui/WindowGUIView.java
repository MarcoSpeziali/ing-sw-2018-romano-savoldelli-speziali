package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.core.Match;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.net.responses.MoveResponse;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class WindowGUIView extends GUIView<IWindow> {

    public boolean isChosen() {
        return chosen;
    }

    private boolean chosen = false;


    public void setProperty(Constants.Property property) {
        this.property = property;
    }

    private Constants.Property property;

    @FXML
    public GridPane gridPane;

    @FXML
    public Label nameLabel;

    @FXML
    public HBox difficultyHbox;


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
                        if (property == Constants.Property.OWNED) {
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
                            MoveResponse moveResponse = Match.getMatchController().tryToMove(
                                    Move.getCurrentMove()
                                            .end(finalI *iWindow.getColumns()+ finalJ)
                            );

                            if (!moveResponse.isValid()) {
                                // TODO: 03/07/18 @savdav96: show error message
                            }

                            target.setCursor(Cursor.CROSSHAIR);
                            event.setDropCompleted(true);
                            event.consume();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

                if (property == Constants.Property.SELECTION) {
                    target.setOnMousePressed(event -> {
                        try {
                            Match.getMatchController().postChosenPosition(finalI*iWindow.getColumns()+finalJ);
                            chosen = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
