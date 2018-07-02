package it.polimi.ingsw.client.ui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.scenes.MatchGUIView;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.controllers.NotEnoughTokensException;
import it.polimi.ingsw.core.CardVisibility;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.Match;
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
    DieMock dieMock1 = new DieMock(3, GlassColor.RED);
    DieMock dieMock2 = new DieMock(1, GlassColor.BLUE);

    CellMock[] cells1 = {new CellMock(0,GlassColor.RED), new CellMock(0, GlassColor.BLUE),new CellMock(6,null),
            new CellMock(0,GlassColor.RED), new CellMock(0, GlassColor.BLUE), new CellMock(6, null),
            new CellMock(5,null), new CellMock(4, null), new CellMock(0, GlassColor.PURPLE),
            new CellMock(0,null), new CellMock(4, null), new CellMock(0, null),
            new CellMock(1, GlassColor.GREEN), new CellMock(0, GlassColor.BLUE), new CellMock(0,null),
            new CellMock(2,null), new CellMock(4,null), new CellMock(0,null),
            new CellMock(0,GlassColor.BLUE), new CellMock(4,null)};

    CellMock[] cells2 = {new CellMock(0, GlassColor.RED), new CellMock(0, GlassColor.BLUE), new CellMock(6,null),
            new CellMock(0, GlassColor.GREEN), new CellMock(0, GlassColor.BLUE), new CellMock(0,null),
            new CellMock(0,null), new CellMock(4,null), new CellMock(0,null),
            new CellMock(0,null), new CellMock(4,null), new CellMock(0,null),
            new CellMock(0, GlassColor.GREEN), new CellMock(1, GlassColor.BLUE), new CellMock(6,null),
            new CellMock(0,null), new CellMock(4,null), new CellMock(0, null),
            new CellMock(0, GlassColor.RED), new CellMock(4, GlassColor.GREEN)};

    CellMock[] cells3 = {new CellMock(0, GlassColor.GREEN), new CellMock(1, GlassColor.BLUE), new CellMock(6,null),
            new CellMock(0,null), new CellMock(4,null), new CellMock(0, null),
            new CellMock(0, GlassColor.RED), new CellMock(0, GlassColor.YELLOW), new CellMock(6,null),
            new CellMock(0,null), new CellMock(4,null), new CellMock(0, null),
            new CellMock(0, GlassColor.RED), new CellMock(3, GlassColor.YELLOW), new CellMock(6, null),
            new CellMock(2,null), new CellMock(4, null), new CellMock(0,null), new CellMock(0, GlassColor.GREEN),
            new CellMock(0, GlassColor.BLUE)};

    CellMock[] cells4 = {new CellMock(0, GlassColor.RED), new CellMock(3, GlassColor.YELLOW), new CellMock(6, null),
            new CellMock(2,null), new CellMock(4, null), new CellMock(0,null), new CellMock(0, GlassColor.GREEN),
            new CellMock(0, GlassColor.BLUE), new CellMock(6, null),
            new CellMock(0,null), new CellMock(4,null), new CellMock(5,null),
            new CellMock(0,GlassColor.RED), new CellMock(0, GlassColor.BLUE),new CellMock(6,null),
            new CellMock(0,GlassColor.RED), new CellMock(0, GlassColor.BLUE), new CellMock(6, null),
            new CellMock(5,null), new CellMock(4, null)};

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
                                        return new WindowMock("finestra 2", 4, 4, 5, cells2);

                                    }

                                    @Override
                                    public IPlayer getPlayer() {
                                        return new IPlayer() {
                                            private static final long serialVersionUID = -5607492780014638998L;

                                            @Override
                                            public int getId() {
                                                return 0;
                                            }

                                            @Override
                                            public String getUsername() {
                                                return "Davide";
                                            }
                                        };
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
                                return new WindowMock("finestra 3", 2, 4, 5, cells3);
                            }

                            @Override
                            public IPlayer getPlayer() {
                                return new IPlayer() {
                                    private static final long serialVersionUID = -8821379905717868921L;

                                    @Override
                                    public int getId() {
                                        return 0;
                                    }

                                    @Override
                                    public String getUsername() {
                                        return "Marco";
                                    }
                                };
                            }
                        }, new ILivePlayer() {
                            private static final long serialVersionUID = 8193853292111083995L;

                            @Override
                            public int getFavourTokens() {
                                return 0;
                            }

                            @Override
                            public IWindow getWindow() {
                                return new WindowMock("finestra 4", 1, 4, 5, cells4);
                            }

                            @Override
                            public boolean hasLeft() {
                                return true;
                            }
                            @Override
                            public IPlayer getPlayer() {
                                return new IPlayer() {
                                    @Override
                                    public int getId() {
                                        return 0;
                                    }

                                    @Override
                                    public String getUsername() {
                                        return "Luca";
                                    }

                                };
                            }
                        }};
                    }

                    @Override
                    public IDraftPool getDraftPool() {
                        return new IDraftPool() {
                            DieMock dieMock11 = new DieMock(3, GlassColor.PURPLE);
                            DieMock dieMock21 = new DieMock(1, GlassColor.YELLOW);
                            DieMock dieMock3 = new DieMock(5, GlassColor.GREEN);
                            DieMock dieMock4 = new DieMock(6, GlassColor.BLUE);
                            DieMock dieMock5 = new DieMock(2, GlassColor.RED);
                            DieMock dieMock6 = new DieMock(4, GlassColor.YELLOW);
                            DieMock dieMock7 = new DieMock(1, GlassColor.PURPLE);
                            DieMock dieMock8 = new DieMock(2, GlassColor.RED);
                            DieMock dieMock9 = new DieMock(5, GlassColor.BLUE);
                            @Override
                            public byte getMaxNumberOfDice() {
                                return 9;
                            }

                            @Override
                            public Map<Integer, IDie> getLocationDieMap() {
                                return Map.of(0, dieMock11, 1, dieMock21, 2, dieMock3, 3, dieMock4, 4, dieMock5,
                                        5, dieMock6, 6, dieMock7, 7, dieMock8, 8, dieMock9);
                            }
                        };
                    }

                    @Override
                    public IRoundTrack getRoundTrack() {
                        IRoundTrack iRoundTrack = new IRoundTrack() {
                            DieMock dieMock1 = new DieMock(3, GlassColor.PURPLE);
                            DieMock dieMock2 = new DieMock(1, GlassColor.YELLOW);
                            DieMock dieMock3 = new DieMock(5, GlassColor.GREEN);
                            DieMock dieMock4 = new DieMock(6, GlassColor.BLUE);
                            DieMock dieMock5 = new DieMock(2, GlassColor.RED);
                            DieMock dieMock6 = new DieMock(4, GlassColor.YELLOW);
                            DieMock dieMock7 = new DieMock(1, GlassColor.PURPLE);
                            DieMock dieMock8 = new DieMock(2, GlassColor.RED);
                            @Override
                            public byte getNumberOfRounds() {
                                return 0;
                            }

                            @Override
                            public Map<Integer, IDie> getLocationDieMap() {
                                return Map.of(0x00000000, dieMock1, 0x00000100, dieMock2, 0x00000101, dieMock1, 0x00000200, dieMock3, 0x00000300, dieMock5, 0x00000400, dieMock6, 0x00000401, dieMock5);
                            }
                        };
                        return iRoundTrack;
                    }

                    @Override
                    public IObjectiveCard[] getPublicObjectiveCards() {
                        return new IObjectiveCard[]{
                                new IObjectiveCard() {
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
                                        return "title2";
                                    }

                                    @Override
                                    public String getDescription() {
                                        return "description2";
                                    }
                                },
                                new IObjectiveCard() {
                                    @Override
                                    public String getId() {
                                        return "row_color_variety";
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
                                        return "title3";
                                    }

                                    @Override
                                    public String getDescription() {
                                        return "description3";
                                    }
                                }
                        };
                    }


                    @Override
                    public IToolCard[] getToolCards() {
                        return new IToolCard[] {
                                new IToolCard() {
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
                                        return "title1";
                                    }

                                    @Override
                                    public String getDescription() {
                                        return "description1";
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
                CellMock[] cells1 = {new CellMock(0,GlassColor.RED), new CellMock(0, GlassColor.BLUE),new CellMock(6,null),
                        new CellMock(1,GlassColor.RED), new CellMock(0, GlassColor.BLUE), new CellMock(6, null),
                        new CellMock(5,null), new CellMock(4, null), new CellMock(0, GlassColor.PURPLE),
                        new CellMock(0,null), new CellMock(4, null), new CellMock(0, null),
                        new CellMock(1, GlassColor.GREEN), new CellMock(0, GlassColor.BLUE), new CellMock(0,null),
                        new CellMock(2,null), new CellMock(4,null), new CellMock(0,null),
                        new CellMock(0,GlassColor.BLUE), new CellMock(4,null)};

                CellMock[] cells2 = {new CellMock(0, GlassColor.RED), new CellMock(0, GlassColor.BLUE), new CellMock(6,null),
                        new CellMock(0, GlassColor.GREEN), new CellMock(0, GlassColor.BLUE), new CellMock(0,null),
                        new CellMock(0,null), new CellMock(4,null), new CellMock(0,null),
                        new CellMock(0,null), new CellMock(4,null), new CellMock(0,null),
                        new CellMock(0, GlassColor.GREEN), new CellMock(1, GlassColor.BLUE), new CellMock(6,null),
                        new CellMock(0,null), new CellMock(4,null), new CellMock(0, null),
                        new CellMock(0, GlassColor.RED), new CellMock(4, GlassColor.GREEN)};

                CellMock[] cells3 = {new CellMock(0, GlassColor.GREEN), new CellMock(1, GlassColor.BLUE), new CellMock(6,null),
                        new CellMock(0,null), new CellMock(4,null), new CellMock(0, null),
                        new CellMock(0, GlassColor.RED), new CellMock(0, GlassColor.YELLOW), new CellMock(6,null),
                        new CellMock(0,null), new CellMock(4,null), new CellMock(0, null),
                        new CellMock(0, GlassColor.RED), new CellMock(3, GlassColor.YELLOW), new CellMock(6, null),
                        new CellMock(2,null), new CellMock(4, null), new CellMock(0,null), new CellMock(0, GlassColor.GREEN),
                        new CellMock(0, GlassColor.BLUE)};

                CellMock[] cells4 = {new CellMock(0, GlassColor.RED), new CellMock(3, GlassColor.YELLOW), new CellMock(6, null),
                        new CellMock(2,null), new CellMock(4, null), new CellMock(0,null), new CellMock(0, GlassColor.GREEN),
                        new CellMock(0, GlassColor.BLUE), new CellMock(6, null),
                        new CellMock(0,null), new CellMock(4,null), new CellMock(5,null),
                        new CellMock(0,GlassColor.RED), new CellMock(0, GlassColor.BLUE),new CellMock(6,null),
                        new CellMock(0,GlassColor.RED), new CellMock(0, GlassColor.BLUE), new CellMock(6, null),
                        new CellMock(5,null), new CellMock(4, null)};
                return  new IWindow[] {new WindowMock("window 1", 3, 4, 5, cells1),
                        new WindowMock("window 2", 3, 4, 5, cells2),
                        new WindowMock("window 3", 1, 4, 5, cells3),
                        new WindowMock("window 4", 1, 4, 5, cells4)
                };

            }
    
            @Override
            public void respondToWindowRequest(IWindow window) throws RemoteException {

            }
    
            @Override
            public int waitForTurnToBegin() throws IOException {
                return 1;
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
            public Map.Entry<JSONSerializable, Set<Integer>> waitForChoosePositionFromLocation() {
                return null;
            }
    
            @Override
            public void postChosenPosition(Integer chosenPosition) {
        
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
            public IResult[] waitForMatchToEnd() {
                return null;
            }
    
            @Override
            public Map.Entry<IEffect[], Range<Integer>> waitForChooseBetweenEffect(){
                return null;
            }
        });
       // primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
