package it.polimi.ingsw.client.ui.gui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


import java.net.MalformedURLException;

import static java.lang.Boolean.TRUE;

public class OptionsApp extends Application {

    private static Scene options;
    private static Scene settings;
    private static Scene lobby;
    private static Scene login;
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws MalformedURLException {
        OptionsApp.stage = primaryStage;
        Parent optionsParent = new OptionsGUIView().getView();
        Parent settingsParent = new SettingsGUIView().getView();
        Parent signInParent = new SignInGUIView().getView();
        Parent lobbyParent = new LobbyGUIView().getView();

        options = new Scene(optionsParent, 1280, 720);
        settings = new Scene(settingsParent, 1280, 720);
        login = new Scene(signInParent, 1280, 720);
        lobby = new Scene(lobbyParent, 1280, 720 );


        loadOptions();
        //primaryStage.setFullScreen(TRUE);
        primaryStage.show();


    }
    public static void loadOptions(){
        OptionsApp.stage.setScene(options);
    }
    public static void loadSettings(){
        OptionsApp.stage.setScene(settings);
    }
    public static void quit(){
        OptionsApp.stage.close();
    }
    public static void loadLobby() throws MalformedURLException {
        OptionsApp.stage.setScene(lobby);
    }
    public static void loadSignIn() throws MalformedURLException {
        OptionsApp.stage.setScene(login);
    }

}
