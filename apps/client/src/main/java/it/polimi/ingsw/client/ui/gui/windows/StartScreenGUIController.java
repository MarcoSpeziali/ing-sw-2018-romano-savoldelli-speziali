package it.polimi.ingsw.client.ui.gui.windows;

import it.polimi.ingsw.client.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

import static it.polimi.ingsw.client.SagradaGUI.primaryStage;

public class
StartScreenGUIController {

    private FXMLLoader loader = new FXMLLoader();

    public StartScreenGUIController() {
    }

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
        primaryStage.show();
    }
}
