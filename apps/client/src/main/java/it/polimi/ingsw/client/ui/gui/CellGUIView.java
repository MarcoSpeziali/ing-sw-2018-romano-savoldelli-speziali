package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.utils.ClientLogger;
import it.polimi.ingsw.net.mocks.ICell;
import it.polimi.ingsw.utils.io.Resources;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

public class CellGUIView extends GUIView<ICell> {

    @FXML
    public AnchorPane colorAnchorPane;

    @FXML
    public AnchorPane dieAnchorPane;

    @FXML
    public AnchorPane defaultDieAnchorPane;

    @FXML
    public ImageView shadeImageView;

    public Pane unallowedPane;


    public CellGUIView() {
    }


    @Override
    public void setModel(ICell iCell) {
        super.model = iCell;

        if (iCell.getShade() > 0) {
            String resourceName = String.format("CELL_%d", iCell.getShade());
            String relativePath = Constants.Resources.valueOf(resourceName).getRelativePath();
            URL resourceUrl = Resources.getResource(CellGUIView.class.getClassLoader(), relativePath);

            if (resourceUrl != null) {
                try {
                    this.shadeImageView.setImage(new Image(resourceUrl.openStream()));
                } catch (IOException e) {
                    ClientLogger.getLogger().log(Level.SEVERE, "IOException while reading image at path: " + relativePath, e);
                }
            } else {
                ClientLogger.getLogger().log(Level.WARNING, "Could not retrieve image for resource {0}", resourceName);
            }
        } else if (iCell.getColor() == null) { // super.iCell.getShade() == 0 always
            colorAnchorPane.setStyle("-fx-background-color: white");
        } else {
            colorAnchorPane.setStyle(String.format("-fx-background-color: #%06X;", iCell.getColor().getHex()));
        }

    }

    @Deprecated
    public void onUpdateReceived(ICell update) {
        Platform.runLater(() -> {
            if (!update.isOccupied()) {
                dieAnchorPane = defaultDieAnchorPane;
            } else {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
                DieGUIView dieGUIView = loader.getController();
                try {
                    dieGUIView.setModel(update.getDie());
                    defaultDieAnchorPane = dieAnchorPane;
                    dieAnchorPane = loader.load();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            setUpUpdateFuture();
        });
    }

    private void setUpUpdateFuture() {
        //CompletableFuture.supplyAsync(unsafe(() -> this.model.waitForUpdate()))
        //        .thenAccept(this::onUpdateReceived);
    }

    @Override
    public void init() {

    }
}



