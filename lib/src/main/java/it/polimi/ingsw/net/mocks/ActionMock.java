package it.polimi.ingsw.net.mocks;

import it.polimi.ingsw.utils.io.json.JSONDesignatedConstructor;
import it.polimi.ingsw.utils.io.json.JSONElement;

public class ActionMock implements IAction {

    private static final long serialVersionUID = -3082010930924224539L;

    //private final String id;
    //private final String name;
    private final String description;

    public ActionMock(IAction action) {
        this(action.getDescription());
    }

    @JSONDesignatedConstructor
    public ActionMock(
            //@JSONElement("id") String id,
            //@JSONElement("name") String name,
            @JSONElement("description") String description
    ) {
        //this.id = id;
        //this.name = name;
        this.description = description;
    }

    /*@Override
    @JSONElement("id")
    public String getId() {
        return id;
    }

    @Override
    @JSONElement("name")
    public String getName() {
        return name;
    }*/

    @Override
    @JSONElement("description")
    public String getDescription() {
        return description;
    }
}
