package it.polimi.ingsw.utils.io.json;

import it.polimi.ingsw.utils.streams.ExceptionWrapper;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// TODO: 20/06/18 document
class JSONSerializationHelper {
    private JSONSerializationHelper() {}

    static <T extends JSONSerializable> JSONObject serialize(T object) {
        JSONObject jsonObject = new JSONObject();

        try {
            Map<String, Object> objectMap = getFieldsValue(object);
            objectMap.putAll(getMethodsValue(object));

            objectMap.forEach(jsonObject::put);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new JSONSerializationException("Cannot access field or method: reflective exception thrown", e);
        }

        if (object.getClass().isAnnotationPresent(JSONRootElement.class)) {
            JSONRootElement jsonRootElement = object.getClass().getAnnotation(JSONRootElement.class);

            JSONObject rootObject = new JSONObject();
            return rootObject.put(jsonRootElement.value(), jsonObject);
        }

        return jsonObject;
    }

    private static <T extends JSONSerializable> Map<String, Object> getFieldsValue(T instance) throws IllegalAccessException {
        Map<String, Object> objectMap = new HashMap<>();

        try {
            Arrays.stream(instance.getClass().getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(JSONElement.class))
                    .forEach(field -> {
                        field.setAccessible(true);

                        JSONElement jsonElement = field.getAnnotation(JSONElement.class);

                        try {
                            Object value = field.get(instance);

                            if (value == null) {
                                return;
                            }

                            if (JSONSerializable.class.isAssignableFrom(value.getClass())) {
                                value = ((JSONSerializable) value).serialize();
                            }
                            else if (JSONSerializable[].class.isAssignableFrom(value.getClass())) {
                                value = Arrays.stream((JSONSerializable[]) value).map(JSONSerializable::serialize).toArray();
                            }

                            objectMap.put(jsonElement.value(), value);
                        }
                        catch (IllegalAccessException e) {
                            ExceptionWrapper.wrap(e);
                        }
                        finally {
                            field.setAccessible(false);
                        }
                    });
        }
        catch (ExceptionWrapper e) {
            e.tryFinalUnwrap(IllegalAccessException.class);
        }

        return objectMap;
    }

    private static <T extends JSONSerializable> Map<String, Object> getMethodsValue(T instance) throws IllegalAccessException, InvocationTargetException {
        Map<String, Object> objectMap = new HashMap<>();

        try {
            Arrays.stream(instance.getClass().getDeclaredMethods())
                    .filter(field -> field.isAnnotationPresent(JSONElement.class))
                    .forEach(field -> {
                        field.setAccessible(true);

                        JSONElement jsonElement = field.getAnnotation(JSONElement.class);

                        try {
                            Object value = field.invoke(instance);

                            if (value == null) {
                                return;
                            }

                            if (JSONSerializable.class.isAssignableFrom(value.getClass())) {
                                value = ((JSONSerializable) value).serialize();
                            }
                            else if (JSONSerializable[].class.isAssignableFrom(value.getClass())) {
                                value = Arrays.stream((JSONSerializable[]) value).map(JSONSerializable::serialize).toArray();
                            }

                            objectMap.put(jsonElement.value(), value);
                        }
                        catch (IllegalAccessException | InvocationTargetException e) {
                            ExceptionWrapper.wrap(e);
                        }
                        finally {
                            field.setAccessible(false);
                        }
                    });
        }
        catch (ExceptionWrapper e) {
            e.tryUnwrap(InvocationTargetException.class)
                    .tryFinalUnwrap(IllegalAccessException.class);
        }

        return objectMap;
    }
}
