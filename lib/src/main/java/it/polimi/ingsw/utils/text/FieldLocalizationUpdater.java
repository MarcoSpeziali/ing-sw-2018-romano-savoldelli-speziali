package it.polimi.ingsw.utils.text;

import java.lang.reflect.Field;

// TODO: docs
@FunctionalInterface
public interface FieldLocalizationUpdater {
    void update(Field field, String localizedText, Object caller) throws IllegalAccessException;
}
