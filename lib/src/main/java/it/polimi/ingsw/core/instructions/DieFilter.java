package it.polimi.ingsw.core.instructions;

public enum DieFilter {
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
