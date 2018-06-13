package it.polimi.ingsw.utils.text;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;

// TODO: docs
@Repeatable(LocalizedText.List.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalizedText {
    String key();
    Class<? extends FieldLocalizationUpdater> fieldUpdater();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface List {
        LocalizedText[] value();
    }

    class Updater {
        private Updater() {}

        public static void update(Object classInstance) {
            StackWalker walker = StackWalker.getInstance(RETAIN_CLASS_REFERENCE);

            Class<?> callerClass = walker.getCallerClass();

            Map<Field, LocalizedText[]> fieldsToLocalize = Arrays.stream(callerClass.getDeclaredFields())
                    .filter(field -> field.getAnnotation(LocalizedText.class) != null)
                    .collect(Collectors.toMap(o -> o, o -> o.getAnnotationsByType(LocalizedText.class)));

            fieldsToLocalize.putAll(Arrays.stream(callerClass.getDeclaredFields())
                    .filter(field -> field.getAnnotation(LocalizedText.List.class) != null)
                    .collect(Collectors.toMap(o -> o, o -> o.getAnnotation(LocalizedText.List.class).value()))
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
    }
}
