package it.polimi.ingsw.utils.text;

import it.polimi.ingsw.utils.InMemoryCache;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Represents a {@code String} localized in a particular {@code Locale} or the default one.
 */
public class LocalizedString {

    /**
     * The key of the {@code String} to localize.
     */
    private String localizationKey;

    /**
     * The cache used to store the localized strings.
     */
    private static InMemoryCache<String, String> memoryCache = new InMemoryCache<>();

    /**
     * The bundle name.
     */
    private static final String STRINGS_BUNDLE = "MessagesBundle";

    /**
     * The resource bundle.
     */
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(LocalizedString.STRINGS_BUNDLE);

    /**
     * @return The key of the {@code String} to localize.
     */
    public String getLocalizationKey() {
        return this.localizationKey;
    }

    /**
     * @param localizationKey The key of the {@code String} to localize.
     */
    public LocalizedString(String localizationKey) {
        this.localizationKey = localizationKey;
    }

    /**
     * @return The localized {@code String} for the current locale.
     */
    @Override
    public synchronized String toString() {
        if (memoryCache.contains(this.localizationKey)) {
            return memoryCache.get(this.localizationKey);
        }

        try {
            String translatedString = LocalizedString.resourceBundle.getString(this.localizationKey);
            memoryCache.add(this.localizationKey, translatedString);

            return translatedString;
        }
        catch (MissingResourceException e) {
            return this.getLocalizationKey();
        }
    }

    /**
     * @param forLocale The locale used for the translation.
     * @return The localized {@code String} for the provided locale.
     */
    public synchronized String toString(Locale forLocale) {
        return ResourceBundle.getBundle(LocalizedString.STRINGS_BUNDLE, forLocale).getString(this.localizationKey);
    }

    /**
     * Invalidates the cache.
     */
    public static void invalidateCache() {
        LocalizedString.memoryCache.invalidate();
    }

    /**
     * Invalidates the cache and changes locale.
     * @param newLocale The new locale used for every new translations.
     */
    public static void invalidateCacheForNewLocale(Locale newLocale) {
        LocalizedString.invalidateCache();
        LocalizedString.resourceBundle = ResourceBundle.getBundle(LocalizedString.STRINGS_BUNDLE, newLocale);
    }
}
