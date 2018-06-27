package it.polimi.ingsw.server.net.commands;

import it.polimi.ingsw.net.Header;
import it.polimi.ingsw.net.Request;
import it.polimi.ingsw.net.Response;
import it.polimi.ingsw.utils.io.json.JSONSerializable;

// TODO: docs
public interface Command<T extends JSONSerializable, K extends JSONSerializable>
        extends RawCommand<Request<T>, Response<K>> {
    
    interface NullResponse<T extends JSONSerializable>
            extends Command<T, it.polimi.ingsw.net.responses.NullResponse> {
        
        default Response<it.polimi.ingsw.net.responses.NullResponse> nullResponse(Request<T> request) {
            return new Response<>(
                    new Header(request.getHeader().getEndPointFunction()),
                    new it.polimi.ingsw.net.responses.NullResponse()
            );
        }
    }
}
