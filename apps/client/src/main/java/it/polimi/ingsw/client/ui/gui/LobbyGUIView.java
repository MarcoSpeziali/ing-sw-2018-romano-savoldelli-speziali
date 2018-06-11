package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.ClientApp;
import it.polimi.ingsw.core.Player;
import javafx.animation.RotateTransition;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.net.MalformedURLException;
import java.net.URL;

import static javafx.scene.text.Font.*;

public class LobbyGUIView {
    private BorderPane view;
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
        Label player_1 = new Label("Player 1");
        Label player_2 = new Label("Player 2");
        Label player_3 = new Label("Player 3");
        Label player_4 = new Label("Player 4");

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
        player_1.setLayoutX(450);
        player_1.setLayoutY(165);
        player_1.setStyle(TEXT_CONSTANT);

        glass.getChildren().add(player_2);
        player_2.setLayoutX(450);
        player_2.setLayoutY(235);
        player_2.setStyle(TEXT_CONSTANT);
        glass.getChildren().add(player_3);
        player_3.setLayoutX(450);
        player_3.setLayoutY(305);
        player_3.setStyle(TEXT_CONSTANT);

        glass.getChildren().add(player_4);
        player_4.setLayoutX(450);
        player_4.setLayoutY(375);
        player_4.setStyle(TEXT_CONSTANT);
        //glass.getChildren().add(wait);
        //glass.getChildren().add(back);
        glass.getChildren().add(lobby);
        lobby.setLayoutX(100);
        lobby.setLayoutY(70);
        lobby.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-font-size: 3em;" +
                        "-fx-text-fill: grey;"
        );




    }

    public Parent getView(){
        return view;
    }

}
