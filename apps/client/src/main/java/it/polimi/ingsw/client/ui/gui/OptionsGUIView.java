package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.ClientApp;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.net.MalformedURLException;
import java.net.URL;


public class OptionsGUIView {
    private BorderPane view;

    public OptionsGUIView() throws MalformedURLException {
        view = new BorderPane();
        Button play = new Button("Play");
        Button settings = new Button("Settings");
        Button exit = new Button("Exit");
        Label playerName = new Label("Welcome %player");
        Separator separator = new Separator();
        Pane optionsContainer = new Pane();
        view.setCenter(optionsContainer);
        optionsContainer.setId("optionsContainer");
        optionsContainer.setMaxHeight(500);
        optionsContainer.setMaxWidth(900);
        optionsContainer.setBackground(new Background(new BackgroundFill(Color.color(0.0, 0.0, 0.0,0.6),
                CornerRadii.EMPTY, Insets.EMPTY)));

        play.setMinHeight(50);
        play.setMinWidth(150);
        play.setStyle(
                "-fx-padding: 8 15 15 15; " +
                        "-fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;" +
                        "-fx-background-radius: 8;" +
                        "-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);" +
                        "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 1.3em;"

        );

        settings.setMinHeight(50);
        settings.setMinWidth(150);
        settings.setStyle(
                "-fx-padding: 8 15 15 15; " +
                        "-fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;" +
                        "-fx-background-radius: 8;" +
                        "-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);" +
                        "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 1.3em;"
        );

        exit.setMinHeight(50);
        exit.setMinWidth(150);
        exit.setStyle(
                "-fx-padding: 8 15 15 15; " +
                        "-fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;" +
                        "-fx-background-radius: 8;" +
                        "-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);" +
                        "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 1.3em;"
        );

        playerName.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-font-size: 1.3em;"+
                        "-fx-text-fill: grey;"
        );

        URL url = new URL(ClientApp.class.getClassLoader().getResource("images"), "background1.jpg");
        Image background = new Image(url.toString(),true);
        BackgroundSize bsize= new BackgroundSize(1280, 720, false, false,
                true, true);
        view.setBackground(new Background(new BackgroundImage(background,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, bsize)));




        separator.setOrientation(Orientation.VERTICAL);
        separator.setMinHeight(300);
        separator.setLayoutX(450);
        separator.setLayoutY(100);
        play.setLayoutX(600);
        play.setLayoutY(140);
        settings.setLayoutX(600);
        settings.setLayoutY(220);
        exit.setLayoutX(600);
        exit.setLayoutY(300);
        playerName.setLayoutX(100);
        playerName.setLayoutY(220);

        optionsContainer.getChildren().add(play);
        optionsContainer.getChildren().add(settings);
        optionsContainer.getChildren().add(exit);
        optionsContainer.getChildren().add(playerName);
        optionsContainer.getChildren().add(separator);

        settings.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OptionsApp.loadSettings();
            }});
        exit.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OptionsApp.quit();
            }
        });
        play.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    OptionsApp.loadLobby();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public Parent getView(){
        return view;
    }


}

