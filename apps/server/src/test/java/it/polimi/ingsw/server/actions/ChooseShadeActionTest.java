package it.polimi.ingsw.server.actions;

/*
class ChooseShadeActionTest {

    private ChooseShadeAction action;
    private Context context = Context.getSharedInstance();
    private ActionData testData = new NullActionData();

    @BeforeEach
    void setUp() {
        ChooseLocation location = mock(ChooseLocation.class);

        UserInteractionProvider interactionProvider = mock(UserInteractionProvider.class);
        when(interactionProvider.chooseDie(location, null, 0))
                .thenReturn(new Die(4, GlassColor.GREEN));

        action = new ChooseShadeAction(this.testData, context -> location, context -> null);
        action.setUserInteractionProvider(interactionProvider);
    }

    @Test
    void run() {
        Object result = this.action.run(this.context);

        Assertions.assertEquals(Integer.class, result.getClass());
        Assertions.assertEquals(4, ((Integer) result).intValue());
    }
}
*/