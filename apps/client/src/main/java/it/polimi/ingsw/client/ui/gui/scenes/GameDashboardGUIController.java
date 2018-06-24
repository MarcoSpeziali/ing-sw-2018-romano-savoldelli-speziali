package it.polimi.ingsw.client.ui.gui.scenes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.controllers.WindowMockController;
import it.polimi.ingsw.client.ui.gui.ObjectiveCardGUIView;
import it.polimi.ingsw.client.ui.gui.ToolCardGUIView;
import it.polimi.ingsw.client.ui.gui.WindowGUIView;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.net.mocks.ILivePlayer;
import it.polimi.ingsw.net.mocks.IWindow;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.jfoenix.controls.JFXDialog.DialogTransition.CENTER;
import static it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper.unsafe;


public class GameDashboardGUIController {

    @FXML
    public StackPane outerPane;

    @FXML
    public GridPane cardsPane;

    @FXML
    public GridPane windowsPane;

    @FXML
    public AnchorPane anchorPane;

    private MatchController matchController;

    public void setController(MatchController matchController) {
        this.matchController = matchController;
        init();
    }

    public void init() {
        chooseWindow();
        loadElements();
        showWindows();
    }

    private void loadElements() {
        CompletableFuture.supplyAsync(unsafe(() -> this.matchController.waitForToolCards()))
                .thenAccept(toolCardControllers -> Platform.runLater(unsafe(() -> {
                    for (int i=0; i<toolCardControllers.length; i++) {
                        JFXDialogLayout content = new JFXDialogLayout();
                        JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(Constants.Resources.TOOL_CARD_VIEW_FXML.getURL());
                        Node node = loader.load();
                        ToolCardGUIView toolCardGUIView = loader.getController();
                        //toolCardGUIView.setToolCard(); // TODO pass controller

                        content.setBody(node);
                        JFXButton use = new JFXButton("Use");
                        //use.setOnMousePressed(event -> toolCardControllers[i].getToolCard(). TODO set actions on tc
                        JFXButton cancel = new JFXButton("Cancel");
                        cancel.setOnMousePressed(event -> dialog.close());
                        content.setActions(use, cancel);

                        cardsPane.add(node, i, 0);
                    }
                })));
        CompletableFuture.supplyAsync(unsafe(() -> this.matchController.waitForPrivateObjectiveCard()))
                .thenAccept(objectiveCardController -> Platform.runLater(unsafe(() -> {
                    JFXDialogLayout content = new JFXDialogLayout();
                    JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Constants.Resources.OBJECTIVE_CARD_VIEW_FXML.getURL());
                    Node node = loader.load();
                    ObjectiveCardGUIView objectiveCardGUIView = loader.getController();
                    //.setToolCard(); // TODO pass controller

                    content.setBody(node);
                    cardsPane.add(node, 0, 1);
                })));
        CompletableFuture.supplyAsync(unsafe(() -> this.matchController.waitForPublicObjectiveCards()))
                .thenAccept(objectiveCardControllers -> Platform.runLater(unsafe(() -> {
                        for (int i=1; i<objectiveCardControllers.length; i++) {
                            JFXDialogLayout content = new JFXDialogLayout();
                            JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);

                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Constants.Resources.OBJECTIVE_CARD_VIEW_FXML.getURL());
                            Node node = loader.load();
                            ObjectiveCardGUIView objectiveCardGUIView = loader.getController();
                            //.setToolCard(); // TODO pass controller

                            content.setBody(node);
                            cardsPane.add(node, i, 1);
                        }
                })));
    }

    public void chooseWindow() {
        GridPane gridPane = new GridPane();
        JFXDialogLayout content = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);

        CompletableFuture.supplyAsync(unsafe(() -> this.matchController.waitForWindowRequest()))
                .thenAccept(iWindows -> Platform.runLater(unsafe(() -> {
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
                                        // TODO: is it correct, Mark?
                                        this.matchController.respondToWindowRequest(iWindows[finalI]);
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                    dialog.close();
                                });
                                window.setCursor(Cursor.CLOSED_HAND);
                                // TODO check indexes
                                gridPane.add(window, i/(iWindows.length/2), i%(iWindows.length/2));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                    content.setHeading(new Text("Choose your Window."));
                    content.setBody(gridPane);
                    content.setAlignment(Pos.CENTER);
                    dialog.show();

                })));
    }
    
    private void showWindows() {
        CompletableFuture.supplyAsync(unsafe(() -> this.matchController.waitForOpponentsWindowsUpdate()))
                .thenAccept(map -> Platform.runLater(unsafe(() -> {
                            for (Map.Entry<ILivePlayer, IWindow> entry : map.entrySet()) {
                                IWindow iWindow = map.get(entry);
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(Constants.Resources.WINDOW_VIEW_FXML.getURL());
                                try {
                                    Node node = loader.load();
                                    WindowGUIView windowGUIView = loader.getController();
                                    windowGUIView.setController(new WindowMockController(iWindow));
                                    //windowsPane.add(node, i / 2, i / 2); // TODO check index
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                )));
    }
}
