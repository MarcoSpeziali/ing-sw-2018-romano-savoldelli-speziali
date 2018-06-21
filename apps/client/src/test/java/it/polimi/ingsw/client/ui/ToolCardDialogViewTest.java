package it.polimi.ingsw.client.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import it.polimi.ingsw.client.Constants;
import it.polimi.ingsw.client.ui.gui.DieGUIView;
import it.polimi.ingsw.core.GlassColor;
import it.polimi.ingsw.models.Die;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import static com.jfoenix.controls.JFXDialog.DialogTransition.CENTER;

public class ToolCardDialogViewTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Constants.Resources.DIE_VIEW_FXML.getURL());
        Node die = loader.load();
        JFXDialogLayout content = new JFXDialogLayout();
        StackPane root = new StackPane();

        JFXDialog dialog = new JFXDialog(root, content, CENTER);
        JFXButton button = new JFXButton("OK");

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();
            }
        });
        JFXButton button1 = new JFXButton("Show die");
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.show();
            }
        });
        root.getChildren().add(button1);
        content.setActions(button);
        DieGUIView controller = loader.getController();
        controller.setDie(new Die(5, GlassColor.BLUE));
        content.setHeading(new Text("Die"));
        content.setBody(die);
        content.setAlignment(Pos.CENTER);
        dialog.setCursor(Cursor.CLOSED_HAND);
        Scene scene = new Scene(root, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
