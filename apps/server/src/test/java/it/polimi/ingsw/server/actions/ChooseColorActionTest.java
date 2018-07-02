package it.polimi.ingsw.server.actions;

/*
class ChooseColorActionTest {

    private ChooseColorAction action;
    private Context context = Context.getSharedInstance();
    private ActionData testData = new NullActionData();

    @BeforeEach
    void setUp() {
        ChooseLocation location = mock(ChooseLocation.class);

        UserInteractionProvider interactionProvider = mock(UserInteractionProvider.class);
        when(interactionProvider.chooseDie(location, null, 0))
                .thenReturn(new Die(4, GlassColor.GREEN));

        action = new ChooseColorAction(this.testData, context -> location, context -> 0);
        action.setUserInteractionProvider(interactionProvider);
    }

    @Test
    void run() {
        Object result = this.action.run(this.context);

        Assertions.assertEquals(GlassColor.class, result.getClass());
        Assertions.assertEquals(GlassColor.GREEN, result);
    }
}
*/