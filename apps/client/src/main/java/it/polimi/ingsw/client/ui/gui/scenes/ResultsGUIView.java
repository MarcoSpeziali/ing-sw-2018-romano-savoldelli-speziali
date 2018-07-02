package it.polimi.ingsw.client.ui.gui.scenes;


import com.jfoenix.controls.JFXListView;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.SagradaGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;

import java.io.IOException;

public class ResultsGUIView {


    private FXMLLoader loader = new FXMLLoader();

    @FXML
    public Label winningMessage;

    @FXML
    public JFXListView<Label> resultsListView;

    public void onBackClicked() throws IOException {
        loader.setLocation(Constants.Resources.START_SCREEN_FXML.getURL());
        SagradaGUI.showStage(loader.load(), 550, 722);
    }
}
