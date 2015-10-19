package flowSnake;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 *This class creates the pane that shows the player scores.
 * @author johanwendt
 */
public class RightPane {
    
    public RightPane(BorderPane borderPane) { 
        VBox rightPane = new VBox();
        borderPane.setRight(rightPane);
        rightPane.setPrefHeight(UserInterface.getScreenHeight());
        rightPane.setPrefWidth((UserInterface.getScreenWidth() - UserInterface.getScreenHeight()) / 2);
        rightPane.setId("RPane");  
    }
}