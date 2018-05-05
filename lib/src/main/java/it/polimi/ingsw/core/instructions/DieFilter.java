package it.polimi.ingsw.core.instructions;

public enum DieFilter {
    SHADE,
    COLOR;

    public static DieFilter fromString(String stringRep) {
        stringRep = stringRep.toLowerCase().trim();

        switch (stringRep) {
            case "shade":
                return DieFilter.SHADE;
            case "color":
                return DieFilter.COLOR;
            default:
                return null;
        }
    }
}
