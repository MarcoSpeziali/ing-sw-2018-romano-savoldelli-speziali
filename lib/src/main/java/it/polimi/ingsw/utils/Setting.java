package it.polimi.ingsw.utils;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.Map;

// TODO: docs
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Setting {
    String id();

    String defaultValue();

    Class<?> type() default String.class;

    class Builder {
        private Builder() {
        }

        public static void build(Object targetObject, Map<String, String> settings) throws IllegalAccessException {
            Class<?> targetClass = targetObject.getClass();

            for (Field field : targetClass.getDeclaredFields()) {
                Setting settingAnnotation = field.getAnnotation(Setting.class);

                if (settingAnnotation != null) {
                    field.setAccessible(true);

                    String value = settings.getOrDefault(settingAnnotation.id(), settingAnnotation.defaultValue());

                    if (settingAnnotation.type().equals(String.class)) {
                        field.set(targetObject, value);
                    }
                    else if (settingAnnotation.type().isEnum()) {
                        field.set(targetObject, Enum.valueOf(field.getType().asSubclass(Enum.class), value));
                    }
                    else if (settingAnnotation.type().equals(Integer.class)) {
                        field.set(targetObject, Integer.parseInt(value));
                    }
                    else if (settingAnnotation.type().equals(Long.class)) {
                        field.set(targetObject, Long.parseLong(value));
                    }
                    else if (settingAnnotation.type().equals(Float.class)) {
                        field.set(targetObject, Float.parseFloat(value));
                    }
                    else if (settingAnnotation.type().equals(Boolean.class)) {
                        field.set(targetObject, Boolean.parseBoolean(value));
                    }
                }

                field.setAccessible(false);
            }
        }
    }
}
