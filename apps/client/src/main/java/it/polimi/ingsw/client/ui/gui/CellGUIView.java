package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.core.Player;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.net.mocks.ILobby;
import it.polimi.ingsw.utils.io.Resources;
import it.polimi.ingsw.views.CellView;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;

public class CellGUIView extends CellView {

    private String path;

    public CellGUIView(Cell cell) {
        setCell(cell);
    }

    private void diePicked() {
        this.cellController.onDiePicked();
    }

    private void diePut() {
        this.cellController.onDiePut(Player.getCurrentPlayer().pickDie());
    }

    @FXML
    public AnchorPane colorAnchorPane, dieAnchorPane;

    public AnchorPane defaultDieAnchorPane;

    @FXML
    public ImageView shadeImageView;

    @Override
    public void setCell(Cell cell) {
        super.cell = cell;
        if (super.cell.getShade() > 0) {
            switch (super.cell.getShade()) {
                case 1:
                    path = Constants.Resources.CELL_ONE.getRelativePath();
                    break;
                case 2:
                    path = Constants.Resources.CELL_TWO.getRelativePath();
                    break;
                case 3:
                    path = Constants.Resources.CELL_THREE.getRelativePath();
                    break;
                case 4:
                    path = Constants.Resources.CELL_FOUR.getRelativePath();
                    break;
                case 5:
                    path = Constants.Resources.CELL_FIVE.getRelativePath();
                    break;
                case 6:
                    path = Constants.Resources.CELL_SIX.getRelativePath();
                    break;

            }

            try {
                shadeImageView.setImage(new Image(Resources.getResource(CellGUIView.class.getClassLoader(), path).openStream()));
            }
            catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        else {
            if (super.cell.getShade() == 0 & super.cell.getColor() == null) {
                colorAnchorPane.setStyle("-fx-background-color: white");
            }
            else {
                colorAnchorPane.setStyle(String.format("-fx-background-color: #%06X;", this.cell.getColor().getHex()));
            }

        }
    }

    public void onUpdateReceived(Cell cell) {
        Platform.runLater(() -> {
            if (!cell.isOccupied()) {
            dieAnchorPane = defaultDieAnchorPane;
            }
            else {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
                DieGUIView dieGUIView = loader.getController();
                dieGUIView.setDie(cell.getDie());
                defaultDieAnchorPane = dieAnchorPane;
                try {
                    dieAnchorPane = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void setCellController(CellController cellController) {
        super.setCellController(cellController);
    }


}



