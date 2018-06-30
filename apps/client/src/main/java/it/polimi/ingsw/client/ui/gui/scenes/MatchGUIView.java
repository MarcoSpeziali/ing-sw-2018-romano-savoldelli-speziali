package it.polimi.ingsw.client.ui.gui.scenes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.*;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.controllers.NotEnoughTokensException;
import it.polimi.ingsw.net.mocks.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jfoenix.controls.JFXDialog.DialogTransition.CENTER;
import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;
import static it.polimi.ingsw.client.ui.gui.WindowGUIView.Property;


public class MatchGUIView extends GUIView<MatchController> {

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


    @Override
    public void init() {
        chooseWindow();
        //setUpUpdateFuture();
        setUpSetShadeFuture();
        loadElements();
    }


    private void loadElements() {
        CompletableFuture.supplyAsync(unsafe(() -> this.model.waitForUpdate()))
                .thenAccept(iMatch -> Platform.runLater(unsafe(() -> {

                   // IRoundTrack iRoundTrack             = iMatch.getRoundTrack();
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
                        toolCardNode.setOnMousePressed(event -> dialog.show());
                        ToolCardGUIView toolCardGUIView = toolCardLoader.getController();
                        //toolCardGUIView.setModel(iToolCard);
                        vBoxToolCard.getChildren().add(toolCardNode);

                        content.setBody(toolCardNode);

                        JFXButton use = new JFXButton("Use");
                        JFXButton cancel = new JFXButton("Back");

                        use.setOnMousePressed(event -> {

                            try {
                                this.model.requestToolCardUsage(iToolCard);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (NotEnoughTokensException e) {
                                JFXDialogLayout content2 = new JFXDialogLayout();
                                JFXDialog dialog2 = new JFXDialog(outerPane, content2, CENTER);
                                JFXButton cancel2 = new JFXButton("Use");
                                cancel2.setOnMousePressed(e1 -> dialog2.close());
                                content2.setHeading(new Text("Non possiedi abbastanza token per attivare l'effetto")); // TODO: settare con costanti
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
                        content.setBody(objectiveCardNode);
                        JFXButton cancel = new JFXButton("Back");
                        cancel.setOnMousePressed(event -> dialog.close());
                        content.setActions(cancel);
                    }

                    FXMLLoader privateCardLoader = new FXMLLoader();
                    privateCardLoader.setLocation(Constants.Resources.OBJECTIVE_CARD_VIEW_FXML.getURL());
                    Node privateCardNode = privateCardLoader.load();
                    ObjectiveCardGUIView objectiveCardGUIView = privateCardLoader.getController();
                    objectiveCardGUIView.setModel(iPrivateObjectiveCard);
                    borderPane.setCenter(privateCardNode); // FIXME: non va messa con questo layout!

                    FXMLLoader draftPoolLoader = new FXMLLoader();
                    draftPoolLoader.setLocation(Constants.Resources.DRAFTPOOL_VIEW_FXML.getURL());
                    Node draftPoolNode = draftPoolLoader.load();
                    DraftPoolGUIView draftPoolGUIView = draftPoolLoader.getController();
                    draftPoolGUIView.setModel(iDraftPool);
                    borderPane.setCenter(draftPoolNode);

                })));}

    public void chooseWindow() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        JFXDialogLayout content = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);

        CompletableFuture.supplyAsync(unsafe(() -> this.model.waitForWindowRequest()))
                .thenAccept(iWindows -> {
                    Platform.runLater(unsafe(() -> {
                        for (int i = 0; i < iWindows.length; i++) {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(Constants.Resources.WINDOW_VIEW_FXML.getURL());

                                Node window = loader.load();
                                WindowGUIView windowGUIView = loader.getController();

                                windowGUIView.setModel(iWindows[i]);
                                windowGUIView.setProperty(Property.NONE);

                                int finalI = i;

                                window.setOnMouseEntered(event -> window.setCursor(Cursor.HAND));

                                window.setOnMousePressed(event -> {
                                    borderPane.setCenter(window);
                                    window.setOnMouseEntered(e -> window.setCursor(Cursor.DEFAULT));
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
                        content.setHeading(new Text("Choose your Window."));
                        content.setBody(gridPane);
                        content.setAlignment(Pos.CENTER);
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
                    node.setScaleX(0.5); // FIXME come facciamo?
                    node.setScaleY(0.5);
                    VBox vBox = new VBox();
                    Label label = new Label("ciao");
                    label.setAlignment(Pos.CENTER);
                    label.setStyle("-fx-font-size: 14; -fx-font-weight: bold");
                    vBox.getChildren().addAll(node, label);

                    WindowGUIView windowGUIView = loader.getController();
                    windowGUIView.setModel(iWindow);
                    windowGUIView.setProperty(Property.OPPONENT);

                    hBoxWindows.setMaxHeight(20);
                    hBoxWindows.setAlignment(Pos.CENTER);
                    hBoxWindows.getChildren().add(node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //setUpUpdateFuture();
        });
    }

    private void setUpUpdateFuture() {
        CompletableFuture.supplyAsync(unsafe(() -> this.model.waitForUpdate()))
                .thenAccept(this::setUpOpponentsWindows);
    }

    private void setDieShade(IDie iDie) {
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
                CellGUIView cellGUIView = innerLoader.getController();
                cellGUIView.setModel(new CellMock(null, i+1));
                gridPane.add(cell, i%3, i/3);
                gridPane.setVgap(10);
                gridPane.setHgap(10);
                int finalI = i+1;
                cell.setOnMouseClicked(event -> {
                    try {
                        dieGUIView.setModel(new DieMock(finalI, iDie.getColor(), 0));
                        shade.set(finalI);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                cell.setOnMouseEntered(event-> cell.setCursor(Cursor.HAND));
            }
            confirm.setOnMousePressed(event -> {
                this.model.postSetShade(shade.get());
                dialog.close();
            });
            hBox.getChildren().addAll(die, gridPane);
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setSpacing(10);
            content.setActions();
            content.setHeading(new Text("Choose shade for your die, then press OK to confirm")); // TODO set with language
            content.setBody(hBox);
            content.setActions(confirm);
            gridPane.setScaleX(0.75);
            gridPane.setScaleY(0.75);
            dialog.show();
            //setUpSetShadeFuture();
            }));
    }

    private void setUpSetShadeFuture() {
        CompletableFuture.supplyAsync(unsafe(() -> this.model.waitForSetShade()))
                .thenAccept(this::setDieShade);
    }

    /*private void setUpEffecFuture() {
        CompletableFuture.supplyAsync(unsafe(() ->
            this.model.waitForChooseBetweenEffect())).thenAccept();
    }*/

    private void setEffect(){}
}
