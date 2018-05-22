package it.polimi.ingsw.client.ui.cli;

import it.polimi.ingsw.controllers.MenuController;
import it.polimi.ingsw.views.Renderable;
import java.util.Scanner;

public class MenuCLIView implements Renderable {

    private int command;
    private MenuController menuController;
    private Scanner reader = new Scanner(System.in);

    public MenuCLIView(MenuController menuController) {
        this.menuController = menuController;
    }

    public int getCommand() {
        return command;
    }

    public void run() {

        this.render();
        command = reader.nextInt();
    }

    public void render() {

        System.out.println("Welcome to ClientApp Game:\n");
        System.out.println("[1] Play game");
        System.out.println("[2] Scoreboard");
        System.out.println("[3] Settings");
    }
}


