package it.polimi.ingsw.client.utils.text;

import it.polimi.ingsw.client.JavaFXBaseTest;
import it.polimi.ingsw.utils.text.Localized;
import it.polimi.ingsw.utils.text.LocalizedString;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class LocalizableTextFieldTest extends JavaFXBaseTest {

    @Localized(key = "localizable_text_view.test.text", fieldUpdater = TextInputControlLocalizationUpdater.class)
    @Localized(key = "localizable_text_view.test.placeholder", fieldUpdater = TextInputControlPlaceholderLocalizationUpdater.class)
    private TextField textField = new TextField("pre_update");

    @Test
    void testUpdate() {
        this.textField.setPromptText("pre_update_placeholder");

        Assertions.assertEquals("pre_update", this.textField.getText());
        Assertions.assertEquals("pre_update_placeholder", this.textField.getPromptText());

        LocalizedString.invalidateCacheForNewLocale(Locale.ENGLISH);

        Localized.Updater.update(this);

        Assertions.assertEquals("Text of textView", this.textField.getText());
        Assertions.assertEquals("Placeholder of textView", this.textField.getPromptText());

        LocalizedString.invalidateCacheForNewLocale(Locale.ITALIAN);

        Localized.Updater.update(this);

        Assertions.assertEquals("Testo di textView", this.textField.getText());
        Assertions.assertEquals("Segnaposto di textView", this.textField.getPromptText());

        LocalizedString.invalidateCacheForNewLocale(Locale.GERMAN);

        Localized.Updater.update(this);

        Assertions.assertEquals("Text von textView", this.textField.getText());
        Assertions.assertEquals("Platzhalter of textView", this.textField.getPromptText());

        LocalizedString.invalidateCacheForNewLocale(Locale.FRENCH);

        Localized.Updater.update(this);

        Assertions.assertEquals("Texte de textView", this.textField.getText());
        Assertions.assertEquals("Espace réservé de textView", this.textField.getPromptText());
    }
}
