package it.polimi.ingsw.utils.text;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Repeatable(Localized.LocalizedFields.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Localized {
    /**
     * Gets the localization key associated with the decorated field.
     *
     * @return the localization key associated with the decorated field
     */
    String key();

    /**
     * Gets a {@link Class} which implements {@link FieldLocalizationUpdater} which is responsible for updating the field with the localized text.
     *
     * @return a {@link Class} which implements {@link FieldLocalizationUpdater} which is responsible for updating the field with the localized text
     */
    Class<? extends FieldLocalizationUpdater> fieldUpdater();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface LocalizedFields {
        Localized[] value();
    }

    class Updater implements LocalizedString.OnLocaleChanged {

        /**
         * Holds the listeners
         */
        private static final List<Object> listeners = new LinkedList<>();

        /**
         * Holds a map of {@link Class} and {@link Map} of {@link Field} and {@link Localized[]},
         * used to cache the fields to update in a class.
         */
        private static final Map<Class<?>, Map<Field, Localized[]>> fieldsTable = new HashMap<>();

        /**
         * Holds whether this class has already registered for localizations updates.
         */
        private static boolean alreadyRegistered = false;

        /**
         * @param callerInstance the instance of the calling class
         */
        private Updater(Object callerInstance) {
            // in case of multiple calls it is better to synchronize this constructor
            synchronized (listeners) {
                // the object could be already registered as listener
                if (!listeners.contains(callerInstance)) {
                    listeners.add(callerInstance);
                }

                // this class needs to be registered as listener of LocalizedString.OnLocaleChanged
                if (!alreadyRegistered) {
                    alreadyRegistered = true;

                    LocalizedString.addListener(this);
                }
            }

            // adds this object to the fields table if needed
            this.buildIfNeeded(callerInstance.getClass());
            // performs the update
            this.performUpdate(callerInstance);
        }

        /**
         * Updates the fields annotated with {@link Localized} the value of {@link Localized#key()} for the current {@link Locale},
         * the updates are performed by calling {@link FieldLocalizationUpdater#update(Field, String, Object)} by instantiating (with an empty constructor)
         * the provided {@link Localized#fieldUpdater()}.
         *
         * @param classInstance the instance of the calling class
         */
        public static void update(Object classInstance) {
            new Updater(classInstance);
        }
    
        /**
         * @param classInstance the instance of the calling class
         */
        public static void remove(Object classInstance) {
            listeners.remove(classInstance);
            fieldsTable.remove(classInstance.getClass());
        }

        /**
         * Adds to {@link #fieldsTable} the fields of {@code currentInstance} if needed.
         *
         * @param callerClass the calling class
         */
        private void buildIfNeeded(Class<?> callerClass) {
            if (!fieldsTable.containsKey(callerClass)) {
                Map<Field, Localized[]> fieldsToLocalize = Arrays.stream(callerClass.getDeclaredFields())
                        .filter(field -> field.getAnnotation(Localized.class) != null)
                        .collect(Collectors.toMap(o -> o, o -> o.getAnnotationsByType(Localized.class)));

                fieldsToLocalize.putAll(Arrays.stream(callerClass.getDeclaredFields())
                        .filter(field -> field.getAnnotation(Localized.LocalizedFields.class) != null)
                        .collect(Collectors.toMap(o -> o, o -> o.getAnnotation(Localized.LocalizedFields.class).value()))
                );

                fieldsTable.put(callerClass, fieldsToLocalize);
            }
        }

        /**
         * Performs the update.
         *
         * @param classInstance the instance of the calling class
         */
        private void performUpdate(Object classInstance) {
            Class<?> callerClass = classInstance.getClass();

            fieldsTable.getOrDefault(callerClass, Map.of()).forEach((field, annotations) -> {
                try {
                    final boolean wasAccessible = field.canAccess(classInstance);

                    // the field could be private so it is set to be accessible
                    field.setAccessible(true);

                    for (Localized annotation : annotations) {
                        // instantiates the updater
                        FieldLocalizationUpdater updater = annotation.fieldUpdater()
                                .getConstructor()
                                .newInstance();

                        // gets the localized value
                        LocalizedString localizedString = new LocalizedString(
                                annotation.key()
                        );

                        // delegates to the updater the update operation
                        updater.update(field, localizedString.toString(), classInstance);
                    }

                    // resets the field accessibility
                    field.setAccessible(wasAccessible);
                }
                catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                    // rethrow the exception
                    throw new RuntimeException(e); // TODO: better handle exceptions
                }
            });
        }

        @Override
        public void onLocaleChanged(Locale oldLocale, Locale newLocale) {
            // foreach listener
            listeners.forEach(Updater::update);
        }
    }
}
