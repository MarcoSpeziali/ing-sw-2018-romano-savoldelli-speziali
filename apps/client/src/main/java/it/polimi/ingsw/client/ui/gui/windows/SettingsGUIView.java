package it.polimi.ingsw.client.ui.gui.windows;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXToggleButton;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.controllers.SettingsController;
import it.polimi.ingsw.client.utils.text.LabeledLocalizationUpdater;
import it.polimi.ingsw.utils.text.Localized;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsGUIView implements Initializable {

    @FXML
    @Localized(key = Constants.Strings.SETTINGS_SAVE_BUTTON_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public JFXButton saveButton;
    @FXML
    @Localized(key = Constants.Strings.SETTINGS_TITLE, fieldUpdater = LabeledLocalizationUpdater.class)
    public Label titleLabel;
    @FXML
    @Localized(key = Constants.Strings.SETTINGS_BACK_BUTTON_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public JFXButton backButton;
    @FXML
    @Localized(key = Constants.Strings.SETTINGS_CONNECTION_TYPE_LABEL_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public Label connectionTypeLabel;
    @FXML
    @Localized(key = Constants.Strings.SETTINGS_FULLSCREEN_TOGGLE_BUTTON_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public JFXToggleButton fullScreenToggle;
    @FXML
    public JFXRadioButton rmiToggle;
    @FXML
    public JFXRadioButton socketToggle;
    @FXML
    public JFXComboBox<String> languageComboBox;
    @FXML
    @Localized(key = Constants.Strings.SETTINGS_LANGUAGE_LABEL_TEXT, fieldUpdater = LabeledLocalizationUpdater.class)
    public Label languageTypeLabel;

    private SettingsController settingsController;

    private FXMLLoader loader = new FXMLLoader();

    public void setController(SettingsController settingsController) {
        this.settingsController = settingsController;
        this.settingsController.init();

        this.fullScreenToggle.setSelected(this.settingsController.isFullScreenMode());
        this.fullScreenToggle.selectedProperty()
                .addListener((observable, oldValue, newValue) -> this.settingsController.onFullScreenStateChanged(newValue));

        this.rmiToggle.setSelected(this.settingsController.isUsingRMI());
        this.rmiToggle.selectedProperty()
                .addListener((observable, oldValue, newValue) -> this.settingsController.onProtocolChanged(newValue ? Constants.Protocols.RMI : Constants.Protocols.SOCKETS));

        this.socketToggle.setSelected(this.settingsController.isUsingSockets());
        this.socketToggle.selectedProperty()
                .addListener((observable, oldValue, newValue) -> this.settingsController.onProtocolChanged(newValue ? Constants.Protocols.SOCKETS : Constants.Protocols.RMI));

        this.languageComboBox.setItems(
                FXCollections.unmodifiableObservableList(FXCollections.observableList(
                        this.settingsController.getSupportedLocales()
                ))
        );
        this.languageComboBox.setValue(this.settingsController.getCurrentLanguageName());
        this.languageComboBox.valueProperty()
                .addListener((observable, oldValue, newValue) -> this.settingsController.onLanguageChanged(newValue));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Localized.Updater.update(this);
    }

    @FXML
    public void onSaveClicked() throws IOException {
        this.settingsController.close(true);

        this.pushBack();
    }

    @FXML
    public void onBackClicked() throws IOException {
        this.settingsController.close(false);

        this.pushBack();
    }

    private void pushBack() throws IOException {
        loader.setLocation(Constants.Resources.START_SCREEN_FXML.getURL());
        SagradaGUI.showStage(loader.load(), 550, 722);
    }
}
