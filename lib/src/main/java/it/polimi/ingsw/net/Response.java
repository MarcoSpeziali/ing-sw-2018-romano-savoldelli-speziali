package it.polimi.ingsw.net;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Response implements Serializable {

    private static final long serialVersionUID = -3163357263339164222L;
    private Map<String, Object> body = new HashMap<>();
    private ResponseError responseError;

}
