package it.polimi.ingsw.client.ui.cli.scenes;

import java.util.Scanner;

public class SignInCLIView {

    static Scanner reader = new Scanner(System.in);

    public static void render() {

        String selection;

        do {
            selection = reader.nextLine();

            switch (selection) {
                case ("--back"):
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
