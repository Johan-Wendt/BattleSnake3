package battlesnake;

/**
 * @author johanwendt
 */
import java.util.HashMap;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;


/**
 *This class is the core game engine. It creates the players, the gamefield and the bonus handler.
 * It takes input from the user and it holds all the GUI-stuff.
 */
public class UserInterface {
    //Fields
    //Panes, scenes and stages.
    private final BorderPane mainPane = new BorderPane();
    private final GridPane firstPane = new GridPane();
    private final VBox rightPane = new VBox();
    private final VBox outerBonusPane = new VBox();
    private final GridPane innerBonusPane = new GridPane();
    private final VBox scorePane = new VBox();
    private final VBox aboutPane = new VBox();
    private final GridPane controlsPane = new GridPane();
    private Scene firstScene;
    private Scene mainScene;
    private Scene aboutScene;
    private Scene controlsScene;
    private Stage firstStage;
    private Stage aboutStage;
    private Stage controlsStage;
    private final Stage battleStage = new Stage();
    
    //Nodes 
    private final MenuBar menuBar = new MenuBar();
    private final MenuItem underMenu4 = new MenuItem("Unpause");
    private ChoiceBox chooseNumberOfPlayers;
    private Button startButton;
    private Button cancelButton;
    private final Text gameInfo = new Text();
    private final Text winnerInfo = new Text();
    private Text playerOneScore;
    private Text playerTwoScore;
    private Text playerThreeScore;
    private Text playerFourScore;
    private final Text regularBonusText = new Text(BonusHandler.REGULAR_BONUS_DESCRIPTION);  
    private final Text makeShortBonusText = new Text(BonusHandler.MAKE_SHORT_BONUS_DESCRIPTION);  
    private final Text addDeathBlockBonusText = new Text(BonusHandler.ADD_DEATH_BLOCK_BONUS_DESCRIPTION);
        
    
    
    
    //Static finals
    //To be able to give every block in the field grid a unique id every 
    //block in y-direction adds 1 to the id while every block in 
    //x-direction adds 1000 (MULIPLIER_X).
    
    public static final Pane PANE = new Pane();
    public static final Color PLAYER_1_COLOR = Color.web("#B200B2");
    public static final Color PLAYER_2_COLOR = Color.web("#66FF33");
    public static final Color PLAYER_3_COLOR = Color.web("#E68A00");
    public static final Color PLAYER_4_COLOR = Color.web("#00FFFF");
    
    private static int screenHeight;
    private static int screenWidth;
    private static int gridSize;
    private static int blockSize;
    
    private static int playerScoreSize;
    
    
    //Regular fields
    private final HashMap<Integer, TextField> playerOneControls = new HashMap<>();
    private final HashMap<Integer, TextField> playerTwoControls = new HashMap<>();
    private final HashMap<Integer, TextField> playerThreeControls = new HashMap<>();
    private final HashMap<Integer, TextField> playerFourControls = new HashMap<>();
    private final ScoreEffect scoreEffect = new ScoreEffect();
    private final GameEngine gameEngine;

    /**
     * Creates the grafical interface.
     * @param newGameEngine The GameEngine that runs the game.
     */
    public UserInterface (GameEngine newGameEngine, int screenHeight, int screenWidth, int gridSize, int blockSize) {
        this.gameEngine = newGameEngine;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        this.gridSize = gridSize;
        this.blockSize = blockSize;
        playerScoreSize = 3 * screenHeight / 40;
        setUpMainScreen();
        setUpBonusInformation();
        setUpControlsScreen();
        setUpFirstScreen();
        setUpAboutScreen();
        setUpRightPane();
    }
    
    public void setCancelButtonDisabled(boolean disable) {
        cancelButton.setDisable(disable);
    }
    public void showFirstStage(boolean show) {
        if(show) {
            firstStage.show();
        }
        else {
            firstStage.hide();
        }
    }
    public void gameOver(Player winner) {
        setUpWinnerInfo(winner);
        setCancelButtonDisabled(true);
        showFirstStage(true);
    }
    public void restart() {
        setUpWinnerInfo(null);
        setUpScoreBoard();
        initiateScoreBoard();
    }
    public void setPause(boolean pause) {
        if(pause) {
            underMenu4.setText("Unpause");
        }
        else {
            underMenu4.setText("Pause");
        }
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
     * Recieves the correct score from the player instances.
     */
    public void showScores() {
        switch(gameEngine.getNumberOfPlayers()) {
            case 4: 
                playerFourScore.setText(gameEngine.getPlayer(3).scoreToString());
            case 3: 
                playerThreeScore.setText(gameEngine.getPlayer(2).scoreToString());
            case 2: 
                playerTwoScore.setText(gameEngine.getPlayer(1).scoreToString());
            case 1: 
                playerOneScore.setText(gameEngine.getPlayer(0).scoreToString());        }
    }
    /**
     * This sets upp the main game screen.
     */
    private void setUpMainScreen() {
        //Exit the game on closing this window.
        battleStage.setOnCloseRequest(c -> {
            System.exit(0);
        });
        //mainScene = new Scene(mainPane, GameGrid.gridSize + 550, GameGrid.gridSize + 60);
        mainScene = new Scene(mainPane, screenWidth, screenHeight);
        mainScene.getStylesheets().add(BattleSnake.class.getResource("BattleSnake.css").toExternalForm());
        
        
        //Takes input from the keybord and lets the active player-instances decide
        //to do with the information.
        mainScene.setOnKeyPressed(e -> {
            gameEngine.takePressedKey(e.getCode());
        });
        
        //Create Menubar-system
        Menu menu = new Menu("Battle Snake");
        menu.setId("menu");
        MenuItem underMenu1 = new MenuItem("Set up game");
        MenuItem underMenu2 = new MenuItem("About");
        MenuItem underMenu3 = new MenuItem("Controls");
        MenuItem underMenu5 = new MenuItem("Quit");
        menu.getItems().addAll(underMenu1, underMenu2, underMenu3, underMenu4, underMenu5);
        menuBar.setMaxHeight(10);
        menuBar.setBackground(new Background(new BackgroundFill(GameGrid.SAFE_ZONE_COLOR, new CornerRadii(5), Insets.EMPTY)));
        menuBar.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, CornerRadii.EMPTY, new BorderWidths(3))));
        menuBar.getMenus().add(menu);
        mainPane.setTop(menuBar);
        menuBar.setEffect(new Lighting(new Light.Distant()));

        menuBar.setId("MBar");

        
        
        //Set Shortcuts for menus.
        underMenu1.setAccelerator(KeyCombination.keyCombination("CTRL + S"));
        underMenu1.setOnAction(a -> {
            gameEngine.setPaused(true);
            firstStage.show();
        });
        
        underMenu2.setAccelerator(KeyCombination.keyCombination("CTRL + A"));
        underMenu2.setOnAction(a -> {
            gameEngine.setPaused(true);
            aboutStage.show();
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

        mainPane.setCenter(rightPane);
        mainPane.setLeft(PANE);
        mainPane.setPadding(new Insets(screenHeight / 160, screenHeight / 40, screenHeight / 40, screenHeight / 40));
        
        
        BorderPane.setMargin(PANE, new Insets(screenHeight / 160));
        BorderPane.setMargin(rightPane, new Insets(screenHeight / 80, screenHeight / 160, screenHeight / 30, screenHeight / 160));
        
        //Activate the stage
        battleStage.setScene(mainScene);
        battleStage.setResizable(false);
        battleStage.setTitle("Battle Snake");
        battleStage.show();   
    }
    /**
     * Sets up everything for the first screen where the user can set number of
     * players and start the game. The same stage is used for restarting game
     * in mid game and after game over.
     */
    private void setUpFirstScreen() {
        firstStage = new Stage();
        firstStage.setAlwaysOnTop(true);
        firstStage.setMaxWidth(510);
        firstStage.setMaxHeight(600);
        
        firstStage.setOnShowing(e -> {
            underMenu4.setDisable(true);
        });
        
        firstStage.setOnHiding(e -> {
            underMenu4.setDisable(false);
        });
        
        //Adjust the spacing between the different parts of the screen.
        firstPane.getColumnConstraints().addAll(new ColumnConstraints(20), new ColumnConstraints(250), new ColumnConstraints(150));
        firstPane.getRowConstraints().addAll(new RowConstraints(10), new RowConstraints(170), new RowConstraints(90), new RowConstraints(5));
        
        //Create info about how to play the game.
        gameInfo.setText("Battle against your friends. Collect \nbonuses for points, and lose "
        + "them when\nyou die. When the field is reduced to the\ncore, "
        + "the eliminationbegins as snakes\nwith negative scores are terminated.\n" 
        + "Last snake standing wins!");
        gameInfo.setId("info-text");
        gameInfo.setEffect(new Bloom());
        
        //Add info about the objective of the game and info about the winner. 
        //Winner info is empty untill a player has won the game and is
        //emptied again after the game is restarted.
        firstPane.add(gameInfo, 1, 1);
        firstPane.add(winnerInfo, 1, 2);
 
        //Create combo-box woth enables to choose number of players in the game.
        ComboBox<String> chooseNumberOfPlayers = new ComboBox<>();
        ObservableList<String> options = FXCollections.observableArrayList("1 player", "2 players","3 players","4 players");
        
        chooseNumberOfPlayers.getItems().addAll(options);
        chooseNumberOfPlayers.setValue("Select number of players");
        chooseNumberOfPlayers.setPrefWidth(240);
        
        chooseNumberOfPlayers.setOnAction(e -> {
            gameEngine.setNumberOfPlayers(options.indexOf(chooseNumberOfPlayers.getValue()) + 1);
            startButton.setDisable(false);
        });
        
        //Enter has the same effekt as clicking the LET'S DO THIS!-button.
        //Escape has the same effect as pressing the cancel-button.
        chooseNumberOfPlayers.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER) && !chooseNumberOfPlayers.getValue().equals("Select number of players")) {
                gameEngine.restart();
                firstStage.hide();
                cancelButton.setDisable(false); 
            }
            if(e.getCode().equals(KeyCode.ESCAPE) && cancelButton.isDisable() == false) {
                gameEngine.setPaused(false);
                firstStage.hide();
            }
        });
        
        //Initiate start and cancelbutton.
        startButton = new Button("LET'S DO THIS!");
        startButton.setDisable(true);
        startButton.setPrefWidth(140);
        startButton.setStyle("-fx-base: #009933;");
        startButton.setOnAction(e -> {
            gameEngine.restart();
            menuBar.setDisable(false);
            firstStage.hide();
            cancelButton.setDisable(false);
        });
        startButton.setOnKeyPressed(k -> {
            gameEngine.restart();
            firstStage.hide();
            cancelButton.setDisable(false);
        });
        
        cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);
        cancelButton.setDisable(true);
        cancelButton.setStyle("-fx-base: #FF3300;");
        cancelButton.setOnAction(e -> {
            gameEngine.setPaused(false);
            firstStage.hide();
        });   
        
        //Add buttons to GUI.
        firstPane.add(chooseNumberOfPlayers, 1, 3);
        firstPane.add(startButton, 2, 3);
        firstPane.add(cancelButton, 3, 3);
        
        
        
        //Make the stage show at startup and disable closing the screen.
        firstScene = new Scene(firstPane, 600, 300);
        firstScene.getStylesheets().add(BattleSnake.class.getResource("BattleSnake.css").toExternalForm());
        firstStage.setScene(firstScene);
        firstStage.setTitle("Set up game");
        
        firstStage.setResizable(false);
        firstStage.show();
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
     * Sets up the stage with information about the game.
     */
    private void setUpAboutScreen() {

        aboutStage = new Stage();
        aboutStage.setAlwaysOnTop(true);
        aboutStage.setMaxWidth(260);
        aboutStage.setMaxHeight(250);
        aboutStage.setResizable(false);
        
        aboutStage.setOnShowing(e -> {
            underMenu4.setDisable(true);
        });
        
        aboutStage.setOnHiding(e -> {
            underMenu4.setDisable(false);
        });
              
        //Set about info
        Text aboutInfo = new Text("Version 1.0. \n"
        + "\n"
        + "Created by Johan Wendt. \n" 
        + "\n"
        + "johan.wendt1981@gmail.com \n"
        + "\n"
        + "Thank you for playing!");
        
        aboutInfo.setFont(Font.font(15));
        
        //Create button to get back to the game
        Button okButton = new Button("I get it, let's play some more.");
        okButton.setStyle("-fx-font: 15 arial; -fx-base: #009933;");
        okButton.setOnAction(e -> {
            gameEngine.setPaused(false);
            aboutStage.hide();
        });
        

        
        //Add info add button
        aboutPane.getChildren().addAll(aboutInfo, okButton);
        
        //Add some spacing
        aboutPane.setSpacing(30);
        aboutPane.setPadding(new Insets(20));
        
        //Make enter and escape take the user back to the game.
        aboutPane.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)) {
                gameEngine.setPaused(false);
                aboutStage.hide();    
            }
            if(e.getCode().equals(KeyCode.ESCAPE)) {
                gameEngine.setPaused(false);
                aboutStage.hide();
            }
        });
        
        //Make the stage ready to be shown upon pressing the menu.
        aboutScene = new Scene(aboutPane, 260, 250);
        aboutStage.setScene(aboutScene);
        aboutStage.setTitle("About Battle Snake");
    }
    /**
     * Sets up the right pane that holds the score info.
     */
    private void setUpRightPane() {
        
        //Set color, add the scoreboard and set some space to the part that is to contain the tostring info.

        rightPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(3))));
        rightPane.getChildren().addAll(scorePane, outerBonusPane);
        rightPane.setPrefHeight(gridSize);
        scorePane.setPrefHeight(2 * rightPane.getPrefHeight() / 3);
        outerBonusPane.setPrefHeight(rightPane.getPrefHeight() / 3);
 
        rightPane.setEffect(new Lighting(new Light.Distant()));
        rightPane.setId("RPane");  
    }
    public void setUpBonusInformation() {
        //Set bonus effects and id for css.
        regularBonusText.setEffect(new Bloom());
        regularBonusText.setId("RBonus");
        regularBonusText.setFont(new Font(playerScoreSize * 0.4));

        makeShortBonusText.setEffect(new Bloom());
        makeShortBonusText.setId("MBonus");
        makeShortBonusText.setFont(new Font(playerScoreSize * 0.4));

        addDeathBlockBonusText.setEffect(new Bloom());
        addDeathBlockBonusText.setId("ABonus");
        addDeathBlockBonusText.setFont(new Font(playerScoreSize * 0.4));
        
        //Create the rectangles that show what type of bonus the description is about.
        Rectangle regularBonusColor = new Rectangle(blockSize * 2, blockSize * 2, BonusHandler.REGULAR_BONUS_COLOR);
        regularBonusColor.setStroke(Color.BLACK);
        regularBonusColor.setEffect(new Lighting());
        Rectangle makeShortBonusColor = new Rectangle(blockSize * 2, blockSize * 2, BonusHandler.MAKE_SHORT_BONUS_COLOR);
        makeShortBonusColor.setStroke(Color.BLACK);
        makeShortBonusColor.setEffect(new Lighting());
        Rectangle addDeathBlockBonusColor = new Rectangle(blockSize * 2, blockSize * 2, BonusHandler.ADD_DEATH_BLOCK_BONUS_COLOR);
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
        innerBonusPane.setVgap(20);
        innerBonusPane.setHgap(20);
        innerBonusPane.setPadding(new Insets(0, screenHeight / 80, 0, screenHeight / 80));
        
        outerBonusPane.setAlignment(Pos.CENTER_LEFT);
        outerBonusPane.getChildren().add(innerBonusPane);
    }
    /**
     * Sets upp the score board for the right pane.
     */
    private void setUpScoreBoard() {
        //Clear the scores for every new game.
        scorePane.getChildren().clear();
        
        //Add some space
        scorePane.setPadding(new Insets(screenHeight / 160, screenHeight / 160, screenHeight / 80, screenHeight / 25));
        
        //Create, add the header nad apply efects.
        Text scoreHeader = new Text("Scores");
        scorePane.getChildren().add(scoreHeader);
        scoreHeader.setFont(Font.font(playerScoreSize));
        scoreHeader.setFill(GameGrid.GAMEGRID_COLOR);
        scoreHeader.setUnderline(true);
        scoreHeader.setEffect(scoreEffect.getEffect(GameGrid.GAMEGRID_COLOR));
    }
    /**
     * Makes the score board only show scores for relevant players.
     */
    private void initiateScoreBoard() {
        switch(gameEngine.getNumberOfPlayers()) {
            case 4: 
                playerFourScore = new Text();
                playerFourScore.setText(gameEngine.getPlayer(3).scoreToString());
                scorePane.getChildren().add(1, playerFourScore);
                playerFourScore.setFont(Font.font(playerScoreSize));
                playerFourScore.setEffect(scoreEffect.getEffect(PLAYER_4_COLOR));
            case 3: 
                playerThreeScore = new Text();
                playerThreeScore.setText(gameEngine.getPlayer(2).scoreToString());
                scorePane.getChildren().add(1, playerThreeScore);
                playerThreeScore.setFont(Font.font(playerScoreSize));
                playerThreeScore.setEffect(scoreEffect.getEffect(PLAYER_3_COLOR));
            case 2: 
                playerTwoScore = new Text();
                playerTwoScore.setText(gameEngine.getPlayer(1).scoreToString());
                scorePane.getChildren().add(1, playerTwoScore);
                playerTwoScore.setFont(Font.font(playerScoreSize));
                playerTwoScore.setEffect(scoreEffect.getEffect(PLAYER_2_COLOR));
            case 1: 
                playerOneScore = new Text();
                playerOneScore.setText(gameEngine.getPlayer(0).scoreToString());
                scorePane.getChildren().add(1, playerOneScore);
                playerOneScore.setFont(Font.font(playerScoreSize));
                playerOneScore.setEffect(scoreEffect.getEffect(PLAYER_1_COLOR));
        }
 
    }
    /**
     * Makes the setup stage display either "Good Luck!" or the name
     * of the winner of the game.
     * @param winner 
     */
    private void setUpWinnerInfo(Player winner) {
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