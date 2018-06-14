package it.polimi.ingsw.client.ui.gui;

import javafx.scene.Node;

import java.io.IOException;

public interface GUIView {
    Node render() throws IOException;
}
