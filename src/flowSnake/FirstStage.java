
package flowSnake;

import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *This class creates the stage that lets the player initiate a new game.
 * @author johanwendt
 */
public class FirstStage extends PopUp{
 //   private final static ComboBox chooseNumberOfPlayers = new ComboBox<>();
    private static final Label winnerInfo = new Label();
    private GameEngine gameEngine;
    
    
    private static final GridPane controlsPane = new GridPane();

    
    
    
    
    public FirstStage(String title, String infoText, String okMessage, String cancelMessage, GameEngine gameEngine) {
        super(title, infoText, okMessage, cancelMessage, PopUp.STANDARD_PANE_WIDTH);
        this.gameEngine = gameEngine;
        
        getOkButton().setDisable(true);
        getCancelButton().setDisable(true);
        setUpWinnerInfo(null, false);
        addComboBox();
        setUpLowerPart();
        addExtraPane(setUpMenuShortcuts());
        showPopUp(true);
    }
    private void addComboBox() {
        ObservableList<String> options = FXCollections.observableArrayList("1 player", "2 players","3 players","4 players");
        ComboBox chooseNumberOfPlayers = new ComboBox<>();
        chooseNumberOfPlayers.getItems().addAll(options);
        chooseNumberOfPlayers.setValue("Select number of players");
        chooseNumberOfPlayers.setPrefWidth(260);
        
        chooseNumberOfPlayers.setOnAction(e -> {
            GameEngine.setNumberOfPlayers(options.indexOf(chooseNumberOfPlayers.getValue()) + 1);
            getOkButton().setDisable(false);
        });
        chooseNumberOfPlayers.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER) && !chooseNumberOfPlayers.getValue().equals("Select number of players")) {
                gameEngine.restart();
                showPopUp(false);
                getCancelButton().setDisable(false);
            }
        });
        addExtraButton(chooseNumberOfPlayers);
    }
    @Override
    protected void setOnActions() {
        getOkButton().setOnAction(e -> {
            gameEngine.restart();
           // UserInterface.setDisableMenuBar(false);
            showPopUp(false);
            getCancelButton().setDisable(false);
        });

        getCancelButton().setOnAction(e -> {
            showPopUp(false);
        });   
        
    }
    //Add info about the objective of the game and info about the winner. 
        //Winner info is empty untill a player has won the game and is
        //emptied again after the game is restarted.
    public void setUpWinnerInfo(PlayerEnum winner, boolean noScore) {
        Label infoLabel = getExtraLabel();
        if(noScore) {
            infoLabel.setText("You lost your flow!");
            infoLabel.setTextFill(UserInterface.infoColor());
        }
        else if(winner == null) {
            infoLabel.setText("May the flow be with you!");
            infoLabel.setTextFill(UserInterface.infoColor());
            infoLabel.setFont(new Font(30));
        }
        else {
            infoLabel.setTextFill(winner.getScoreColor());
            infoLabel.setText("The winner is " + winner.getName());                    
        }
    }
    public HBox setUpMenuShortcuts() {
        
        HBox innerButtonPane = new HBox();
        
        final Button controllButton = new Button("Controlls");
        final Button ruleButton = new Button("  Rules  ");
        final Button quitButton = new Button("   Quit  ");
        
        controllButton.setStyle("-fx-base: #01c9f3; -fx-font-size: 15px;");
        ruleButton.setStyle("-fx-base: #01c9f3; -fx-font-size: 15px;");
        quitButton.setStyle("-fx-base: #01c9f3; -fx-font-size: 15px;");
        
        controllButton.setMinWidth(150);
        ruleButton.setMinWidth(150);
        ruleButton.setMinWidth(150);
        
        controllButton.setPadding(new Insets(PopUp.getStandardPadding() * 3));
        ruleButton.setPadding(new Insets(PopUp.getStandardPadding() * 3));
        quitButton.setPadding(new Insets(PopUp.getStandardPadding() * 3));
        
        controllButton.setMinWidth(100);
        ruleButton.setMinWidth(100);
        ruleButton.setMinWidth(100);
        
        
        HBox.setMargin(ruleButton, new Insets(0, PopUp.getStandardPadding() / 4, 0, PopUp.getStandardPadding() / 4));
        
        controllButton.setOnAction(e -> {
            UserInterface.showControllStage(true);
        });
        ruleButton.setOnAction(e -> {
            UserInterface.showRulesStage(true);
        });
        quitButton.setOnAction(e -> {
            UserInterface.showQuitStage(true);
        });
        
        innerButtonPane.getChildren().addAll(controllButton, ruleButton, quitButton);
        
        return innerButtonPane;
    }
}
