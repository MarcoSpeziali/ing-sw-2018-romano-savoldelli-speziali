package it.polimi.ingsw.utils.io.json;

import java.lang.annotation.*;

// TODO: 20/06/18 document
@Inherited
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONElement {
    String value();
    boolean keepRaw() default false;
}
