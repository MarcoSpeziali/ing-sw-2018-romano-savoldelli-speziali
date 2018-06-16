package it.polimi.ingsw.utils.text;

import it.polimi.ingsw.utils.InMemoryCache;

import java.io.Serializable;
import java.util.*;

/**
 * Represents a {@link String} localized in a particular {@link Locale} or the default one.
 */
public class LocalizedString implements Serializable {

    private static final long serialVersionUID = -9055375854863507509L;
    /**
     * The bundle name.
     */
    private static final String STRINGS_BUNDLE = "MessagesBundle";
    /**
     * The cache used to store the localized strings.
     */
    private static transient InMemoryCache<String, String> memoryCache = new InMemoryCache<>();
    /**
     * The resource bundle.
     */
    private static transient ResourceBundle resourceBundle;
    private static List<OnLocaleChanged> listeners = new LinkedList<>();

    static {
        try {
            resourceBundle = ResourceBundle.getBundle(STRINGS_BUNDLE);
        }
        catch (MissingResourceException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The key of the {@link String} to localize.
     */
    private String localizationKey;

    /**
     * @param localizationKey The key of the {@link String} to localize.
     */
    public LocalizedString(String localizationKey) {
        this.localizationKey = localizationKey;
    }

    /**
     * Invalidates the cache.
     */
    public static void invalidateCache() {
        LocalizedString.memoryCache.invalidate();
    }

    /**
     * Invalidates the cache and changes locale.
     *
     * @param newLocale The new locale used for every new translations.
     */
    public static void invalidateCacheForNewLocale(Locale newLocale) {
        Locale oldLocale = resourceBundle.getLocale();

        LocalizedString.invalidateCache();
        LocalizedString.resourceBundle = ResourceBundle.getBundle(STRINGS_BUNDLE, newLocale);

        listeners.forEach(onLocaleChanged -> onLocaleChanged.onLocaleChanged(oldLocale, newLocale));
    }

    public static void addListener(OnLocaleChanged onLocaleChanged) {
        listeners.add(onLocaleChanged);
    }

    /**
     * @return The key of the {@link String} to localize.
     */
    public String getLocalizationKey() {
        return this.localizationKey;
    }

    /**
     * @return The localized {@link String} for the current locale.
     */
    @Override
    public synchronized String toString() {
        if (memoryCache.contains(this.localizationKey)) {
            return memoryCache.get(this.localizationKey);
        }

        try {
            if (resourceBundle != null) {
                String translatedString = resourceBundle.getString(this.localizationKey);
                memoryCache.add(this.localizationKey, translatedString);

                return translatedString;
            }

            return this.localizationKey;
        }
        catch (MissingResourceException e) {
            return this.getLocalizationKey();
        }
    }

    /**
     * @param forLocale The locale used for the translation.
     * @return The localized {@link String} for the provided locale.
     */
    public synchronized String toString(Locale forLocale) {
        return ResourceBundle.getBundle(STRINGS_BUNDLE, forLocale).getString(this.localizationKey);
    }

    @FunctionalInterface
    public interface OnLocaleChanged {
        void onLocaleChanged(Locale oldLocale, Locale newLocale);
    }
}
