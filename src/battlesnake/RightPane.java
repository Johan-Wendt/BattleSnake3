package battlesnake;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    private static final Text regularBonusText = new Text(BonusHandler.getRegularBonusDescription());  
    private static final Text makeShortBonusText = new Text(BonusHandler.getMakeShortDescription());  
    private static final Text addDeathBlockBonusText = new Text(BonusHandler.getAddDeathBlockBonusDescription());
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
        //Set bonus effects and id for css.
        regularBonusText.setEffect(scoreEffect.getEffect(BonusHandler.getRegularBonusColor()));
        regularBonusText.setFont(new Font(UserInterface.getPlayerScoreSize() * 0.6));

        makeShortBonusText.setEffect(scoreEffect.getEffect(BonusHandler.getMakeShortBonusColor()));
        makeShortBonusText.setFont(new Font(UserInterface.getPlayerScoreSize() * 0.6));

        addDeathBlockBonusText.setEffect(scoreEffect.getEffect(BonusHandler.getAddDeathBlockBonusColor()));
        addDeathBlockBonusText.setFont(new Font(UserInterface.getPlayerScoreSize() * 0.6));
        
        //Create the rectangles that show what type of bonus the description is about.
        Rectangle regularBonusColor = new Rectangle(UserInterface.getBlockSize() * 1.5, UserInterface.getBlockSize() * 1.5, BonusHandler.getRegularBonusColor());
        regularBonusColor.setStroke(Color.BLACK);
        regularBonusColor.setEffect(new Lighting());
        Rectangle makeShortBonusColor = new Rectangle(UserInterface.getBlockSize() * 1.5, UserInterface.getBlockSize() * 1.5, BonusHandler.getMakeShortBonusColor());
        makeShortBonusColor.setStroke(Color.BLACK);
        makeShortBonusColor.setEffect(new Lighting());
        Rectangle addDeathBlockBonusColor = new Rectangle(UserInterface.getBlockSize() * 1.5, UserInterface.getBlockSize() * 1.5, BonusHandler.getAddDeathBlockBonusColor());
        addDeathBlockBonusColor.setStroke(Color.BLACK);
        addDeathBlockBonusColor.setEffect(new Lighting());
        
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
        scorePane.setPadding(new Insets(2 * UserInterface.getStandardPadding()));
        //Create, add the header nad apply efects.
        Text scoreHeader = new Text("Scores");
        HBox scoreHeaderPane = new HBox(scoreHeader);
        scoreHeaderPane.setAlignment(Pos.CENTER);
        scorePane.getChildren().add(scoreHeaderPane);
        scoreHeader.setFont(Font.font(1.5 * UserInterface.getPlayerScoreSize()));
        scoreHeader.setFill(GameGrid.GAMEGRID_COLOR);
        scoreHeader.setUnderline(true);
        scoreHeader.setEffect(scoreEffect.getEffect(GameGrid.GAMEGRID_COLOR));
        scoreHeaderPane.setPadding(new Insets(0, 0, 2 * UserInterface.getStandardPadding(), 0));
        
    }
    /**
     * Makes the score board only show scores for relevant players.
     */
    public static void initiateScoreBoard() {
        playerScores.add(playerOneScore);
        playerScores.add(playerTwoScore);
        playerScores.add(playerThreeScore);
        playerScores.add(playerFourScore);
        
        int i = GameEngine.getPlayers().size() - 1;
        while(i >= 0) {
            playerScores.get(i).setText(GameEngine.getPlayers().get(i).scoreToString());
            playerScores.get(i).setFont(Font.font(UserInterface.getPlayerScoreSize()));
            playerScores.get(i).setEffect(scoreEffect.getEffect(GameEngine.getPlayers().get(i).getPlayerColor()));
            scorePane.getChildren().add(1, playerScores.get(i));
            playerScores.add(new Text());
            i--;
        }
    }
    
    public static void showScores() {
        int i = 0;
        while(i < GameEngine.getPlayers().size()) {
            playerScores.get(i).textProperty().set(GameEngine.getPlayers().get(i).scoreToString());
            i++;
        }
    }
    
    public static VBox getPane() {
        return rightPane;
    }
}
