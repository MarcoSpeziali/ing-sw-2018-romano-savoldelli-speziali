package it.polimi.ingsw.client.ui.gui.scenes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.ui.gui.*;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.core.Player;
import it.polimi.ingsw.net.mocks.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.jfoenix.controls.JFXDialog.DialogTransition.CENTER;
//import static it.polimi.ingsw.client.ui.gui.WindowGUIView.Property;
import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;


public class MatchGUIView extends GUIView<MatchController> {

    private Timer timer = new Timer();
    private int remainingTime = -1;

    @FXML
    public HBox bottomBar;

    @FXML
    public JFXButton endTurnButton;

    @FXML
    public VBox turnBox;

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
    public WindowGUIView windowGUIView;
    public List<Node> toolCardNodes = new LinkedList<>();

    @FXML
    public Label secondsLabel;

    @Override
    public void init() {
        chooseWindow();
        // setUpOpponentsWindowsFuture();
        loadElements();
        MatchGUIViewToolcardHelper helper = new MatchGUIViewToolcardHelper(this.model, outerPane);
        //helper.init();
        setUpWaitForTurnToBeginFuture();
        setUpWaitForTurnToEndFuture();
        setUpWaitForMatchToEnd();
    }

    private void loadElements() {
        CompletableFuture.supplyAsync(unsafe(() -> this.model.waitForUpdate()))
                .thenAccept(iMatch -> Platform.runLater(unsafe(() -> {
                    IRoundTrack iRoundTrack             = iMatch.getRoundTrack();
                    IDraftPool iDraftPool               = iMatch.getDraftPool();
                    IToolCard[] iToolCards              = iMatch.getToolCards();
                    IObjectiveCard[] iObjectiveCards    = iMatch.getPublicObjectiveCards();
                    IObjectiveCard iPrivateObjectiveCard = iMatch.getPrivateObjectiveCard();

                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setRadius(5.0);
                    dropShadow.setOffsetX(3.0);
                    dropShadow.setOffsetY(3.0);
                    dropShadow.setColor(Color.color(0.4, 0.5, 0.5));

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
                        toolCardNode.setCursor(Cursor.HAND);
                        //toolCardNode.setDisable(true);
                        toolCardNode.setEffect(dropShadow);
                        toolCardNodes.add(toolCardNode);

                        JFXButton use = new JFXButton(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_USE_BUTTON));
                        JFXButton cancel = new JFXButton(Constants.Strings.toLocalized(Constants.Strings.MATCH_GUI_BACK_BUTTON));
                        content.setMinSize(350, 500);
                        FXMLLoader loader =  new FXMLLoader();
                        loader.setLocation(Constants.Resources.TOOL_CARD_VIEW_FXML.getURL());
                        Node tc = loader.load();
                        ToolCardGUIView controller = loader.getController();
                        controller.setModel(iToolCard);
                        content.setBody(tc);
                        tc.setScaleX(1.6); //FIXME io le ingrandirei, sia queste che le obj card
                        tc.setScaleY(1.6);
                        StackPane.setAlignment(tc, Pos.CENTER);
                        cancel.setOnMousePressed(event -> dialog.close());
                        content.setActions(cancel);

                        use.setOnMousePressed(event -> {

                            try {
                                this.model.requestToolCardUsage(iToolCard);
                                dialog.close();
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
                        objectiveCardNode.setCursor(Cursor.HAND);
                        objectiveCardNode.setEffect(dropShadow);
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
                        StackPane.setAlignment(oc, Pos.CENTER);
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
                    privateCardNode.setCursor(Cursor.HAND);
                    privateCardNode.setEffect(dropShadow);
                    ObjectiveCardGUIView objectiveCardGUIView = privateCardLoader.getController();
                    objectiveCardGUIView.setModel(iPrivateObjectiveCard);
                    FXMLLoader loader =  new FXMLLoader();
                    loader.setLocation(Constants.Resources.OBJECTIVE_CARD_VIEW_FXML.getURL());
                    Node oc = loader.load();
                    ObjectiveCardGUIView controller = loader.getController();
                    controller.setModel(iPrivateObjectiveCard);
                    content.setBody(oc);
                    StackPane.setAlignment(oc, Pos.CENTER);
                    JFXButton cancel = new JFXButton("Back");
                    cancel.setOnMousePressed(event -> dialog.close());
                    content.setActions(cancel);

                    centerPane.setRight(privateCardNode);
                    BorderPane.setAlignment(privateCardNode, Pos.BOTTOM_CENTER);
                    BorderPane.setMargin(privateCardNode, new Insets(0,10,220,10));

                    FXMLLoader draftPoolLoader = new FXMLLoader();
                    draftPoolLoader.setLocation(Constants.Resources.DRAFTPOOL_VIEW_FXML.getURL());
                    Node draftPoolNode = draftPoolLoader.load();
                    DraftPoolGUIView draftPoolGUIView = draftPoolLoader.getController();
                    draftPoolGUIView.setModel(iDraftPool);
                    draftPoolGUIView.setProperty(Constants.Property.OWNED);
                    centerPane.setTop(draftPoolNode);
                    BorderPane.setAlignment(draftPoolNode, Pos.BOTTOM_CENTER);

                    FXMLLoader roundTrackLoader = new FXMLLoader();
                    roundTrackLoader.setLocation(Constants.Resources.ROUNDTRACK_VIEW_FXML.getURL());
                    Node roundTrackNode = roundTrackLoader.load();
                    RoundTrackGUIView roundTrackGUIView = roundTrackLoader.getController();
                    roundTrackGUIView.setModel(iRoundTrack);
                    bottomBar.getChildren().add(roundTrackNode);

                    this.setUpOpponentsWindows(iMatch);
                })));
    }

    public void chooseWindow() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(75);
        gridPane.setVgap(65);
        JFXDialogLayout content = new JFXDialogLayout();
        content.setMinSize(650, 420);
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
                                windowGUIView = loader.getController();
                                windowGUIView.setModel(iWindows[i]);
                                windowGUIView.setProperty(Constants.Property.OWNED);

                                int finalI = i;

                                window.setOnMouseEntered(event -> window.setCursor(Cursor.HAND));

                                window.setOnMousePressed(event -> {
                                    centerPane.setCenter(window);
                                    window.setScaleY(1.7);
                                    window.setScaleX(1.7);
                                    windowGUIView.setProperty(Constants.Property.OWNED);

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
            int i = 0;
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
                    label.setStyle("-fx-font-size: 12; -fx-font-weight: bold");
                    vBox.getChildren().addAll(node, label);
                    WindowGUIView windowGUIView = loader.getController();
                    windowGUIView.setModel(iWindow);
                    windowGUIView.setProperty(Constants.Property.OPPONENT);
                    hBoxWindows.setAlignment(Pos.TOP_CENTER);
                    hBoxWindows.getChildren().add(vBox); //TODO check
                    if(hBoxWindows.getChildren().get(i)!=null){
                        hBoxWindows.getChildren().remove(i);
                        hBoxWindows.getChildren().add(vBox);
                    }

                    hBoxWindows.setSpacing(100);
                    hBoxWindows.setAlignment(Pos.TOP_CENTER);
                    HBox.setMargin(vBox, new Insets(0, 0, 0, 0));
                    i++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // setUpOpponentsWindowsFuture();
        });
    }

    private void setUpOpponentsWindowsFuture() {
        CompletableFuture.supplyAsync(unsafe(() -> this.model.waitForUpdate()))
                .thenAccept(this::setUpOpponentsWindows);
    }

    public void onEndTurnClicked() throws IOException {
        this.model.endTurn();
        this.timer.cancel();
    }

    private void setUpWaitForTurnToBeginFuture() {
        CompletableFuture.supplyAsync(unsafe(()-> this.model.waitForTurnToBegin()))
                .thenAccept(this::setUpWaitForTurnToBegin);
    }

    private void setUpWaitForTurnToBegin(int remainingTime) {
        Platform.runLater(unsafe(() -> {
            this.remainingTime = remainingTime;
            startTimer();
            windowGUIView.setProperty(Constants.Property.OWNED);
            draftPoolGUIView.setProperty(Constants.Property.OWNED);
            for(Node node: toolCardNodes) {
                node.setDisable(false);
            }
            endTurnButton.setDisable(false);
            //endTurnButton.setOnMouseClicked();
            setUpWaitForTurnToBeginFuture();
            }
        ));

    }

    private void startTimer() {
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

    private void setUpWaitForTurnToEndFuture() {
        CompletableFuture.runAsync(unsafe(() -> this.model.waitForTurnToEnd()))
                .thenAccept(this::setUpWaitForTurnToEnd);
    }

    private void setUpWaitForTurnToEnd(Void aVoid) {
        Platform.runLater(unsafe(() -> {
            this.timer.cancel();
            windowGUIView.setProperty(Constants.Property.NONE);
            draftPoolGUIView.setProperty(Constants.Property.NONE);
            for(Node node: toolCardNodes) {
                node.setDisable(true);
            }
            setUpWaitForTurnToEndFuture();
        }));
    }

    private void setUpWaitForMatchToEnd() {
        CompletableFuture.supplyAsync(unsafe(()-> this.model.waitForMatchToEnd()))
                .thenAccept(iResults -> Platform.runLater(unsafe(() -> {
                    IResult[] sortedResults = Arrays.stream(iResults)
                            .sorted(Comparator.comparing(IResult::getPoints))
                            .toArray(IResult[]::new);

                    FXMLLoader loader = new FXMLLoader();
                    Parent root = loader.load();
                    ResultsGUIView resultsGUIView = loader.getController();
                    String currentPlayerName = Player.getCurrentPlayer().getUsername();

                    for (IResult result : sortedResults) {
                        Label label = new Label(result.getPlayer().getUsername() + " " + result.getPoints());
                        label.setStyle("-fx-alignment: CENTER; " +
                                "-fx-font-size: 14+" +
                                (result.getPlayer().getUsername().equals(currentPlayerName)?
                                        "-fx-font-weight: bold;":";"));
                        resultsGUIView.resultsListView.getItems().add(label);
                    }
                    if (sortedResults[0].getPlayer().getUsername().equals(currentPlayerName)) {
                        resultsGUIView.winningMessage.setDisable(false);
                    }

                    SagradaGUI.showStage(root, 353, 546);
                })));
    }
}

