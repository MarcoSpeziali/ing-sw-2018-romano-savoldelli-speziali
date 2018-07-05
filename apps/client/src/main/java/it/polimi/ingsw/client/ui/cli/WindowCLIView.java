package it.polimi.ingsw.client.ui.cli;


import it.polimi.ingsw.net.mocks.IWindow;

import java.io.IOException;

public class WindowCLIView extends CLIView<IWindow> {

    private CellCLIView[][] cellCLIViews;


    @Override
    public void setModel(IWindow iWindow) throws IOException {
        super.setModel(iWindow);

        if (cellCLIViews != null) {
            for (int i = 0; i < iWindow.getRows(); i++) {
                for (int j = 0; j < iWindow.getColumns(); j++) {
                    cellCLIViews[i][j].onUpdateReceived(this.model.getCells()[i][j].getDie());
                }
            }
            return;
        }

        this.cellCLIViews = new CellCLIView[iWindow.getRows()][iWindow.getColumns()];

        for (int i = 0; i < iWindow.getRows(); i++) {
            for (int j = 0; j < iWindow.getColumns(); j++) {
                cellCLIViews[i][j] = new CellCLIView();
                cellCLIViews[i][j].setModel(iWindow.getCells()[i][j]);
            }
        }
    }

    @Override
    public void render() {

        int c = 0;
        System.out.println("Window name:\t" + this.model.getId());
        System.out.println("Difficulty:\t\t" + this.model.getDifficulty() + "\n");

        for (int i = -1; i < model.getRows(); i++) {
            for (int j = -1; j < model.getColumns(); j++) {
                if (i == -1 && j == -1) {
                    System.out.print("  ");
                }
                else if (j == -1) {
                    System.out.print(c + " ");
                    c++;
                }
                else if (i == -1) {
                    System.out.print(" " + (j) + " ");
                }
                else {
                    cellCLIViews[i][j].render();
                }
            }
            System.out.println();
        }
        System.out.println();


    }


    @Override
    public void init() {

    }
}
