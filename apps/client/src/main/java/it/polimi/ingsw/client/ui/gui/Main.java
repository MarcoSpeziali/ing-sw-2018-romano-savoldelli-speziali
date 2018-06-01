package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.ClientApp;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Boolean.TRUE;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws MalformedURLException {
        primaryStage.setTitle("Sagrada");
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1280,720);
        primaryStage.setFullScreen(TRUE);

        URL url = new URL(ClientApp.class.getClassLoader().getResource("background.jpg"), "");
        Image background = new Image(url.toString(),true);
        BackgroundSize bsize= new BackgroundSize(1280, 720, false, false,
                true, true);
        root.setBackground(new Background(new BackgroundImage(background,BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, bsize)));

        //first pane
        VBox leftMenu = new VBox();
        root.setLeft(leftMenu);
        leftMenu.setId("leftMenu");
        leftMenu.prefHeightProperty().bind(root.heightProperty());
        leftMenu.setPrefWidth(500);
        leftMenu.setTranslateX(-520);
        TranslateTransition leftMenuTranslation = new TranslateTransition(Duration.millis(500), leftMenu);
        leftMenuTranslation.setFromX(-520);
        leftMenuTranslation.setToX(0);
        leftMenu.setEffect(new DropShadow(15d, 5d, 0d, Color.BLACK));

        Button openToolCard = new Button("Tool Card");

        leftMenu.getChildren().add(openToolCard);
        leftMenu.setMargin(openToolCard, new Insets(360,-80,300 ,500 ));
        openToolCard.setPrefHeight(20);
        openToolCard.setPrefWidth(100);
        openToolCard.setRotate(-90);
        openToolCard.setMinHeight(50);
        openToolCard.setMinWidth(100);
        openToolCard.setStyle(
                "-fx-padding: 8 15 15 15; " +
                        "-fx-background-insets: 0,0 0 5 0, 0 0 6 0, 0 0 7 0;" +
                        "-fx-background-radius: 8;" +
                        "-fx-background-color: linear-gradient(from 0% 93% to 0% 100%, #a34313 0%, #903b12 100%), #9d4024, #d86e3a, radial-gradient(center 50% 50%, radius 100%, #d86e3a, #c54e2c);" +
                        "-fx-effect: dropshadow( gaussian , rgba(0,0,0,0.75) , 4,0,0,1 );" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 1.1em;"
        );


        openToolCard.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(leftMenuTranslation.getRate()==-1) {
                    leftMenuTranslation.setRate(1);
                    leftMenuTranslation.play();
                }
                else {
                    leftMenuTranslation.setRate(-1);
                    leftMenuTranslation.play();
                }
            }
        });

        URL urlCards = new URL(ClientApp.class.getClassLoader().getResource("texture.jpg"), "images/");
        Image cardBackground = new Image (urlCards.toString(), true);
        BackgroundSize cardBSize = new BackgroundSize(500, 500, false,false, true, true);

        leftMenu.setBackground(new Background(new BackgroundImage(cardBackground, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, cardBSize)));



/*
        //second pane
        TitledPane objectiveCard = new TitledPane();
        root.setRight(objectiveCard);
        objectiveCard.setText("Objective Card");

    */

    primaryStage.setScene(scene);

    primaryStage.show();


        

    }
}
