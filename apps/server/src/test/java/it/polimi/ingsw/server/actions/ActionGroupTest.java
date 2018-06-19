package it.polimi.ingsw.server.actions;

import it.polimi.ingsw.core.Context;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.UserInteractionProvider;
import it.polimi.ingsw.core.locations.ChoosablePickLocation;
import it.polimi.ingsw.core.locations.ChoosablePutLocation;
import it.polimi.ingsw.core.locations.ChooseLocation;
import it.polimi.ingsw.core.locations.RandomPutLocation;
import it.polimi.ingsw.models.Die;
import it.polimi.ingsw.server.constraints.Constraint;
import it.polimi.ingsw.server.constraints.ConstraintEvaluationException;
import it.polimi.ingsw.server.constraints.Operator;
import it.polimi.ingsw.utils.IterableRange;
import it.polimi.ingsw.utils.Range;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class ActionGroupTest {

    private ActionGroup actionGroup;
    private ActionData actionData = new NullActionData();
    private Context context = Context.getSharedInstance();
    private UserInteractionProvider interactionProvider;
    private ActionGroupCallbacks actionGroupCallbacks;

    private ChooseLocation chooseLocation;
    private ChoosablePickLocation choosablePickLocation;
    private RandomPutLocation randomPutLocation;
    private ChoosablePutLocation choosablePutLocation;

    @BeforeEach
    void setUp() {
        this.chooseLocation = mock(ChooseLocation.class);
        this.choosablePickLocation = mock(ChoosablePickLocation.class);
        this.randomPutLocation = mock(RandomPutLocation.class);
        this.choosablePutLocation = mock(ChoosablePutLocation.class);

        this.interactionProvider = mock(UserInteractionProvider.class);
        when(this.interactionProvider.chooseDie(this.chooseLocation, null, 0))
                .thenReturn(new Die(3, GlassColor.BLUE));
        when(this.interactionProvider.chooseDie(this.chooseLocation, GlassColor.BLUE, 0))
                .thenReturn(new Die(2, GlassColor.BLUE));

        this.actionGroupCallbacks = mock(ActionGroupCallbacks.class);
    }

    @Test
    void testGetters() {
        this.actionGroup = new ActionGroup(
                this.actionData,
                IterableRange.unitaryInteger(),
                IterableRange.singleValued(15, IterableRange.INTEGER_INCREMENT_FUNCTION),
                null
        );
        this.actionGroup.setCallbacks(this.actionGroupCallbacks);

        Assertions.assertEquals(IterableRange.unitaryInteger(), this.actionGroup.getRepetitionNumber());
        Assertions.assertEquals(IterableRange.singleValued(15, IterableRange.INTEGER_INCREMENT_FUNCTION), this.actionGroup.getChooseBetween());
        Assertions.assertNull(this.actionGroup.getActions());
        Assertions.assertSame(this.actionGroupCallbacks, this.actionGroup.getCallbacks());
    }

    @Test
    void testRun() {
        this.actionGroup = new ActionGroup(
                this.actionData,
                IterableRange.unitaryInteger(),
                IterableRange.singleValued(15, IterableRange.INTEGER_INCREMENT_FUNCTION),
                null
        );
        this.actionGroup.setCallbacks(this.actionGroupCallbacks);

        Assertions.assertNull(this.actionGroup.run(Context.getSharedInstance()));
    }

    @Test
    void testRunThrows() {
        this.actionGroup = new ActionGroup(
                new ActionData(
                        null,
                        new Constraint(
                                null,
                                context -> 12,
                                Operator.EQUALS,
                                context -> 13
                        ),
                        null
                ),
                IterableRange.unitaryInteger(),
                IterableRange.singleValued(15, IterableRange.INTEGER_INCREMENT_FUNCTION),
                null
        );
        this.actionGroup.setCallbacks(this.actionGroupCallbacks);

        Assertions.assertThrows(ConstraintEvaluationException.class, () -> {
            this.actionGroup.run(Context.getSharedInstance());
        });
    }

    @Test
    void testSequential() {
        when(this.chooseLocation.getDice()).thenReturn(List.of(
                new Die(3, GlassColor.BLUE),
                new Die(5, GlassColor.GREEN),
                new Die(6, GlassColor.PURPLE)
        ));

        when(this.choosablePickLocation.pickDie(any(Die.class)))
                .thenReturn(new Die(2, GlassColor.BLUE));

        doAnswer(invocationOnMock -> {
            Die die = invocationOnMock.getArgumentAt(0, Die.class);

            Assertions.assertEquals(GlassColor.BLUE, die.getColor());
            Assertions.assertEquals(5, die.getShade().intValue());

            return null;
        }).when(this.randomPutLocation).putDie(any(Die.class));

        ChooseColorAction chooseColorAction = new ChooseColorAction(
                new ActionData(
                        null,
                        null,
                        "COLOR"
                ),
                context -> this.chooseLocation,
                context -> 0
        );
        chooseColorAction.setUserInteractionProvider(interactionProvider);

        ChooseDieAction chooseDieAction = new ChooseDieAction(
                new ActionData(
                        null,
                        null,
                        "DIE"
                ),
                context -> this.chooseLocation,
                context -> (GlassColor) context.get("COLOR"),
                context -> 0
        );
        chooseDieAction.setUserInteractionProvider(interactionProvider);

        this.actionGroup = new ActionGroup(
                this.actionData,
                new IterableRange<>(1, 1, num -> ++num),
                null,
                List.of(
                        chooseColorAction,
                        chooseDieAction,
                        new PickDieAction(
                                new ActionData(
                                        null,
                                        null,
                                        "DIE"
                                ),
                                context -> this.choosablePickLocation,
                                context -> (Die) context.get("DIE")
                        ),
                        new FlipAction(
                                new ActionData(
                                        null,
                                        null,
                                        null
                                ),
                                context -> (Die) context.get("DIE")
                        ),
                        new PutAction(
                                new ActionData(
                                        null,
                                        null,
                                        null
                                ),
                                context -> (Die) context.get("DIE"),
                                context -> this.randomPutLocation
                        )
                )
        );
        this.actionGroup.setCallbacks(this.actionGroupCallbacks);

        this.actionGroup.run(this.context);
    }

    @Test
    void testRepeated() {
        doNothing().when(this.choosablePutLocation).putDie(any(Die.class), eq(15));

        this.actionGroup = new ActionGroup(
                this.actionData,
                new IterableRange<>(5, 5, num -> ++num),
                null,
                List.of(
                        new PlaceAction(
                                new ActionData(
                                        null,
                                        null,
                                        null
                                ),
                                context -> new Die(4, GlassColor.RED),
                                context -> this.choosablePutLocation,
                                context -> 15
                        )
                )
        );

        this.actionGroup.run(this.context);

        verify(this.choosablePutLocation, times(5)).putDie(any(Die.class), eq(15));
    }

    @Test
    void testOptionalRepetitions() {
        doNothing().when(this.choosablePutLocation).putDie(any(Die.class), eq(15));
        when(this.actionGroupCallbacks.shouldRepeat(anyInt(), eq(5))).thenReturn(false);

        this.actionGroup = new ActionGroup(
                this.actionData,
                new IterableRange<>(0, 5, num -> ++num),
                null,
                List.of(
                        new PlaceAction(
                                new ActionData(
                                        null,
                                        null,
                                        null
                                ),
                                context -> new Die(4, GlassColor.RED),
                                context -> this.choosablePutLocation,
                                context -> 15
                        )
                )
        );
        this.actionGroup.setCallbacks(this.actionGroupCallbacks);

        this.actionGroup.run(this.context);

        verify(this.choosablePutLocation, times(0)).putDie(any(Die.class), eq(15));
    }

    @Test
    void testOptionalRepetitionsWithChooseBetween() {
        doNothing().when(this.choosablePutLocation).putDie(any(Die.class), eq(15));
        when(this.actionGroupCallbacks.shouldRepeat(anyInt(), eq(5))).thenReturn(true);
        when(this.actionGroupCallbacks.getChosenActions(any(), any())).thenAnswer(invocationOnMock -> {
            @SuppressWarnings("unchecked")
            List<ExecutableAction> actions = (List<ExecutableAction>) invocationOnMock.getArguments()[0];
            return List.of(actions.get(0));
        });

        this.actionGroup = new ActionGroup(
                this.actionData,
                new IterableRange<>(0, 5, num -> ++num),
                IterableRange.singleValued(4),
                List.of(
                        new PlaceAction(
                                new ActionData(
                                        null,
                                        null,
                                        null
                                ),
                                context -> new Die(4, GlassColor.RED),
                                context -> this.choosablePutLocation,
                                context -> 15
                        )
                )
        );
        this.actionGroup.setCallbacks(this.actionGroupCallbacks);

        this.actionGroup.run(this.context);

        verify(this.choosablePutLocation, times(5)).putDie(any(Die.class), eq(15));
    }

    @Test
    void testOptionalRepetitionsWithMandatoryRepetitions() {
        doNothing().when(this.choosablePutLocation).putDie(any(Die.class), eq(15));
        when(this.actionGroupCallbacks.shouldRepeat(anyInt(), eq(7))).thenAnswer(invocationOnMock -> {
            Integer alreadyRepeatedFor = invocationOnMock.getArgumentAt(0, Integer.class);

            return alreadyRepeatedFor <= 5;
        });

        this.actionGroup = new ActionGroup(
                this.actionData,
                new IterableRange<>(3, 7, num -> ++num),
                null,
                List.of(
                        new PlaceAction(
                                new ActionData(
                                        null,
                                        null,
                                        null
                                ),
                                context -> new Die(4, GlassColor.RED),
                                context -> this.choosablePutLocation,
                                context -> 15
                        )
                )
        );
        this.actionGroup.setCallbacks(this.actionGroupCallbacks);

        this.actionGroup.run(this.context);

        verify(this.choosablePutLocation, times(6)).putDie(any(Die.class), eq(15));
    }

    @Test
    void testFixedChoice() {
        when(this.actionGroupCallbacks.getChosenActions(any(), any())).thenAnswer(invocationOnMock -> {
            @SuppressWarnings("unchecked")
            List<ExecutableAction> actions = (List<ExecutableAction>) invocationOnMock.getArguments()[0];
            return List.of(actions.get(0));
        });

        Die die = new Die(3, GlassColor.BLUE);

        this.actionGroup = new ActionGroup(
                this.actionData,
                new IterableRange<>(1, 1, num -> ++num),
                Range.singleValued(1),
                List.of(
                        new IncrementAction(
                                new ActionData(
                                        null,
                                        null,
                                        null
                                ),
                                context -> die,
                                context -> 1
                        ),
                        new DecrementAction(
                                new ActionData(
                                        null,
                                        null,
                                        null
                                ),
                                context -> die,
                                context -> 1
                        )
                )
        );
        this.actionGroup.setCallbacks(this.actionGroupCallbacks);

        this.actionGroup.run(this.context);

        Assertions.assertEquals(4, die.getShade().intValue());
        Assertions.assertEquals(GlassColor.BLUE, die.getColor());
    }
}