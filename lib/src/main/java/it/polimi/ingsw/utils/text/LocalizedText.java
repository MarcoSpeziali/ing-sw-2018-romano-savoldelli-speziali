package it.polimi.ingsw.utils.text;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

// TODO: docs
@Repeatable(LocalizedText.LocalizedFields.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalizedText {
    String key();
    Class<? extends FieldLocalizationUpdater> fieldUpdater();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface LocalizedFields {
        LocalizedText[] value();
    }

    class Updater implements LocalizedString.OnLocaleChanged {

        private static List<Object> listeners = new LinkedList<>();

        private static boolean alreadyRegistered = false;

        private Updater(Object callerInstance) {
            if (!listeners.contains(callerInstance)) {
                listeners.add(callerInstance);
            }

            if (!alreadyRegistered) {
                alreadyRegistered = true;

                LocalizedString.addListener(this);
            }

            this.build(callerInstance);
        }

        public static void update(Object classInstance) {
            new Updater(classInstance);
        }

        private void build(Object classInstance) {
            Class<?> callerClass = classInstance.getClass();

            Map<Field, LocalizedText[]> fieldsToLocalize = Arrays.stream(callerClass.getDeclaredFields())
                    .filter(field -> field.getAnnotation(LocalizedText.class) != null)
                    .collect(Collectors.toMap(o -> o, o -> o.getAnnotationsByType(LocalizedText.class)));

            fieldsToLocalize.putAll(Arrays.stream(callerClass.getDeclaredFields())
                    .filter(field -> field.getAnnotation(LocalizedText.LocalizedFields.class) != null)
                    .collect(Collectors.toMap(o -> o, o -> o.getAnnotation(LocalizedText.LocalizedFields.class).value()))
            );

            fieldsToLocalize.forEach((field, annotations) -> {
                try {
                    field.setAccessible(true);
                    for (LocalizedText annotation : annotations) {
                        FieldLocalizationUpdater updater = annotation.fieldUpdater()
                                .getConstructor()
                                .newInstance();

                        LocalizedString localizedString = new LocalizedString(
                                annotation.key()
                        );

                        updater.update(field, localizedString.toString(), classInstance);
                    }
                    field.setAccessible(false);
                }
                catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                    // rethrow the exception
                    throw new RuntimeException(e);
                }
            });
        }

        @Override
        public void onLocaleChanged(Locale oldLocale, Locale newLocale) {
            listeners.forEach(Updater::update);
        }
    }
}
