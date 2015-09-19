/*
 * Copyright (C) 2015 johanwendt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package battlesnake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
/**
 *This class is the master class. It drives the game via a loop.
 * @author johanwendt
 */
public class GameEngine extends Application {
    //To be able to give every block in the field grid a unique id every 
    //block in y-direction adds 1 to the id while every block in 
    //x-direction adds 1000 (MULIPLIER_X).
    public static final int MULIPLIER_X = 1000;
    public static final int RIGHT = MULIPLIER_X;
    public static final int LEFT = -MULIPLIER_X;
    public static final int DOWN = 1;
    public static final int UP = -1;
    
    public static final int PLAYER_START_SLOWNESS = 25;
    public static final int PLAYER_DEATH_PENALTY = -2;
    public static final int PLAYER_START_LENGTH = 20;
    public static final int MAX_NUMBER_OF_PLAYERS = 4;
    
    //Must be an uneven number
    public static final int BRICKS_PER_ROW = 41;
    
    private static final boolean isRunning = true;
    
    private static final HashSet<Player> players = new HashSet<>(4);
    private static final HashMap<KeyCode, Integer> player1Controls = new HashMap<>();
    private static final HashMap<KeyCode, Integer> player2Controls = new HashMap<>();
    private static final HashMap<KeyCode, Integer> player3Controls = new HashMap<>();
    private static final HashMap<KeyCode, Integer> player4Controls = new HashMap<>();
    
    private static int gameSpeed = 3;
    private static Stage battleStage;
    
    private static boolean isPaused = true;
    private static int numberToPlay = 1;
    private Thread thread;
    private static BonusHandler bonusHandler;
    private static GameGrid gameGrid;
    private static KeyCode pauseKey = KeyCode.P;
    
    private static boolean pauseButtonPressed = false;
    private static int numberOfPopUpsOpen = 0;
    
    private static UserInterface userInterface;
        
    public void NewGameEngine() {
    }
    @Override
    public void start(Stage battleStage) throws InterruptedException {
        this.battleStage = battleStage;

        userInterface = new UserInterface(this);
        gameGrid = new GameGrid();
        
        bonusHandler = new BonusHandler();
        setUpDefaultControlKeys();
        startGameThread();
    }

        private void startGameThread() {
        
        //Get the thread that is running movoment of the players and creations of bonuses started.
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
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
                                RightPane.showScores();
                            }
                        }
                        if((gameGrid.isDeathRunning() == false && getNumberOfAlivePlayers() < 2 && !isPaused) || getNumberOfAlivePlayers() < 1 && !isPaused) {
                            Platform.runLater(() -> {
                                gameOver();
                            });
                        }
                        thread.sleep(gameSpeed);
                    }
                }
                catch (InterruptedException ex) {
                }
            }
        });
        thread.start();
    }
/*
 * Brings up the initial screen and displays the winner of the game.
 */
    public void gameOver() {
        PlayerEnum winner = null;
        int highest = -10000;
        for(Player player: players) {
            if(player.getScore() > highest) {
                highest = player.getScore();
                winner = player.getPlayerDetails();
            }
        }
        UserInterface.gameOver(winner);
    }
    public static void takePressedKey(KeyCode key) {
        if(key.equals(pauseKey)) {
            pauseUnpause();
        }
        else {
            for(Player player: players) {
                player.setCurrentDirection(key);
            }
        }
    }
    /**
     * Unpauses the game and sets all players in status alive so they start moving.
     */
    public static void begin() {
        for(Player player: players) {
            player.setAlive(true);
        }
    }
    /**
     * Restart the game from the begining. Everything needed is reset or recreated
     * to enable every game to start from scratch.
     */
    public void restart() {
        thread.interrupt();
        gameGrid = new GameGrid();
        createPlayers ();
        UserInterface.restart();
        startGameThread();
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
    public static void popUpOpened() {
        numberOfPopUpsOpen ++;
        checkStartOrPauseGame();
        
    }
    public static void popUpClosed() {
        numberOfPopUpsOpen --;
        checkStartOrPauseGame();
    }
    public static void pauseUnpause() {
        if(pauseButtonPressed) {
            pauseButtonPressed = false;
        }
        else {
            pauseButtonPressed = true;
        }
        checkStartOrPauseGame();
    }
    public static void checkStartOrPauseGame() {
        
        if (!pauseButtonPressed && numberOfPopUpsOpen == 0) {
            isPaused = false;
        }
        else {
            isPaused = true;
        }
    }
    public static void setNumberOfPlayers(int toPlay) {
        numberToPlay = toPlay;
    }
    public static int getNumberOfPlayers() {
        return numberToPlay;
    }
    public static Player getPlayer(int playerNumber) {
        for(Player player: players) {
            if(player.getPlayerDetails().getNumber() == playerNumber) {
                return player;
            }
        }
        return null;
    }
    public static GameGrid getCurrentGameGrid() {
        return gameGrid;
    }
    public static HashSet<Player> getPlayers() {
        return players;
    }
    public static void setPauseKey(KeyCode newPauseKey) {
        ControlsStage.updatePausedKeyText(newPauseKey.toString());
        pauseKey = newPauseKey;
    }
    /**
     * Checks if a given deathblock is located inside a player and kills it if it does.
     * This method is used to make changes in the grid be able to
     * kill a player.
     * @param deathBlock the id of the deathBlock. 
     */
    public void playerKiller(BuildingBlock deathBlock) {
        for(Player player: players) {
            if(player.containsBlock(deathBlock)) {
                player.killPlayer();
                RightPane.showScores();
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
        players.clear();
        switch(numberToPlay) {
            case 4: players.add(new Player(PlayerEnum.PLAYER_FOUR, bonusHandler, player4Controls));
            case 3: players.add(new Player(PlayerEnum.PLAYER_THREE, bonusHandler, player3Controls));
            case 2: players.add(new Player(PlayerEnum.PLAYER_TWO, bonusHandler, player2Controls));
            case 1: players.add(new Player(PlayerEnum.PLAYER_ONE, bonusHandler, player1Controls));
        } 
    }
    /**
     * Adds a key for contolling the direction of the player. If a key for the 
     * given direction already was assigned it is removed.
     * @param playerName The name of the player to controll.
     * @param direction The direction the key should make the player turn.
     * @param newKey The key used for turning the player in the given direction.
     */
    public static void setControlKey(String playerName, int direction, KeyCode newKey) {
        if(playerName.equals(PlayerEnum.PLAYER_ONE.getName())) {
            removeDuplicateKeys(player1Controls, direction);
            player1Controls.put(newKey, direction);
            ControlsStage.updateControlText(playerName, direction, newKey.toString());
        }
        if(playerName.equals(PlayerEnum.PLAYER_TWO.getName())) {
            removeDuplicateKeys(player2Controls, direction);
            player2Controls.put(newKey, direction);
            ControlsStage.updateControlText(playerName, direction, newKey.toString());
        }
        if(playerName.equals(PlayerEnum.PLAYER_THREE.getName())) {
            removeDuplicateKeys(player3Controls, direction);
            player3Controls.put(newKey, direction);
            ControlsStage.updateControlText(playerName, direction, newKey.toString());
        }
        if(playerName.equals(PlayerEnum.PLAYER_FOUR.getName())) {
            removeDuplicateKeys(player4Controls, direction);
            player4Controls.put(newKey, direction);
            ControlsStage.updateControlText(playerName, direction, newKey.toString());
        }
    }
    /**
     * Shaves away duplicate input-keys given via the setControlKey() method.
     * @param toShave The HashMap that the new key was added to.
     * @param direction The direction the new key controlls.
     */
    private static void removeDuplicateKeys(HashMap<KeyCode, Integer> toShave, int direction) {
        for(KeyCode keyCode: toShave.keySet()) {
            if(toShave.get(keyCode).equals(direction)) {
                toShave.remove(keyCode);
                return;
            }
        }
    } 
    /**
     * Sets upp the deafult controll keys for the game. These keys
     * are set everytime the application is restarted.
     */
    
    public static void setUpDefaultControlKeys() {
        setControlKey(PlayerEnum.PLAYER_ONE.getName(), UP, KeyCode.UP);
        setControlKey(PlayerEnum.PLAYER_ONE.getName(), RIGHT, KeyCode.RIGHT);
        setControlKey(PlayerEnum.PLAYER_ONE.getName(), DOWN, KeyCode.DOWN);
        setControlKey(PlayerEnum.PLAYER_ONE.getName(), LEFT, KeyCode.LEFT);
        
        setControlKey(PlayerEnum.PLAYER_TWO.getName(), UP, KeyCode.W);
        setControlKey(PlayerEnum.PLAYER_TWO.getName(), RIGHT, KeyCode.D);
        setControlKey(PlayerEnum.PLAYER_TWO.getName(), DOWN, KeyCode.S);
        setControlKey(PlayerEnum.PLAYER_TWO.getName(), LEFT, KeyCode.A);
        
        setControlKey(PlayerEnum.PLAYER_THREE.getName(), UP, KeyCode.T);
        setControlKey(PlayerEnum.PLAYER_THREE.getName(), RIGHT, KeyCode.H);
        setControlKey(PlayerEnum.PLAYER_THREE.getName(), DOWN, KeyCode.G);
        setControlKey(PlayerEnum.PLAYER_THREE.getName(), LEFT, KeyCode.F);
        
        setControlKey(PlayerEnum.PLAYER_FOUR.getName(), UP, KeyCode.I);
        setControlKey(PlayerEnum.PLAYER_FOUR.getName(), RIGHT, KeyCode.L);
        setControlKey(PlayerEnum.PLAYER_FOUR.getName(), DOWN, KeyCode.K);
        setControlKey(PlayerEnum.PLAYER_FOUR.getName(), LEFT, KeyCode.J);
        
        setPauseKey(pauseKey);
    }
}
