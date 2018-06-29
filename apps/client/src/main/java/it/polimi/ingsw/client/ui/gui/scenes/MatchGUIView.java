package it.polimi.ingsw.client.ui.gui.scenes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.*;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.net.mocks.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jfoenix.controls.JFXDialog.DialogTransition.CENTER;
import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;


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
        setUpUpdateFuture();
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
                        toolCardGUIView.setModel(iToolCard);
                        vBoxToolCard.getChildren().add(toolCardNode);

                        /*content.setBody(toolCardNode);
                        JFXButton use = new JFXButton("Use");
                        //use.setOnMousePressed(event -> toolCardControllers[i].getToolCard(). TODO set actions on tc
                        JFXButton cancel = new JFXButton("Back");
                        cancel.setOnMousePressed(event -> dialog.close());
                        content.setActions(use, cancel);*/
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

                        /*content.setBody(objectiveCardNode);
                        JFXButton cancel = new JFXButton("Back");
                        cancel.setOnMousePressed(event -> dialog.close());
                        content.setActions(cancel);*/
                    }
                    FXMLLoader privateCardLoader = new FXMLLoader();
                    privateCardLoader.setLocation(Constants.Resources.OBJECTIVE_CARD_VIEW_FXML.getURL());
                    Node privateCardNode = privateCardLoader.load();
                    ObjectiveCardGUIView objectiveCardGUIView = privateCardLoader.getController();
                    objectiveCardGUIView.setModel(iPrivateObjectiveCard);
                    borderPane.setCenter(privateCardNode);


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
                                WindowGUIView guiView = loader.getController();
                                try {
                                    guiView.setModel(iWindows[i]);
                                    int finalI = i;
                                    window.setOnMousePressed(event -> {
                                        borderPane.setCenter(window);
                                        try {
                                            this.model.respondToWindowRequest(iWindows[finalI]);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        dialog.close();
                                    });

                                    gridPane.add(window, i/(iWindows.length/2), i%(iWindows.length/2));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }
                        content.setHeading(new Text("Choose your Window."));
                        content.setBody(gridPane);
                        content.setAlignment(Pos.CENTER);
                        dialog.show();

                    }
                ));
                });
    }
    
    private void setUpOpponentsWindows(IMatch update) { //TODO change me
        Platform.runLater(() -> {
            AtomicInteger index = new AtomicInteger();

            for (ILivePlayer player: update.getPlayers()) {

                IWindow iWindow = player.getWindow();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Constants.Resources.WINDOW_VIEW_FXML.getURL());
                try {
                    Node node = loader.load();
                    node.setScaleX(0.5);
                    node.setScaleY(0.5);
                    VBox vBox = new VBox();
                    Label label = new Label("ciao");
                    label.setAlignment(Pos.CENTER);
                    label.setStyle("-fx-font-size: 14; -fx-font-weight: bold");
                    vBox.getChildren().addAll(node, label);
                    WindowGUIView windowGUIView = loader.getController();
                    windowGUIView.setModel(iWindow);
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
}
