package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.controllers.CellController;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.views.CellView;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

public class CellGUIView extends CellView implements GUIView {

    private String path;

    private ImageView view;

    private void diePicked(){
        this.cellController.onDiePicked();
    }

    private void diePut(){
       // this.cellController.onDiePut(); TODO da aggiungere dopo player
       /*if (Player.getCurrentPlayer().getDie() == null) {

       }
       else {
           this.cellController.onDiePut(Player.getCurrentPlayer().pickDie());
       }*/
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
        if(super.cell.getColor() == null){
            switch (super.cell.getShade()){
                case 1:
                    path = Constants.Resources.CELL_ONE.getRelativePath();
                    view = new ImageView(path);
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
                case 0:
                    view = new ImageView();
                    view.setStyle("-fx-background-color: white");
                    return this.view;
            }
            System.out.println(path);
            view = new ImageView(path);
        }
        else {
            view = new ImageView();
            view.setStyle(
                    "-fx-background-color: #" + Integer.toHexString(this.cell.getColor().getHex())
            );
        }
        this.cell.addListener((OnDiePutListener) (die, location) -> {
            this.cell.putDie(die);
        });
        this.cell.addListener((OnDiePickedListener)(die, location) -> {
            this.cell.pickDie();
        });
        return this.view;
    }
}
