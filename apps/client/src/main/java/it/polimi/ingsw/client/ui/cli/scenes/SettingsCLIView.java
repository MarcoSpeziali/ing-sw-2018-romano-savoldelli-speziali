package it.polimi.ingsw.client.ui.cli.scenes;

import it.polimi.ingsw.client.controllers.SettingsController;

import java.util.Scanner;

public class SettingsCLIView  {

    static Scanner scanner = new Scanner(System.in);

    static SettingsController settingsController = new SettingsController();

    public static void render() {

        settingsController.init();

        String command;
        do {
            System.out.println("Type rmi or socket to choose your connection, back to go to main menu.");
            command = scanner.nextLine();

            switch (command) {

                case "rmi" : {
                    settingsController.isUsingRMI();
                    settingsController.close(true);
                    System.out.println("Saved!");
                    break;
                }
                case "socket" : {
                    settingsController.isUsingSockets();
                    settingsController.close(true);
                    System.out.println("Saved!");
                    break;
                }

                case "back" : {
                    break;
                }

                default: {
                    System.out.println("Invalid option chosen");
                    break;
                }
            }

        }
        while (!command.equals("back") & !command.equals("rmi") & !command.equals("socket"));

        System.out.println("Back to main menu...");
    }
}
