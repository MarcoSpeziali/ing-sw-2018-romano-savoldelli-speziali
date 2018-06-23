package it.polimi.ingsw.client.ui.gui.scenes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.controllers.WindowMockController;
import it.polimi.ingsw.client.ui.gui.WindowGUIView;
import it.polimi.ingsw.controllers.MatchController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.io.IOException;
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

    private FXMLLoader loader = new FXMLLoader();

    private MatchController matchController;

    public void setController(MatchController matchController) throws IOException {
        this.matchController = matchController;
        init();
    }



    public void init() throws IOException {
        chooseWindow();
    }

    public void chooseWindow() {
        GridPane gridPane = new GridPane();
        JFXDialogLayout content = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(outerPane, content, CENTER);
        JFXButton button = new JFXButton("Exit");
        button.setOnAction(event -> dialog.close());

        CompletableFuture.supplyAsync(unsafe(() -> this.matchController.waitForWindowRequest()))
                .thenAccept(iWindows -> Platform.runLater(unsafe(() -> {
                    for (int i = 0; i < iWindows.length; i++) {
                        for (int j = 0; j < 2; j++) {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(Constants.Resources.WINDOW_VIEW_FXML.getURL());

                            Node window = loader.load();
                            WindowGUIView guiView = loader.getController();
                            try {
                                Node iwindow = loader.load();
                                guiView.setController(new WindowMockController(iWindows[i]));
                                window.setOnMousePressed(event -> showChosen(window));
                                window.setCursor(Cursor.CLOSED_HAND);
                                gridPane.add(window, j, i);
                                content.setHeading(new Text("Choose your Window."));
                                content.setBody(gridPane);
                                content.setActions(button);
                                content.setAlignment(Pos.CENTER);
                                dialog.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })));
    }

    private void showChosen(Node window) {

    }
}
