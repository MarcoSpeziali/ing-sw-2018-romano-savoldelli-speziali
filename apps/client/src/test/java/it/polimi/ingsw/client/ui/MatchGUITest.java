package it.polimi.ingsw.client.ui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.scenes.MatchGUIView;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.controllers.NotEnoughTokensException;
import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.Move;
import it.polimi.ingsw.net.mocks.*;
import it.polimi.ingsw.net.responses.MoveResponse;
import it.polimi.ingsw.utils.Range;
import it.polimi.ingsw.utils.io.json.JSONSerializable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.mock;


public class MatchGUITest extends Application {

    CellMock[] cells1 = {new CellMock(0, GlassColor.RED), new CellMock(0, GlassColor.BLUE), new CellMock(6, null),
            new CellMock(0, null), new CellMock(4, null), new CellMock(0, GlassColor.PURPLE) };

    CellMock[] cells2 = {new CellMock(0, GlassColor.RED), new CellMock(0, GlassColor.BLUE), new CellMock(6, null),
            new CellMock(0, null), new CellMock(4, null), new CellMock(0, null) };

    CellMock[] cells3 = {new CellMock(0, GlassColor.GREEN), new CellMock(0, GlassColor.BLUE), new CellMock(6, null),
            new CellMock(0, null), new CellMock(4, null), new CellMock(0, null) };

    CellMock[] cells4 = {new CellMock(0, GlassColor.RED), new CellMock(0, GlassColor.YELLOW), new CellMock(6, null),
            new CellMock(0, null), new CellMock(4, null), new CellMock(0, null) };

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Constants.Resources.MATCH_FXML.getURL());
        Parent root = loader.load();
        Scene scene = new Scene(root, 1280, 720);
        MatchGUIView matchGUIView = loader.getController();

        matchGUIView.setModel(new MatchController() {
    
            private static final long serialVersionUID = 5104576545583400399L;
    
            @Override
            public void init(Object... args) throws IOException {
        
            }
    
            @Override
            public void close(Object... args) throws IOException {
        
            }
    
            @Override
            public IMatch waitForUpdate() throws RemoteException, InterruptedException {
                return new IMatch() {
                    private static final long serialVersionUID = 1516527336752132977L;
    
                    @Override
                    public int getId() {
                        return 0;
                    }

                    @Override
                    public long getStartingTime() {
                        return 0;
                    }

                    @Override
                    public long getEndingTime() {
                        return 0;
                    }

                    @Override
                    public ILobby getLobby() {
                        return null;
                    }

                    @Override
                    public ILivePlayer[] getPlayers() {
                        return new ILivePlayer[]{
                                new ILivePlayer() {
                                    private static final long serialVersionUID = -4729773730451937954L;
    
                                    @Override
                                    public int getFavourTokens() {
                                        return 0;
                                    }

                                    @Override
                                    public IWindow getWindow() {
                                        return new WindowMock("giocatore2", 1, 2, 3, cells2);

                                    }

                                    @Override
                                    public IPlayer getPlayer() {
                                        return null;
                                    }
                                }
                        , new ILivePlayer() {
                            private static final long serialVersionUID = 4883402830129829725L;
    
                            @Override
                            public int getFavourTokens() {
                                return 0;
                            }

                            @Override
                            public IWindow getWindow() {
                                return new WindowMock("giocatore3", 1, 2, 3, cells3);
                            }

                            @Override
                            public IPlayer getPlayer() {
                                return null;
                            }
                        }, new ILivePlayer() {
                            private static final long serialVersionUID = 8193853292111083995L;
    
                            @Override
                            public int getFavourTokens() {
                                return 0;
                            }

                            @Override
                            public IWindow getWindow() {
                                return new WindowMock("giocatore4", 1, 2, 3, cells4);
                            }

                            @Override
                            public IPlayer getPlayer() {
                                return null;
                            }
                        }};
                    }

                    @Override
                    public IDraftPool getDraftPool() {
                        return null;
                    }

                    @Override
                    public IRoundTrack getRoundTrack() {
                        return null;
                    }

                    @Override
                    public IObjectiveCard[] getPublicObjectiveCards() {
                        return new IObjectiveCard[]{
                                new IObjectiveCard() {
                                    private static final long serialVersionUID = 7005937290370505338L;
    
                                    @Override
                                    public String getId() {
                                        return "color_diagonals";
                                    }

                                    @Override
                                    public CardVisibility getVisibility() {
                                        return CardVisibility.PUBLIC;
                                    }

                                    @Override
                                    public IObjective getObjective() {
                                        return mock(IObjective.class);
                                    }

                                    @Override
                                    public String getTitle() {
                                        return "title1";
                                    }

                                    @Override
                                    public String getDescription() {
                                        return "description1";
                                    }
                                },
                                new IObjectiveCard() {
                                    private static final long serialVersionUID = 552597124268545053L;
    
                                    @Override
                                    public String getId() {
                                        return "medium_shades";
                                    }

                                    @Override
                                    public CardVisibility getVisibility() {
                                        return CardVisibility.PUBLIC;
                                    }

                                    @Override
                                    public IObjective getObjective() {
                                        return mock(IObjective.class);
                                    }

                                    @Override
                                    public String getTitle() {
                                        return null;
                                    }

                                    @Override
                                    public String getDescription() {
                                        return "description2";
                                    }
                                }
                        };
                    }


                    @Override
                    public IToolCard[] getToolCards() {
    
                        return new IToolCard[]{
                                new IToolCard() {
                                    private static final long serialVersionUID = 590822143671363988L;
    
                                    @Override
                                    public String getCardId() {
                                        return "cork_backed_strainghtedge";
                                    }

                                    @Override
                                    public IEffect getEffect() {
                                        return new EffectMock(3, "ciao");
                                    }

                                    @Override
                                    public String getTitle() {
                                        return null;
                                    }

                                    @Override
                                    public String getDescription() {
                                        return null;
                                    }
                                },
                                new IToolCard() {
                                    private static final long serialVersionUID = -2166360354810458383L;
    
                                    @Override
                                    public String getCardId() {
                                        return "flux_brush";
                                    }

                                    @Override
                                    public IEffect getEffect() {
                                        return new EffectMock(4, "ciao1");
                                    }

                                    @Override
                                    public String getTitle() {
                                        return "title2";
                                    }

                                    @Override
                                    public String getDescription() {
                                        return "description2";
                                    }
                                }, new IToolCard() {
                            private static final long serialVersionUID = 8472747339099896345L;
    
                            @Override
                            public String getCardId() {
                                return "lathekin";
                            }

                            @Override
                            public IEffect getEffect() {
                                return new EffectMock(5, "ciao2");
                            }

                            @Override
                            public String getTitle() {
                                return null;
                            }

                            @Override
                            public String getDescription() {
                                return null;
                            }
                                }};
                    }

                    @Override
                    public IObjectiveCard getPrivateObjectiveCard() {
                        return new IObjectiveCard() {
                            private static final long serialVersionUID = -8405330536339884826L;
    
                            @Override
                            public String getId() {
                                return "red_shade";
                            }

                            @Override
                            public CardVisibility getVisibility() {
                                return CardVisibility.PRIVATE;
                            }

                            @Override
                            public IObjective getObjective() {
                                return mock(IObjective.class);
                            }

                            @Override
                            public String getTitle() {
                                return "titlePrivate";
                            }

                            @Override
                            public String getDescription() {
                                return "descriptionPrivate";
                            }
                        };
                    }
                };
            }
            
            @Override
            public IWindow[] waitForWindowRequest() throws RemoteException {
                CellMock[] cells1 = {new CellMock(0, GlassColor.RED), new CellMock(0, GlassColor.BLUE), new CellMock(6, null),
                        new CellMock(0, null), new CellMock(4, null), new CellMock(0, GlassColor.PURPLE) };
    
                CellMock[] cells2 = {new CellMock(0, GlassColor.RED), new CellMock(0, GlassColor.BLUE), new CellMock(6, null),
                        new CellMock(0, null), new CellMock(4, null), new CellMock(0, null) };
    
                CellMock[] cells3 = {new CellMock(0, GlassColor.GREEN), new CellMock(0, GlassColor.BLUE), new CellMock(6, null),
                        new CellMock(0, null), new CellMock(4, null), new CellMock(0, null) };
    
                CellMock[] cells4 = {new CellMock(0, GlassColor.RED), new CellMock(0, GlassColor.YELLOW), new CellMock(6, null),
                        new CellMock(0, null), new CellMock(4, null), new CellMock(0, null) };
    
                return new IWindow[]{new WindowMock("window 1", 3, 2, 3, cells1),
                        new WindowMock("window 2", 1, 2, 3, cells2),
                        new WindowMock("window 3", 1, 2, 3, cells3),
                        new WindowMock("window 4", 1, 2, 3, cells4)
                };
            }
    
            @Override
            public void respondToWindowRequest(IWindow window) throws RemoteException {

            }
    
            @Override
            public void waitForTurnToBegin() throws IOException {
        
            }
    
            @Override
            public void endTurn() throws IOException {
        
            }
    
            @Override
            public void waitForTurnToEnd() throws IOException {
        
            }
    
            @Override
            public MoveResponse tryToMove(Move move) throws IOException {
                return null;
            }
    
            @Override
            public void requestToolCardUsage(IToolCard toolCard) throws IOException, NotEnoughTokensException {
        
            }
    
            @Override
            public Map.Entry<JSONSerializable, Set<Integer>> waitForChooseDiePositionFromLocation() {
                return null;
            }
    
            @Override
            public void postChosenDiePosition(Map.Entry<IDie, Integer> chosenPosition) {
        
            }
    
            public Map.Entry<IEffect[], Range<Integer>> waitForChooseBetweenEffect(IEffect[] availableEffects, Range<Integer> chooseBetween) {
                return null;
            }
    
            @Override
            public void postChosenEffects(IEffect[] effects) {
        
            }
    
            @Override
            public IEffect waitForContinueToRepeat() {
                return null;
            }
    
            @Override
            public void postContinueToRepeatChoice(boolean continueToRepeat) {
        
            }
    
            @Override
            public IDie waitForSetShade() {
                return new IDie() {
    
                    private static final long serialVersionUID = -8144175517603151757L;
    
                    @Override
                    public GlassColor getColor() {
                        return GlassColor.PURPLE;
                    }

                    @Override
                    public Integer getShade() {
                        return 5;
                    }
                };
            }
    
            @Override
            public void postSetShade(Integer shade) {
        
            }
            @Override
            public Map.Entry<IEffect[], Range<Integer>> waitForChooseBetweenEffect(){
                return null;
            }
        });
        primaryStage.setFullScreen(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
