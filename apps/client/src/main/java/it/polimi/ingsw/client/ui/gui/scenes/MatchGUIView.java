package it.polimi.ingsw.client.ui.gui.scenes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.controllers.WindowMockController;
import it.polimi.ingsw.client.ui.gui.*;
import it.polimi.ingsw.client.utils.ClientLogger;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.net.mocks.ILivePlayer;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.utils.io.Resources;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jfoenix.controls.JFXDialog.DialogTransition.CENTER;
import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;


public class MatchGUIView extends GUIView<MatchController> {

    @FXML
    public StackPane outerPane;

    @FXML
    public GridPane cardsPane;

    @FXML
    public GridPane windowsPane;

    @FXML
    public AnchorPane anchorPane;
    
    public BorderPane borderPane;

    @Override
    public void init() {
        chooseWindow();
        loadElements();
        setUpUpdateFuture();
    }

    private void loadElements() {
        CompletableFuture.supplyAsync(unsafe(() -> this.controller.waitForToolCardControllers()))
                .thenAccept(toolCardControllers -> Platform.runLater(unsafe(() -> {
                    for (int i=0; i<toolCardControllers.length; i++) {
                        JFXDialogLayout content = new JFXDialogLayout();
                        JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Constants.Resources.TOOL_CARD_VIEW_FXML.getURL());
                        Node node = loader.load();
                        node.setOnMousePressed(event -> dialog.show());
                        ToolCardGUIView toolCardGUIView = loader.getController();
                        toolCardGUIView.setController(toolCardControllers[i]);

                        content.setBody(node);
                        JFXButton use = new JFXButton("Use");
                        //use.setOnMousePressed(event -> toolCardControllers[i].getToolCard(). TODO set actions on tc
                        JFXButton cancel = new JFXButton("Back");
                        cancel.setOnMousePressed(event -> dialog.close());
                        content.setActions(use, cancel);

                        cardsPane.add(node, i, 0);
                    }
                })));
        CompletableFuture.supplyAsync(unsafe(() -> this.controller.waitForPrivateObjectiveCardController()))
                .thenAccept(objectiveCardController -> Platform.runLater(unsafe(() -> {
                    JFXDialogLayout content = new JFXDialogLayout();
                    JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Constants.Resources.OBJECTIVE_CARD_VIEW_FXML.getURL());
                    Node node = loader.load();
                    node.setOnMousePressed(event -> dialog.show());
                    ObjectiveCardGUIView objectiveCardGUIView = loader.getController();
                    objectiveCardGUIView.setController(objectiveCardController);

                    content.setBody(node);
                    JFXButton cancel = new JFXButton("Back");
                    cancel.setOnMousePressed(event -> dialog.close());
                    content.setActions(cancel);
                    cardsPane.add(node, 0, 1);
                })));
        CompletableFuture.supplyAsync(unsafe(() -> this.controller.waitForPublicObjectiveCardControllers()))
                .thenAccept(objectiveCardControllers -> Platform.runLater(unsafe(() -> {
                        for (int i=0; i<objectiveCardControllers.length; i++) {
                            JFXDialogLayout content = new JFXDialogLayout();
                            JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);

                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Constants.Resources.OBJECTIVE_CARD_VIEW_FXML.getURL());
                            Node node = loader.load();
                            node.setOnMousePressed(event -> dialog.show());
                            ObjectiveCardGUIView objectiveCardGUIView = loader.getController();
                            objectiveCardGUIView.setController(objectiveCardControllers[i]);

                            content.setBody(node);
                            JFXButton cancel = new JFXButton("Back");
                            cancel.setOnMousePressed(event -> dialog.close());
                            content.setActions(cancel);
                            cardsPane.add(node, i+1, 1);
                        }
                })));
        CompletableFuture.supplyAsync(unsafe(() -> this.controller.waitForDraftPoolController()))
                .thenAccept(draftPoolController -> Platform.runLater(unsafe(() -> {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Constants.Resources.DRAFTPOOL_VIEW_FXML.getURL());
                    Node node = loader.load();
                    DraftPoolGUIView draftPoolGUIView = loader.getController();
                    draftPoolGUIView.setController(draftPoolController);
                    // TODO Luca: add layout position (use anchors)
                })));
        CompletableFuture.supplyAsync(unsafe(() -> this.controller.waitForRoundTrackController()))
                .thenAccept(roundTrackController -> Platform.runLater(unsafe(() -> {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Constants.Resources.ROUNDTRACK_VIEW_FXML.getURL());
                    Node node = loader.load();
                    RoundTrackGUIView roundTrackGUIView = loader.getController();
                    //roundTrackGUIView.setController(roundTrackController); TODO Luca: fix all these lacks
                    // TODO Luca: add layout position (use anchors)
                })));
    }

    public void chooseWindow() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        JFXDialogLayout content = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);

        CompletableFuture.supplyAsync(unsafe(() -> this.controller.waitForWindowRequest()))
                .thenAccept(iWindows -> {
                    Platform.runLater(unsafe(() -> {
                        for (int i = 0; i < iWindows.length; i++) {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(Constants.Resources.WINDOW_VIEW_FXML.getURL());

                                Node window = loader.load();
                                WindowGUIView guiView = loader.getController();
                                try {
                                    guiView.setController(new WindowMockController(iWindows[i]));
                                    int finalI = i;
                                    window.setOnMousePressed(event -> {anchorPane.getChildren().add(window);
                                        AnchorPane.setBottomAnchor(window, 14.0);
                                        AnchorPane.setLeftAnchor(window, 14.0);
                                        try {
                                            this.controller.respondToWindowRequest(iWindows[finalI]);
                                        } catch (RemoteException e) {
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
                    VBox vBox = new VBox();
                    Label label = new Label(player.getPlayer().getUsername());
                    label.setAlignment(Pos.CENTER);
                    label.setStyle("-fx-font-size: 14; -fx-font-weight: bold");
                    vBox.getChildren().addAll(node, label);
                    WindowGUIView windowGUIView = loader.getController();
                    windowGUIView.setController(new WindowMockController(iWindow));
                    windowsPane.add(node, index.get() / 2, index.get() / 2); // TODO check indexes
                } catch (IOException e) {
                    e.printStackTrace();
                    }
                    index.getAndIncrement();
                }
            setUpUpdateFuture();
        });
    }

    private void setUpUpdateFuture() {
        CompletableFuture.supplyAsync(unsafe(() -> this.controller.waitForUpdate()))
                .thenAccept(this::setUpOpponentsWindows);
    }
}
