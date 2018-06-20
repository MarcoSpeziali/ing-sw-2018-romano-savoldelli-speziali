package it.polimi.ingsw.utils.io.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO: 20/06/18 document
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONRootElement {
    String value();
    boolean isOptional() default false;
}
