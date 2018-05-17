package it.polimi.ingsw.utils.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

class LocalizedStringTest {

    @Test
    void testGetters() {
        LocalizedString localizedString =
                new LocalizedString("localized_string.test.test_getters");


        Assertions.assertEquals("localized_string.test.test_getters", localizedString.getLocalizationKey());
    }

    @Test
    void testToString() {
        LocalizedString localizedString =
                new LocalizedString("localized_string.test.test_to_string");

        LocalizedString.invalidateCacheForNewLocale(Locale.ENGLISH);

        Assertions.assertEquals("Example", localizedString.toString());
    }

    @Test
    void testToStringWithLocale() {
        LocalizedString localizedString1 =
                new LocalizedString("localized_string.test.test_to_string_with_locale.1");
        LocalizedString localizedString2 =
                new LocalizedString("localized_string.test.test_to_string_with_locale.2");
        LocalizedString localizedString3 =
                new LocalizedString("localized_string.test.test_to_string_with_locale.3");

        Assertions.assertEquals("Hello", localizedString1.toString(Locale.ENGLISH));
        Assertions.assertEquals("Hallo", localizedString1.toString(Locale.GERMAN));
        Assertions.assertEquals("Bonjour", localizedString1.toString(Locale.FRENCH));
        Assertions.assertEquals("Ciao", localizedString1.toString(Locale.ITALIAN));

        Assertions.assertEquals("Goodbye", localizedString2.toString(Locale.ENGLISH));
        Assertions.assertEquals("Auf Wiedersehen", localizedString2.toString(Locale.GERMAN));
        Assertions.assertEquals("Au revoir", localizedString2.toString(Locale.FRENCH));
        Assertions.assertEquals("Arrivederci", localizedString2.toString(Locale.ITALIAN));

        Assertions.assertEquals("How are you?", localizedString3.toString(Locale.ENGLISH));
        Assertions.assertEquals("Wie gehts?", localizedString3.toString(Locale.GERMAN));
        Assertions.assertEquals("Comment allez-vous?", localizedString3.toString(Locale.FRENCH));
        Assertions.assertEquals("Come stai?", localizedString3.toString(Locale.ITALIAN));
    }

    @Test
    void testInvalidateCacheForNewLocale() {
        LocalizedString localizedString =
                new LocalizedString("localized_string.test.test_invalidate_for_locale");

        LocalizedString.invalidateCacheForNewLocale(Locale.ENGLISH);

        Assertions.assertEquals("Invalidate", localizedString.toString());

        LocalizedString.invalidateCacheForNewLocale(Locale.GERMAN);

        Assertions.assertEquals("Ungültig machen", localizedString.toString());

        LocalizedString.invalidateCacheForNewLocale(Locale.FRENCH);

        Assertions.assertEquals("Invalider", localizedString.toString());

        LocalizedString.invalidateCacheForNewLocale(Locale.ITALIAN);

        Assertions.assertEquals("Invalidare", localizedString.toString());
    }

    @Test
    void testReturnDefaultIfNotTranslated() {
        LocalizedString localizedString =
                new LocalizedString("localized_string.test.test_not_translated");

        LocalizedString.invalidateCacheForNewLocale(Locale.ENGLISH);

        Assertions.assertEquals("Untranslated text", localizedString.toString());
        Assertions.assertEquals("Untranslated text", localizedString.toString(Locale.GERMAN));
    }

    @Test
    void testReturnKeyIfNotExisting() {
        LocalizedString localizedString =
                new LocalizedString("localized_string.test.not_existing_key");

        Assertions.assertEquals("localized_string.test.not_existing_key", localizedString.toString());
    }

    @Test
    void testReturnDefaultTranslationIfUnsupportedLanguage() {
        LocalizedString localizedString =
                new LocalizedString("localized_string.test.test_not_translated");

        LocalizedString.invalidateCacheForNewLocale(Locale.ENGLISH);

        Assertions.assertEquals("Untranslated text", localizedString.toString());
        Assertions.assertEquals("Untranslated text", localizedString.toString(Locale.SIMPLIFIED_CHINESE));
    }
}