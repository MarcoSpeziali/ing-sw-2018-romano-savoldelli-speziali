package it.polimi.ingsw.client.ui.gui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.core.Player;
import it.polimi.ingsw.listeners.OnDiePickedListener;
import it.polimi.ingsw.listeners.OnDiePutListener;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.views.CellView;
import javafx.scene.image.ImageView;

public class CellGUIView extends CellView {

    private String path;

    private ImageView imageView;


    @Override
    public void setCell(Cell cell) {
        super.setCell(cell);
        if(super.cell.getColor() == null){

            switch (super.cell.getShade()){
                case 1:
                    path = Constants.Resources.CELL_ONE.getRelativePath();
                    imageView = new ImageView(path);
                    break;
                case 2:
                    path = Constants.Resources.CELL_TWO.getRelativePath();
                    imageView = new ImageView(path);
                    break;
                case 3:
                    path = Constants.Resources.CELL_THREE.getRelativePath();
                    imageView = new ImageView(path);
                    break;
                case 4:
                    path = Constants.Resources.CELL_FOUR.getRelativePath();
                    imageView = new ImageView(path);
                    break;
                case 5:
                    path = Constants.Resources.CELL_FIVE.getRelativePath();
                    imageView = new ImageView(path);
                    break;
                case 6:
                    path = Constants.Resources.CELL_SIX.getRelativePath();
                    imageView = new ImageView(path);
                    break;
            }
        }
        else {
            imageView.setStyle(
                    "-fx-background-color: #" + Integer.toHexString(this.cell.getColor().getHex())
            );
        }
        this.cell.addListener((OnDiePutListener) (die, location) -> {
            this.cell.putDie(die);
        });
        this.cell.addListener((OnDiePickedListener)(die, location) -> {
            this.cell.pickDie();
        });
    }
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

    public CellGUIView() {

    }
}
