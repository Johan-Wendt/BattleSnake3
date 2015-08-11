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




/**
 *
 * @author johanwendt
 */
public class GameEngine extends Application {
    
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
    private EventHandler eventHandler;
    private int numberOfPlayers = 2;
    private Scene mainScene;
    
    public GameEngine () {
        
    }
    
    @Override
    public void start(Stage BattleStage) throws InterruptedException {
        mainScene = new Scene(pane, GameGrid.GRID_SIZE, GameGrid.GRID_SIZE);
        gameGrid = new GameGrid(pane);
        eventHandler = new EventHandler(gameGrid);
        activatePlayers(numberOfPlayers);
        
        
        BattleStage.setScene(mainScene);
        BattleStage.show();
        
        thread = new Thread(new Runnable()  {
        @Override
        public void run() {
            try {
                while (isRunning) {
                    for(Player player: players) {
                        player.movePlayer();
                        playerKiller(gameGrid.deathBuilder(numberOfPlayers));
                    }
                    eventHandler.eventRound();
                    getPlayerScores();

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

            }
        });
    }
    public void createScene() {
            
    }
    
    public void begin() {
        for(Player player: players) {
            player.setAlive();
        }
    }
    public void turnUpGameSpeed() {
        if(gameSpeed > 1) {
            gameSpeed --;
            System.out.println(gameSpeed);
        }
    }
    public void turnDownGameSpeed() {
        if(gameSpeed < 10) {
            gameSpeed ++;
        }
    }
    public void playerKiller(int deathBlock) {
        for(Player player: players) {
            if(player.containsBlock(deathBlock)) {
                player.killPlayer();
            }
        }
    }
    public void activatePlayers (int numberOfPlayers) {
        switch(numberOfPlayers) {
        case 4: players.add(player4 = new Player(PLAYER_4_STARTDIRECTION, PLAYER_4_COLOR, gameGrid, eventHandler));
        case 3: players.add(player3 = new Player(PLAYER_3_STARTDIRECTION, PLAYER_3_COLOR, gameGrid, eventHandler));
        case 2: players.add(player2 = new Player(PLAYER_2_STARTDIRECTION, PLAYER_2_COLOR, gameGrid, eventHandler));
        case 1: players.add(player1 = new Player(PLAYER_1_STARTDIRECTION, PLAYER_1_COLOR, gameGrid, eventHandler));
    }
    }
    public void setNumberOfPlayers(int players) {
        numberOfPlayers = players;
    }
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    public void getPlayerScores() {
        int n = 1;
        for(Player player: players) {
            int score = player.getScore();
            System.out.println(" player:" + n + " score:" + score);
            n++;            
        }
    }
}