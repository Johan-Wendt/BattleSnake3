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
public class MainSnakeBoard extends Application {
    
    private Pane pane = new Pane();
    public static final int BLOCK_SIZE = 16;
    public static final int GRID_WIDTH = 800;
    public static final int GRID_HEIGTH = 640;
    public static final int MULIPLIER_X = 1000;
    public static final int RIGHT = MULIPLIER_X;
    public static final int LEFT = -MULIPLIER_X;
    public static final int DOWN = 1;
    public static final int UP = -1;
    public static final Color PLAYER_1_COLOR = Color.RED;
    public static final Color PLAYER_2_COLOR = Color.GREEN;
    public static final Color PLAYER_3_COLOR = Color.BLUE;
    public static final Color PLAYER_4_COLOR = Color.YELLOW;
    public static final int PLAYER_STARTPOINT = ((GRID_WIDTH * MULIPLIER_X) / BLOCK_SIZE) / 2 + ((GRID_HEIGTH / BLOCK_SIZE) / 2);
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
    
    public MainSnakeBoard () {
        
    }
    
    @Override
    public void start(Stage BattleStage) throws InterruptedException {
        Scene mainScene = new Scene(pane, GRID_WIDTH, GRID_HEIGTH);
        gameGrid = new GameGrid(GRID_HEIGTH, GRID_WIDTH, pane, MULIPLIER_X, BLOCK_SIZE);
        eventHandler = new EventHandler(gameGrid);
        players.add(player1 = new Player(PLAYER_1_STARTDIRECTION, PLAYER_1_COLOR, gameGrid, eventHandler));
        players.add(player2 = new Player(PLAYER_2_STARTDIRECTION, PLAYER_2_COLOR, gameGrid, eventHandler));
        //players.add(player3 = new Player(PLAYER_3_STARTDIRECTION, PLAYER_3_COLOR, gameGrid, eventHandler));
        //players.add(player4 = new Player(PLAYER_4_STARTDIRECTION, PLAYER_4_COLOR, gameGrid, eventHandler));
        
        
        BattleStage.setScene(mainScene);
        BattleStage.show();
        
        thread = new Thread(new Runnable()  {
        @Override
        public void run() {
            try {
                while (isRunning) {
                    for(Player player: players) {
                        player.movePlayer();
                    }
                    eventHandler.eventRound();

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
}