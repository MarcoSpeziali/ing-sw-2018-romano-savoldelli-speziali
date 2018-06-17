package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.core.Player;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.utils.io.Resources;
import it.polimi.ingsw.views.CellView;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class CellGUIView extends CellView implements GUIView {

    private String path;

    public CellGUIView(Cell cell) {
        super(cell);
    }

    private void diePicked() {
        this.cellController.onDiePicked();
    }

    private void diePut() {
        this.cellController.onDiePut(Player.getCurrentPlayer().pickDie());
    }

    @Override
    public void setCell(Cell cell) {
        this.cell = cell;
    }

    @Override
    public void setCellController(CellController cellController) {
        super.setCellController(cellController);
    }

    @Override
    public Node render() {

        Node cellView;
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
            cellView = new ImageView();
            ((ImageView) cellView).setFitWidth(150);
            ((ImageView) cellView).setFitHeight(150);
            try {
                ((ImageView) cellView).setImage(new Image(Resources.getResource(CellGUIView.class.getClassLoader(), path).openStream()));
            }
            catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        else {
            cellView = new StackPane();
            ((StackPane) cellView).setMinSize(150, 150);
            if (super.cell.getShade() == 0 & super.cell.getColor() == null) {
                cellView.setStyle("-fx-background-color: white");
            }
            else {
                cellView.setStyle(String.format("-fx-background-color: #%06X;", this.cell.getColor().getHex()));
            }

        }
        this.cell.addListener((OnDiePickedListener) (die, location) -> this.cell.pickDie());
        this.cell.addListener((OnDiePutListener) (die, location) -> this.cell.putDie(die));
        return cellView;
    }
}



