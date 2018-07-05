package it.polimi.ingsw.client.ui.gui.scenes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.Match;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.ui.gui.*;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.jfoenix.controls.JFXDialog.DialogTransition.CENTER;
import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;
import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.wrap;

public class MatchGUIView extends GUIView<MatchController> implements Initializable {

    @FXML
    public JFXButton endTurnButton;

    private Timer timer = new Timer();
    private int remainingTime = -1;

    @FXML
    public HBox bottomBar;

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

    @FXML
    public VBox turnBox;

    private DraftPoolGUIView draftPoolGUIView;
    private WindowGUIView ownedWindowGUIView;
    private WindowGUIView[] opponentsWindowsGUIViews;
    private RoundTrackGUIView roundTrackGUIView;
    private ToolCardGUIView[] toolCardGUIViews;
    private ObjectiveCardGUIView[] publicObjectiveCardGUIViews;
    private ObjectiveCardGUIView privateObjectiveCardGUIView;

    private MatchGUIViewToolCardHelper helper;

    private ILivePlayer currentPlayer;

    private ExecutorService matchExecutorService = Executors.newFixedThreadPool(10);

    public List<Node> toolCardNodes = new LinkedList<>();
    @FXML
    public Label timerLabel = new Label("00");

    private Label[] playerLabel;


    @Override
    public void init() {
        Match.setOuterPane(outerPane);
        chooseWindow();
        loadElementsFuture();

        helper = new MatchGUIViewToolCardHelper(this.model, outerPane);
        
        setUpWaitForTurnToBegin();
        setUpWaitForMatchToEnd();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        endTurnButton.setText(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_END_TURN));
        turnBox.setVisible(false);
    }

    private final Object updateSyncObject = new Object();

    private void loadElements(IMatch iMatch) {
        Platform.runLater(unsafe(() -> {
            synchronized (updateSyncObject) {
                loadRoundTrack(iMatch.getRoundTrack());
                loadDraftPool(iMatch.getDraftPool());
                loadToolCards(iMatch.getToolCards());
                loadPrivateObjectiveCard(iMatch.getPrivateObjectiveCard());
                loadPublicObjectiveCards(iMatch.getPublicObjectiveCards());
                loadOpponentsWindows(iMatch.getPlayers());
                loadOwnedWindow(iMatch.getCurrentPlayer().getWindow());

                this.currentPlayer = iMatch.getCurrentPlayer();

                updateSyncObject.notifyAll();
            }
        }));
    }

    private void loadElementsFuture() {
        matchExecutorService.submit(() -> {
            synchronized (updateSyncObject) {
                while (true) {
                    IMatch match = this.model.waitForUpdate();
                    loadElements(match);

                    updateSyncObject.wait();
                }
            }
        });
    }

    public void chooseWindow() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(75);
        gridPane.setVgap(65);
        JFXDialogLayout content = new JFXDialogLayout();
        content.setMinSize(650, 420);
        JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);
        dialog.setOverlayClose(false);

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
                                    windowGUIView.setStatus(Constants.Status.GAME_LOCKED);

                                    int finalI = i;

                                    window.setOnMouseEntered(event -> window.setCursor(Cursor.HAND));

                                    window.setOnMousePressed(event -> {

                                        try {

                                            this.loadOwnedWindow(iWindows[finalI]);
                                            this.model.respondToWindowRequest(iWindows[finalI]);
                                        }
                                        catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        dialog.close();
                                    });

                                    gridPane.add(window, i / (iWindows.length / 2), i % (iWindows.length / 2));

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

    public void onEndTurnClicked() throws IOException {
        this.model.endTurn();
        this.timer.cancel();
    }

    private void setUpWaitForTurnToBegin() {
        this.matchExecutorService.submit((Callable<Void>) () -> {
            this.remainingTime = this.model.waitForTurnToBegin();

            Match.performedAction = 0b00;

            Platform.runLater(unsafe(() -> {
                        turnBox.setVisible(true);
                        startTimer();
                        ownedWindowGUIView.setStatus(Constants.Status.OWNER_UNLOCKED);
                        draftPoolGUIView.setStatus(Constants.Status.OWNER_UNLOCKED);

                        for (Node node : toolCardNodes) {
                            node.setDisable(false);
                        }
                    }
            ));

            setUpWaitForTurnToEnd();

            return null;
        });
    }

    private void startTimer() {
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> MatchGUIView.this.timerLabel.setText(String.valueOf(remainingTime--)));
            }
        }, 0, 1000);
    }

    private void setUpWaitForTurnToEnd() {
        this.matchExecutorService.submit((Callable<Void>) () -> {
            this.model.waitForTurnToEnd();

            turnBox.setVisible(false);

            this.timer.cancel();

            Match.performedAction = 0;

            this.timer.cancel();
            ownedWindowGUIView.setStatus(Constants.Status.GAME_LOCKED);
            draftPoolGUIView.setStatus(Constants.Status.GAME_LOCKED);

            for (Node node : toolCardNodes) {
                node.setDisable(true);
            }

            setUpWaitForTurnToBegin();

            return null;
        });
    }

    private void setUpWaitForMatchToEnd() {
        this.matchExecutorService.submit((Callable<Void>) () -> {
            IResult[] results = Arrays.stream(this.model.waitForMatchToEnd())
                    .sorted(Comparator.comparing(IResult::getPoints))
                    .toArray(IResult[]::new);

            try {
                Platform.runLater(wrap(() -> {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Constants.Resources.RESULTS_FXML.getURL());
                    Parent root = loader.load();
                    ResultsGUIView resultsGUIView = loader.getController();
                    String currentPlayerName = this.currentPlayer.getPlayer().getUsername();

                    for (IResult result : results) {
                        Label label = new Label(this.currentPlayer.getPlayer().getUsername() + " " + result.getPoints());
                        label.setStyle("-fx-alignment: CENTER; " +
                                "-fx-font-size: 14+" +
                                (result.getPlayer().getUsername().equals(currentPlayerName) ?
                                        "-fx-font-weight: bold;" : ";"));
                        resultsGUIView.resultsListView.getItems().add(label);
                    }

                    if (results[0].getPlayer().getUsername().equals(currentPlayerName)) {
                        resultsGUIView.winningMessage.setDisable(false);
                    }

                    try {
                        this.model.close();
                    }
                    catch (Exception ignoredAsItShouldRaise) {
                    }

                    SagradaGUI.showStage(root, 910, 720);
                }));
            }
            catch (FunctionalExceptionWrapper e) {
                e.tryFinalUnwrap(IOException.class);
            }

            return null;
        });
    }


    private void loadDraftPool(IDraftPool iDraftPool) throws IOException {
        if (draftPoolGUIView == null) {
            FXMLLoader draftPoolLoader = new FXMLLoader();
            draftPoolLoader.setLocation(Constants.Resources.DRAFTPOOL_VIEW_FXML.getURL());
            Node draftPoolNode = draftPoolLoader.load();
            draftPoolGUIView = draftPoolLoader.getController();

            draftPoolGUIView.setStatus(Constants.Status.GAME_LOCKED);
            centerPane.setTop(draftPoolNode);
            BorderPane.setAlignment(draftPoolNode, Pos.BOTTOM_CENTER);
            BorderPane.setMargin(draftPoolNode, new Insets(300, 100, 0, 100));
        }

        draftPoolGUIView.setModel(iDraftPool);
    }

    private void loadRoundTrack(IRoundTrack iRoundTrack) throws IOException {
        if (roundTrackGUIView == null) {
            FXMLLoader roundTrackLoader = new FXMLLoader();
            roundTrackLoader.setLocation(Constants.Resources.ROUNDTRACK_VIEW_FXML.getURL());
            Node roundTrackNode = roundTrackLoader.load();
            roundTrackGUIView = roundTrackLoader.getController();

            bottomBar.getChildren().add(roundTrackNode);
            BorderPane.setAlignment(bottomBar, Pos.TOP_CENTER);
            HBox.setMargin(roundTrackNode, new Insets(0, 0, 300, 0));
        }

        roundTrackGUIView.setModel(iRoundTrack);
    }

    private void loadOpponentsWindows(ILivePlayer[] players) throws IOException {
        if (opponentsWindowsGUIViews == null) {
            opponentsWindowsGUIViews = new WindowGUIView[players.length];

            this.playerLabel = new Label[players.length];

            for (int i = 0; i < players.length; i++) {

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Constants.Resources.WINDOW_VIEW_FXML.getURL());
                Node node = loader.load();
                VBox vBox = new VBox();
                playerLabel[i] = new Label();
                playerLabel[i].setAlignment(Pos.CENTER);
                playerLabel[i].setStyle("-fx-font-size: 12; -fx-font-weight: bold");
                vBox.getChildren().addAll(node, playerLabel[i]);
                opponentsWindowsGUIViews[i] = loader.getController();
                opponentsWindowsGUIViews[i].setStatus(Constants.Status.OPPONENT_LOCKED);
                hBoxWindows.setAlignment(Pos.TOP_CENTER);
                hBoxWindows.getChildren().add(vBox);

                hBoxWindows.setSpacing(100);
                hBoxWindows.setAlignment(Pos.TOP_CENTER);
                HBox.setMargin(vBox, new Insets(10, 0, 0, 0));
            }
        }


        for (int i = 0; i < players.length; i++) {
            ILivePlayer player = players[i];

            opponentsWindowsGUIViews[i].setModel(player.getWindow());
            playerLabel[i].setText(player.hasLeft() ? player.getPlayer().getUsername() + " (Offline)" :
                    player.getPlayer().getUsername());
        }
    }

    private void loadOwnedWindow(IWindow iWindow) throws IOException {
        if (ownedWindowGUIView == null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Constants.Resources.WINDOW_VIEW_FXML.getURL());
            Node window = loader.load();
            ownedWindowGUIView = loader.getController();

            ownedWindowGUIView.setStatus(Constants.Status.GAME_LOCKED);
            centerPane.setCenter(window);
            window.setScaleY(1.7);
            window.setScaleX(1.7);
            BorderPane.setMargin(window, new Insets(10, 300, 5, 200));
        }

        ownedWindowGUIView.setModel(iWindow);
    }

    private void loadToolCards(IToolCard[] iToolCards) throws IOException {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));

        if (toolCardGUIViews == null) {
            toolCardGUIViews = new ToolCardGUIView[iToolCards.length];

            for (int i = 0; i < iToolCards.length; i++) {
                JFXDialogLayout content = new JFXDialogLayout();
                JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);
                dialog.setOverlayClose(false);

                FXMLLoader toolCardLoader = new FXMLLoader();
                toolCardLoader.setLocation(Constants.Resources.TOOL_CARD_VIEW_FXML.getURL());
                Node toolCardNode = toolCardLoader.load();
                toolCardGUIViews[i] = toolCardLoader.getController();
                toolCardGUIViews[i].setModel(iToolCards[i]);
                vBoxToolCard.getChildren().add(toolCardNode);
                vBoxToolCard.setMinHeight(1000);
                vBoxToolCard.setMaxWidth(200);
                vBoxToolCard.setSpacing(20);
                vBoxToolCard.setAlignment(Pos.TOP_CENTER);
                toolCardNode.setOnMousePressed(event -> dialog.show());
                toolCardNode.setCursor(Cursor.HAND);
                toolCardNode.setDisable(true);
                toolCardNode.setEffect(dropShadow);
                toolCardNodes.add(toolCardNode);

                JFXButton use = new JFXButton(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_USE_BUTTON));
                JFXButton cancel = new JFXButton(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_BACK_BUTTON));
                content.setMinSize(350, 500);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Constants.Resources.TOOL_CARD_VIEW_FXML.getURL());
                Node tc = loader.load();
                ToolCardGUIView controller = loader.getController();
                controller.setModel(iToolCards[i]);
                content.setBody(tc);
                tc.setScaleX(1.6);
                tc.setScaleY(1.6);
                StackPane.setAlignment(tc, Pos.CENTER);
                cancel.setOnMousePressed(event -> dialog.close());
                content.setActions(cancel);

                int finalI = i;
                try {
                    use.setOnMousePressed(event -> {

                        JFXDialogLayout content2 = new JFXDialogLayout();
                        JFXDialog dialog2 = new JFXDialog(outerPane, content2, CENTER);

                        if ((Match.performedAction & 0b10) != 0b10) {
                            helper.init();
                            this.matchExecutorService.submit((Callable<Void>) () -> {
                                try {
                                    this.model.requestToolCardUsage(iToolCards[finalI]);

                                    dialog.close();
                                    helper.close();

                                    Match.performedAction |= 0b10;
                                }
                                catch (IOException e) {
                                    FunctionalExceptionWrapper.wrap(e);
                                }
                                catch (RuntimeException e) {
                                    Platform.runLater(() -> {
                                        JFXButton cancel2 = new JFXButton(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_USE_BUTTON));
                                        cancel2.setOnMousePressed(e1 -> dialog2.close());
                                        content2.setHeading(new Text(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_NOT_ENOUGH_TOKEN)));
                                        content2.setActions(cancel2);
                                        dialog2.show();
                                    });
                                }

                                return null;
                            });
                        }
                        else {
                            content2.setHeading(new Text("You cannot perform the same action twice."));
                        }
                    });
                }
                catch (FunctionalExceptionWrapper e) {
                    e.tryFinalUnwrap(IOException.class);
                }

                cancel.setOnMousePressed(event ->
                        dialog.close()
                );
                content.setActions(use, cancel);
            }
        }
    }

    private void loadPublicObjectiveCards(IObjectiveCard[] iObjectiveCards) throws IOException {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));

        if (publicObjectiveCardGUIViews == null) {
            publicObjectiveCardGUIViews = new ObjectiveCardGUIView[iObjectiveCards.length];
        }

        for (int i = 0; i < iObjectiveCards.length; i++) {
            if (publicObjectiveCardGUIViews[i] == null) {
                JFXDialogLayout content = new JFXDialogLayout();
                JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);
                dialog.setOverlayClose(false);

                FXMLLoader objectiveCardLoader = new FXMLLoader();
                objectiveCardLoader.setLocation(Constants.Resources.OBJECTIVE_CARD_VIEW_FXML.getURL());
                Node objectiveCardNode = objectiveCardLoader.load();
                objectiveCardNode.setOnMousePressed(event -> dialog.show());
                publicObjectiveCardGUIViews[i] = objectiveCardLoader.getController();
                publicObjectiveCardGUIViews[i].setModel(iObjectiveCards[i]);
                objectiveCardNode.setCursor(Cursor.HAND);
                objectiveCardNode.setEffect(dropShadow);
                vBoxObjectiveCard.getChildren().add(objectiveCardNode);
                vBoxObjectiveCard.setMaxWidth(200);
                vBoxObjectiveCard.setMinHeight(1000);
                vBoxObjectiveCard.setSpacing(20);
                vBoxObjectiveCard.setAlignment(Pos.TOP_CENTER);

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Constants.Resources.OBJECTIVE_CARD_VIEW_FXML.getURL());
                Node oc = loader.load();
                ObjectiveCardGUIView controller = loader.getController();
                controller.setModel(iObjectiveCards[i]);
                content.setBody(oc);
                StackPane.setAlignment(oc, Pos.CENTER);
                oc.setScaleY(1.6);
                oc.setScaleX(1.6);
                content.setMinSize(350, 500);
                JFXButton cancel = new JFXButton(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_BACK_BUTTON));
                cancel.setOnMousePressed(event -> dialog.close());
                content.setActions(cancel);
            }
        }
    }

    private void loadPrivateObjectiveCard(IObjectiveCard objectiveCard) throws IOException {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));

        if (privateObjectiveCardGUIView == null) {
            JFXDialogLayout content = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);
            dialog.setOverlayClose(false);
            FXMLLoader privateCardLoader = new FXMLLoader();
            privateCardLoader.setLocation(Constants.Resources.OBJECTIVE_CARD_VIEW_FXML.getURL());
            Node privateCardNode = privateCardLoader.load();
            privateCardNode.setOnMousePressed(event -> dialog.show());
            privateCardNode.setCursor(Cursor.HAND);
            privateCardNode.setEffect(dropShadow);
            privateObjectiveCardGUIView = privateCardLoader.getController();
            privateObjectiveCardGUIView.setModel(objectiveCard);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Constants.Resources.OBJECTIVE_CARD_VIEW_FXML.getURL());
            Node oc = loader.load();
            ObjectiveCardGUIView controller = loader.getController();
            controller.setModel(objectiveCard);
            content.setBody(oc);
            StackPane.setAlignment(oc, Pos.CENTER);
            JFXButton cancel = new JFXButton("Back");
            cancel.setOnMousePressed(event -> dialog.close());
            content.setActions(cancel);
            centerPane.setRight(privateCardNode);
            BorderPane.setAlignment(privateCardNode, Pos.BOTTOM_CENTER);
            BorderPane.setMargin(privateCardNode, new Insets(0, 10, 220, 10));
        }
    }

    @FXML
    public void onCloseClicked() throws IOException {
        this.model.close();
        System.exit(0);
    }
}


