package it.polimi.ingsw.client.ui.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.Match;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.mocks.ICell;
import it.polimi.ingsw.net.mocks.IDie;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.net.responses.MoveResponse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;

public class WindowGUIView extends GUIView<IWindow> {

    public void setStatus(Constants.Status status) {
        this.Status = status;
    }

    private CellGUIView[][] cellGUIViews;

    private Constants.Status Status;

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

        if (cellGUIViews != null) {
            for (int i = 0; i < iWindow.getRows(); i++) {
                for (int j = 0; j < iWindow.getColumns(); j++) {
                    ICell[][] cells = model.getCells();
                    IDie die = cells[i][j].getDie();

                    cellGUIViews[i][j].onUpdateReceived(die);
                }
            }
            return;
        }

        cellGUIViews = new CellGUIView[iWindow.getRows()][iWindow.getColumns()];

        for (int i = 0; i < iWindow.getRows(); i++) {
            for (int j = 0; j < iWindow.getColumns(); j++) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Constants.Resources.CELL_VIEW_FXML.getURL());
                Node target = loader.load();
                cellGUIViews[i][j] = loader.getController();
                cellGUIViews[i][j].setModel(model.getCells()[i][j]);

                target.setOnDragOver(event -> {
                    if (Status == Constants.Status.OWNER_UNLOCKED) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    event.consume();
                });

                int finalJ = j, finalI = i;

                target.setOnDragDropped(event -> {
                    Dragboard db = event.getDragboard();

                    try {
                        MoveResponse moveResponse = Match.getMatchController().tryToMove(
                                Move.getCurrentMove()
                                        .end(finalI * iWindow.getColumns() + finalJ)
                        );


                        if (!moveResponse.isValid()) {
                            JFXButton button = new JFXButton("OK");
                            JFXDialogLayout content = new JFXDialogLayout();
                            JFXDialog dialog = new JFXDialog(Match.getOuterPane(), content, JFXDialog.DialogTransition.CENTER);
                            content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.WINDOW_GUI_INVALID_MOVE)));
                            content.setBody(new Text(Constants.Strings.toLocalized(Constants.Strings.WINDOW_GUI_MOVE_NOT_MATCHES_RULES)));
                            content.setActions(button);
                            button.setOnMousePressed(event1 -> dialog.close());
                            dialog.show();
                        }
                        else Match.performedAction |= 0b01;

                        target.setCursor(Cursor.CROSSHAIR);
                        event.setDropCompleted(true);
                        event.consume();
                    }
                    catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    event.consume();
                });


                target.setOnMousePressed(event -> {
                    if (Status == Constants.Status.SELECTION_UNLOCKED) {
                        try {
                            target.setCursor(Cursor.HAND);
                            Match.getMatchController().postChosenPosition(finalI * iWindow.getColumns() + finalJ);
                            Match.dialog.close();
                        }
                            catch (IOException e) {
                            e.printStackTrace();
                            }
                        }
                });


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
