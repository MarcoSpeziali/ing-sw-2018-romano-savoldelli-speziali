package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.ClientApp;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
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

public class SettingsGUIView {
    private BorderPane view;
    public SettingsGUIView() throws MalformedURLException {

        view = new BorderPane();
        Button rmi = new Button("RMI");
        Button socket = new Button("Socket");
        Button back = new Button("< Back");
        Label text = new Label("Change network config");
        Separator separator = new Separator();
        Pane settingsContainer = new Pane();
        view.setCenter(settingsContainer);
        settingsContainer.setId("SettingsContainer");
        settingsContainer.setMaxHeight(500);
        settingsContainer.setMaxWidth(900);
        settingsContainer.setBackground(new Background(new BackgroundFill(Color.color(0.0, 0.0, 0.0,0.6),
                CornerRadii.EMPTY, Insets.EMPTY)));

        rmi.setMinHeight(50);
        rmi.setMinWidth(150);
        rmi.setStyle(
                "-fx-padding: 8 15 15 15; " +
                        "-fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;" +
                        "-fx-background-radius: 8;" +
                        "-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);" +
                        "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 1.3em;"

        );

        socket.setMinHeight(50);
        socket.setMinWidth(150);
        socket.setStyle(
                "-fx-padding: 8 15 15 15; " +
                        "-fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;" +
                        "-fx-background-radius: 8;" +
                        "-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);" +
                        "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 1.3em;"
        );

        back.setMinHeight(50);
        back.setMinWidth(150);
        back.setStyle(
                "-fx-padding: 8 15 15 15; " +
                        "-fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;" +
                        "-fx-background-radius: 8;" +
                        "-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);" +
                        "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 1.3em;"
        );

        text.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-font-size: 1.3em;" +
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
        rmi.setLayoutX(600);
        rmi.setLayoutY(140);
        socket.setLayoutX(600);
        socket.setLayoutY(220);
        back.setLayoutX(600);
        back.setLayoutY(300);
        text.setLayoutX(100);
        text.setLayoutY(220);

        settingsContainer.getChildren().add(rmi);
        settingsContainer.getChildren().add(socket);
        settingsContainer.getChildren().add(back);
        settingsContainer.getChildren().add(text);
        settingsContainer.getChildren().add(separator);

        back.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OptionsApp.loadOptions();
            }
        });
    }

    public Parent getView(){
        return view;
    }
}
