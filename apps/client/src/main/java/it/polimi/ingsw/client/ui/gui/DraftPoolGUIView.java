package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.models.DraftPool;
import it.polimi.ingsw.views.DraftPoolView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class DraftPoolGUIView extends DraftPoolView {

    public DraftPoolGUIView(DraftPool draftPool) {
        super(draftPool);
    }

    @FXML
    public AnchorPane anchorPane, dice_1, dice_2, dice_3, dice_4, dice_5, dice_6, dice_7, dice_8, dice_9;


    public void onUpdateReceived(DraftPool draftPool){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
        DieGUIView dieGUIView1 = fxmlLoader.getController();
        DieGUIView dieGUIView2 = fxmlLoader.getController();
        DieGUIView dieGUIView3 = fxmlLoader.getController();
        DieGUIView dieGUIView4 = fxmlLoader.getController();
        DieGUIView dieGUIView5 = fxmlLoader.getController();
        DieGUIView dieGUIView6 = fxmlLoader.getController();
        DieGUIView dieGUIView7 = fxmlLoader.getController();
        DieGUIView dieGUIView8 = fxmlLoader.getController();
        DieGUIView dieGUIView9 = fxmlLoader.getController();
        dieGUIView1.setDie(draftPool.getDice().get(0));
        dieGUIView2.setDie(draftPool.getDice().get(1));
        dieGUIView3.setDie(draftPool.getDice().get(2));
        dieGUIView4.setDie(draftPool.getDice().get(3));
        dieGUIView5.setDie(draftPool.getDice().get(4));
        dieGUIView6.setDie(draftPool.getDice().get(5));
        dieGUIView7.setDie(draftPool.getDice().get(6));
        dieGUIView8.setDie(draftPool.getDice().get(7));
        dieGUIView9.setDie(draftPool.getDice().get(8));
        try {
            dice_1 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dice_2 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dice_3 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dice_4 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dice_5 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dice_6 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dice_7 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dice_8 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dice_9 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Node render() {
        return new Pane();
    }
}

