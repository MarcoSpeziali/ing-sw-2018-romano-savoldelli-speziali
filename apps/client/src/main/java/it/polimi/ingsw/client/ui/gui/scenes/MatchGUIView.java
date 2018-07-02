package it.polimi.ingsw.client.ui.gui.scenes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.*;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.utils.Range;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jfoenix.controls.JFXDialog.DialogTransition.CENTER;
import static it.polimi.ingsw.client.ui.gui.WindowGUIView.Property;
import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;


public class MatchGUIView extends GUIView<MatchController> {

    private Timer timer = new Timer();
    private int remainingTime = -1;

    @FXML
    public StackPane outerPane;

    @FXML
    public HBox hBoxWindows;

    @FXML
    public VBox vBoxToolCard;

    @FXML
    public VBox vBoxObjectiveCard;

    @FXML
    public BorderPane borderPane;

    @FXML
    public BorderPane centerPane;

    public DraftPoolGUIView draftPoolGUIView;

    @FXML
    public Label secondsLabel;

    @Override
    public void init() {
        chooseWindow();
        setUpOpponentsWindowsFuture();
        setUpShadeFuture();
        loadElements();
    }


    private void loadElements() {
        CompletableFuture.supplyAsync(unsafe(() -> this.model.waitForUpdate()))
                .thenAccept(iMatch -> Platform.runLater(unsafe(() -> {
                    //if(this.draftPoolGUIView == null)
                    IRoundTrack iRoundTrack             = iMatch.getRoundTrack();
                    IDraftPool iDraftPool               = iMatch.getDraftPool();
                    IToolCard[] iToolCards              = iMatch.getToolCards();
                    IObjectiveCard[] iObjectiveCards    = iMatch.getPublicObjectiveCards();
                    IObjectiveCard iPrivateObjectiveCard = iMatch.getPrivateObjectiveCard();

                    for (IToolCard iToolCard : iToolCards) { //ToolCard
                        JFXDialogLayout content = new JFXDialogLayout();
                        JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);

                        FXMLLoader toolCardLoader = new FXMLLoader();
                        toolCardLoader.setLocation(Constants.Resources.TOOL_CARD_VIEW_FXML.getURL());
                        Node toolCardNode = toolCardLoader.load();
                        ToolCardGUIView toolCardGUIView = toolCardLoader.getController();
                        toolCardGUIView.setModel(iToolCard);
                        vBoxToolCard.getChildren().add(toolCardNode);
                        vBoxToolCard.setMinHeight(1000);
                        vBoxToolCard.setMaxWidth(200);
                        vBoxToolCard.setSpacing(20);
                        vBoxToolCard.setAlignment(Pos.TOP_CENTER);
                        toolCardNode.setOnMousePressed(event -> dialog.show());


                        JFXButton use = new JFXButton(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_USE_BUTTON));
                        JFXButton cancel = new JFXButton(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_BACK_BUTTON));
                        FXMLLoader loader =  new FXMLLoader();
                        loader.setLocation(Constants.Resources.TOOL_CARD_VIEW_FXML.getURL());
                        Node tc = loader.load();
                        ToolCardGUIView controller = loader.getController();
                        controller.setModel(iToolCard);
                        content.setBody(tc);
                        StackPane.setAlignment(tc, Pos.CENTER);
                        cancel.setOnMousePressed(event -> dialog.close());
                        content.setActions(cancel);

                        use.setOnMousePressed(event -> {

                            try {
                                this.model.requestToolCardUsage(iToolCard);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (RuntimeException e) {
                                JFXDialogLayout content2 = new JFXDialogLayout();
                                JFXDialog dialog2 = new JFXDialog(outerPane, content2, CENTER);
                                JFXButton cancel2 = new JFXButton(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_USE_BUTTON));
                                cancel2.setOnMousePressed(e1 -> dialog2.close());
                                content2.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_NOT_ENOUGH_TOKEN)));
                                content2.setActions(cancel2);
                                dialog2.show();
                            }
                        });

                        cancel.setOnMousePressed(event ->
                                dialog.close()
                        );
                        content.setActions(use, cancel);
                    }

                    for (IObjectiveCard iObjectiveCard : iObjectiveCards) { //ObjectiveCard
                        JFXDialogLayout content = new JFXDialogLayout();
                        JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);

                        FXMLLoader objectiveCardLoader = new FXMLLoader();
                        objectiveCardLoader.setLocation(Constants.Resources.OBJECTIVE_CARD_VIEW_FXML.getURL());
                        Node objectiveCardNode = objectiveCardLoader.load();
                        objectiveCardNode.setOnMousePressed(event -> dialog.show());
                        ObjectiveCardGUIView objectiveCardGUIView = objectiveCardLoader.getController();
                        objectiveCardGUIView.setModel(iObjectiveCard);
                        vBoxObjectiveCard.getChildren().add(objectiveCardNode);
                        vBoxObjectiveCard.setMaxWidth(200);
                        vBoxObjectiveCard.setMinHeight(1000);
                        vBoxObjectiveCard.setSpacing(20);
                        vBoxObjectiveCard.setAlignment(Pos.TOP_CENTER);
                        FXMLLoader loader =  new FXMLLoader();
                        loader.setLocation(Constants.Resources.OBJECTIVE_CARD_VIEW_FXML.getURL());
                        Node oc = loader.load();

                        ObjectiveCardGUIView controller = loader.getController();
                        controller.setModel(iObjectiveCard);content.setBody(oc);
                        content.setAlignment(oc, Pos.CENTER);
                        JFXButton cancel = new JFXButton("Back");
                        cancel.setOnMousePressed(event -> dialog.close());
                        content.setActions(cancel);
                    }

                    JFXDialogLayout content = new JFXDialogLayout();
                    JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);
                    FXMLLoader privateCardLoader = new FXMLLoader();
                    privateCardLoader.setLocation(Constants.Resources.OBJECTIVE_CARD_VIEW_FXML.getURL());
                    Node privateCardNode = privateCardLoader.load();
                    privateCardNode.setOnMousePressed(event -> dialog.show());
                    ObjectiveCardGUIView objectiveCardGUIView = privateCardLoader.getController();
                    objectiveCardGUIView.setModel(iPrivateObjectiveCard);
                    FXMLLoader loader =  new FXMLLoader();
                    loader.setLocation(Constants.Resources.OBJECTIVE_CARD_VIEW_FXML.getURL());
                    Node oc = loader.load();
                    ObjectiveCardGUIView controller = loader.getController();
                    controller.setModel(iPrivateObjectiveCard);
                    content.setBody(oc);
                    content.setAlignment(oc, Pos.CENTER);
                    JFXButton cancel = new JFXButton("Back");
                    cancel.setOnMousePressed(event -> dialog.close());
                    content.setActions(cancel);

                    centerPane.setRight(privateCardNode);
                    centerPane.setAlignment(privateCardNode, Pos.BOTTOM_CENTER);
                    centerPane.setMargin(privateCardNode, new Insets(0,10,220,10));
                    //centerPane.setMargin(privateCardNode, new Insets(10));

                   FXMLLoader draftPoolLoader = new FXMLLoader();
                    draftPoolLoader.setLocation(Constants.Resources.DRAFTPOOL_VIEW_FXML.getURL());
                    Node draftPoolNode = draftPoolLoader.load();
                    DraftPoolGUIView draftPoolGUIView = draftPoolLoader.getController();
                    draftPoolGUIView.setModel(iDraftPool);
                    centerPane.setTop(draftPoolNode);

                    FXMLLoader roundTrackLoader = new FXMLLoader();
                    roundTrackLoader.setLocation(Constants.Resources.ROUNDTRACK_VIEW_FXML.getURL());
                    Node roundTrackNode = roundTrackLoader.load();
                    RoundTrackGUIView roundTrackGUIView = roundTrackLoader.getController();
                    roundTrackGUIView.setModel(iRoundTrack);
                    centerPane.setBottom(roundTrackNode);
                    centerPane.setAlignment(roundTrackNode, Pos.CENTER);
                    centerPane.setMargin(roundTrackNode, new Insets(0,30,50,30));

                })));}

    public void chooseWindow() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(175);
        gridPane.setVgap(140);
        JFXDialogLayout content = new JFXDialogLayout();
        content.setMinSize(870, 750);
        JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);


        CompletableFuture.supplyAsync(unsafe(() -> this.model.waitForWindowRequest()))
                .thenAccept(iWindows -> {
                    Platform.runLater(unsafe(() -> {
                        for (int i = 0; i < iWindows.length; i++) {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(Constants.Resources.WINDOW_VIEW_FXML.getURL());

                                Node window = loader.load();
                                window.setScaleX(1.2);
                                window.setScaleY(1.2);
                                WindowGUIView windowGUIView = loader.getController();
                                windowGUIView.setModel(iWindows[i]);
                                windowGUIView.setProperty(Property.NONE);

                                int finalI = i;

                                window.setOnMouseEntered(event -> window.setCursor(Cursor.HAND));

                                window.setOnMousePressed(event -> {
                                    centerPane.setCenter(window);
                                    window.setScaleY(2.5);
                                    window.setScaleX(2.5);
                                    BorderPane.setMargin(window, new Insets(0, 0, 100, 0));
                                    windowGUIView.setProperty(Property.OWNED);

                                    try {
                                        this.model.respondToWindowRequest(iWindows[finalI]);
                                    }
                                    catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    dialog.close();
                                });

                                gridPane.add(window, i/(iWindows.length/2), i%(iWindows.length/2));

                        }
                        content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_CHOOSE_YOUR_WINDOW)));
                        content.setBody(gridPane);
                        content.setAlignment(Pos.CENTER);
                        gridPane.setAlignment(Pos.CENTER);
                        dialog.show();

                    }
                ));
                });
    }
    
    private void setUpOpponentsWindows(IMatch update) {
        Platform.runLater(() -> {
            for (ILivePlayer player: update.getPlayers()) {

                IWindow iWindow = player.getWindow();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Constants.Resources.WINDOW_VIEW_FXML.getURL());
                try {
                    Node node = loader.load();
                    VBox vBox = new VBox();
                    Label label = new Label(player.hasLeft()? player.getPlayer().getUsername()+" (Offline)":
                            player.getPlayer().getUsername());
                    label.setAlignment(Pos.CENTER);
                    label.setStyle("-fx-font-size: 14; -fx-font-weight: bold");
                    vBox.getChildren().addAll(node, label);
                    WindowGUIView windowGUIView = loader.getController();
                    windowGUIView.setModel(iWindow);
                    windowGUIView.setProperty(Property.OPPONENT);
                    hBoxWindows.setAlignment(Pos.TOP_CENTER);
                    hBoxWindows.getChildren().add(vBox);
                    hBoxWindows.setSpacing(30);
                    hBoxWindows.setAlignment(Pos.CENTER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //setUpOpponentWindowsFuture();
        });
    }

    private void setUpOpponentsWindowsFuture() {
        CompletableFuture.supplyAsync(unsafe(() -> this.model.waitForUpdate()))
                .thenAccept(this::setUpOpponentsWindows);
    }

    private void setUpShade(IDie iDie) {
        Platform.runLater(unsafe(() -> {
            AtomicInteger shade = new AtomicInteger();
            JFXDialogLayout content = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);
            JFXButton confirm = new JFXButton("OK");
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
                    shade.set(finalI);
                });
            }
            try {
                confirm.setOnMousePressed(event -> {
                    try {
                        this.model.postSetShade(shade.get());
                    }
                    catch (IOException e) {
                        FunctionalExceptionWrapper.wrap(e);
                    }
                    dialog.close();
                });
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            hBox.getChildren().addAll(die, gridPane);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setSpacing(10);
            content.setActions();
            content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_CHOOSE_SHADE_FOR_DIE)));
            content.setBody(hBox);
            content.setActions(confirm);
            gridPane.setScaleX(0.75);
            gridPane.setScaleY(0.75);
            dialog.show();
            //setUpSetShadeFuture();
            }));
    }

    private void setUpEffect(Map.Entry<IEffect[], Range<Integer>> map){
        Platform.runLater(unsafe(()-> {

        }));
    }


    private void setUpShadeFuture() {
        CompletableFuture.supplyAsync(unsafe(() -> this.model.waitForSetShade()))
                .thenAccept(this::setUpShade);
    }

    private void setUpEffectFuture() {
        CompletableFuture.supplyAsync(unsafe(() ->
            this.model.waitForChooseBetweenEffect())).thenAccept(this::setUpEffect);
    }

    private void setUpEffect(){}

    private void setUpContinueToRepeatFuture() {
        CompletableFuture.supplyAsync(unsafe(()-> this.model.waitForContinueToRepeat()))
                .thenAccept(this::setUpContinueToRepeat);
    }

    private void setUpContinueToRepeat(IEffect iEffect) {
        Platform.runLater(unsafe(()-> {
            JFXButton again = new JFXButton(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_RUN_AGAIN));
            JFXButton stop = new JFXButton(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_STOP));
            JFXDialogLayout content = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);
            content.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_CHOOSE_ACTION_FOR_EFFECT)));
            content.setBody(new Text(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_EFFECT)+": "+iEffect.getDescriptionKey()));
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

    private void setUpChoosePositionFuture() {
        CompletableFuture.supplyAsync(unsafe(() -> this.model.waitForChoosePositionFromLocation()))
                .thenAccept(this::setUpChoosePosition);
    }

    private void setUpChoosePosition(Map.Entry<JSONSerializable,Set<Integer>> jsonSerializableSetEntry) {
        Platform.runLater(unsafe(() -> {
            JFXDialog dialog = new JFXDialog();
            JFXDialogLayout content = new JFXDialogLayout();
            JFXButton button = new JFXButton("OK");
            FXMLLoader loader = new FXMLLoader();
            GridPane node = null;
            JSONSerializable object = jsonSerializableSetEntry.getKey();
            Set<Integer> set = jsonSerializableSetEntry.getValue();

            if (object instanceof IWindow) {
                loader.setLocation(Constants.Resources.WINDOW_VIEW_FXML.getURL());
                try {
                    node = loader.load();
                    WindowGUIView windowGUIView = loader.getController();
                    windowGUIView.setModel((IWindow) object);
                    windowGUIView.setProperty(Property.SELECTION);
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
        }));
    }

    private void setUpWaitForTurnToBeginFuture() {
        CompletableFuture.supplyAsync(unsafe(()-> this.model.waitForTurnToBegin()))
                .thenAccept(this::setUpWaitForTurnToBegin);
    }

    private void setUpWaitForTurnToBegin(int remainingTime) {
        Platform.runLater(unsafe(() -> {
            this.remainingTime = remainingTime;
            startTimer();

                }
        ));
    }


    public void startTimer() {
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    MatchGUIView.this.secondsLabel.setText(String.valueOf(remainingTime));
                    remainingTime--;
                });
            }
        }, 0, 1000);
    }
}

