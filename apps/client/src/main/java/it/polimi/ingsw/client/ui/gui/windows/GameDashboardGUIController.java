package it.polimi.ingsw.client.ui.gui.windows;

import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.ToolCardGUIView;
import it.polimi.ingsw.client.ui.gui.WindowGUIView;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.core.IEffect;
import it.polimi.ingsw.models.Cell;
import it.polimi.ingsw.models.ToolCard;
import it.polimi.ingsw.models.Window;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class GameDashboardGUIController extends Application {

    private FXMLLoader loader = new FXMLLoader();

    private Cell[][] cells = new Cell[][] {
            {
                    new Cell(0, null),      new Cell(5, null),      new Cell(4, null),       new Cell(0, GlassColor.GREEN)
            },
            {
                    new Cell(0, null),      new Cell(0, null),      new Cell(2, null),       new Cell(0, GlassColor.PURPLE)
            },
            {
                    new Cell(0, GlassColor.BLUE), new Cell(2, null),      new Cell(0, GlassColor.RED),   new Cell(0, GlassColor.YELLOW)
            }
    };

    WindowGUIView windowGUIView =  new WindowGUIView(new Window(3,3,4,"ciao", null, cells));
    private ToolCard toolCard = new ToolCard("ciao", "test", "eccomi", new IEffect() {
        @Override
        public void run(String cardId) {

        }

        @Override
        public int getCost() {
            return 0;
        }

        @Override
        public String getDescriptionKey() {
            return null;
        }
    });
    ToolCardGUIView toolCardGUIView = new ToolCardGUIView(toolCard);

    public static Stage primaryStage;
    public static double xOffset = 0;
    public static double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        loader.setLocation(Constants.Resources.GAME_DASHBOARD.getURL());

        BorderPane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Pane main = (Pane) root.getChildren().get(0);
        root.setCenter(toolCardGUIView.render());
        primaryStage = stage;
        showStage(root, 1280, 720);
    }

    public static void showStage(Parent root, int width, int height) {
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });

        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
}
