package it.polimi.ingsw.client.utils.text;

import it.polimi.ingsw.client.JavaFXBaseTest;
import it.polimi.ingsw.utils.text.Localized;
import it.polimi.ingsw.utils.text.LocalizedString;
import javafx.scene.control.Label;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

class LocalizableLabelTest extends JavaFXBaseTest {

    @Localized(key = "localizable_label.test.label1", fieldUpdater = LabeledLocalizationUpdater.class)
    private Label label1 = new Label("pre_update");

    @Localized(key = "localizable_label.test.label2", fieldUpdater = LabeledLocalizationUpdater.class)
    private Label label2 = new Label("pre_update2");

    @Test
    void testUpdate() {
        Assertions.assertEquals("pre_update", this.label1.getText());
        Assertions.assertEquals("pre_update2", this.label2.getText());

        LocalizedString.invalidateCacheForNewLocale(Locale.ENGLISH);

        Localized.Updater.update(this);

        Assertions.assertEquals("Text of label1", this.label1.getText());
        Assertions.assertEquals("Text of label2", this.label2.getText());

        LocalizedString.invalidateCacheForNewLocale(Locale.ITALIAN);

        Localized.Updater.update(this);

        Assertions.assertEquals("Testo della label1", this.label1.getText());
        Assertions.assertEquals("Testo della label2", this.label2.getText());

        LocalizedString.invalidateCacheForNewLocale(Locale.GERMAN);

        Localized.Updater.update(this);

        Assertions.assertEquals("Text von label1", this.label1.getText());
        Assertions.assertEquals("Text von label2", this.label2.getText());

        LocalizedString.invalidateCacheForNewLocale(Locale.FRENCH);

        Localized.Updater.update(this);

        Assertions.assertEquals("Texte de label1", this.label1.getText());
        Assertions.assertEquals("Texte de label2", this.label2.getText());
    }
}