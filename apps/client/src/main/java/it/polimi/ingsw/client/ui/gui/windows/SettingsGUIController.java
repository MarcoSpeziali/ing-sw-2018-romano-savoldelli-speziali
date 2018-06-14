package it.polimi.ingsw.client.ui.gui.windows;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.net.authentication.SignInManager;
import it.polimi.ingsw.client.ui.gui.WindowGUIView;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.Window;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SettingsGUIController {

    private FXMLLoader loader = new FXMLLoader();
    private SignInManager model;
    private Cell[][] cells = new Cell[][] {
            {
                    new Cell(0, null),      new Cell(5, null),      new Cell(4, null),       new Cell(0, GlassColor.GREEN)
            },
            {
                    new Cell(0, null),      new Cell(0, null),      new Cell(2, null),       new Cell(0, GlassColor.PURPLE)
            },
            {
                    new Cell(0, GlassColor.BLUE), new Cell(2, null),      new Cell(0, GlassColor.RED),   new Cell(0, GlassColor.YELLOW)
            }
    };
    WindowGUIView windowGUIView =  new WindowGUIView(new Window(3,3,4,"ciao", null, cells));

    public SettingsGUIController() {}


    public void onSaveClicked() throws IOException {
        BorderPane pane = new BorderPane();
        pane.setCenter(windowGUIView.render());
        String ciao = pane.getStyle();
        this.setScene(new Scene(pane, 500, 500));
    }

    public void onBackClicked() throws IOException {
        loader.setLocation(Constants.Resources.START_SCREEN_FXML.getURL());
        this.setScene(new Scene(loader.load(), 550, 722));
    }

    private void setScene(Scene scene) {
        SagradaGUI.primaryStage.setScene(scene);
        //SagradaGUI.primaryStage
        SagradaGUI.primaryStage.show();
    }
}
