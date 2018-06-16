package it.polimi.ingsw.client.ui.gui.windows;

import com.jfoenix.controls.JFXListView;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import it.polimi.ingsw.client.controllers.LobbyController;
import it.polimi.ingsw.net.mocks.ILobby;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class LobbyGUIController extends LobbyController implements Initializable {

    @FXML
    public JFXListView<Label> listView;
    @FXML
    public Label seconds;
    private FXMLLoader loader = new FXMLLoader();


    public LobbyGUIController() {
    }


    public void onBackClicked() throws IOException {
        loader.setLocation(Constants.Resources.SIGN_IN_FXML.getURL());
        SagradaGUI.showStage(loader.load(), 720, 480);
    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) { // delete
        for (int i = 0; i < 4; i++) {
            try {
                Label lbl = new Label("Item " + i);
                lbl.setMinHeight(100);

                listView.getItems().add(lbl);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTimerStarted(int duration) throws RemoteException {

    }

    @Override
    public void onTimerStopped() throws RemoteException {

    }

    @Override
    public void onMatchStarting(int matchId) throws RemoteException {

    }

    @Override
    public void onUpdateReceived(ILobby update) throws RemoteException {

    }
}
