package it.polimi.ingsw.client.controllers;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.Settings;
import it.polimi.ingsw.utils.text.LocalizedString;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SettingsController implements LocallyInitializable, LocallyClosable {

    private Locale previousLocale;

    public List<String> getSupportedLocales() {
        return Arrays.stream(Constants.Locales.values())
                .filter(locales -> locales != Constants.Locales.DEFAULT)
                .map(Enum::toString)
                .collect(Collectors.toList());
    }

    public boolean isFullScreenMode() {
        return Settings.getSettings().isFullScreenMode();
    }

    public boolean isUsingRMI() {
        return Settings.getSettings().getProtocol() == Constants.Protocols.RMI;
    }

    public boolean isUsingSockets() {
        return Settings.getSettings().getProtocol() == Constants.Protocols.SOCKETS;
    }

    public String getCurrentLanguageName() {
        Constants.Locales currentLocale = Settings.getSettings().getLanguage();

        return currentLocale == Constants.Locales.DEFAULT ? Constants.Locales.ENGLISH.name() : currentLocale.name();
    }

    public void onLanguageChanged(String newLocale) {
        Constants.Locales locale = Constants.Locales.valueOf(newLocale);
        LocalizedString.invalidateCacheForNewLocale(locale.getLocale());

        Settings.getSettings().setLanguage(locale);
    }

    public void onProtocolChanged(Constants.Protocols protocol) {
        Settings.getSettings().setProtocol(protocol);
    }

    public void onFullScreenStateChanged(boolean fullscreen) {
        Settings.getSettings().setFullScreenMode(fullscreen);
    }

    @Override
    public void init(Object... args) {
        previousLocale = Settings.getSettings().getLanguage().getLocale();
    }

    @Override
    public void close(Object... args) {
        if (((Boolean) args[0])) {
            Settings.getSettings().save();
        }
        else {
            LocalizedString.invalidateCacheForNewLocale(previousLocale);
        }
    }
}
