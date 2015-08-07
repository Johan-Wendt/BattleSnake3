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
    private long gameSpeed = 10;
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
        player = new Player(1010, PLAYER_START_LENGTH, MULIPLIER_X, Color.RED, gameGrid);
        eventHandler = new EventHandler(gameGrid);
        
        
        BattleStage.setScene(mainScene);
        BattleStage.show();
        
        thread = new Thread(new Runnable()  {
        @Override
        public void run() {
            try {
                while (isRunning) {
                    player.movePlayer();
                    eventHandler.eventRound();

                    Thread.sleep(1000/gameSpeed);
                }    
            }
            catch (InterruptedException ex) {
            }
        }

        });
        thread.start();
        
               
        
        mainScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                
                case RIGHT: player.setCurrentDirection("Right"); break;
                case LEFT: player.setCurrentDirection("Left"); break;
                case UP: player.setCurrentDirection("Up"); break;
                case DOWN: player.setCurrentDirection("Down"); break;
                case ENTER: player.setAlive(); break;
                case L: player.setLength(player.getLength() + 1); break;
                case F: gameSpeed += 2; break;
                case R: restart(); break;
                case P: System.out.println(thread.isAlive()); break;
                //default only for testing
                default:player.movePlayer();
            }
        });
    }
    public void restart() {
        player.erasePlayer();
        player = new Player(1010, PLAYER_START_LENGTH, MULIPLIER_X, Color.RED, gameGrid);

    }
}