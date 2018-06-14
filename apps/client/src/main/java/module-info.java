module it.polimi.ingsw.client {
    requires it.polimi.ingsw;
    requires jansi;
    requires java.rmi;
    requires javafx.graphics;
    requires java.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires json;
    requires com.jfoenix;
    requires java.logging;

    exports it.polimi.ingsw.client.utils.text;
    exports it.polimi.ingsw.client.ui.gui.windows;
    exports it.polimi.ingsw.client to javafx.graphics;
}