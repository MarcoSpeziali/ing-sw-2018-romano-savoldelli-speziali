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
    /**
     * Gets the {@link EndPointFunction}s handles by the decorated type.
     *
     * @return the {@link EndPointFunction}s handles by the decorated type
     */
    EndPointFunction[] value();

    /**
     * Gets whether the {@link EndPointFunction} returned by {@link #value()} requires the user to be authenticated.
     *
     * @return whether the {@link EndPointFunction} returned by {@link #value()} requires the user to be authenticated
     */
    boolean requiresAuthentication() default false;
}
