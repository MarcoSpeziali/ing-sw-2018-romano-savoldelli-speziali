package it.polimi.ingsw.client.controllers;

import it.polimi.ingsw.client.Settings;

public class SettingsController {

    public void onSaveRequested() {
        Settings.getSettings().save();
    }
}
