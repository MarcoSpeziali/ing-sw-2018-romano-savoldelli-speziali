package it.polimi.ingsw.utils.io.json;

import it.polimi.ingsw.utils.ReflectionUtils;
import it.polimi.ingsw.utils.streams.FunctionalExceptionWrapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

// TODO: 20/06/18 document
class JSONDeserializationHelper {

    private JSONDeserializationHelper() {}

    static <T extends JSONSerializable> T deserialize(Class<T> targetClass, JSONObject jsonObject) {
        jsonObject = JSONDeserializationHelper.parseRootElement(targetClass, jsonObject);

        Constructor<?> designatedConstructor = ReflectionUtils.findAnnotatedConstructor(targetClass, JSONDesignatedConstructor.class);
        
        if (designatedConstructor != null) {
            try {
                return parseDesignatedConstructor(designatedConstructor, jsonObject);
            }
            catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new JSONDeserializationException(
                        "Cannot instantiate class: reflective exception thrown",
                        e
                );
            }
        }

        try {
            Constructor<T> defaultConstructor = targetClass.getDeclaredConstructor();
            return parseDefaultConstructor(targetClass, defaultConstructor, jsonObject);
        }
        catch (NoSuchMethodException e) {
            throw new JSONDeserializationException(String.format(
                    "Cannot instantiate class %s because it does not declare an empty constructor",
                    targetClass.getName()
            ));
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new JSONDeserializationException(
                    "Cannot instantiate class: reflective exception thrown",
                    e
            );
        }
    }

    private static JSONObject parseRootElement(Class<?> targetClass, JSONObject jsonObject) {
        JSONRootElement rootElement = targetClass.getAnnotation(JSONRootElement.class);

        if (rootElement != null) {
            if (jsonObject.has(rootElement.value())) {
                jsonObject = jsonObject.getJSONObject(rootElement.value());
            }
            else if (!rootElement.isOptional()) {
                throw new JSONDeserializationException(String.format(
                        "Cannot deserialize json \"%s\" because the class %s expects the root element to be %s and it is not marked as optional",
                        jsonObject,
                        targetClass.getName(),
                        rootElement.value()
                ));
            }
        }

        return jsonObject;
    }

    private static <T extends JSONSerializable> T parseDesignatedConstructor(Constructor<?> designatedConstructor, JSONObject jsonObject) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?>[] parameters = designatedConstructor.getParameterTypes();
        JSONElement[] parametersElements = extractJSONElementsFromParameters(designatedConstructor.getParameterAnnotations());

        Object[] args = new Object[parameters.length];

        for (int i = 0; i < args.length; i++) {
            if (parametersElements[i].keepRaw()) {
                args[i] = getOrNull(jsonObject, parametersElements[i].value());
            }
            else {
                args[i] = deserializeObject(parameters[i], parametersElements[i], jsonObject);
            }
        }

        designatedConstructor.setAccessible(true);

        @SuppressWarnings("unchecked")
        T instance = (T) designatedConstructor.newInstance(args);

        designatedConstructor.setAccessible(false);

        return instance;
    }

    private static <T extends JSONSerializable> T parseDefaultConstructor(Class<T> targetClass, Constructor<T> defaultConstructor, JSONObject jsonObject) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        defaultConstructor.setAccessible(true);

        T instance = defaultConstructor.newInstance();

        defaultConstructor.setAccessible(false);

        try {
            Arrays.stream(targetClass.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(JSONElement.class))
                    .forEach(field -> {
                        field.setAccessible(true);

                        try {
                            field.set(instance, deserializeObject(field.getType(), field.getAnnotation(JSONElement.class), jsonObject));
                        }
                        catch (IllegalAccessException e) {
                            FunctionalExceptionWrapper.wrap(e);
                        }

                        field.setAccessible(false);
                    });
        }
        catch (FunctionalExceptionWrapper e) {
            e.tryFinalUnwrap(IllegalAccessException.class);
        }

        return instance;
    }

    private static JSONElement[] extractJSONElementsFromParameters(Annotation[][] parameters) {
        JSONElement[] parametersElements = new JSONElement[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            for (int j = 0; j < parameters[i].length; j++) {
                if (parameters[i][j].annotationType().equals(JSONElement.class)) {
                    parametersElements[i] = (JSONElement) parameters[i][j];
                    break;
                }
            }
        }

        return parametersElements;
    }

    private static Object deserializeObject(Class<?> targetClass, JSONElement elementInfo, JSONObject jsonObject) {
        if (targetClass.isArray()) {
            return getArrayObject(targetClass, elementInfo, jsonObject);
        }
        else if (List.class.isAssignableFrom(targetClass)) {
            Object obj = getArrayObject(targetClass, elementInfo, jsonObject);
            
            if (obj == null) {
                return null;
            }
            
            return List.of((Object[]) obj);
        }
        else if (Set.class.isAssignableFrom(targetClass)) {
            Object obj = getArrayObject(targetClass, elementInfo, jsonObject);
    
            if (obj == null) {
                return null;
            }

            if (obj instanceof JSONArray) {
                return new HashSet<>(((JSONArray) obj).toList());
            }

            return Set.of((Object[]) obj);
        }
        else if (Map.class.isAssignableFrom(targetClass)) {
            return getMapOrNull(elementInfo, jsonObject);
        }
        else if (JSONSerializable.class.isAssignableFrom(targetClass)) {
            if (jsonObject.has(elementInfo.value())) {
                return deserialize(targetClass.asSubclass(JSONSerializable.class), jsonObject.getJSONObject(elementInfo.value()));
            }

            return null;
        }

        return getStandardObject(targetClass, elementInfo, jsonObject);
    }

    private static Object getStandardObject(Class<?> targetClass, JSONElement elementInfo, JSONObject jsonObject) {
        if (targetClass.equals(String.class)) {
            return getOrNull(jsonObject, elementInfo.value());
        }
        else if (targetClass.equals(Integer.class) || targetClass.equals(int.class)) {
            return jsonObject.has(elementInfo.value()) ? jsonObject.getInt(elementInfo.value()) : 0;
        }
        else if (targetClass.equals(Byte.class) || targetClass.equals(byte.class)) {
            return jsonObject.has(elementInfo.value()) ? (byte) jsonObject.getInt(elementInfo.value()) : (byte) 0;
        }
        else if (targetClass.equals(Long.class) || targetClass.equals(long.class)) {
            return jsonObject.has(elementInfo.value()) ? jsonObject.getLong(elementInfo.value()) : 0L;
        }
        else if (targetClass.equals(Float.class) || targetClass.equals(float.class)) {
            return jsonObject.has(elementInfo.value()) ? jsonObject.getFloat(elementInfo.value()) : 0F;
        }
        else if (targetClass.equals(Double.class) || targetClass.equals(double.class)) {
            return jsonObject.has(elementInfo.value()) ? jsonObject.getDouble(elementInfo.value()) : 0D;
        }
        else if (targetClass.equals(BigDecimal.class)) {
            return jsonObject.has(elementInfo.value()) ? jsonObject.getBigDecimal(elementInfo.value()) : BigDecimal.ZERO;
        }
        else if (targetClass.equals(BigInteger.class)) {
            return jsonObject.has(elementInfo.value()) ? jsonObject.getBigInteger(elementInfo.value()) : BigInteger.ZERO;
        }
        else if (targetClass.equals(Boolean.class) || targetClass.equals(boolean.class)) {
            return getOrFalse(jsonObject, elementInfo.value());
        }
        else if (targetClass.isEnum()) {
            return getEnumOrNull(jsonObject, elementInfo.value(), targetClass.asSubclass(Enum.class));
        }
        else if (targetClass.equals(Number.class)) {
            return jsonObject.has(elementInfo.value()) ? jsonObject.getNumber(elementInfo.value()) : 0;
        }

        return null;
    }

    private static <T> Object getArrayObject(Class<?> targetClass, JSONElement elementInfo, JSONObject jsonObject) {
        if (JSONSerializable[].class.isAssignableFrom(targetClass)) {
            Object array = getOrNull(jsonObject, elementInfo.value());

            if (array == null) {
                return null;
            }

            JSONObject[] jsonObjects;

            if (array instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) array;

                jsonObjects = new JSONObject[jsonArray.length()];

                for (int i = 0; i < jsonObjects.length; i++) {
                    jsonObjects[i] = jsonArray.getJSONObject(i);
                }
            }
            else if (array instanceof JSONObject[]) {
                jsonObjects = (JSONObject[]) array;
            }
            else {
                Object[] objects = (Object[]) array;

                jsonObjects = Arrays.copyOf(objects, objects.length, JSONObject[].class);
            }

            Object[] objects = getJSONSerializableObjects(targetClass.getComponentType(), jsonObjects);

            //noinspection unchecked
            return Arrays.copyOf(objects, objects.length, (Class<T[]>) targetClass);
        }
        else {
            return getOrNull(jsonObject, elementInfo.value());
        }
    }
    
    private static Map getMapOrNull(JSONElement elementInfo, JSONObject jsonObject) {
        if (!jsonObject.has(elementInfo.value())) {
            return null;
        }

        Object obj = jsonObject.get(elementInfo.value());

        if (obj instanceof Map) {
            return ((Map) obj);
        }
        else if (obj instanceof JSONObject) {
            return Map.of();
        }

        return null;
    }

    private static <T extends JSONSerializable> Object[] getJSONSerializableObjects(Class<?> targetClass, JSONObject[] jsonObjects) {
        Object[] objects = new Object[jsonObjects.length];

        for (int i = 0; i < objects.length; i++) {
            //noinspection unchecked
            objects[i] = deserialize((Class<T>) targetClass, jsonObjects[i]);
        }

        return objects;
    }

    private static Object getOrNull(JSONObject jsonObject, String key) {
        return jsonObject.has(key) ? jsonObject.get(key) : null;
    }

    private static boolean getOrFalse(JSONObject jsonObject, String key) {
        return jsonObject.has(key) && jsonObject.getBoolean(key);
    }

    private static <E extends Enum<E>> E getEnumOrNull(JSONObject jsonObject, String key, Class<E> enumClass) {
        return jsonObject.has(key) ? jsonObject.getEnum(enumClass, key) : null;
    }
}
