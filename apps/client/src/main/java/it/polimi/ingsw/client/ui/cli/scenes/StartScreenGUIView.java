package it.polimi.ingsw.client.ui.cli.scenes;


import java.util.Scanner;

public class StartScreenGUIView {

    private static Scanner reader = new Scanner(System.in);

    public static void render() {

        System.out.println("Welcome to Sagrada Game.");

        String selection;
        do {
            System.out.println("Type help for a list of commands.");
            selection = reader.nextLine();

            switch (selection) {
                case ("help"):
                    System.out.println("signin signup settings quit");
                    break;

                case ("signup"):
                    SignUpCLIVIew.render();
                    break;

                case ("signin"):
                    SignInCLIView.render();
                    break;

                case ("settings"):
                    SettingsCLIView.render();
                    break;

                case ("quit"):
                    System.out.println("Quitting...");
                    break;

                default:
                    System.out.println("Invalid command.");
            }
        } while (!selection.equals("quit"));
    }
}


