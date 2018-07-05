package it.polimi.ingsw.net.responses;

import it.polimi.ingsw.net.interfaces.MatchInteraction;
import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class NotEnoughTokensResponse implements MatchInteraction {

    private static final long serialVersionUID = 7947049361452584423L;

    private final int matchId;
    private final int requiredTokens;
    private final int currentTokens;

    @JSONDesignatedConstructor
    public NotEnoughTokensResponse(
            @JSONElement("match-id") int matchId,
            @JSONElement("required-tokens") int requiredTokens,
            @JSONElement("current-tokens") int currentTokens
    ) {
        this.matchId = matchId;
        this.requiredTokens = requiredTokens;
        this.currentTokens = currentTokens;
    }

    @Override
    @JSONElement("match-id")
    public int getMatchId() {
        return matchId;
    }

    @JSONElement("required-tokens")
    public int getRequiredTokens() {
        return requiredTokens;
    }

    @JSONElement("current-tokens")
    public int getCurrentTokens() {
        return currentTokens;
    }
}
