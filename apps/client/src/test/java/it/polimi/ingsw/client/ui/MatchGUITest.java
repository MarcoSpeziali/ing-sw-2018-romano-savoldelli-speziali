package it.polimi.ingsw.client.ui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.scenes.MatchGUIView;
import it.polimi.ingsw.controllers.*;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.net.mocks.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;


public class MatchGUITest extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Constants.Resources.MATCH_FXML.getURL());
        Parent root = loader.load();
        Scene scene = new Scene(root, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        MatchGUIView matchGUIView = loader.getController();

        matchGUIView.setController(new MatchController() {
            @Override
            public IWindow[] waitForWindowRequest() throws RemoteException {
                CellMock[] cells1 = {new CellMock(GlassColor.RED, 0), new CellMock(GlassColor.BLUE, 0), new CellMock(null, 6),
                        new CellMock(null, 0), new CellMock(null, 4), new CellMock(GlassColor.PURPLE,0) };

                CellMock[] cells2 = {new CellMock(GlassColor.RED, 0), new CellMock(GlassColor.BLUE, 0), new CellMock(null, 6),
                        new CellMock(null, 0), new CellMock(null, 4), new CellMock(null,0) };

                CellMock[] cells3 = {new CellMock(GlassColor.GREEN, 0), new CellMock(GlassColor.BLUE, 0), new CellMock(null, 6),
                        new CellMock(null, 0), new CellMock(null, 4), new CellMock(null,0) };

                CellMock[] cells4 = {new CellMock(GlassColor.RED, 0), new CellMock(GlassColor.YELLOW, 0), new CellMock(null, 6),
                        new CellMock(null, 0), new CellMock(null, 4), new CellMock(null,0) };

                IWindow[] iWindows = {new WindowMock("window 1", 3, 2, 3, cells1),
                        new WindowMock("window 2", 1, 2, 3, cells2),
                        new WindowMock("window 3", 1, 2, 3, cells3),
                        new WindowMock("window 4", 1, 2, 3, cells4)
                };

                return iWindows;
            }

            @Override
            public WindowController respondToWindowRequest(IWindow window) throws RemoteException {
                return null;
            }

            @Override
            public ToolCardController[] waitForToolCards() throws RemoteException {
                return new ToolCardController[0];
            }

            @Override
            public ObjectiveCardController[] waitForPublicObjectiveCards() throws RemoteException {
                return new ObjectiveCardController[0];
            }

            @Override
            public ObjectiveCardController waitForPrivateObjectiveCard() throws RemoteException {
                return null;
            }

            @Override
            public Map<ILivePlayer, IWindow> waitForOpponentsWindowsUpdate() throws RemoteException {
                return null;
            }

            @Override
            public DraftPoolController waitForDraftPool() {
                return null;
            }

            @Override
            public RoundTrackController waitForRoundTrack() {
                return null;
            }

            @Override
            public IMatch waitForUpdate() throws RemoteException, InterruptedException {
                return null;
            }

            @Override
            public void close(Object... args) throws IOException {

            }

            @Override
            public void init(Object... args) throws IOException {

            }
        });
        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
