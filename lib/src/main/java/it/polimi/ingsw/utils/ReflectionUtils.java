package it.polimi.ingsw.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

// TODO: docs
public final class ReflectionUtils {

    private ReflectionUtils() { }

    public static List<Method> findAnnotatedMethods(Class<?> targetClass, Class<? extends Annotation> annotationClass) {
        Method[] methods = targetClass.getMethods();
        List<Method> annotatedMethods = new ArrayList<>(methods.length);

        for (Method method : methods) {
            if (method.isAnnotationPresent(annotationClass)) {
                annotatedMethods.add(method);
            }
        }

        return annotatedMethods;
    }

    public static <T extends Annotation> Method findAnnotatedMethod(Class<?> targetClass, Class<T> annotationClass, Predicate<T> filter) {
        return findAnnotatedMethods(targetClass, annotationClass).stream()
                .filter(method -> filter.test(method.getAnnotation(annotationClass)))
                .findFirst()
                .orElse(null);
    }
}
