package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.core.Player;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.utils.io.Resources;
import it.polimi.ingsw.views.CellView;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class CellGUIView extends CellView implements GUIView {

    private String path;

    private void diePicked() {
        this.cellController.onDiePicked();
    }

    private void diePut() {
        this.cellController.onDiePut(Player.getCurrentPlayer().pickDie());
    }

    public CellGUIView(Cell cell) {
        super(cell);
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

        Node view;
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
            view = new ImageView();
            ((ImageView) view).setFitWidth(100);
            ((ImageView) view).setFitHeight(100);
            try {
                ((ImageView) view).setImage(new Image(Resources.getResource(CellGUIView.class.getClassLoader(), path).openStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            view = new StackPane();
            ((StackPane) view).setMinSize(100, 100);
            if (super.cell.getShade() == 0 && super.cell.getColor() == null) {
                view.setStyle("-fx-background-color: white");
            } else {
                view.setStyle(String.format("-fx-background-color: #%06X;",this.cell.getColor().getHex()));
            }

        }
        this.cell.addListener((OnDiePutListener) (die, location) -> {
            this.cell.putDie(die);
        });
        this.cell.addListener((OnDiePickedListener) (die, location) -> {
            this.cell.pickDie();
        });
        return view;
    }
}



