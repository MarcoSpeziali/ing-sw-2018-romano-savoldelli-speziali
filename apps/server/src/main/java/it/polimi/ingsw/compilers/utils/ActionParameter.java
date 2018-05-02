package it.polimi.ingsw.compilers.utils;

// TODO: docs
public class ActionParameter {
    private Class<?> type;
    private Integer position;
    private ActionParameterValue value;
    private String optionalName;
    private Boolean isOptional;
    private String defaultValue;

    public ActionParameter(Class<?> type, Integer position, ActionParameterValue value, String optionalName, String defaultValue) {
        this.type = type;
        this.position = position;
        this.value = value;
        this.optionalName = optionalName;
        this.isOptional = optionalName != null;
        this.defaultValue = defaultValue;
    }

    public Class<?> getType() {
        return type;
    }

    public Integer getPosition() {
        return position;
    }

    public ActionParameterValue getValue() {
        return value;
    }

    public String getOptionalName() {
        return optionalName;
    }

    public Boolean getOptional() {
        return isOptional;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}