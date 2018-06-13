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

    exports it.polimi.ingsw.client.ui.gui;
    exports it.polimi.ingsw.client to javafx.graphics;

}