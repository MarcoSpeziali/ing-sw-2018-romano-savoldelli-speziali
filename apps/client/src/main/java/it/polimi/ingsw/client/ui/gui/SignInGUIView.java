package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.ClientApp;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.MalformedURLException;
import java.net.URL;

public class SignInGUIView {
    private BorderPane view;


    public SignInGUIView() throws MalformedURLException{
        view = new BorderPane();
        Label user = new Label("User: ");
        Label password = new Label("Password: ");
        Button login = new Button("Login");
        Button back = new Button("< Back");
        TextField userNameField = new TextField("Enter user name here");
        PasswordField passwordField =  new PasswordField();
        Pane glass = new Pane();
        view.setCenter(glass);

        glass.setId("Sign In container");
        glass.setMaxHeight(500);
        glass.setMaxWidth(900);
        glass.setBackground(new Background(new BackgroundFill(Color.color(0.0, 0.0, 0.0,0.6),
                CornerRadii.EMPTY, Insets.EMPTY)));

        String buttonStyle = "-fx-padding: 8 15 15 15; " +
                "-fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;" +
                "-fx-background-radius: 8;" +
                "-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);" +
                "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 1.3em;";

        glass.getChildren().add(user);
        user.setLayoutX(0);
        user.setLayoutY(0);

        glass.getChildren().add(password);
        password.setLayoutY(500);
        password.setLayoutX(900);

        glass.getChildren().add(userNameField);
        userNameField.setLayoutX(100);
        userNameField.setLayoutY(0);

        glass.getChildren().add(passwordField);
        passwordField.setLayoutX(0);
        passwordField.setLayoutY(200);

        glass.getChildren().add(back);
        back.setMinHeight(50);
        back.setMinWidth(150);
        back.setStyle(buttonStyle);
        back.setLayoutX(400);
        back.setLayoutY(300);
        back.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OptionsApp.loadOptions();
            }
        });

        glass.getChildren().add(login);
        login.setMinHeight(50);
        login.setMinWidth(150);
        login.setStyle(buttonStyle);
        login.setLayoutX(600);
        login.setLayoutY(300);
        login.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    OptionsApp.loadLobby();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        });
        URL url = new URL(ClientApp.class.getClassLoader().getResource("images"), "background1.jpg");
        Image background = new Image(url.toString(),true);
        BackgroundSize bsize= new BackgroundSize(1280, 720, false, false,
                true, true);
        view.setBackground(new Background(new BackgroundImage(background,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, bsize)));

    }

    public Parent getView() {
        return view;
    }
}
