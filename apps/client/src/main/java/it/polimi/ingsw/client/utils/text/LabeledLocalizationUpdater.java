package it.polimi.ingsw.client.utils.text;

import it.polimi.ingsw.utils.text.FieldLocalizationUpdater;
import javafx.scene.control.Labeled;

import java.lang.reflect.Field;

public class LabeledLocalizationUpdater implements FieldLocalizationUpdater {
    @Override
    public void update(Field field, String localizedText, Object caller) throws IllegalAccessException {
        Labeled label = (Labeled) field.get(caller);
        label.setText(localizedText);
    }
}
