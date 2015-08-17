package battlesnake3;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author johanwendt
 */
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import javafx.application.Platform;
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
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;





/**
 *
 * @author johanwendt
 */
public class GameEngine extends Application {
    
    private final BorderPane mainPane = new BorderPane();
    private final Pane pane = new Pane();
    public static final int MULIPLIER_X = 1000;
    public static final int RIGHT = MULIPLIER_X;
    public static final int LEFT = -MULIPLIER_X;
    public static final int DOWN = 1;
    public static final int UP = -1;
    public static final Color PLAYER_1_COLOR = Color.RED;
    public static final Color PLAYER_2_COLOR = Color.GREEN;
    public static final Color PLAYER_3_COLOR = Color.BLUE;
    public static final Color PLAYER_4_COLOR = Color.YELLOW;
    public static final int PLAYER_1_STARTDIRECTION = RIGHT;
    public static final int PLAYER_2_STARTDIRECTION = LEFT;
    public static final int PLAYER_3_STARTDIRECTION = UP;
    public static final int PLAYER_4_STARTDIRECTION = DOWN;
    public static final int PLAYER_SCORE_SIZE = 30;
    private long gameSpeed = 3;
    private final boolean isRunning = true;
    private GameGrid gameGrid;
    private final ArrayList<Player> players = new ArrayList<>();
    private Thread thread;
    private BonusHandler bonusHandler;
    private int numberOfPlayers = 1;
    private Scene mainScene;
    private Text playerOneScore;
    private Text playerTwoScore;
    private Text playerThreeScore;
    private Text playerFourScore;
    private final VBox scorePane = new VBox();
    private Scene firstScene;
    private Stage firstStage;
    private final Stage battleStage = new Stage();
    private final GridPane firstPane = new GridPane();
    private ChoiceBox chooseNumberOfPlayers;
    private boolean isPaused = true;
    private Button startButton;
    private Button cancelButton;
    private Text gameInfo = new Text();
    private GridPane rightPane = new GridPane();

    
    public GameEngine () {
        
    }
    
    @Override
    public void start(Stage battleStage) throws InterruptedException {
        //Set up everything all the screens and activate the players.
        setUpMainScreen();
        gameGrid = new GameGrid(pane);
        bonusHandler = new BonusHandler(gameGrid);
        activatePlayers();
        setUpControllers();
        setUpGameInfo(null);
        setUpFirstScreen();
        setUpScoreBoard();
        setUpRightPane();
        initiateScoreBoard();
        
        //Get the thread that is running movoment of the players and creations of bonuses started.
        thread = new Thread(() -> {
            try {
                while (isRunning) {
                    if(!isPaused) {
                        for(Player player: players) {
                            player.movePlayer();                      
                        }
                        bonusHandler.eventRound();
                        playerKiller(gameGrid.deathBuilder());
                        showScores();   
                    }
                    if((gameGrid.isDeathRunning() == false && getNumberOfAlivePlayers() < 2 && !isPaused) || getNumberOfAlivePlayers() < 1 && !isPaused) {
                        Platform.runLater(() -> {
                            gameOver();
                        });
                        
                    }
                    Thread.sleep(gameSpeed);
                }    
            }
            catch (InterruptedException ex) {
            }
        });
        thread.start();
    }
    /**
     * Set up the controllers for the players. This should be made 
     * dependant on the chosen amount of players.
     */
    public void setUpControllers() {
        
        mainScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {

                case RIGHT:
                    players.get(0).setCurrentDirection(RIGHT);
                    break;
                case LEFT:
                    players.get(0).setCurrentDirection(LEFT);
                    break;
                case UP:
                    players.get(0).setCurrentDirection(UP);
                    break;
                case DOWN:
                    players.get(0).setCurrentDirection(DOWN);
                    break;
                case D:
                    players.get(1).setCurrentDirection(RIGHT);
                    break;
                case A:
                    players.get(1).setCurrentDirection(LEFT);
                    break;
                case W:
                    players.get(1).setCurrentDirection(UP);
                    break;
                case S:
                    players.get(1).setCurrentDirection(DOWN);
                    break;
                case H:
                    players.get(2).setCurrentDirection(RIGHT);
                    break;
                case F:
                    players.get(2).setCurrentDirection(LEFT);
                    break;
                case T:
                    players.get(2).setCurrentDirection(UP);
                    break;
                case G:
                    players.get(2).setCurrentDirection(DOWN);
                    break;
                case L:
                    players.get(3).setCurrentDirection(RIGHT);
                    break;
                case J:
                    players.get(3).setCurrentDirection(LEFT);
                    break;
                case I:
                    players.get(3).setCurrentDirection(UP);
                    break;
                case K:
                    players.get(3).setCurrentDirection(DOWN);
                    break;
            }
        });
    }
/**
 * Brings up the initial screen and displays the winner of the game.
 */
    public void gameOver() {
        Player winner = null;
        int highest = -10;
        for(Player player: players) {
            if(player.getScore() > highest) {
                highest = player.getScore();
                winner = player;
            }
        }
        setUpGameInfo(winner);
        setIspaused(true);
        cancelButton.setDisable(true);
        firstStage.show();
    }
    /**
     * Unpauses the game and sets all players in status alive so they start moving.
     */
    public void begin() {
        isPaused = false;
        for(Player player: players) {
            player.setIsAlive(true);
        }
    }
    /**
     * Restart the game from the begining. Everything is reset.
     */
    public void restart() {
        setUpGameInfo(null);
        gameGrid = new GameGrid(pane);
        bonusHandler = new BonusHandler(gameGrid);
        setNumberOfPlayers(numberOfPlayers);
        begin();
        
    }
    /**
     * Turns upp the game speed for all the players. It does this by making
     * the pause in the gameloop-thread shorter. This is not yet implemented 
     * anywhere in the game.
     */
    public void turnUpGameSpeed() {
        if(gameSpeed > 1) {
            gameSpeed --;
        }
    }
    public void turnDownGameSpeed() {
        if(gameSpeed < 10) {
            gameSpeed ++;
        }
    }
    private void setIspaused(boolean pause) {
        isPaused = pause;
    }
    public void erasePlayers() {
        for(Player player: players) {
            player.erasePlayer();
        }
    }
    public void playerKiller(int deathBlock) {
        for(Player player: players) {
            if(player.containsBlock(deathBlock)) {
                player.killPlayer();
            }
        }
    }
    public void activatePlayers () {
        switch(numberOfPlayers) {
            case 4: players.add(0, new Player("Player 4", PLAYER_4_STARTDIRECTION, PLAYER_4_COLOR, gameGrid, bonusHandler));
            case 3: players.add(0, new Player("Player 3", PLAYER_3_STARTDIRECTION, PLAYER_3_COLOR, gameGrid, bonusHandler));
            case 2: players.add(0, new Player("Player 2", PLAYER_2_STARTDIRECTION, PLAYER_2_COLOR, gameGrid, bonusHandler));
            case 1: players.add(0, new Player("Player 1", PLAYER_1_STARTDIRECTION, PLAYER_1_COLOR, gameGrid, bonusHandler));
    } 
    }
    public void setNumberOfPlayers(int toPlay) {
        
        erasePlayers();
        players.clear();
        numberOfPlayers = toPlay;
        activatePlayers ();
        setUpScoreBoard();
        initiateScoreBoard();
    }
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    public void getPlayerScores() {
        
    }
    public void setUpMainScreen() {
        mainScene = new Scene(mainPane, GameGrid.GRID_SIZE + 450, GameGrid.GRID_SIZE + 20);
        
        final Menu menu = new Menu("Battle Snake");
        MenuItem underMenu1 = new MenuItem("Set up game");
        MenuItem underMenu2 = new MenuItem("Rules");
        MenuItem underMenu3 = new MenuItem("About");
        MenuItem underMenu4 = new MenuItem("Quit");
        menu.getItems().addAll(underMenu1, underMenu2, underMenu3, underMenu4);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        menuBar.setUseSystemMenuBar(true);
        
        underMenu1.setAccelerator(KeyCombination.keyCombination("META + S"));
        
        underMenu1.setOnAction(e -> {
            setIspaused(true);
            firstStage.show();
        });
        
        underMenu4.setAccelerator(KeyCombination.keyCombination("META + Q"));
        
        underMenu4.setOnAction(e -> {
            Platform.exit();
        });
        
        mainPane.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        mainPane.setCenter(rightPane);
        mainPane.setLeft(pane);
        mainPane.setPadding(new Insets(10, 20, 10, 20));
        mainPane.getChildren().add(menuBar);
        
 
        battleStage.setScene(mainScene);
        battleStage.setTitle("Battle Snake");
        battleStage.show();
        
    }
    public void setUpFirstScreen() {
        firstStage = new Stage();
        firstStage.setAlwaysOnTop(true);
        firstStage.setMaxWidth(500);
        firstStage.setMaxHeight(400);
        
        firstPane.getColumnConstraints().addAll(new ColumnConstraints(20), new ColumnConstraints(230), new ColumnConstraints(150));
        firstPane.getRowConstraints().addAll(new RowConstraints(10), new RowConstraints(70), new RowConstraints(190), new RowConstraints(5));
        
        Text controlInfo = new Text("Player 1: up, right, down left \n"
        + "Player 2: w, d, s, a \n" 
        + "Player 3: t, h, g, f \n" 
        + "Player 4: i, l, k, j ");
        
        controlInfo.setFont(Font.font(15));
        
        firstPane.add(gameInfo, 1, 1);
        firstPane.add(controlInfo, 1, 2);
 
        
        ComboBox<String> chooseNumberOfPlayers = new ComboBox<>();
        ObservableList<String> options = FXCollections.observableArrayList("1 player", "2 players","3 players","4 players");
        chooseNumberOfPlayers.getItems().addAll(options);
        chooseNumberOfPlayers.setValue("Select number of players");
        chooseNumberOfPlayers.setPrefWidth(220);
        chooseNumberOfPlayers.setStyle("-fx-font: 15 arial; -fx-base: #FF00FF;");
        
        chooseNumberOfPlayers.setOnAction(e -> {
            numberOfPlayers = (options.indexOf(chooseNumberOfPlayers.getValue()) + 1);
            startButton.setDisable(false);
        });
        
        chooseNumberOfPlayers.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER) && !chooseNumberOfPlayers.getValue().equals("Select number of players")) {
                restart();
                firstStage.hide();
                cancelButton.setDisable(false);    
            }
            if(e.getCode().equals(KeyCode.ESCAPE) && cancelButton.isDisable() == false) {
                setIspaused(false);
                firstStage.hide();
            }
        });
        
        startButton = new Button("LET'S DO THIS!");
        startButton.setDisable(true);
        startButton.setPrefWidth(140);
        startButton.setStyle("-fx-font: 15 arial; -fx-base: #009933;");
        startButton.setOnAction(e -> {
            restart();
            firstStage.hide();
            cancelButton.setDisable(false);
        });
        cancelButton = new Button("Cancel");
        cancelButton.setDisable(true);
        cancelButton.setStyle("-fx-font: 15 arial; -fx-base: #FF3300;");
        cancelButton.setOnAction(e -> {
            setIspaused(false);
            firstStage.hide();
        });
        
        firstPane.add(chooseNumberOfPlayers, 1, 3);
        firstPane.add(startButton, 2, 3);
        firstPane.add(cancelButton, 3, 3);
        firstScene = new Scene(firstPane, 600, 300);
        firstStage.setScene(firstScene);
        firstStage.setTitle("Set up game");
        firstStage.show();
    }
    public void setUpRightPane() {
        int fontSize = 17;
        rightPane.setBackground(new Background(new BackgroundFill(GameGrid.SAFE_ZONE_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        rightPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.FULL)));
        rightPane.getChildren().add(scorePane);
        rightPane.setConstraints(scorePane, 1, 1);
        rightPane.getColumnConstraints().addAll(new ColumnConstraints(30), new ColumnConstraints(10), new ColumnConstraints(30));
        rightPane.getRowConstraints().addAll(new RowConstraints(10), new RowConstraints(500), new RowConstraints(30), new RowConstraints(30), new RowConstraints(30), new RowConstraints(30), new RowConstraints(30));

        
        Text regularBonusText = new Text(BonusHandler.REGULAR_BONUS_DESCRIPTION);
        regularBonusText.setFont(new Font(fontSize));
        Text makeShortBonusText = new Text(BonusHandler.MAKE_SHORT_BONUS_DESCRIPTION);
        makeShortBonusText.setFont(new Font(fontSize));
        Text addDeathBlockBonusText = new Text(BonusHandler.ADD_DEATH_BLOCK_BONUS_DESCRIPTION);
        addDeathBlockBonusText.setFont(new Font(fontSize));
        
        
        Rectangle regularBonusColor = new Rectangle(fontSize, fontSize, BonusHandler.REGULAR_BONUS_COLOR);
        Rectangle makeShortBonusColor = new Rectangle(fontSize, fontSize, BonusHandler.MAKE_SHORT_BONUS_COLOR);
        Rectangle addDeathBlockBonusColor = new Rectangle(fontSize, fontSize, BonusHandler.ADD_DEATH_BLOCK_BONUS_COLOR);
        
        rightPane.getChildren().addAll(regularBonusText, makeShortBonusText, addDeathBlockBonusText, makeShortBonusColor, regularBonusColor, addDeathBlockBonusColor);
        
        rightPane.setConstraints(regularBonusText, 3, 4);
        rightPane.setConstraints(makeShortBonusText, 3, 5);
        rightPane.setConstraints(addDeathBlockBonusText, 3, 6);
        
        rightPane.setConstraints(regularBonusColor, 1, 4);
        rightPane.setConstraints(makeShortBonusColor, 1, 5);
        rightPane.setConstraints(addDeathBlockBonusColor, 1, 6);

    }
    public void setUpScoreBoard() {
        scorePane.getChildren().clear();
        scorePane.setAlignment(Pos.TOP_LEFT);
        scorePane.setPadding(new Insets(5, 5, 10, 30));
        Text scoreHeader = new Text("Scores");
        scorePane.getChildren().add(scoreHeader);
        scoreHeader.setFont(Font.font(STYLESHEET_MODENA, 30));
        scoreHeader.setFill(Color.GREY);
        scoreHeader.setUnderline(true);
    }
    public void initiateScoreBoard() {
        switch(numberOfPlayers) {
            case 4: 
                playerFourScore = new Text();
                playerFourScore.setText(players.get(3).toString());
                scorePane.getChildren().add(1, playerFourScore);
                playerFourScore.setFont(Font.font(PLAYER_SCORE_SIZE));
                playerFourScore.setFill(PLAYER_4_COLOR);
            case 3: 
                playerThreeScore = new Text();
                playerThreeScore.setText(players.get(2).toString());
                scorePane.getChildren().add(1, playerThreeScore);
                playerThreeScore.setFont(Font.font(PLAYER_SCORE_SIZE));
                playerThreeScore.setFill(PLAYER_3_COLOR);
            case 2: 
                playerTwoScore = new Text();
                playerTwoScore.setText(players.get(1).toString());
                scorePane.getChildren().add(1, playerTwoScore);
                playerTwoScore.setFont(Font.font(PLAYER_SCORE_SIZE));
                playerTwoScore.setFill(PLAYER_2_COLOR);
            case 1: 
                playerOneScore = new Text();
                playerOneScore.setText(players.get(0).toString());
                scorePane.getChildren().add(1, playerOneScore);
                playerOneScore.setFont(Font.font(PLAYER_SCORE_SIZE));
                playerOneScore.setFill(PLAYER_1_COLOR);
        }
 
    }
    public void showScores() {
        switch(numberOfPlayers) {
            case 4: 
                playerFourScore.setText(players.get(3).toString());
            case 3: 
                playerThreeScore.setText(players.get(2).toString());
            case 2: 
                playerTwoScore.setText(players.get(1).toString());
            case 1: 
                playerOneScore.setText(players.get(0).toString());
        }
    }
    public int getNumberOfAlivePlayers() {
        int result = 0;
        for(Player player: players) {
            if(player.isAlive()) {
                result ++;
            }
        }
        return result;
    }

    public void setUpGameInfo(Player winner) {
        if(winner == null) {
            gameInfo.setText("Battle against your friends. Collect bounses for points, and loose \n"
            + "them when you die. When the field is reduced to the core, the \n"
            + "elimination begins as snakes with negative scores are terminated. \n" 
            + "Last snake standing wins!");
            gameInfo.setFill(Color.BLACK);
            gameInfo.setFont(Font.font(15));
        }
        else {
            gameInfo.setText("The winner is " + winner.getName());
            gameInfo.setFill(winner.getPlayerColor());
            gameInfo.setFont(Font.font(30));
        }
    }
}