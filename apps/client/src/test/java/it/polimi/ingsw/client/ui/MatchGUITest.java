package it.polimi.ingsw.client.ui;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.scenes.MatchGUIView;
import it.polimi.ingsw.controllers.MatchController;
import it.polimi.ingsw.controllers.NotEnoughTokensException;
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


public class MatchGUITest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Constants.Resources.MATCH_FXML.getURL());
        Parent root = loader.load();
        Scene scene = new Scene(root, 1280, 720);
        MatchGUIView matchGUIView = loader.getController();

        matchGUIView.setModel(new MatchController() {
    
            @Override
            public void init(Object... args) throws IOException {
        
            }
    
            @Override
            public void close(Object... args) throws IOException {
        
            }
    
            @Override
            public IMatch waitForUpdate() throws RemoteException, InterruptedException {
                return new IMatch() {
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
                        return new ILivePlayer[0];
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
                    public IObjectiveCard[] getObjectiveCards() {
                        return new IObjectiveCard[0];
                    }

                    @Override
                    public IToolCard[] getToolCards() {
                        IToolCard[] toolCards = {
                                new IToolCard() {
                                    @Override
                                    public String getCardId() {
                                        return "cork_backed_strainghtedge";
                                    }

                                    @Override
                                    public IEffect getEffect() {
                                        return new EffectMock();
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
                                    @Override
                                    public String getCardId() {
                                        return "flux_brush";
                                    }

                                    @Override
                                    public IEffect getEffect() {
                                        return new EffectMock();
                                    }

                                    @Override
                                    public String getTitle() {
                                        return null;
                                    }

                                    @Override
                                    public String getDescription() {
                                        return null;
                                    }
                                }, new IToolCard() {
                            @Override
                            public String getCardId() {
                                return "lathekin";
                            }

                            @Override
                            public IEffect getEffect() {
                                return new EffectMock();
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

                        return new IToolCard[0];
                    }
                };
            }
            
            @Override
            public IWindow[] waitForWindowRequest() throws RemoteException {
                CellMock[] cells1 = {new CellMock(GlassColor.RED, 0), new CellMock(GlassColor.BLUE, 0), new CellMock(null, 6),
                        new CellMock(null, 0), new CellMock(null, 4), new CellMock(GlassColor.PURPLE,0) };
    
                CellMock[] cells2 = {new CellMock(GlassColor.RED, 0), new CellMock(GlassColor.BLUE, 0), new CellMock(null, 6),
                        new CellMock(null, 0), new CellMock(null, 4), new CellMock(null,0) };
    
                CellMock[] cells3 = {new CellMock(GlassColor.GREEN, 0), new CellMock(GlassColor.BLUE, 0), new CellMock(null, 6),
                        new CellMock(null, 0), new CellMock(null, 4), new CellMock(null,0) };
    
                CellMock[] cells4 = {new CellMock(GlassColor.RED, 0), new CellMock(GlassColor.YELLOW, 0), new CellMock(null, 6),
                        new CellMock(null, 0), new CellMock(null, 4), new CellMock(null,0) };
    
                IWindow[] iWindows = {new WindowMock("window 1", 3, 2, 3, cells1),
                        new WindowMock("window 2", 1, 2, 3, cells2),
                        new WindowMock("window 3", 1, 2, 3, cells3),
                        new WindowMock("window 4", 1, 2, 3, cells4)
                };
    
                return iWindows;
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
    
            @Override
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
                return null;
            }
    
            @Override
            public void postSetShade(Integer shade) {
        
            }
        });
        primaryStage.setFullScreen(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
