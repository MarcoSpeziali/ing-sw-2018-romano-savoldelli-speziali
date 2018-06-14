package it.polimi.ingsw.client.ui.gui.windows;

import it.polimi.ingsw.client.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import static it.polimi.ingsw.client.SagradaGUI.primaryStage;

public class StartScreenGUIController {

    private FXMLLoader loader = new FXMLLoader();
    public double xOffset = 0;
    public double yOffset = 0;

    public StartScreenGUIController() {}

    public void onExitClicked() {
        primaryStage.close();
    }

    public void onSettingClicked() throws IOException {
        loader.setLocation(Constants.Resources.SETTINGS_FXML.getURL());
        this.setScene(new Scene(loader.load(), 720, 480));
    }

    public void onPlayGameClicked() throws IOException {
        loader.setLocation(Constants.Resources.SIGN_IN_FXML.getURL());
        this.setScene(new Scene(loader.load(), 720, 480));
    }

    private void setScene(Scene scene) {
        primaryStage.setScene(scene);
        //SagradaGUI.primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    public void onMousePressed(MouseEvent event){ // FIXME not correct offsets
            xOffset = primaryStage.getX() - event.getScreenX();
            yOffset = primaryStage.getY() - event.getScreenY();
    }

    public void onMouseDragged(MouseEvent event){ // FIXME not correct offsets
            primaryStage.setX(event.getScreenX() + xOffset);
            primaryStage.setY(event.getScreenY() + yOffset);
    }
}
