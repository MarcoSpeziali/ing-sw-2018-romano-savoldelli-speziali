package it.polimi.ingsw.client.ui.gui.windows;

import com.jfoenix.controls.JFXButton;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.utils.text.LabeledLocalizationUpdater;
import it.polimi.ingsw.utils.text.Localized;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.client.SagradaGUI.primaryStage;

public class StartScreenGUIController implements Initializable {

    @FXML
    @Localized(key = Constants.Strings.START_SCREEN_PLAY_BUTTON_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public JFXButton playButton;
    @FXML
    @Localized(key = Constants.Strings.START_SCREEN_SETTINGS_BUTTON_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public JFXButton settingsButton;
    @FXML
    @Localized(key = Constants.Strings.START_SCREEN_EXIT_BUTTON_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public JFXButton exitButton;

    private FXMLLoader loader = new FXMLLoader();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Localized.Updater.update(this);
    }

    public void onExitClicked() {
        primaryStage.close();
    }

    public void onSettingClicked() throws IOException {
        loader.setLocation(Constants.Resources.SETTINGS_FXML.getURL());
        SagradaGUI.showStage(loader.load(), 720, 480);
    }

    public void onPlayGameClicked() throws IOException {
        loader.setLocation(Constants.Resources.SIGN_IN_FXML.getURL());
        SagradaGUI.showStage(loader.load(), 720, 480);
    }
}
