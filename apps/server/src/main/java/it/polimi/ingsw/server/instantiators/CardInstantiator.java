package it.polimi.ingsw.server.instantiators;

import it.polimi.ingsw.models.ObjectiveCard;
import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.server.compilers.cards.CompiledObjectiveCard;
import it.polimi.ingsw.server.compilers.cards.CompiledToolCard;

import java.lang.reflect.InvocationTargetException;

public class CardInstantiator {
    private CardInstantiator() {
    }

    /**
     * @param toolCard the {@link CompiledToolCard} to instantiate
     * @return an instance of {@link ToolCard}
     * @throws NoSuchMethodException     if the constructor could not be found
     * @throws IllegalAccessException    if this {@code Constructor} object
     *                                   is enforcing Java language access control and the underlying
     *                                   constructor is inaccessible
     * @throws InstantiationException    if the class that declares the
     *                                   underlying constructor represents an abstract class
     * @throws InvocationTargetException if the underlying constructor
     *                                   throws an exception
     */
    public static ToolCard instantiate(CompiledToolCard toolCard) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return new ToolCard(
                toolCard.getId(),
                toolCard.getName(),
                toolCard.getEffect().getDescription(),
                EffectInstantiator.instantiate(toolCard.getEffect())
        );
    }

    /**
     * @param objectiveCard the {@link CompiledObjectiveCard} to instantiate
     * @return an instance of {@link ObjectiveCard}
     * @throws NoSuchMethodException     if the constructor could not be found
     * @throws IllegalAccessException    if this {@code Constructor} object
     *                                   is enforcing Java language access control and the underlying
     *                                   constructor is inaccessible
     * @throws InstantiationException    if the class that declares the
     *                                   underlying constructor represents an abstract class
     * @throws InvocationTargetException if the underlying constructor
     *                                   throws an exception
     */
    public static ObjectiveCard instantiate(CompiledObjectiveCard objectiveCard) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return new ObjectiveCard(
                objectiveCard.getId(),
                objectiveCard.getVisibility(),
                objectiveCard.getName(),
                objectiveCard.getObjective().getDescription(),
                ObjectiveInstantiator.instantiate(objectiveCard.getObjective())
        );
    }
}
