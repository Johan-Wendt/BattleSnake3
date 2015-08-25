
package battlesnake;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
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
    
    //Player fields
    private final ArrayList<Player> players = new ArrayList<>();
    private HashMap<KeyCode, Integer> player1Controls = new HashMap<>();
    private HashMap<KeyCode, Integer> player2Controls = new HashMap<>();
    private HashMap<KeyCode, Integer> player3Controls = new HashMap<>();
    private HashMap<KeyCode, Integer> player4Controls = new HashMap<>();
    
    //Game fields
    private long gameSpeed = 3;
    private final boolean isRunning = true;
    private boolean isPaused = true;
    private int numberOfPlayers = 1;
    private Thread thread;
    private BonusHandler bonusHandler;
    private GameGrid gameGrid;
    private UserInterface GUI;
    
    
    public void NewGameEngine() {
    }
    @Override
    public void start(Stage battleStage) throws InterruptedException {
        //Set up the screens and activate the players.
    
        //Set up the screens and activate the players.
        GUI = new UserInterface(this);
        GUI.setUpMainScreen();
        gameGrid = new GameGrid(UserInterface.PANE);
        bonusHandler = new BonusHandler(gameGrid, numberOfPlayers);
        setUpDefaultControlKeys();
        GUI.setUpControlsScreen();
        GUI.setUpFirstScreen();
        GUI.setUpAboutScreen();
        GUI.setUpRightPane();

        
        //Get the thread that is running movoment of the players and creations of bonuses started.
        thread = new Thread(() -> {
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
                            GUI.showScores();
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
        GUI.setUpWinnerInfo(winner);
        setPaused(true);
        GUI.setCancelButtonDisabled(true);
        GUI.showFirstStage(true);
    }
    public void takePressedKey(KeyCode key) {
        for(Player player: players) {
            player.setCurrentDirection(key);
        }
    }
    /**
     * Unpauses the game and sets all players in status alive so they start moving.
     */
    public void begin() {
        setPaused(false);
        for(Player player: players) {
            player.setAlive(true);
        }
    }
    /**
     * Restart the game from the begining. Everything needed is reset or recreated
     * to enable every game to start from scratch.
     */
    public void restart() {
        GUI.setUpWinnerInfo(null);
        gameGrid = new GameGrid(UserInterface.PANE);
        bonusHandler = new BonusHandler(gameGrid, numberOfPlayers);
        erasePlayers();
        players.clear();
        createPlayers ();
        GUI.setUpScoreBoard();
        GUI.initiateScoreBoard();
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
    /**
     * Turns down the game speed for all the players. It does this by making
     * the pause in the gameloop-thread shorter. This is not yet implemented 
     * anywhere in the game.
     */
    public void turnDownGameSpeed() {
        if(gameSpeed < 10) {
            gameSpeed ++;
        }
    }
    /**
     * Pauses the game. Used for when menus are up.
     * @param pause
     */
    public void setPaused(boolean pause) {
        isPaused = pause;
        if(pause) {
            //underMenu4.setText("Unpause");
        }
        else {
            //underMenu4.setText("Pause");
        }
    }
    public boolean isPaused() {
        return isPaused;
    }
    public void setNumberOfPlayers(int toPlay) {
        numberOfPlayers = toPlay;
    }
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
    public Player getPlayer(int playerNumber) {
        return players.get(playerNumber);
    }
    /**
     * Erases all the players in the current game.
     */
    public void erasePlayers() {
        for(Player player: players) {
            player.erasePlayer();
        }
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
                GUI.showScores();
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
    public void createPlayers () {
        switch(numberOfPlayers) {
            case 4: players.add(0, new Player("Player 4", PLAYER_4_STARTDIRECTION, UserInterface.PLAYER_4_COLOR, gameGrid, bonusHandler, player4Controls));
            case 3: players.add(0, new Player("Player 3", PLAYER_3_STARTDIRECTION, UserInterface.PLAYER_3_COLOR, gameGrid, bonusHandler, player3Controls));
            case 2: players.add(0, new Player("Player 2", PLAYER_2_STARTDIRECTION, UserInterface.PLAYER_2_COLOR, gameGrid, bonusHandler, player2Controls));
            case 1: players.add(0, new Player("Player 1", PLAYER_1_STARTDIRECTION, UserInterface.PLAYER_1_COLOR, gameGrid, bonusHandler, player1Controls));
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
            switch(direction) {
                case UP:
                    removeDuplicateKeys(player1Controls, UP);
                    player1Controls.put(newKey, UP);
                    GUI.player1Up.setText(newKey.toString());
                    break;
                case RIGHT:
                    removeDuplicateKeys(player1Controls, RIGHT);
                    player1Controls.put(newKey, RIGHT);
                    GUI.player1Right.setText(newKey.toString());
                    break;
                case DOWN: 
                    removeDuplicateKeys(player1Controls, DOWN);
                    player1Controls.put(newKey, DOWN);
                    GUI.player1Down.setText(newKey.toString());
                    break;
                case LEFT: 
                    removeDuplicateKeys(player1Controls, LEFT);
                    player1Controls.put(newKey, LEFT);
                    GUI.player1Left.setText(newKey.toString());
                    break;              
            }
        }
        if(playerName.equals("Player 2")) {
            switch(direction) {
                case UP: 
                    removeDuplicateKeys(player2Controls, UP);
                    player2Controls.put(newKey, UP);
                    GUI.player2Up.setText(newKey.toString());
                    break;
                case RIGHT: 
                    removeDuplicateKeys(player2Controls, RIGHT);
                    player2Controls.put(newKey, RIGHT);
                    GUI.player2Right.setText(newKey.toString());
                    break;
                case DOWN: 
                    removeDuplicateKeys(player2Controls, DOWN);
                    player2Controls.put(newKey, DOWN);
                    GUI.player2Down.setText(newKey.toString());
                    break;
                case LEFT: 
                    removeDuplicateKeys(player2Controls, LEFT);
                    player2Controls.put(newKey, LEFT);
                    GUI.player2Left.setText(newKey.toString());
                    break;
            }
        }
        if(playerName.equals("Player 3")) {
            switch(direction) {
                case UP:
                    removeDuplicateKeys(player3Controls, UP);
                    player3Controls.put(newKey, UP);
                    GUI.player3Up.setText(newKey.toString());
                    break;
                case RIGHT: 
                    removeDuplicateKeys(player3Controls, RIGHT);
                    player3Controls.put(newKey, RIGHT);
                    GUI.player3Right.setText(newKey.toString());
                    break;
                case DOWN: 
                    removeDuplicateKeys(player3Controls, DOWN);
                    player3Controls.put(newKey, DOWN);
                    GUI.player3Down.setText(newKey.toString());
                    break;
                case LEFT: 
                    removeDuplicateKeys(player3Controls, LEFT);
                    player3Controls.put(newKey, LEFT);
                    GUI.player3Left.setText(newKey.toString());
                    break;
            }
        }
        if(playerName.equals("Player 4")) {
            switch(direction) {
                case UP: 
                    removeDuplicateKeys(player4Controls, UP);
                    player4Controls.put(newKey, UP);
                    GUI.player4Up.setText(newKey.toString());
                    break;
                case RIGHT: 
                    removeDuplicateKeys(player4Controls, RIGHT);
                    player4Controls.put(newKey, RIGHT);
                    GUI.player4Right.setText(newKey.toString());
                    break;
                case DOWN: 
                    removeDuplicateKeys(player4Controls, DOWN);
                    player4Controls.put(newKey, DOWN);
                    GUI.player4Down.setText(newKey.toString());
                    break;
                case LEFT: 
                    removeDuplicateKeys(player4Controls, LEFT);
                    player4Controls.put(newKey, LEFT);
                    GUI.player4Left.setText(newKey.toString());
                    break;
            }
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
