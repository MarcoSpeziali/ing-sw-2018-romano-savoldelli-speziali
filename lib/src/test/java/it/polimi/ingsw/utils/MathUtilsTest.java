package it.polimi.ingsw.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {

    @Test
    void modular() {
        int m = 5;

        for (int i = -5; i < 0; i++) {
            Assertions.assertEquals(5 + i, MathUtils.modular(i, m));
        }

        for (int i = 0; i < 5; i++) {
            Assertions.assertEquals(i, MathUtils.modular(i, m));
        }

        for (int i = 6; i < 10; i++) {
            Assertions.assertEquals(i - 5, MathUtils.modular(i, m));
        }
    }
}