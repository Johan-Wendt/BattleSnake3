
package battlesnake;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 *
 * @author johanwendt
 */
public class GameEngine extends Application {
    //Static finals
    //To be able to give every block in the field grid a unique id every 
    //block in y-direction adds 1 to the id while every block in 
    //x-direction adds 1000 (MULIPLIER_X).
    public static final int MULIPLIER_X = 1000;
    public static final int RIGHT = MULIPLIER_X;
    public static final int LEFT = -MULIPLIER_X;
    public static final int DOWN = 1;
    public static final int UP = -1;

    public static final int PLAYER_1_STARTDIRECTION = RIGHT;
    public static final int PLAYER_2_STARTDIRECTION = LEFT;
    public static final int PLAYER_3_STARTDIRECTION = UP;
    public static final int PLAYER_4_STARTDIRECTION = DOWN;
    
    public static final int PLAYER_START_SLOWNESS = 25;
    public static final int PLAYER_DEATH_PENALTY = -2;
    public static final int PLAYER_START_LENGTH = 20;
    
    //Must be an uneven number
    public static final int BRICKS_PER_ROW = 41;
    
    private static final boolean isRunning = true;
    
    //Player fields
    private static final ArrayList<Player> players = new ArrayList<>();
    private static final HashMap<KeyCode, Integer> player1Controls = new HashMap<>();
    private static final HashMap<KeyCode, Integer> player2Controls = new HashMap<>();
    private static final HashMap<KeyCode, Integer> player3Controls = new HashMap<>();
    private static final HashMap<KeyCode, Integer> player4Controls = new HashMap<>();
    
    private static int gameSpeed = 3;
    private static Stage battleStage;
    
    //Game fields
    private static boolean isPaused = true;
    private static int numberOfPlayers = 1;
    private Thread thread;
    private static BonusHandler bonusHandler;
    private static GameGrid gameGrid;
    private static UserInterface GUI;
    
    
    public void NewGameEngine() {
    }
    @Override
    public void start(Stage battleStage) throws InterruptedException {
        this.battleStage = battleStage;

        GUI = new UserInterface(this, players);
        gameGrid = new GameGrid();
        
        bonusHandler = new BonusHandler();
        setUpDefaultControlKeys();

        
        //Get the thread that is running movoment of the players and creations of bonuses started.
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                thread.setPriority(Thread.MAX_PRIORITY);
                try {
                    while (isRunning) {
                        if(!isPaused) {
                            int moved = 0;
                            for(Player player: players) {
                                moved += player.movePlayer();
                            }
                            playerKiller(gameGrid.deathBuilder());
                            if(moved > 0) {
                                bonusHandler.bonusRound();
                                RightPane.showScores(players);
                            }
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
            }
        });
        thread.start();
    }
/**
 * Brings up the initial screen and displays the winner of the game.
 */
    public void gameOver() {
        Player winner = null;
        int highest = -10000;
        for(Player player: players) {
            if(player.getScore() > highest) {
                highest = player.getScore();
                winner = player;
            }
        }
        GUI.gameOver(winner);
        setPaused(true);
    }
    public void takePressedKey(KeyCode key) {
        for(Player player: players) {
            player.setCurrentDirection(key);
        }
    }
    /**
     * Unpauses the game and sets all players in status alive so they start moving.
     */
    public static void begin() {
        setPaused(false);
        for(Player player: players) {
            player.setAlive(true);
        }
    }
    /**
     * Restart the game from the begining. Everything needed is reset or recreated
     * to enable every game to start from scratch.
     */
    public static void restart() {
        gameGrid = new GameGrid();
        players.clear();
        createPlayers ();
        UserInterface.restart(players);   
        begin(); 
    }
    /**
     * Turns upp the game speed for all the players. It does this by making
     * the pause in the gameloop-thread shorter. This is not yet implemented 
     * anywhere in the game.
     */
    public static void turnUpGameSpeed() {
        if(gameSpeed > 1) {
            gameSpeed --;
        }
    }
    /**
     * Turns down the game speed for all the players. It does this by making
     * the pause in the gameloop-thread shorter. This is not yet implemented 
     * anywhere in the game.
     */
    public static void turnDownGameSpeed() {
        if(gameSpeed < 10) {
            gameSpeed ++;
        }
    }
    /**
     * Pauses the game. Used for when menus are up.
     * @param pause
     */
    public static void setPaused(boolean pause) {
        isPaused = pause;
    }
    public static boolean isPaused() {
        return isPaused;
    }
    public static void setNumberOfPlayers(int toPlay) {
        numberOfPlayers = toPlay;
    }
    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    public Player getPlayer(int playerNumber) {
        return players.get(playerNumber);
    }
    public static GameGrid getCurrentGameGrid() {
        return gameGrid;
    }
    /**
     * Checks if a given deathblock is located inside a player and kills it if it does.
     * This method is used to make changes in the grid be able to
     * kill a player.
     * @param deathBlock the id of the deathBlock. 
     */
    public void playerKiller(int deathBlock) {
        for(Player player: players) {
            if(player.containsBlock(deathBlock)) {
                player.killPlayer();
                RightPane.showScores(players);
            }
        }
    }   
    /**
     * Number of players that are still alive. Currently only used for determening 
     * wheter the game is over or not.
     * @return The number of players still alive.
     */
    public int getNumberOfAlivePlayers() {
        int result = 0;
        for(Player player: players) {
            if(player.isAlive()) {
                result ++;
            }
        }
        return result;
    }
    /**
     * Creates the chosen number of players.
     */
    private static void createPlayers () {
        switch(numberOfPlayers) {
            case 4: players.add(0, new Player("Player 4", PLAYER_4_STARTDIRECTION, UserInterface.playerFourColor, bonusHandler, player4Controls));
            case 3: players.add(0, new Player("Player 3", PLAYER_3_STARTDIRECTION, UserInterface.playerThreeColor, bonusHandler, player3Controls));
            case 2: players.add(0, new Player("Player 2", PLAYER_2_STARTDIRECTION, UserInterface.playerTwoColor, bonusHandler, player2Controls));
            case 1: players.add(0, new Player("Player 1", PLAYER_1_STARTDIRECTION, UserInterface.playerOneColor, bonusHandler, player1Controls));
        } 
    }
    /**
     * Adds a key for contolling the direction of the player. If a key for the 
     * given direction already was assigned it is removed.
     * @param playerName The name of the player to controll.
     * @param direction The direction the key should make the player turn.
     * @param newKey The key used for turning the player in the given direction.
     */
    public void setControlKey(String playerName, int direction, KeyCode newKey) {
        if(playerName.equals("Player 1")) {
            removeDuplicateKeys(player1Controls, direction);
            player1Controls.put(newKey, direction);
            GUI.updateControlText(playerName, direction, newKey.toString());
        }
        if(playerName.equals("Player 2")) {
            removeDuplicateKeys(player2Controls, direction);
            player2Controls.put(newKey, direction);
            GUI.updateControlText(playerName, direction, newKey.toString());
        }
        if(playerName.equals("Player 3")) {
            removeDuplicateKeys(player3Controls, direction);
            player3Controls.put(newKey, direction);
            GUI.updateControlText(playerName, direction, newKey.toString());
        }
        if(playerName.equals("Player 4")) {
            removeDuplicateKeys(player4Controls, direction);
            player4Controls.put(newKey, direction);
            GUI.updateControlText(playerName, direction, newKey.toString());
        }
    }
    /**
     * Shaves away duplicate input-keys given via the setControlKey() method.
     * @param toShave The HashMap that the new key was added to.
     * @param direction The direction the new key controlls.
     */
    private void removeDuplicateKeys(HashMap<KeyCode, Integer> toShave, int direction) {
        for(KeyCode keyCode: toShave.keySet())
            if(toShave.get(keyCode).equals(direction)) {
                toShave.remove(keyCode);
                return;
            }
    } 
    /**
     * Sets upp the deafult controll keys for the game. These keys
     * are set everytime the application is restarted.
     */
    
    public void setUpDefaultControlKeys() {
        setControlKey("Player 1", UP, KeyCode.UP);
        setControlKey("Player 1", RIGHT, KeyCode.RIGHT);
        setControlKey("Player 1", DOWN, KeyCode.DOWN);
        setControlKey("Player 1", LEFT, KeyCode.LEFT);
        
        setControlKey("Player 2", UP, KeyCode.W);
        setControlKey("Player 2", RIGHT, KeyCode.D);
        setControlKey("Player 2", DOWN, KeyCode.S);
        setControlKey("Player 2", LEFT, KeyCode.A);
        
        setControlKey("Player 3", UP, KeyCode.T);
        setControlKey("Player 3", RIGHT, KeyCode.H);
        setControlKey("Player 3", DOWN, KeyCode.G);
        setControlKey("Player 3", LEFT, KeyCode.F);
        
        setControlKey("Player 4", UP, KeyCode.I);
        setControlKey("Player 4", RIGHT, KeyCode.L);
        setControlKey("Player 4", DOWN, KeyCode.K);
        setControlKey("Player 4", LEFT, KeyCode.J);
        
    }
}
