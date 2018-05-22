package it.polimi.ingsw.client;

import it.polimi.ingsw.client.ui.cli.MenuCLIView;
import it.polimi.ingsw.controllers.MenuController;

public class ClientApp {

    public static void main(String args[]) {

        MenuCLIView menuCLIView = new MenuCLIView(new MenuController());
        menuCLIView.run();
    }
}
