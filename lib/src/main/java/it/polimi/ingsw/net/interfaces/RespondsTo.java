package it.polimi.ingsw.net.interfaces;

import it.polimi.ingsw.net.utils.EndPointFunction;

import java.lang.annotation.*;

/**
 * TODO: docs
 */
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RespondsTo {
    EndPointFunction value();
}