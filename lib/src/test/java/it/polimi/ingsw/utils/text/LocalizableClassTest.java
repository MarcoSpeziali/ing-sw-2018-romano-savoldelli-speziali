package it.polimi.ingsw.utils.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

class LocalizableClassTest {

    @Localized(key = "localizable_class.test", fieldUpdater = StringFieldLocalizationUpdater.class)
    private String text = "pre_update";

    @Localized(key = "localizable_class.test2", fieldUpdater = StringFieldLocalizationUpdater.class)
    private String text2 = "pre_update2";

    @Test
    void testLoading() {
        Assertions.assertEquals("pre_update", this.text);
        Assertions.assertEquals("pre_update2", this.text2);

        LocalizedString.invalidateCacheForNewLocale(Locale.ENGLISH);

        Localized.Updater.update(this);

        Assertions.assertEquals("I'm a man", this.text);
        Assertions.assertEquals("I'm a woman", this.text2);

        LocalizedString.invalidateCacheForNewLocale(Locale.ITALIAN);

        // Localized.Updater.update(this);

        Assertions.assertEquals("Io sono un uomo", this.text);
        Assertions.assertEquals("Io sono una donna", this.text2);

        LocalizedString.invalidateCacheForNewLocale(Locale.GERMAN);

        // Localized.Updater.update(this);

        Assertions.assertEquals("Ich bin ein Mann", this.text);
        Assertions.assertEquals("Ich bin eine Frau", this.text2);

        LocalizedString.invalidateCacheForNewLocale(Locale.FRENCH);

        // Localized.Updater.update(this);

        Assertions.assertEquals("Je suis un homme", this.text);
        Assertions.assertEquals("Je suis une femme", this.text2);
    }
}