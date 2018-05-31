module it.polimi.ingsw.client {
    requires it.polimi.ingsw;
    requires jansi;
    requires java.rmi;
    requires  javafx.graphics;
    requires java.base;
    requires javafx.controls;
    exports it.polimi.ingsw.client.ui.gui to javafx.graphics;

}