package it.polimi.ingsw.client.ui.cli;


import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.net.mocks.IWindow;

import java.io.IOException;
import java.util.Scanner;

public class WindowCLIView extends CLIView<IWindow> {

    private CellCLIView[][] cellViews;

    public WindowCLIView() {
    }

    @Override
    public void setModel(IWindow iWindow) throws IOException {
        super.setModel(iWindow);

        this.cellViews = new CellCLIView[iWindow.getRows()][iWindow.getColumns()];

        for (int i = 0; i < iWindow.getRows(); i++) {
            for (int j = 0; j < iWindow.getColumns(); j++) {
                cellViews[i][j] = new CellCLIView();
                cellViews[i][j].setModel(iWindow.getCells()[i][j]);
            }
        }
    }

    @Override
    public void render() {

        char c = 'A';
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
                    System.out.print(" " + (j + 1) + " ");
                }
                else {
                    cellViews[i][j].render();
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
