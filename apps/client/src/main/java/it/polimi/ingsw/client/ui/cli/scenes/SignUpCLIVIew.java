package it.polimi.ingsw.client.ui.cli.scenes;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.controllers.SignUpController;
import it.polimi.ingsw.net.utils.ResponseFields;
import javafx.scene.text.Text;

import java.util.Scanner;

public class SignUpCLIVIew {

    static Scanner reader = new Scanner(System.in);

    public static void render() {

        System.out.print("username: ");
        String username = reader.nextLine();

        System.out.print("password: ");
        String password = reader.nextLine();

        System.out.print("retype password: ");
        String retypedPassword = reader.nextLine();


        if (username.length() > 8 && !username.contains(" ")) {

            if (password.equals(retypedPassword) && retypedPassword.length() >= 8 && !password.contains(" ")) {
                SignUpController signUpController = new SignUpController();
                signUpController.onSignUpRequested(username, password, () -> {
                    try {
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, error -> {

                    if (error == ResponseFields.Error.ALREADY_EXISTS) {
                        System.out.println(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_ALREADY_EXISTS_CONTENT_TEXT));
                    } else {
                        System.out.println(Constants.Strings.toLocalized(Constants.Strings.CONNECTION_ERROR_CONTENT_TEXT));
                    }
                });
            } else {
                if (password.length() < 8 || password.contains(" ")) {

                    System.out.println(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_CREDENTIAL_PROPERTIES_ERROR_CONTENT_TEXT));

                } else {
                    System.out.println(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_MATCH_FAILED_CONTENT_TEXT));
                }
            }
        } else {
            System.out.println(Constants.Strings.toLocalized(Constants.Strings.SIGN_UP_USER_PROPERTIES_ERROR_CONTENT_TEXT));
        }
        System.out.println("Back to main manu...");
    }
}

