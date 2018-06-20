package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.utils.ClientLogger;
import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.utils.io.Resources;
import it.polimi.ingsw.views.CellView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

public class CellGUIView extends CellView {

    @FXML
    public AnchorPane colorAnchorPane;

    @FXML
    public AnchorPane dieAnchorPane;

    @FXML
    public AnchorPane defaultDieAnchorPane;

    @FXML
    public ImageView shadeImageView;

    private String path;

    public CellGUIView() {}

    private void diePicked() {
        // this.cellController.onDiePicked();
        // TODO: 20/06/18 First: correct using new implementation
        // TODO: 20/06/18 Second: pick die returns a die, where should we put it? In the player? This should not be done here
    }

    private void diePut() {
        // this.cellController.onDiePut(Player.getCurrentPlayer().pickDie());
        // TODO: 20/06/18 First: correct using new implementation
    }

    @Override
    public void setCell(Cell cell) {
        super.cell = cell;
        if (super.cell.getShade() > 0) {
            String resourceName = String.format("CELL_%d", super.cell.getShade());
            String relativePath = Constants.Resources.valueOf(resourceName).getRelativePath();
            URL resourceUrl = Resources.getResource(CellGUIView.class.getClassLoader(), relativePath);

            if (resourceUrl != null) {
                try {
                    this.shadeImageView.setImage(new Image(resourceUrl.openStream()));
                }
                catch (IOException e) {
                    ClientLogger.getLogger().log(Level.SEVERE, "IOException while reading image at path: " + relativePath, e);
                }
            }
            else {
                ClientLogger.getLogger().log(Level.WARNING, "Could not retrieve image for resource {0}", resourceName);
            }
        }
        else if (super.cell.getColor() == null) { // super.cell.getShade() == 0 always
            colorAnchorPane.setStyle("-fx-background-color: white");
        }
        else {
            colorAnchorPane.setStyle(String.format("-fx-background-color: #%06X;", this.cell.getColor().getHex()));
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
                }
                catch (IOException e) {
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



