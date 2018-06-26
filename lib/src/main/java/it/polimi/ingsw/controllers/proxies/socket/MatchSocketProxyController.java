package it.polimi.ingsw.controllers.proxies.socket;

import it.polimi.ingsw.controllers.*;
import it.polimi.ingsw.net.mocks.ILivePlayer;
import it.polimi.ingsw.net.mocks.IMatch;
import it.polimi.ingsw.net.mocks.IWindow;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

public class MatchSocketProxyController implements MatchController {
    @Override
    public IWindow[] waitForWindowRequest() throws RemoteException {
        return new IWindow[0];
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
}
