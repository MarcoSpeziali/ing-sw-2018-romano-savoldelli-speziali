package it.polimi.ingsw.server.net.commands;

import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

public interface InvertedCommand<T extends JSONSerializable, K extends JSONSerializable>
        extends RawCommand<Response<K>, Request<T>> {
    
    interface NullRequest<K extends JSONSerializable>
            extends InvertedCommand<it.polimi.ingsw.net.requests.NullRequest, K> {
        
        default Request<it.polimi.ingsw.net.requests.NullRequest> nullRequest(Response<K> response) {
            return new Request<>(
                    new Header(response.getHeader().getEndPointFunction()),
                    new it.polimi.ingsw.net.requests.NullRequest()
            );
        }
    }
}
