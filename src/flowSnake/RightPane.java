package flowSnake;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *This class creates the pane that shows the player scores.
 * @author johanwendt
 */
public class RightPane {
    private static final VBox rightPane = new VBox();
    private static final VBox outerBonusPane = new VBox();
    private static final GridPane innerBonusPane = new GridPane();
    private static final VBox scorePane = new VBox();
    private static final Text regularBonusText = new Text(BonusEnum.REGULAR_BONUS.getBonusDescription());  
    private static final Text makeShortBonusText = new Text(BonusEnum.MAKE_SHORT_BONUS.getBonusDescription());  
    private static final Text addDeathBlockBonusText = new Text(BonusEnum.ADD_DEATH_BLOCK_BONUS.getBonusDescription());
    private static final ScoreEffect scoreEffect = new ScoreEffect();
    private static ArrayList<Text> playerScores = new ArrayList<>();
    
    private static final Text playerOneScore = new Text();
    private static final Text playerTwoScore = new Text();
    private static final Text playerThreeScore = new Text();
    private static final Text playerFourScore = new Text();
    
    public RightPane() {      
        rightPane.getChildren().addAll(scorePane, outerBonusPane);
        rightPane.setPrefHeight(UserInterface.getScreenHeight());
        rightPane.setPrefWidth(UserInterface.getScreenWidth() - UserInterface.getScreenHeight());
        scorePane.setPrefHeight(2 * rightPane.getPrefHeight() / 3);
        outerBonusPane.setPrefHeight(rightPane.getPrefHeight() / 3);
        scorePane.setPrefWidth(rightPane.getPrefWidth());
        rightPane.setEffect(new Lighting(new Light.Distant()));
        rightPane.setId("RPane");  
        
        setUpBonusInformation();
        setUpScoreBoard();
        initiateScoreBoard();
                
        
    }
    public void setUpBonusInformation() {
        regularBonusText.setEffect(scoreEffect.getEffect(Color.RED));
        regularBonusText.setFont(new Font(UserInterface.getPlayerScoreSize() * 0.6));

        makeShortBonusText.setEffect(scoreEffect.getEffect(Color.RED));
        makeShortBonusText.setFont(new Font(UserInterface.getPlayerScoreSize() * 0.6));

        addDeathBlockBonusText.setEffect(scoreEffect.getEffect(Color.RED));
        addDeathBlockBonusText.setFont(new Font(UserInterface.getPlayerScoreSize() * 0.6));
        
        //Create the rectangles that show what type of bonus the description is about.
        Rectangle regularBonusColor = new Rectangle(UserInterface.getBlockSize() * 1.5, UserInterface.getBlockSize() * 1.5);
        Image imageRegular = new Image(getClass().getResourceAsStream(BonusEnum.REGULAR_BONUS.getBonusImage()));
        ImagePattern imagePatternRegular = new ImagePattern(imageRegular);  
        regularBonusColor.setFill(imagePatternRegular);
        
        Rectangle makeShortBonusColor = new Rectangle(UserInterface.getBlockSize() * 1.5, UserInterface.getBlockSize() * 1.5);
        Image imageMakeShort = new Image(getClass().getResourceAsStream(BonusEnum.MAKE_SHORT_BONUS.getBonusImage()));
        ImagePattern imagePatternMakeShort = new ImagePattern(imageMakeShort);  
        makeShortBonusColor.setFill(imagePatternMakeShort);
        
        Rectangle addDeathBlockBonusColor = new Rectangle(UserInterface.getBlockSize() * 1.5, UserInterface.getBlockSize() * 1.5);
        Image imageAddDeath = new Image(getClass().getResourceAsStream(BonusEnum.ADD_DEATH_BLOCK_BONUS.getBonusImage()));
        ImagePattern imagePatternAddDeath = new ImagePattern(imageAddDeath);  
        addDeathBlockBonusColor.setFill(imagePatternAddDeath);
        
        //Add the bonus information to the pane.
        innerBonusPane.add(regularBonusColor, 0, 0);
        innerBonusPane.add(makeShortBonusColor, 0, 1);
        innerBonusPane.add(addDeathBlockBonusColor, 0, 2);
        
        innerBonusPane.add(regularBonusText, 1, 0);
        innerBonusPane.add(makeShortBonusText, 1, 1);
        innerBonusPane.add(addDeathBlockBonusText, 1, 2);
        
        //Adjust positioning
        innerBonusPane.setVgap(UserInterface.getStandardPadding() / 2);
        innerBonusPane.setHgap(UserInterface.getStandardPadding());
        innerBonusPane.setPadding(new Insets(UserInterface.getStandardPadding()));
        
        outerBonusPane.setAlignment(Pos.CENTER_LEFT);
        outerBonusPane.getChildren().add(innerBonusPane);
        
    }
    /**
     * Sets upp the score board for the right pane.
     */
    public static void setUpScoreBoard() {
        //Clear the scores for every new game.
        scorePane.getChildren().clear();
        
        scorePane.setSpacing(UserInterface.getStandardPadding());
        scorePane.setPadding(new Insets(4 * UserInterface.getStandardPadding(), 2 * UserInterface.getStandardPadding(), 2 * UserInterface.getStandardPadding(), 2 * UserInterface.getStandardPadding()));
        //Create, add the header nad apply efects.

        /**
        Text scoreHeader = new Text();
        HBox scoreHeaderPane = new HBox(scoreHeader);
        scoreHeaderPane.setAlignment(Pos.CENTER);
        scorePane.getChildren().add(scoreHeaderPane);
        scoreHeader.setFont(Font.font(1.5 * UserInterface.getPlayerScoreSize()));
        scoreHeader.setFill(Color.web("#4D4DFF"));
        scoreHeader.setUnderline(true);
        scoreHeader.setEffect(scoreEffect.getEffect(Color.web("#4D4DFF")));
        scoreHeaderPane.setPadding(new Insets(0, 0, 2 * UserInterface.getStandardPadding() / 4, 0));
        **/
    }
    /**
     * Makes the score board only show scores for relevant players.
     */
    public static void initiateScoreBoard() {
        playerScores.add(playerOneScore);
        playerScores.add(playerTwoScore);
        playerScores.add(playerThreeScore);
        playerScores.add(playerFourScore);
        
        int i = GameEngine.getNumberOfPlayers();
        while(i >= 1) {
            playerScores.get(i).setText(GameEngine.getPlayer(i).scoreToString());
            playerScores.get(i).setFont(Font.font(UserInterface.getPlayerScoreSize()));
            playerScores.get(i).setFill(GameEngine.getPlayer(i).getPlayerDetails().getScoreColor());
            scorePane.getChildren().add(0, playerScores.get(i));
            playerScores.add(new Text());
            i--;
        }
    }
    
    public static void showScores() {
        int i = 0;
        while(i < GameEngine.getPlayers().size()) {
            playerScores.get(i + 1).textProperty().set(GameEngine.getPlayer(i + 1).scoreToString());
            i++;
        }
    }
    
    public static VBox getPane() {
        return rightPane;
    }
}
