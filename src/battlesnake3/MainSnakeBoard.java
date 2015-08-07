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




/**
 *
 * @author johanwendt
 */
public class MainSnakeBoard extends Application {
    
    private Pane pane = new Pane();
    public static final int BLOCK_SIZE = 16;
    public static final int GRID_WIDTH = 800;
    public static final int GRID_HEIGTH = 640;
    public static final int PLAYER_START_LENGTH = 8; 
    public static final int MULIPLIER_X = 1000;
    public static final int RIGHT = MULIPLIER_X;
    public static final int LEFT = -MULIPLIER_X;
    public static final int DOWN = 1;
    public static final int UP = -1;
    private long gameSpeed = 3;
    private boolean isRunning = true;
    private String currentDirection = "Right";
    private String turnDirection = "Right";
    private boolean isHorizontal = true;
    private GameGrid gameGrid;
    private Player player;
    private Thread thread;
    private EventHandler eventHandler;
    
    public MainSnakeBoard () {
        
    }
    
    @Override
    public void start(Stage BattleStage) throws InterruptedException {
        Scene mainScene = new Scene(pane, GRID_WIDTH, GRID_HEIGTH);
        gameGrid = new GameGrid(GRID_HEIGTH, GRID_WIDTH, pane, MULIPLIER_X, Color.AQUA, BLOCK_SIZE);
        eventHandler = new EventHandler(gameGrid);
        player = new Player(1010, PLAYER_START_LENGTH, Color.RED, gameGrid, eventHandler);

        
        
        BattleStage.setScene(mainScene);
        BattleStage.show();
        
        thread = new Thread(new Runnable()  {
        @Override
        public void run() {
            try {
                while (isRunning) {
                    player.movePlayer();
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
                
                case RIGHT: player.setCurrentDirection(RIGHT); break;
                case LEFT: player.setCurrentDirection(LEFT); break;
                case UP: player.setCurrentDirection(UP); break;
                case DOWN: player.setCurrentDirection(DOWN); break;
                case ENTER: player.setAlive(); break;
                case L: player.setLength(player.getLength() + 1); break;
                case F: turnUpGameSpeed(); break;
                case S: turnDownGameSpeed(); break;
                case R: restart(); break;
                case P: System.out.println(thread.isAlive()); break;
                //default only for testing
                default:player.movePlayer();
            }
        });
    }
    public void restart() {
        player.erasePlayer();
        player = new Player(1010, PLAYER_START_LENGTH, Color.RED, gameGrid, eventHandler);
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