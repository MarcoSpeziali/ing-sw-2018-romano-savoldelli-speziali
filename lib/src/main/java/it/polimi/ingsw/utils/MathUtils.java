package it.polimi.ingsw.utils;

public final class MathUtils {
    private MathUtils() {}

    public static int modular(int number, int module) {
        return ((number % module) + module) % module;
    }
}
