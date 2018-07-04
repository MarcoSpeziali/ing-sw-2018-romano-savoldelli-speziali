package it.polimi.ingsw.client.ui.cli.scenes;


import java.util.Scanner;

public class StartScreenGUIView {

    private static Scanner reader = new Scanner(System.in);

    public static void render() {

        System.out.println("Welcome to Sagrada Game. (type --help for a list of commands).");

        String selection;
        do {
            selection = reader.nextLine();

            switch (selection) {
                case ("--help"):
                    System.out.println("--signin --signup --settings --quit");
                    break;

                case ("--signup"):
                    SignUpCLIVIew.render();
                    break;

                case ("--signin"):
                    SignInCLIView.render();
                    break;

                case ("--settting"):
                    SettingsCLIView.render();
                    break;

                default:
                    System.out.println("Invalid command. Type --help for a list of commands.");
            }
        } while (!selection.equals("--quit"));
    }
}


