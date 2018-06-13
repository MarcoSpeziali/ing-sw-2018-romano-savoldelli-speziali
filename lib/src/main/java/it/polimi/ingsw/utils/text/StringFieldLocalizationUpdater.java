package it.polimi.ingsw.utils.text;

import java.lang.reflect.Field;

public class StringFieldLocalizationUpdater implements FieldLocalizationUpdater {
    @Override
    public void update(Field field, String localizedText, Object caller) throws IllegalAccessException {
        field.set(caller, localizedText);
    }
}
