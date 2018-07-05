package it.polimi.ingsw.client.ui.gui.scenes;

import com.jfoenix.controls.*;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.Match;
import it.polimi.ingsw.client.ui.gui.*;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.net.requests.ChooseBetweenActionsRequest;
import it.polimi.ingsw.net.requests.ChoosePositionForLocationRequest;
import it.polimi.ingsw.utils.Range;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.jfoenix.controls.JFXDialog.DialogTransition.CENTER;
import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;

public class MatchGUIViewToolCardHelper {

    private MatchController model;
    private StackPane outerPane;

    private ExecutorService toolCardExecutorService = Executors.newFixedThreadPool(4);
    private boolean closed = true;

    public MatchGUIViewToolCardHelper(MatchController matchController) {
        this.model = matchController;
        this.outerPane = Match.getOuterPane();
    }

    public void init() {
        this.closed = false;

        setUpChoosePosition();
        setUpContinueToRepeat();
        setUpEffect();
        setUpShade();
    }

    private void setUpShade() {
        this.toolCardExecutorService.submit((Callable<Void>) () -> {
            while (!closed) {
                IDie iDie = this.model.waitForSetShade();

                Platform.runLater(unsafe(() -> {
                    JFXDialogLayout content = new JFXDialogLayout();
                    JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);
                    dialog.setOverlayClose(false);
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
                        cellGUIView.setModel(new CellMock(i + 1, null));
                        gridPane.add(cell, i % 3, i / 3);
                        gridPane.setVgap(35);
                        gridPane.setHgap(35);
                        int finalI = i + 1;
                        cell.setOnMouseEntered(event -> {
                            try {
                                cell.setCursor(Cursor.HAND);
                                dieGUIView.setModel(new DieMock(finalI, iDie.getColor()));
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        cell.setOnMouseClicked(event -> {
                            try {
                                this.model.postSetShade(finalI);
                            }
                            catch (IOException e) {
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
                }));
            }

            return null;
        });
    }

    private void setUpEffect() {
        this.toolCardExecutorService.submit((Callable<Void>) () -> {
            while (!closed) {
                ChooseBetweenActionsRequest actionsRequest = this.model.waitForChooseBetweenActions();

                Platform.runLater(() -> {
                    JFXDialogLayout content = new JFXDialogLayout();
                    JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);
                    dialog.setOverlayClose(false);
                    IAction[] iActions = actionsRequest.getActions();
                    JFXButton button = new JFXButton("OK");
                    Range<Integer> range = actionsRequest.getActionsRange();

                    List<IAction> chosenActions = new ArrayList<>();

                    JFXListView<JFXCheckBox> list = new JFXListView<>();
                    list.setDepth(10);
                    Arrays.stream(iActions).forEach(iAction -> {
                        JFXCheckBox checkBox = new JFXCheckBox(iAction.getDescription());
                        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                if (newValue) {
                                    chosenActions.add(iAction);
                                }
                                if (!newValue) {
                                    chosenActions.remove(iAction);
                                }
                            }
                        });
                        list.getItems().add(checkBox);
                    });

                    button.setOnMouseClicked(event -> {
                        if (range.containsValue(chosenActions.size())) {
                            try {
                                this.model.postChosenActions(chosenActions.toArray(new IAction[0]));
                                dialog.close();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            JFXDialogLayout layout = new JFXDialogLayout();
                            JFXDialog error = new JFXDialog(outerPane, layout, CENTER);
                            content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.TOOL_CARD_HELPER_CORRECT_NUMBER)));
                            error.show();
                        }
                    });

                    content.setHeading(new Text((Constants.Strings.toLocalized(Constants.Strings.TOOL_CARD_HELPER_CHOOSE) + range.getStart() + " to " + range.getEnd() +
                            Constants.Strings.toLocalized(Constants.Strings.TOOL_CARD_HELPER_ACTION_PERFORM))));
                    content.setBody(list);
                    content.setActions(button);
                    dialog.show();
                });
            }

            return null;
        });
    }

    private void setUpContinueToRepeat() {
        this.toolCardExecutorService.submit((Callable<Void>) () -> {
            while (!closed) {
                IAction iAction = this.model.waitForContinueToRepeat();

                Platform.runLater(unsafe(() -> {
                    JFXButton again = new JFXButton(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_RUN_AGAIN));
                    JFXButton stop = new JFXButton(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_STOP));
                    JFXDialogLayout content = new JFXDialogLayout();
                    JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);
                    dialog.setOverlayClose(false);
                    content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_CHOOSE_ACTION_FOR_EFFECT)));
                    content.setBody(new Text(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_EFFECT) + ": " + iAction.getDescription()));
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
                    content.setActions(stop, again);
                }));
            }

            return null;
        });
    }

    private void setUpChoosePosition() {
        this.toolCardExecutorService.submit((Callable<Void>) () -> {
            while (!closed) {
                ChoosePositionForLocationRequest choosePosition = this.model.waitForChoosePositionFromLocation();

                Platform.runLater(unsafe(() -> {
                    JFXDialogLayout content = new JFXDialogLayout();
                    Match.dialog = new JFXDialog(outerPane, content, CENTER);
                    Match.dialog.setOverlayClose(false);
                    FXMLLoader loader = new FXMLLoader();
                    Parent node = null;
                    JSONSerializable object = choosePosition.getLocation();
                    Set<Integer> set = choosePosition.getUnavailableLocations();


                    if (object instanceof IWindow) {
                        loader.setLocation(Constants.Resources.WINDOW_VIEW_FXML.getURL());
                        try {
                            node = loader.load();
                            WindowGUIView windowGUIView = loader.getController();
                            windowGUIView.setModel((IWindow) object);
                            windowGUIView.setStatus(Constants.Status.SELECTION_UNLOCKED);
                            for (Integer location : set) {
                                windowGUIView.gridPane.getChildren().get(location).setDisable(true);
                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (object instanceof IDraftPool) {
                        loader.setLocation(Constants.Resources.DRAFTPOOL_VIEW_FXML.getURL());
                        try {
                            node = loader.load();
                            DraftPoolGUIView draftPoolGUIView = loader.getController();
                            draftPoolGUIView.setModel((IDraftPool) object);
                            draftPoolGUIView.setStatus(Constants.Status.SELECTION_UNLOCKED);
                            content.setMinSize(800, 262);
                            for (Integer location : set) {
                                AnchorPane anchorPane = (AnchorPane) draftPoolGUIView.pane.getChildren().get(location);

                                if (!anchorPane.getChildren().isEmpty()) {
                                    anchorPane.getChildren().get(0).setDisable(true);
                                }
                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (object instanceof IRoundTrack) {
                        loader.setLocation(Constants.Resources.ROUNDTRACK_VIEW_FXML.getURL());
                        try {
                            node = loader.load();
                            RoundTrackGUIView roundTrackGUIView = loader.getController();
                            roundTrackGUIView.setModel((IRoundTrack) object);
                            roundTrackGUIView.setStatus(Constants.Status.SELECTION_UNLOCKED);
                            for (Integer location : set) {
                                byte index = (byte) ((location & 0x0000FF00) >> 8);
                                VBox vBox = (VBox) roundTrackGUIView.gridPane.getChildren().get(index);

                                if (vBox.getChildren().size() >= 2) {
                                    vBox.getChildren().get(1).setDisable(true);
                                }
                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    content.setBody(node);
                    content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_CHOOSE_LOCATION) + ": "));
                    Match.dialog.show();
                }));
            }

            return null;
        });
    }

    public void close() {
        this.closed = true;
    }
}
