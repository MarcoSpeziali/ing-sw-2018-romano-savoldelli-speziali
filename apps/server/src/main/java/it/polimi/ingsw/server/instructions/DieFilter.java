package it.polimi.ingsw.server.instructions;

import java.io.Serializable;

public enum DieFilter implements Serializable {
    SHADE,
    COLOR;

    public static DieFilter fromString(String stringRepresentation) {
        stringRepresentation = stringRepresentation.toLowerCase().trim();

        switch (stringRepresentation) {
            case "shade":
                return DieFilter.SHADE;
            case "color":
                return DieFilter.COLOR;
            default:
                throw new IllegalArgumentException("Unrecognized filter: " + stringRepresentation);
        }
    }
}
