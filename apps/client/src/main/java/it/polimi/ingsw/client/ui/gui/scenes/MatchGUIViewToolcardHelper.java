package it.polimi.ingsw.client.ui.gui.scenes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.*;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.net.requests.ChoosePositionForLocationRequest;
import it.polimi.ingsw.utils.Range;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.jfoenix.controls.JFXDialog.DialogTransition.CENTER;
import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;

public class MatchGUIViewToolcardHelper {

    private MatchController model;
    private StackPane outerPane;

    public MatchGUIViewToolcardHelper(MatchController matchController, StackPane outerpane) {
        this.model = matchController;
        this.outerPane = outerpane;
    }

    public void init() {
        setUpChoosePositionFuture();
        setUpContinueToRepeatFuture();
        setUpEffectFuture();
        setUpShadeFuture();
    }


    private void setUpShadeFuture() {
        CompletableFuture.supplyAsync(unsafe(() -> this.model.waitForSetShade()))
                .thenAccept(this::setUpShade);
    }

    private void setUpChoosePositionFuture() {
        CompletableFuture.supplyAsync(unsafe(() -> this.model.waitForChoosePositionFromLocation()))
                .thenAccept(this::setUpChoosePosition);
    }

    private void setUpEffectFuture() {
        CompletableFuture.supplyAsync(unsafe(() ->
                this.model.waitForChooseBetweenActions())).thenAccept(this::setUpEffect);
    }

    private void setUpContinueToRepeatFuture() {
        CompletableFuture.supplyAsync(unsafe(()-> this.model.waitForContinueToRepeat()))
                .thenAccept(this::setUpContinueToRepeat);
    }

    private void setUpShade(IDie iDie) {
        Platform.runLater(unsafe(() -> {
            JFXDialogLayout content = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);
            HBox hBox = new HBox();
            GridPane gridPane = new GridPane();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
            Node die = loader.load();

            DieGUIView dieGUIView = loader.getController();
            dieGUIView.setModel(iDie);

            for (int i = 0; i < 6; i++) {
                FXMLLoader innerLoader = new FXMLLoader();
                innerLoader.setLocation(Constants.Resources.CELL_VIEW_FXML.getURL());
                Node cell = innerLoader.load();
                cell.setScaleY(1.7);
                cell.setScaleX(1.7);
                CellGUIView cellGUIView = innerLoader.getController();
                cellGUIView.setModel(new CellMock(i+1, null));
                gridPane.add(cell, i%3, i/3);
                gridPane.setVgap(35);
                gridPane.setHgap(35);
                int finalI = i+1;
                cell.setOnMouseEntered(event -> {
                    try {
                        cell.setCursor(Cursor.HAND);
                        dieGUIView.setModel(new DieMock(finalI, iDie.getColor()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                cell.setOnMouseClicked(event -> {
                    try {
                        this.model.postSetShade(finalI);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialog.close();
                });
            }
            hBox.getChildren().addAll(die, gridPane);
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);
            content.setActions();
            content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_CHOOSE_SHADE_FOR_DIE)));
            content.setBody(hBox);
            gridPane.setScaleX(0.75);
            gridPane.setScaleY(0.75);
            dialog.show();
            setUpShadeFuture();
        }));
    }

    private void setUpEffect(Map.Entry<IAction[], Range<Integer>> m) {
        //TODO: implement
    }

    private void setUpContinueToRepeat(IAction iEffect) {
        Platform.runLater(unsafe(()-> {
            JFXButton again = new JFXButton(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_RUN_AGAIN));
            JFXButton stop = new JFXButton(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_STOP));
            JFXDialogLayout content = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);
            content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_CHOOSE_ACTION_FOR_EFFECT)));
            content.setBody(new Text(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_EFFECT)+": " + iEffect.getDescription()));
            again.setOnMouseClicked(event -> {
                try {
                    this.model.postContinueToRepeatChoice(true);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                dialog.close();
            });
            stop.setOnMouseClicked(event -> {
                try {
                    this.model.postContinueToRepeatChoice(false);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                dialog.close();
            });
            dialog.show();
            setUpContinueToRepeatFuture();
        }));
    }

    private void setUpChoosePosition(ChoosePositionForLocationRequest choosePosition) {
        //TODO: finish
        Platform.runLater(unsafe(() -> {
            JFXDialog dialog = new JFXDialog();
            JFXDialogLayout content = new JFXDialogLayout();
            JFXButton button = new JFXButton("OK");
            FXMLLoader loader = new FXMLLoader();
            GridPane node = null;
            JSONSerializable object = choosePosition.getLocation();
            Set<Integer> set = choosePosition.getUnavailableLocations();

            if (object instanceof IWindow) {
                loader.setLocation(Constants.Resources.WINDOW_VIEW_FXML.getURL());
                try {
                    node = loader.load();
                    WindowGUIView windowGUIView = loader.getController();
                    windowGUIView.setModel((IWindow) object);
                    windowGUIView.setProperty(Constants.Property.SELECTION);
                    for (Integer location : set) {
                        Node cell = windowGUIView.gridPane.getChildren().get(location);
                        cell.setDisable(true);
                    }
                    this.model.postChosenPosition(windowGUIView.getSelectedLocation());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (object instanceof IDraftPool) {
                loader.setLocation(Constants.Resources.DRAFTPOOL_VIEW_FXML.getURL());
                try {
                    node = loader.load();
                    DraftPoolGUIView draftPoolGUIView = loader.getController();
                    draftPoolGUIView.setModel((IDraftPool) object);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            if (object instanceof IRoundTrack) {
                loader.setLocation(Constants.Resources.ROUNDTRACK_VIEW_FXML.getURL());
                try {
                    node = loader.load();
                    RoundTrackGUIView roundTrackGUIView = loader.getController();
                    roundTrackGUIView.setModel((IRoundTrack) object);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            content.setBody(node);
            content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_CHOOSE_LOCATION)+": "));
            dialog.show();
            setUpChoosePositionFuture();
        }));
    }
}
