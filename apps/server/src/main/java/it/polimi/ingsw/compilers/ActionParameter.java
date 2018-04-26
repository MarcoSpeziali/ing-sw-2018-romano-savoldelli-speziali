package it.polimi.ingsw.compilers;

public class ActionParameter {
    Class<?> type;
    Integer position;
    ActionParameterValue value;
    String optionalName;
    Boolean isOptional;
    String defaultValue;

    public ActionParameter(Class<?> type, Integer position, ActionParameterValue value, String optionalName, String defaultValue) {
        this.type = type;
        this.position = position;
        this.value = value;
        this.optionalName = optionalName;
        this.isOptional = optionalName != null;
        this.defaultValue = defaultValue;
    }

}