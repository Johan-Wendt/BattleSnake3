/**
 * @author johanwendt
 */
package battlesnake;

import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author johanwendt
 */
public class ControlsStage {
    private static final GridPane controlsPane = new GridPane();
    private static Scene controlsScene;
    private static Stage controlsStage;
    
    private static final HashMap<Integer, TextField> playerOneControls = new HashMap<>();
    private static final HashMap<Integer, TextField> playerTwoControls = new HashMap<>();
    private static final HashMap<Integer, TextField> playerThreeControls = new HashMap<>();
    private static final HashMap<Integer, TextField> playerFourControls = new HashMap<>();
    private static TextField pauseKeyField = new TextField();
    
    public ControlsStage() {
        setUpControlsScreen();
        setUpInitialControlsInfo();
        setUpInfoTexts();
        makeControlsEditable();
        createButtons();

        controlsScene = new Scene(controlsPane, 600, 300);
        controlsStage.setScene(controlsScene);
        controlsStage.setTitle("Set player controls");
    }
    private void setUpControlsScreen() {
        controlsStage = new Stage();
        controlsStage.setAlwaysOnTop(true);
        controlsStage.setMaxWidth(500);
        controlsStage.setMaxHeight(230);
        controlsStage.initOwner(UserInterface.getBattleStage());
        controlsStage.setResizable(false);
    }
    private void setUpInitialControlsInfo() {
        
        playerOneControls.put(GameEngine.UP, new TextField());
        playerOneControls.put(GameEngine.RIGHT, new TextField());
        playerOneControls.put(GameEngine.DOWN, new TextField());
        playerOneControls.put(GameEngine.LEFT, new TextField());
        
        playerTwoControls.put(GameEngine.UP, new TextField());
        playerTwoControls.put(GameEngine.RIGHT, new TextField());
        playerTwoControls.put(GameEngine.DOWN, new TextField());
        playerTwoControls.put(GameEngine.LEFT, new TextField());
        
        playerThreeControls.put(GameEngine.UP, new TextField());
        playerThreeControls.put(GameEngine.RIGHT, new TextField());
        playerThreeControls.put(GameEngine.DOWN, new TextField());
        playerThreeControls.put(GameEngine.LEFT, new TextField());
        
        playerFourControls.put(GameEngine.UP, new TextField());
        playerFourControls.put(GameEngine.RIGHT, new TextField());
        playerFourControls.put(GameEngine.DOWN, new TextField());
        playerFourControls.put(GameEngine.LEFT, new TextField());
    }
 
    private void setUpInfoTexts() {
        
        controlsPane.setPadding(new Insets(20));
        
        //Set up info texts
        Text player1 = new Text("Player 1 ");
        Text player2 = new Text("Player 2 ");
        Text player3 = new Text("Player 3 ");
        Text player4 = new Text("Player 4 ");
        Text pauseKey = new Text("Pause key");
        
        Text space = new Text("");
        Text Up = new Text("Up");
        Text Right = new Text("Right");
        Text Down = new Text("Down");
        Text Left = new Text("Left");
        
        player1.setFont(Font.font(15));
        player2.setFont(Font.font(15));
        player3.setFont(Font.font(15));
        player4.setFont(Font.font(15));
        pauseKey.setFont(Font.font(15));
        Up.setFont(Font.font(15));
        Right.setFont(Font.font(15));
        Down.setFont(Font.font(15));
        Left.setFont(Font.font(15));
  
        controlsPane.addRow(0, space, Up, Right, Down, Left);
        
        //Add input text fields for editing the controls and display current controls.
        
        controlsPane.add(player1, 0, 1);
        controlsPane.add(player2, 0, 2);
        controlsPane.add(player3, 0, 3);
        controlsPane.add(player4, 0, 4);
        controlsPane.add(pauseKey, 0, 5);
        
        controlsPane.add(playerOneControls.get(GameEngine.UP), 1, 1);
        controlsPane.add(playerOneControls.get(GameEngine.RIGHT), 2, 1);
        controlsPane.add(playerOneControls.get(GameEngine.DOWN), 3, 1);
        controlsPane.add(playerOneControls.get(GameEngine.LEFT), 4, 1);
        
        controlsPane.add(playerTwoControls.get(GameEngine.UP), 1, 2);
        controlsPane.add(playerTwoControls.get(GameEngine.RIGHT), 2, 2);
        controlsPane.add(playerTwoControls.get(GameEngine.DOWN), 3, 2);
        controlsPane.add(playerTwoControls.get(GameEngine.LEFT), 4, 2);
        
        controlsPane.add(playerThreeControls.get(GameEngine.UP), 1, 3);
        controlsPane.add(playerThreeControls.get(GameEngine.RIGHT), 2, 3);
        controlsPane.add(playerThreeControls.get(GameEngine.DOWN), 3, 3);
        controlsPane.add(playerThreeControls.get(GameEngine.LEFT), 4, 3);
        
        controlsPane.add(playerFourControls.get(GameEngine.UP), 1, 4);
        controlsPane.add(playerFourControls.get(GameEngine.RIGHT), 2, 4);
        controlsPane.add(playerFourControls.get(GameEngine.DOWN), 3, 4);
        controlsPane.add(playerFourControls.get(GameEngine.LEFT), 4, 4);
        
        controlsPane.add(pauseKeyField, 1, 5);
        
    
        //Make inputfields only show values that are set.
        playerOneControls.forEach((key, value) -> {
            value.setEditable(false);
        });
        playerTwoControls.forEach((key, value) -> {
            value.setEditable(false);
        });
        playerThreeControls.forEach((key, value) -> {
            value.setEditable(false);
        });
        playerFourControls.forEach((key, value) -> {
            value.setEditable(false);
        });
        pauseKeyField.setEditable(false);
    }
    private void makeControlsEditable() {
        
        //Update player controls when pressing a button in the grid.
        playerOneControls.get(GameEngine.UP).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 1", GameEngine.UP, e.getCode());
            playerOneControls.get(GameEngine.RIGHT).requestFocus();
        });
        playerOneControls.get(GameEngine.RIGHT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 1", GameEngine.RIGHT, e.getCode());
            playerOneControls.get(GameEngine.DOWN).requestFocus();
        });
        playerOneControls.get(GameEngine.DOWN).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 1", GameEngine.DOWN, e.getCode());
            playerOneControls.get(GameEngine.LEFT).requestFocus();
        });
        playerOneControls.get(GameEngine.LEFT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 1", GameEngine.LEFT, e.getCode());
            playerTwoControls.get(GameEngine.UP).requestFocus();
        });
        
        playerTwoControls.get(GameEngine.UP).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 2", GameEngine.UP, e.getCode());
            playerTwoControls.get(GameEngine.RIGHT).requestFocus();
        });
        playerTwoControls.get(GameEngine.RIGHT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 2", GameEngine.RIGHT, e.getCode());
            playerTwoControls.get(GameEngine.DOWN).requestFocus();
        });
        playerTwoControls.get(GameEngine.DOWN).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 2", GameEngine.DOWN, e.getCode());
            playerTwoControls.get(GameEngine.LEFT).requestFocus();
        });
        playerTwoControls.get(GameEngine.LEFT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 2", GameEngine.LEFT, e.getCode());
            playerThreeControls.get(GameEngine.UP).requestFocus();
        });
        
        playerThreeControls.get(GameEngine.UP).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 3", GameEngine.UP, e.getCode());
            playerThreeControls.get(GameEngine.RIGHT).requestFocus();
        });
        playerThreeControls.get(GameEngine.RIGHT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 3", GameEngine.RIGHT, e.getCode());
            playerThreeControls.get(GameEngine.DOWN).requestFocus();
        });
        playerThreeControls.get(GameEngine.DOWN).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 3", GameEngine.DOWN, e.getCode());
            playerThreeControls.get(GameEngine.LEFT).requestFocus();
        });
        playerThreeControls.get(GameEngine.LEFT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 3", GameEngine.LEFT, e.getCode());
            playerFourControls.get(GameEngine.UP).requestFocus();
        });
        
        playerFourControls.get(GameEngine.UP).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 4", GameEngine.UP, e.getCode());
            playerFourControls.get(GameEngine.RIGHT).requestFocus();
        });
        playerFourControls.get(GameEngine.RIGHT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 4", GameEngine.RIGHT, e.getCode());
            playerFourControls.get(GameEngine.DOWN).requestFocus();
        });
        playerFourControls.get(GameEngine.DOWN).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 4", GameEngine.DOWN, e.getCode());
            playerFourControls.get(GameEngine.LEFT).requestFocus();
        });
        playerFourControls.get(GameEngine.LEFT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey("Player 4", GameEngine.LEFT, e.getCode());
            pauseKeyField.requestFocus();
        });
        pauseKeyField.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER) || e.getCode().equals(KeyCode.ESCAPE)) {
                GameEngine.setPaused(false);
                controlsStage.hide();
            }
            else {
            GameEngine.setPauseKey(e.getCode());
            }
        });
    }

    private void createButtons() {
        //Setup confirmation button.
        Button backButton = new Button("Back to the battlin'");
        backButton.setPrefWidth(200);
        backButton.setStyle("-fx-font: 15 arial; -fx-base: #009933;");
        backButton.setOnAction(e -> {
            GameEngine.setPaused(false);
            controlsStage.hide();
        });
        
        //Setup button for using default keys.
        Button deafultButton = new Button("Use default keys");
        deafultButton.setPrefWidth(200);
        deafultButton.setStyle("-fx-font: 15 arial; -fx-base: #FF00FF;");
        deafultButton.setOnAction(e -> {
            GameEngine.setUpDefaultControlKeys();
        });
        
        //Add empty row,for spacing in the grid, and button.
        Text emptyRow = new Text("");
        
        controlsPane.add(emptyRow, 2, 5);
        controlsPane.add(deafultButton, 0, 6, 2, 1);
        controlsPane.add(backButton, 3, 6, 2, 1);

        //Make enter and escape return the user to the game. N.B escape 
        //does not reset keys to the state before the stage was brought up..
        controlsPane.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)) {
                GameEngine.setPaused(false);
                controlsStage.hide();    
            }
            if(e.getCode().equals(KeyCode.ESCAPE)) {
                GameEngine.setPaused(false);
                controlsStage.hide();
            }
        });
    }
    /**
     * Updates the information about the current controls for the players.
     * @param playerName name of the player
     * @param direction direction to be controlled by the key
     * @param key key used to turn in the given direction
     */
    public static void updateControlText(String playerName, Integer direction, String key) {
        if(playerName.equals("Player 1")) {
            playerOneControls.get(direction).setText(key);
        }
        if(playerName.equals("Player 2")) {
            playerTwoControls.get(direction).setText(key);
        }
        if(playerName.equals("Player 3")) {
            playerThreeControls.get(direction).setText(key);
        }
        if(playerName.equals("Player 4")) {
            playerFourControls.get(direction).setText(key);
        }
    }
    public static void updatePausedKeyText(String key) {
        pauseKeyField.setText(key);
    }
    public static void showStage(boolean show) {
        if(show) controlsStage.show();
        else controlsStage.hide();
    }
    
}
