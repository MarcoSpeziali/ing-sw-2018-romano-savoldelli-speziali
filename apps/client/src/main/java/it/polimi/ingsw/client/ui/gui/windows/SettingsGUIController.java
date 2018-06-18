package it.polimi.ingsw.client.ui.gui.windows;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXToggleButton;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.client.controllers.SettingsController;
import it.polimi.ingsw.client.utils.text.LabeledLocalizationUpdater;
import it.polimi.ingsw.utils.text.Localized;
import it.polimi.ingsw.utils.text.LocalizedString;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SettingsGUIController extends SettingsController implements Initializable {

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

    private FXMLLoader loader = new FXMLLoader();

    private Locale previousLocale;

    public SettingsGUIController() {
        previousLocale = Settings.getSettings().getLanguage().getLocale();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Localized.Updater.update(this);

        this.fullScreenToggle.setSelected(Settings.getSettings().isFullScreenMode());
        this.rmiToggle.setSelected(Settings.getSettings().getProtocol() == Constants.Protocols.RMI);
        this.socketToggle.setSelected(Settings.getSettings().getProtocol() == Constants.Protocols.SOCKETS);
        this.languageComboBox.setItems(
                FXCollections.unmodifiableObservableList(FXCollections.observableList(
                        Arrays.stream(Constants.Locales.values())
                                .filter(locales -> locales != Constants.Locales.DEFAULT)
                                .map(Enum::toString)
                                .collect(Collectors.toList())
                ))
        );
        this.languageComboBox.setValue(Settings.getSettings().getLanguage().name());
        this.languageComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Constants.Locales locale = Constants.Locales.valueOf(newValue);
            LocalizedString.invalidateCacheForNewLocale(locale.getLocale());
        });
    }

    @FXML
    public void onSaveClicked() throws IOException {
        Settings.getSettings().setFullScreenMode(this.fullScreenToggle.isSelected());
        Settings.getSettings().setProtocol(this.rmiToggle.isSelected() ? Constants.Protocols.RMI : Constants.Protocols.SOCKETS);
        Settings.getSettings().setLanguage(this.languageComboBox.getSelectionModel().getSelectedItem());

        previousLocale = Settings.getSettings().getLanguage().getLocale();

        super.onSaveRequested();
        this.onBackClicked();
    }

    @FXML
    public void onBackClicked() throws IOException {
        LocalizedString.invalidateCacheForNewLocale(previousLocale);

        loader.setLocation(Constants.Resources.START_SCREEN_FXML.getURL());
        SagradaGUI.showStage(loader.load(), 550, 722);
    }
}
