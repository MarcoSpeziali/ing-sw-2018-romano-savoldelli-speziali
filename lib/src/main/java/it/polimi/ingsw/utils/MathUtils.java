package it.polimi.ingsw.utils;

public final class MathUtils {
    private MathUtils() {
    }

    /**
     * @param number the number to modulate
     * @param module the module
     * @return the modulated number
     */
    public static int modular(int number, int module) {
        return ((number % module) + module) % module;
    }
}
