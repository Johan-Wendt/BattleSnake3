
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
    private static final HashMap<Integer, TextField> playerOneControls = new HashMap<>();
    private static final HashMap<Integer, TextField> playerTwoControls = new HashMap<>();
    private static final HashMap<Integer, TextField> playerThreeControls = new HashMap<>();
    private static final HashMap<Integer, TextField> playerFourControls = new HashMap<>();
    private static TextField pauseKeyField = new TextField();
    
    
    
    
    public FirstStage(String title, String infoText, String okMessage, String cancelMessage, GameEngine gameEngine) {
        super(title, infoText, okMessage, cancelMessage, PopUp.STANDARD_PANE_WIDTH);
        this.gameEngine = gameEngine;
        
        getOkButton().setDisable(true);
        getCancelButton().setDisable(true);
        setUpWinnerInfo(null, false);
        addComboBox();
        setUpLowerPart();
        addExtraPane(setUpBonusInformation());
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
            UserInterface.setDisableMenuBar(false);
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
    public GridPane setUpBonusInformation() {
        
        GridPane innerBonusPane = new GridPane();
        
        Color textColor = UserInterface.infoColor();
        
        Text regularBonusText = new Text(BonusEnum.REGULAR_BONUS.getBonusDescription());  
        Text makeShortBonusText = new Text(BonusEnum.MAKE_SHORT_BONUS.getBonusDescription());  
        Text addDeathBlockBonusText = new Text(BonusEnum.ADD_DEATH_BLOCK_BONUS.getBonusDescription());
        Text deathBlockText = new Text(BonusEnum.DEATH_BLOCK.getBonusDescription());

        regularBonusText.setFill(textColor);
        makeShortBonusText.setFill(textColor);
        addDeathBlockBonusText.setFill(textColor);
        deathBlockText.setFill(textColor);
                
        Font textFont = new Font("Impact", UserInterface.getPlayerScoreSize());
        regularBonusText.setFont(textFont);
        makeShortBonusText.setFont(textFont);
        addDeathBlockBonusText.setFont(textFont);
        deathBlockText.setFont(textFont);
        
        //Create the rectangles that show what type of bonus the description is about.
        Rectangle regularBonusColor = new Rectangle(UserInterface.getBlockSize() * 2.5, UserInterface.getBlockSize() * 2.5);
        Image imageRegular = new Image(getClass().getResourceAsStream(BonusEnum.REGULAR_BONUS.getBonusImage()));
        ImagePattern imagePatternRegular = new ImagePattern(imageRegular);  
        regularBonusColor.setFill(imagePatternRegular);
        
        Rectangle makeShortBonusColor = new Rectangle(UserInterface.getBlockSize() * 2.5, UserInterface.getBlockSize() * 2.5);
        Image imageMakeShort = new Image(getClass().getResourceAsStream(BonusEnum.MAKE_SHORT_BONUS.getBonusImage()));
        ImagePattern imagePatternMakeShort = new ImagePattern(imageMakeShort);  
        makeShortBonusColor.setFill(imagePatternMakeShort);
        
        Rectangle addDeathBlockBonusColor = new Rectangle(UserInterface.getBlockSize() * 2.5, UserInterface.getBlockSize() * 2.5);
        Image imageAddDeath = new Image(getClass().getResourceAsStream(BonusEnum.ADD_DEATH_BLOCK_BONUS.getBonusImage()));
        ImagePattern imagePatternAddDeath = new ImagePattern(imageAddDeath);  
        addDeathBlockBonusColor.setFill(imagePatternAddDeath);
        
        Rectangle deathBlockColor = new Rectangle(UserInterface.getBlockSize() * 2.5, UserInterface.getBlockSize() * 2.5);
        Image imageDeath = new Image(getClass().getResourceAsStream(BonusEnum.DEATH_BLOCK.getBonusImage()));
        ImagePattern imagePatternDeath = new ImagePattern(imageDeath);  
        deathBlockColor.setFill(imagePatternDeath);
        
        //Add the bonus information to the pane.
        
        
        innerBonusPane.add(regularBonusColor, 0, 0);
        innerBonusPane.add(makeShortBonusColor, 0, 1);
        innerBonusPane.add(addDeathBlockBonusColor, 0, 2);
        innerBonusPane.add(deathBlockColor, 0, 3);
        
        innerBonusPane.add(regularBonusText, 1, 0);
        innerBonusPane.add(makeShortBonusText, 1, 1);
        innerBonusPane.add(addDeathBlockBonusText, 1, 2);
        innerBonusPane.add(deathBlockText, 1, 3);
        
        //Adjust positioning
        innerBonusPane.setVgap(UserInterface.getStandardPadding() / 2);
        innerBonusPane.setHgap(UserInterface.getStandardPadding());
        innerBonusPane.setPadding(new Insets(UserInterface.getStandardPadding()));
        
        return innerBonusPane;
    }
}
