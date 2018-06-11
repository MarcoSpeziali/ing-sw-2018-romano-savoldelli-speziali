package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.ClientApp;
import it.polimi.ingsw.core.Player;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;

import static javafx.scene.text.Font.*;

public class LobbyGUIView {
    private BorderPane view;
    private Label seconds;
    private static final Integer AMOUNT = 20;
    private Integer timeSeconds = AMOUNT;
    private static Timeline timeline;


    public static final String TEXT_CONSTANT =
            "-fx-font-weight: bold;" +
                    "-fx-font-size: 2em;" +
                    "-fx-text-fill: grey;";

    public  LobbyGUIView() throws MalformedURLException {
        view = new BorderPane();
        Label lobby = new Label("Lobby");
        Button back = new Button("Back");
        Pane glass = new Pane();
        Label wait = new Label("Wait for other players");
        Label countDown = new Label("seconds left");
        seconds = new Label(timeSeconds.toString());
        Label player_1 = new Label("Player 1");
        Label player_2 = new Label("Player 2");
        Label player_3 = new Label("Player 3");
        Label player_4 = new Label("Player 4");
        Separator separator = new Separator();

        URL url = new URL(ClientApp.class.getClassLoader().getResource("images"), "background.jpg");
        Image background = new Image(url.toString(),true);
        BackgroundSize bsize= new BackgroundSize(1280, 720, false, false,
                true, true);
        view.setBackground(new Background(new BackgroundImage(background, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, bsize)));

        glass.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.4); -fx-background-radius: 2;"
        );
        glass.setMaxWidth(1100);
        glass.setMaxHeight(618.75);
        view.setCenter(glass);
        glass.getChildren().add(player_1);
        player_1.setLayoutX(565);
        player_1.setLayoutY(165);
        player_1.setStyle(TEXT_CONSTANT);

        glass.getChildren().add(player_2);
        player_2.setLayoutX(565);
        player_2.setLayoutY(235);
        player_2.setStyle(TEXT_CONSTANT);

        glass.getChildren().add(player_3);
        player_3.setLayoutX(565);
        player_3.setLayoutY(305);
        player_3.setStyle(TEXT_CONSTANT);

        glass.getChildren().add(player_4);
        player_4.setLayoutX(565);
        player_4.setLayoutY(375);
        player_4.setStyle(TEXT_CONSTANT);

        glass.getChildren().add(wait);
        wait.setLayoutX(80);
        wait.setLayoutY(235);
        wait.setStyle(TEXT_CONSTANT);

        glass.getChildren().add(back);
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
        back.setLayoutX(850);
        back.setLayoutY(500);
        back.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                OptionsApp.loadOptions();
                timeline.stop();
            }
        });

        glass.getChildren().add(lobby);
        lobby.setLayoutX(80);
        lobby.setLayoutY(70);
        lobby.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-font-size: 3em;" +
                        "-fx-text-fill: grey;"
        );

        glass.getChildren().add(separator);
        separator.setOrientation(Orientation.VERTICAL);
        separator.setMinHeight(300);
        separator.setLayoutX(430);
        separator.setLayoutY(145);

        glass.getChildren().add(seconds);
        seconds.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-font-size: 5em;" +
                        "-fx-text-fill: grey;"
        );
        seconds.setLayoutX(160);
        seconds.setLayoutY(305);

        //Timer
        seconds.setText(timeSeconds.toString());
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1), e -> {
                    timeSeconds--;
                    seconds.setText(timeSeconds.toString());
                    if(timeSeconds<=0){
                        timeline.stop();
                        }
                    }));
        timeline.playFromStart();

        glass.getChildren().add(countDown);
        countDown.setLayoutX(162);
        countDown.setLayoutY(375);
        countDown.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-font-size: 1.0em;" +
                        "-fx-text-fill: grey;"
        );



    }

    public Parent getView(){
        return view;
    }

}
