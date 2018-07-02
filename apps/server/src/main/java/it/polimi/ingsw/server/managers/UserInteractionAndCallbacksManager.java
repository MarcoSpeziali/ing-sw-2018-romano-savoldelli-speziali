package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;
import it.polimi.ingsw.core.locations.RestrictedChoosablePutLocation;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.DraftPool;
import it.polimi.ingsw.models.RoundTrack;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.net.mocks.IRoundTrack;
import it.polimi.ingsw.net.mocks.RoundTrackMock;
import it.polimi.ingsw.server.actions.ActionGroup;
import it.polimi.ingsw.server.actions.ActionGroupCallbacks;
import it.polimi.ingsw.server.actions.ExecutableAction;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.utils.Range;

import java.util.List;

public class UserInteractionAndCallbacksManager implements UserInteractionProvider, ActionGroupCallbacks {

    private final DatabasePlayer databasePlayer;
    private final MatchObjectsManager matchObjectsManager;
    private final MatchCommunicationsManager matchCommunicationsManager;

    public UserInteractionAndCallbacksManager(DatabasePlayer databasePlayer, MatchObjectsManager matchObjectsManager, MatchCommunicationsManager matchCommunicationsManager) {
        this.databasePlayer = databasePlayer;
        this.matchObjectsManager = matchObjectsManager;
        this.matchCommunicationsManager = matchCommunicationsManager;
    }
    
    // ------ USER INTERACTION PROVIDER ------
    
    @Override
    public Integer choosePosition(ChooseLocation location, GlassColor color, Integer shade) {
        if (location instanceof RoundTrack) {
            IRoundTrack roundTrack = new RoundTrackMock((IRoundTrack) location);
            
        }
        else if (location instanceof DraftPool) {
        
        }
        
        return null;
    }
    
    @Override
    public Integer choosePositionForDie(RestrictedChoosablePutLocation location, Die die, Boolean ignoreColor, Boolean ignoreShade, Boolean ignoreAdjacency) {
        if (location instanceof Window) {
        
        }
        
        return null;
    }
    
    @Override
    public Integer chooseShade(Die die) {
        return null;
    }
    
    // ------ ACTION GROUP CALLBACKS ------
    
    @Override
    public boolean shouldRepeat(ActionGroup actionGroup, int alreadyRepeatedFor, int maximumRepetitions) {
        return false;
    }
    
    @Override
    public List<ExecutableAction> getChosenActions(List<ExecutableAction> actions, Range<Integer> chooseBetween) {
        return null;
    }
}
