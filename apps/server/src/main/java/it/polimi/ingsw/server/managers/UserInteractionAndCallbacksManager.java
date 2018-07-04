package it.polimi.ingsw.server.managers;

import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChooseLocation;
import it.polimi.ingsw.core.locations.RestrictedChoosablePutLocation;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.models.DraftPool;
import it.polimi.ingsw.models.RoundTrack;
import it.polimi.ingsw.models.Window;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.server.actions.ActionGroup;
import it.polimi.ingsw.server.actions.ActionGroupCallbacks;
import it.polimi.ingsw.server.actions.ExecutableAction;
import it.polimi.ingsw.server.sql.DatabasePlayer;
import it.polimi.ingsw.utils.Range;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        try {
            JSONSerializable jsonSerializable;
            Set<Integer> unavailableLocations;

            if (location instanceof RoundTrack) {
                RoundTrack roundTrack = (RoundTrack) location;
                unavailableLocations = getUnavailableLocations(
                        roundTrack.getAllLocations(),
                        roundTrack.getLocationDieMap(),
                        color,
                        shade
                );

                jsonSerializable = new RoundTrackMock(roundTrack);
            }
            else if (location instanceof DraftPool) {
                DraftPool draftPool = ((DraftPool) location);
                unavailableLocations = getUnavailableLocations(
                        draftPool.getAllLocations(),
                        draftPool.getLocationDieMap(),
                        color,
                        shade
                );

                jsonSerializable = new DraftPoolMock(draftPool);
            }
            else {
                return null;
            }

            return this.matchCommunicationsManager.sendChoosePosition(databasePlayer, jsonSerializable, unavailableLocations);
        }
        catch (IOException | InterruptedException e) {
            return null;
        }
    }

    private Set<Integer> getUnavailableLocations(List<Integer> locations, Map<Integer, IDie> positionDieMap, GlassColor color, Integer shade) {
        Set<Integer> unavailableLocations = new HashSet<>(locations);
        unavailableLocations.removeAll(
                positionDieMap.entrySet().stream()
                .filter(entry -> {
                    if (color == null && shade == 0) {
                        return true;
                    }
                    else if (color == null) {
                        return entry.getValue().getShade().equals(shade);
                    }
                    else if (shade == 0) {
                        return entry.getValue().getColor().equals(color);
                    }
                    else {
                        return entry.getValue().getColor().equals(color) &&
                                entry.getValue().getShade().equals(shade);
                    }
                }).map(Map.Entry::getKey)
                .collect(Collectors.toSet())
        );

        return unavailableLocations;
    }
    
    @Override
    public Integer choosePositionForDie(RestrictedChoosablePutLocation location, Die die, Boolean ignoreColor, Boolean ignoreShade, Boolean ignoreAdjacency) {
        try {
            if (location instanceof Window) {
                Window window = ((Window) location);

                List<Integer> possiblePositions = window.getPossiblePositionsForDie(die, ignoreColor, ignoreShade, ignoreAdjacency);

                if (possiblePositions.isEmpty()) {
                    return null;
                }

                Set<Integer> unavailablePositions = new HashSet<>(window.getAllLocations());
                unavailablePositions.removeAll(possiblePositions);

                JSONSerializable jsonSerializable = new WindowMock(window);
                return this.matchCommunicationsManager.sendChoosePosition(
                        databasePlayer,
                        jsonSerializable,
                        unavailablePositions
                );
            }
            else {
                return null;
            }
        }
        catch (IOException | InterruptedException e) {
            return null;
        }
    }

    @Override
    public Integer chooseShade(Die die) {
        try {
            return this.matchCommunicationsManager.sendChooseShade(databasePlayer, new DieMock(die));
        }
        catch (IOException | InterruptedException e) {
            return null;
        }
    }
    
    // ------ ACTION GROUP CALLBACKS ------
    
    @Override
    public boolean shouldRepeat(ActionGroup actionGroup, int alreadyRepeatedFor, int maximumRepetitions) {
        try {
            return this.matchCommunicationsManager.sendShouldRepeat(
                    databasePlayer,
                    new ActionMock(
                            actionGroup.getActionData().getDescriptionKey()
                    )
            );
        }
        catch (IOException | InterruptedException e) {
            return false;
        }
    }
    
    @Override
    public List<ExecutableAction> getChosenActions(List<ExecutableAction> actions, Range<Integer> chooseBetween) {
        try {
            List<String> chosenDescriptionKeys = this.matchCommunicationsManager.sendChooseActions(
                    databasePlayer,
                    actions.stream().map(executableAction -> new ActionMock(
                            executableAction.getActionData().getDescriptionKey()
                    )).toArray(ActionMock[]::new),
                    chooseBetween
            ).stream()
                    .map(IAction::getDescription)
                    .collect(Collectors.toList());

            return actions.stream()
                    .filter(executableAction -> chosenDescriptionKeys.contains(
                            executableAction.getActionData().getDescriptionKey()
                    ))
                    .collect(Collectors.toList());
        }
        catch (IOException | InterruptedException e) {
            return List.of();
        }
    }
}
