package it.polimi.ingsw.server.net.handlers;

import it.polimi.ingsw.net.utils.EndPointFunction;

import java.lang.annotation.*;

/**
 * TODO: docs
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Handles {
    EndPointFunction value();
}
