package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.views.WindowView;

public class WindowCLIView extends WindowView {

    public WindowCLIView(Window window){
        super(window);

        this.cellViews = new CellCLIView[window.getRows()][window.getColumns()];

        for (int i = 0; i < window.getRows(); i++) {
            for (int j = 0; j < window.getColumns(); j++) {
                cellViews[i][j] = new CellCLIView(window.getCells()[i][j]);
            }
        }
    }

    @Override
    public void render() {

        char c='A';
        System.out.println("Window name:\t" + this.window.getId());
        System.out.println("Difficulty:\t\t" + this.window.getDifficulty()+"\n");

        for (int i = -1; i < window.getRows() ; i++) {
            for (int j = -1; j < window.getColumns(); j++) {
                if (i == -1 && j == -1) {
                    System.out.print("  ");
                }
                else if (j == -1) {
                    System.out.print(c+" ");
                    c++;
                }
                else if (i == -1) {
                    System.out.print(" "+(j+1)+" ");
                }
                else cellViews[i][j].render();
            }
            System.out.println();
        }
        System.out.println();


    }
}
