package it.polimi.ingsw.server.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

class LoggingLevelValueConverterTest {

    private LoggingLevelValueConverter loggingLevelValueConverter = new LoggingLevelValueConverter();

    @Test
    void testConvert() {
        Assertions.assertEquals(Level.INFO, this.loggingLevelValueConverter.convert("info"));
        Assertions.assertEquals(Level.INFO, this.loggingLevelValueConverter.convert("INFO"));
        Assertions.assertEquals(Level.INFO, this.loggingLevelValueConverter.convert("iNfO"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            this.loggingLevelValueConverter.convert("__awda");
        });
    }

    @Test
    void testValueType() {
        Assertions.assertEquals(Level.class, this.loggingLevelValueConverter.valueType());
    }

    @Test
    void testValuePattern() {
        Assertions.assertEquals(
                "severe|warning|info|fine|finer|finest|all|off",
                this.loggingLevelValueConverter.valuePattern()
        );
    }
}