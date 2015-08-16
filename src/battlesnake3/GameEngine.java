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
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;





/**
 *
 * @author johanwendt
 */
public class GameEngine extends Application {
    
    private BorderPane mainPane = new BorderPane();
    private Pane pane = new Pane();
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
    private long gameSpeed = 3;
    private boolean isRunning = true;
    private GameGrid gameGrid;
    private ArrayList<Player> players = new ArrayList<>();
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Thread thread;
    private BonusHandler bonusHandler;
    private int numberOfPlayers = 1;
    private Scene mainScene;
    private Text playerOneScore;
    private Text playerTwoScore;
    private Text playerThreeScore;
    private Text playerFourScore;
    private VBox scorePane;
    public static final int PLAYER_SCORE_SIZE = 20;
    private Scene firstScene;
    private Stage firstStage;
    private Stage battleStage = new Stage();
    private VBox firstPane;
    private ChoiceBox chooseNumberOfPlayers;
    private boolean isPaused = true;
    private Button startButton;
    private Button cancelButton;
    private Text winnerInfo = new Text("");
    
    public GameEngine () {
        
    }
    
    @Override
    public void start(Stage battleStage) throws InterruptedException {  
        setUpMainScreen();
        gameGrid = new GameGrid(pane);
        bonusHandler = new BonusHandler(gameGrid);
        activatePlayers();
        setUpFirstScreen();
        setUpScoreBoard();
        initiateScoreBoard();
      
        thread = new Thread(new Runnable()  {
        @Override
        public void run() {
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
                        Platform.runLater(new Runnable() {
                            @Override 
                            public void run() {
                                gameOver();
                            }
                        });
                        
                    }
                    Thread.sleep(gameSpeed);
                }    
            }
            catch (InterruptedException ex) {
            }
        }

        });
        thread.start();
        
        
               
        
        mainScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                
                case RIGHT: player1.setCurrentDirection(RIGHT); break;
                case LEFT: player1.setCurrentDirection(LEFT); break;
                case UP: player1.setCurrentDirection(UP); break;
                case DOWN: player1.setCurrentDirection(DOWN); break;
                case D: player2.setCurrentDirection(RIGHT); break;
                case A: player2.setCurrentDirection(LEFT); break;
                case W: player2.setCurrentDirection(UP); break;
                case S: player2.setCurrentDirection(DOWN); break;
                case H: player3.setCurrentDirection(RIGHT); break;
                case F: player3.setCurrentDirection(LEFT); break;
                case T: player3.setCurrentDirection(UP); break;
                case G: player3.setCurrentDirection(DOWN); break;
                case L: player4.setCurrentDirection(RIGHT); break;
                case J: player4.setCurrentDirection(LEFT); break;
                case I: player4.setCurrentDirection(UP); break;
                case K: player4.setCurrentDirection(DOWN); break;
                case ENTER: begin();break;   
                case P: firstStage.show(); break;

            }
        });
    }
    public void test() {

    }
    public void pause() {
        isPaused = true;
    }
    public void unPause() {
        isPaused = false;
    }
    public void gameOver() {
        Player winner = null;
        int highest = -10;
        for(Player player: players) {
            if(player.getScore() > highest) {
                highest = player.getScore();
                winner = player;
            }
        }
        winnerInfo.setText("The winner is " + winner.getName());
        winnerInfo.setFill(winner.getPlayerColor());
        winnerInfo.setFont(Font.font(30));
        pause();
        cancelButton.setDisable(true);
        firstStage.show();
    }
    public void begin() {
        isPaused = false;
        for(Player player: players) {
            player.setIsAlive(true);
        }
    }
    public void restart() {
        winnerInfo.setText("");
        gameGrid = new GameGrid(pane);
        bonusHandler = new BonusHandler(gameGrid);
        setNumberOfPlayers(numberOfPlayers);
        begin();
        
    }
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
            case 4: players.add(0, player4 = new Player("Player 4", PLAYER_4_STARTDIRECTION, PLAYER_4_COLOR, gameGrid, bonusHandler));
            case 3: players.add(0, player3 = new Player("Player 3", PLAYER_3_STARTDIRECTION, PLAYER_3_COLOR, gameGrid, bonusHandler));
            case 2: players.add(0, player2 = new Player("Player 2", PLAYER_2_STARTDIRECTION, PLAYER_2_COLOR, gameGrid, bonusHandler));
            case 1: players.add(0, player1 = new Player("Player 1", PLAYER_1_STARTDIRECTION, PLAYER_1_COLOR, gameGrid, bonusHandler));
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
        mainScene = new Scene(mainPane, GameGrid.GRID_SIZE + 200, GameGrid.GRID_SIZE + 20);
        
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
            pause();
            firstStage.show();
        });
        
        underMenu4.setAccelerator(KeyCombination.keyCombination("META + Q"));
        
        underMenu4.setOnAction(e -> {
            Platform.exit();
        });
        
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
        firstStage.setMaxWidth(400);
        firstStage.setMaxHeight(400);
        ComboBox<String> chooseNumberOfPlayers = new ComboBox<>();
        ObservableList<String> options = FXCollections.observableArrayList("1 player", "2 players","3 players","4 players");
        chooseNumberOfPlayers.getItems().addAll(options);
        chooseNumberOfPlayers.setValue("Select number of players"); 
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
        });
        startButton = new Button("Start Game");
        startButton.setDisable(true);
        startButton.setOnAction(e -> {
            restart();
            firstStage.hide();
            cancelButton.setDisable(false);
        });
        cancelButton = new Button("Cancel");
        cancelButton.setDisable(true);
        cancelButton.setOnAction(e -> {
            unPause();
            firstStage.hide();
        });

        
        
        firstPane = new VBox(40);
        firstPane.setAlignment(Pos.CENTER);
        firstPane.setPadding(new Insets(40, 40, 40, 40));
        firstPane.getChildren().addAll(winnerInfo, chooseNumberOfPlayers, startButton, cancelButton);
        firstScene = new Scene(firstPane, 600, 300);
        firstStage.setScene(firstScene);
        firstStage.setTitle("Set up game");
        firstStage.show();
    }
    public void setUpScoreBoard() {
        scorePane = new VBox();
        mainPane.setCenter(scorePane);
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
}