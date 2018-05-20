package it.polimi.ingsw.server.utils;

import joptsimple.ValueConverter;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Converts a string into a {@link java.util.logging.Level}.
 */
public class LoggingLevelValueConverter implements ValueConverter<Level> {
    @Override
    public Level convert(String s) {
        return Level.parse(s.toUpperCase());
    }

    @Override
    public Class<? extends Level> valueType() {
        return Level.class;
    }

    @Override
    public String valuePattern() {
        return List.of(
                Level.SEVERE,
                Level.WARNING,
                Level.INFO,
                Level.FINE,
                Level.FINER,
                Level.FINEST,
                Level.ALL,
                Level.OFF
        ).stream()
                .map(Level::getName)
                .map(String::toLowerCase)
                .collect(Collectors.joining("|"));
    }
}
