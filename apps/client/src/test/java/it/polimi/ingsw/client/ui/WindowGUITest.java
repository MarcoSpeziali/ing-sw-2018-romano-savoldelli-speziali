package it.polimi.ingsw.client.ui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.WindowGUIView;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.net.mocks.CellMock;
import it.polimi.ingsw.net.mocks.IWindow;
import it.polimi.ingsw.net.mocks.WindowMock;
import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

public class WindowGUITest extends Application {

    CellMock[] cells1 = {new CellMock(0,GlassColor.RED), new CellMock(0, GlassColor.BLUE),new CellMock(6,null),
            new CellMock(0,GlassColor.RED), new CellMock(0, GlassColor.BLUE), new CellMock(6, null),
            new CellMock(0,null), new CellMock(4, null), new CellMock(0, GlassColor.PURPLE),
            new CellMock(0,null), new CellMock(4, null), new CellMock(0, null) };

    IWindow iWindow = new WindowMock("window 1", 3, 3, 4, cells1);


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Constants.Resources.WINDOW_VIEW_FXML.getURL());
        Parent node = loader.load();
        WindowGUIView guiView = loader.getController();
        guiView.setModel(iWindow);
        Scene scene = new Scene(node, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
