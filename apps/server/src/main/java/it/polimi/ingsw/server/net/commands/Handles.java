package it.polimi.ingsw.server.net.commands;

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
    boolean requiresAuthentication() default false;
}
