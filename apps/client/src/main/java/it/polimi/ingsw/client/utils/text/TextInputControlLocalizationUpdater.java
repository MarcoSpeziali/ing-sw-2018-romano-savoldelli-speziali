package it.polimi.ingsw.client.utils.text;

import it.polimi.ingsw.utils.text.FieldLocalizationUpdater;
import javafx.scene.control.TextInputControl;

import java.lang.reflect.Field;

public class TextInputControlLocalizationUpdater implements FieldLocalizationUpdater {
    @Override
    public void update(Field field, String localizedText, Object caller) throws IllegalAccessException {
        TextInputControl textField = (TextInputControl) field.get(caller);
        textField.setText(localizedText);
    }
}
