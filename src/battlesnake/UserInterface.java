package battlesnake;

/**
 * @author johanwendt
 */
import java.util.ArrayList;
import java.util.HashMap;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;




/**
 *This class is the core game engine. It creates the players, the gamefield and the bonus handler.
 * It takes input from the user and it holds all the GUI-stuff.
 */
public class UserInterface {
    //Fields
    //Panes, scenes and stages.
    private static final BorderPane mainPane = new BorderPane();
    //private static final GridPane controlsPane = new GridPane();
    private static Scene mainScene;
    //private static Scene controlsScene;
    //private static Stage controlsStage;
    private static final Stage battleStage = new Stage();
    
    //Nodes 
    private static MenuBar menuBar;
    private static final MenuItem underMenu4 = new MenuItem("Pause / Unpause");
    private static ChoiceBox chooseNumberOfPlayers;
    private static Menu menu;
    private static final Text winnerInfo = new Text();

        
    private static FirstStage firstStage;
    private static AboutStage aboutStage;
    private static RightPane rightPane;
    private static ControlsStage controlsStage;
    
    //Static finals
    //To be able to give every block in the field grid a unique id every 
    //block in y-direction adds 1 to the id while every block in 
    //x-direction adds 1000 (MULIPLIER_X).
    
    public static final Pane gameGridPane = new Pane();
    
    public static final Color playerOneColor = Color.web("#B200B2");
    public static final Color playerTwoColor = Color.web("#66FF33");
    public static final Color playerThreeColor = Color.web("#E68A00");
    public static final Color playerFourColor = Color.web("#00FFFF");
    
    private static final double screenHeight = Screen.getPrimary().getVisualBounds().getHeight() - 23;
    private static final double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    private static int gridSize;
    private static int blockSize;
    private static double standardPadding;
    private static double borderWidth; 
    private static double menuBarSize;
   
    private static double playerScoreSize;
    
    //Regular fields
    private final HashMap<Integer, TextField> playerOneControls = new HashMap<>();
    private final HashMap<Integer, TextField> playerTwoControls = new HashMap<>();
    private final HashMap<Integer, TextField> playerThreeControls = new HashMap<>();
    private final HashMap<Integer, TextField> playerFourControls = new HashMap<>();
    private final GameEngine gameEngine;

    /**
     * Creates the grafical interface.
     * @param gameEngine The GameEngine that runs the game.
     */
    public UserInterface (GameEngine gameEngine, ArrayList<Player> players) {
        this.gameEngine = gameEngine;
        
        menuBarSize = 23;
        borderWidth = screenHeight / 200.0;
        blockSize = (int) ((screenHeight - (screenHeight * 0.06)) / GameEngine.BRICKS_PER_ROW);
        gridSize = blockSize * GameEngine.BRICKS_PER_ROW;
        standardPadding = (screenHeight - gridSize - menuBarSize) / 2;
        playerScoreSize = 1.4 * screenHeight / standardPadding;
        setUpMainScreen();
        setUpControlsScreen();
        aboutStage = new AboutStage();
        rightPane = new RightPane(players);
        firstStage = new FirstStage();
    }
    public void gameOver(Player winner) {
        setUpWinnerInfo(winner);
        firstStage.showWelcomeScreen(true);
    }
    public static void restart(ArrayList<Player> players) {
        setUpWinnerInfo(null);
        RightPane.setUpScoreBoard();
        RightPane.initiateScoreBoard(players);         
    }
    public static int getGridSize() {
        return gridSize;
    }
    public static int getBlockSize() {
        return blockSize;
    }
    /**
     * Updates the information about the current controls for the players.
     * @param playerName name of the player
     * @param direction direction to be controlled by the key
     * @param key key used to turn in the given direction
     */
    public void updateControlText(String playerName, Integer direction, String key) {
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
    /**
     * This sets upp the main game screen.
     */
    private void setUpMainScreen() {
        mainScene = new Scene(mainPane, screenWidth, screenHeight);
        
        //Exit the game on closing this window.
        battleStage.setOnCloseRequest(c -> {
            System.exit(0);
        });
        //mainScene = new Scene(mainPane, screenWidth, screenHeight);
        mainScene.getStylesheets().add(BattleSnake.class.getResource("BattleSnake.css").toExternalForm());
        
        
        //Takes input from the keybord and lets the active player-instances decide
        //to do with the information.
        mainScene.setOnKeyPressed(e -> {
            gameEngine.takePressedKey(e.getCode());
        });
        
        //Create Menubar-system
        menu = new Menu("Battle Snake");

        menu.setId("menu");
        MenuItem underMenu1 = new MenuItem("Set up game");
        MenuItem underMenu2 = new MenuItem("About");
        MenuItem underMenu3 = new MenuItem("Controls");
        MenuItem underMenu5 = new MenuItem("Quit");
        menu.getItems().addAll(underMenu1, underMenu2, underMenu3, underMenu4, underMenu5);
        menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        menuBar.setPadding(new Insets(0, 0, 0, standardPadding));
        mainPane.setTop(menuBar);
        
        menu.setOnShowing(s -> {
            gameEngine.setPaused(true);
          
        });
        menu.setOnHiding(h -> {
            if(!underMenu4.isDisable())
                gameEngine.setPaused(false);
        });

        //Set Shortcuts for menus.
        underMenu1.setAccelerator(KeyCombination.keyCombination("CTRL + S"));
        underMenu1.setOnAction(a -> {
            gameEngine.setPaused(true);
            firstStage.showWelcomeScreen(true);
        });
        
        underMenu2.setAccelerator(KeyCombination.keyCombination("CTRL + A"));
        underMenu2.setOnAction(a -> {
            gameEngine.setPaused(true);
            AboutStage.getStage().show();
        });
        
        underMenu3.setAccelerator(KeyCombination.keyCombination("CTRL + C"));
        underMenu3.setOnAction(a -> {
            gameEngine.setPaused(true);
            controlsStage.show();
        });
        
        underMenu4.setAccelerator(KeyCombination.keyCombination("CTRL + P"));
        underMenu4.setOnAction(a -> {
            if(!gameEngine.isPaused()) {
                gameEngine.setPaused(true);
            }
            else {
                gameEngine.setPaused(false);
            }
        });
        
        underMenu5.setAccelerator(KeyCombination.keyCombination("CTRL + Q"));
        underMenu5.setOnAction(a -> {
            System.exit(0);
        });
        //Adjust the GUI.
        gameGridPane.setPrefSize(gridSize, gridSize);
        mainPane.setCenter(RightPane.getPane());
        mainPane.setLeft(gameGridPane);
        
        BorderPane.setMargin(gameGridPane, new Insets(standardPadding));
        
        //Activate the stage
        battleStage.setScene(mainScene);
        battleStage.setResizable(false);
        battleStage.setTitle("Battle Snake");
        battleStage.show();   
    }
    public static Stage getBattleStage() {
        return battleStage;
    }
    public static Text getWinnerInfo() {
        return winnerInfo;
    }
    public void restartGameEngine() {
        gameEngine.restart();
    }
    public void setPauseGameEngine(boolean pause) {
        if(pause) gameEngine.setPaused(true);
        else gameEngine.setPaused(false);
    }
    public static void setDisableMenuBar(boolean disabled) {
        if(disabled) menuBar.setDisable(true);
        else menuBar.setDisable(false);
    }
    public static void setDisableUnderMenuFour(boolean disabled) {
        if(disabled) underMenu4.setDisable(true);
        else underMenu4.setDisable(false);
    }
    public static double getScreenHeight() {
        return screenHeight;
    }
    public static double getScreenWidth() {
        return screenWidth;
    }
    public static double getPlayerScoreSize() {
        return playerScoreSize;
    }
    public static double getStandardPadding() {
        return standardPadding;
    }

    /**
     * Sets up the stage where the user can set the player controls. 
     * This is reached via the menus in the main game stage.
     */
    private void setUpControlsScreen() {
        controlsStage = new Stage();
        controlsStage.setAlwaysOnTop(true);
        controlsStage.setMaxWidth(500);
        controlsStage.setMaxHeight(230);
        controlsStage.initOwner(battleStage);
        controlsStage.setResizable(false);
        
        controlsStage.setOnShowing(e -> {
            underMenu4.setDisable(true);
        });
        
        controlsStage.setOnHiding(e -> {
            underMenu4.setDisable(false);
        });
        
        playerOneControls.put(gameEngine.UP, new TextField());
        playerOneControls.put(gameEngine.RIGHT, new TextField());
        playerOneControls.put(gameEngine.DOWN, new TextField());
        playerOneControls.put(gameEngine.LEFT, new TextField());
        
        playerTwoControls.put(gameEngine.UP, new TextField());
        playerTwoControls.put(gameEngine.RIGHT, new TextField());
        playerTwoControls.put(gameEngine.DOWN, new TextField());
        playerTwoControls.put(gameEngine.LEFT, new TextField());
        
        playerThreeControls.put(gameEngine.UP, new TextField());
        playerThreeControls.put(gameEngine.RIGHT, new TextField());
        playerThreeControls.put(gameEngine.DOWN, new TextField());
        playerThreeControls.put(gameEngine.LEFT, new TextField());
        
        playerFourControls.put(gameEngine.UP, new TextField());
        playerFourControls.put(gameEngine.RIGHT, new TextField());
        playerFourControls.put(gameEngine.DOWN, new TextField());
        playerFourControls.put(gameEngine.LEFT, new TextField());
 
        
        //Set up info texts
        Text player1 = new Text("Player 1 ");
        Text player2 = new Text("Player 2 ");
        Text player3 = new Text("Player 3 ");
        Text player4 = new Text("Player 4 ");
        
        Text space = new Text("");
        Text Up = new Text("Up");
        Text Right = new Text("Right");
        Text Down = new Text("Down");
        Text Left = new Text("Left");
        
        player1.setFont(Font.font(15));
        player2.setFont(Font.font(15));
        player3.setFont(Font.font(15));
        player4.setFont(Font.font(15));
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
        
        controlsPane.add(playerOneControls.get(gameEngine.UP), 1, 1);
        controlsPane.add(playerOneControls.get(gameEngine.RIGHT), 2, 1);
        controlsPane.add(playerOneControls.get(gameEngine.DOWN), 3, 1);
        controlsPane.add(playerOneControls.get(gameEngine.LEFT), 4, 1);
        
        controlsPane.add(playerTwoControls.get(gameEngine.UP), 1, 2);
        controlsPane.add(playerTwoControls.get(gameEngine.RIGHT), 2, 2);
        controlsPane.add(playerTwoControls.get(gameEngine.DOWN), 3, 2);
        controlsPane.add(playerTwoControls.get(gameEngine.LEFT), 4, 2);
        
        controlsPane.add(playerThreeControls.get(gameEngine.UP), 1, 3);
        controlsPane.add(playerThreeControls.get(gameEngine.RIGHT), 2, 3);
        controlsPane.add(playerThreeControls.get(gameEngine.DOWN), 3, 3);
        controlsPane.add(playerThreeControls.get(gameEngine.LEFT), 4, 3);
        
        controlsPane.add(playerFourControls.get(gameEngine.UP), 1, 4);
        controlsPane.add(playerFourControls.get(gameEngine.RIGHT), 2, 4);
        controlsPane.add(playerFourControls.get(gameEngine.DOWN), 3, 4);
        controlsPane.add(playerFourControls.get(gameEngine.LEFT), 4, 4);
        
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
        //Update player controls when pressing a button in the grid.
        playerOneControls.get(gameEngine.UP).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 1", GameEngine.UP, e.getCode());
            playerOneControls.get(gameEngine.RIGHT).requestFocus();
    });
        playerOneControls.get(gameEngine.RIGHT).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 1", GameEngine.RIGHT, e.getCode());
            playerOneControls.get(gameEngine.DOWN).requestFocus();
    });
        playerOneControls.get(gameEngine.DOWN).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 1", GameEngine.DOWN, e.getCode());
            playerOneControls.get(gameEngine.LEFT).requestFocus();
    });
        playerOneControls.get(gameEngine.LEFT).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 1", GameEngine.LEFT, e.getCode());
            playerTwoControls.get(gameEngine.UP).requestFocus();
    });
        
        playerTwoControls.get(gameEngine.UP).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 2", GameEngine.UP, e.getCode());
            playerTwoControls.get(gameEngine.RIGHT).requestFocus();
    });
        playerTwoControls.get(gameEngine.RIGHT).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 2", GameEngine.RIGHT, e.getCode());
            playerTwoControls.get(gameEngine.DOWN).requestFocus();
    });
        playerTwoControls.get(gameEngine.DOWN).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 2", GameEngine.DOWN, e.getCode());
            playerTwoControls.get(gameEngine.LEFT).requestFocus();
    });
        playerTwoControls.get(gameEngine.LEFT).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 2", GameEngine.LEFT, e.getCode());
            playerThreeControls.get(gameEngine.UP).requestFocus();
    });
        
        playerThreeControls.get(gameEngine.UP).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 3", GameEngine.UP, e.getCode());
            playerThreeControls.get(gameEngine.RIGHT).requestFocus();
    });
        playerThreeControls.get(gameEngine.RIGHT).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 3", GameEngine.RIGHT, e.getCode());
            playerThreeControls.get(gameEngine.DOWN).requestFocus();
    });
        playerThreeControls.get(gameEngine.DOWN).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 3", GameEngine.DOWN, e.getCode());
            playerThreeControls.get(gameEngine.LEFT).requestFocus();
    });
        playerThreeControls.get(gameEngine.LEFT).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 3", GameEngine.LEFT, e.getCode());
            playerFourControls.get(gameEngine.UP).requestFocus();
    });
        
        playerFourControls.get(gameEngine.UP).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 4", GameEngine.UP, e.getCode());
            playerFourControls.get(gameEngine.RIGHT).requestFocus();
    });
        playerFourControls.get(gameEngine.RIGHT).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 4", GameEngine.RIGHT, e.getCode());
            playerFourControls.get(gameEngine.DOWN).requestFocus();
    });
        playerFourControls.get(gameEngine.DOWN).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 4", GameEngine.DOWN, e.getCode());
            playerFourControls.get(gameEngine.LEFT).requestFocus();
    });
        playerFourControls.get(gameEngine.LEFT).setOnKeyPressed(e ->  {
            gameEngine.setControlKey("Player 4", GameEngine.LEFT, e.getCode());
    });

        //Setup confirmation button.
        Button backButton = new Button("Back to the battlin'");
        backButton.setPrefWidth(200);
        backButton.setStyle("-fx-font: 15 arial; -fx-base: #009933;");
        backButton.setOnAction(e -> {
            gameEngine.setPaused(false);
            controlsStage.hide();
        });
        
        //Setup button for using default keys.
        Button deafultButton = new Button("Use default keys");
        deafultButton.setPrefWidth(200);
        deafultButton.setStyle("-fx-font: 15 arial; -fx-base: #FF00FF;");
        deafultButton.setOnAction(e -> {
            gameEngine.setUpDefaultControlKeys();
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
                gameEngine.setPaused(false);
                controlsStage.hide();    
            }
            if(e.getCode().equals(KeyCode.ESCAPE)) {
                gameEngine.setPaused(false);
                controlsStage.hide();
            }
        });
        
        //Add some space
        controlsPane.setPadding(new Insets(20));
        
        //Make the stage ready to be shown upon pressing the menu.
        controlsScene = new Scene(controlsPane, 600, 300);
        controlsStage.setScene(controlsScene);
        controlsStage.setTitle("Set player controls");
    }
    /**
     * Makes the setup stage display either "Good Luck!" or the name
     * of the winner of the game.
     * @param winner 
     */
    private static void setUpWinnerInfo(Player winner) {
        if(winner == null) {
            winnerInfo.setText("Good Luck!");
            winnerInfo.setFill(Color.BLACK);
            winnerInfo.setFont(Font.font(15));
        }
        else {
            winnerInfo.setText("The winner is " + winner.getName());
            winnerInfo.setFill(winner.getPlayerColor());
            winnerInfo.setFont(Font.font(30));
        }
    }
    
}