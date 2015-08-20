
package battlesnake;

/**
 *
 * @author johanwendt
 */
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.Iterator;
import javafx.scene.input.KeyCode;

/**
 *Every player in the game is an instance of this class. 
 * @author johanwendt
 */
public class Player {
    //Fields
    //Final static fields
    public static final int PLAYER_START_SLOWNESS = 25;
    public static final int PLAYER_DEATH_PENALTY = -5;
    public static final int PLAYER_START_LENGTH = 20;
    
    //Final fields
    private final Color playerColor;
    private final GameGrid gameGrid;
    private Stack<BuildingBlock> body;
    private final BonusHandler events;
    private final int startDirection;
    private final String name;
    
    //Regular fields
    private int turn;
    private int currentLocation;
    private int currentDirection;
    private int currentLength = PLAYER_START_LENGTH;
    private int playerSlownes = PLAYER_START_SLOWNESS;
    private boolean isAlive = true;
    private boolean mayChangeDirection = true;
    private int score = 0;
    private HashMap<KeyCode, Integer> controls = new HashMap<>();
    
    /**
     * Create a player instance.The names cannot yet be set by players. To implement this feature one
     * needs to give the player an id for methods to call the player instace.
     * he body of the snake concists of a stack of BuildingBlocks that are fetched from the GameGrid.
     * @param name The name of the player. Used to destinguish between players and for diplaying scores.
     * @param startDirection The start direction of this player.
     * @param playerColor Color of the player.
     * @param gameGrid The GameGrid to place the player in.
     * @param bonusHandler The BonusHandler this player should recieve bonus events from. 
     * @param controls The start controls for this player.
     */
    public Player(String name, int startDirection, Color playerColor, GameGrid gameGrid, BonusHandler bonusHandler, HashMap controls) {
        this.name = name;
        this.startDirection = startDirection;
        this.playerColor = playerColor;
        this.gameGrid = gameGrid;
        this.events = bonusHandler;
        this.controls = controls;
        body = new Stack<>();
        createPlayer(); 
    }
    /**
     * This creates the player on game start and recreates the player after death by stripping 
     * the player of it's bonuses and placing it in the startpossition with the startdirection
     * activated.
     */
    public void createPlayer() {
        makeShort();
        makeSlow();
        BuildingBlock startSnake = gameGrid.getBlock(GameGrid.PLAYER_STARTPOINT + (startDirection));
        startSnake.revertDeathBlock(true);
        startSnake.setIsDeathBlock();
        startSnake.setBlockColor(playerColor);
        
        //Only first block in the stack is added. This makes the snake start 
        //being only one block in size. The rest is added by mevement.
        body.add(0, startSnake);    
        
        //If deathblocks are blocking the the newly created player, get rid of them.
        gameGrid.getBlock(GameGrid.PLAYER_STARTPOINT + (startDirection * 2)).revertDeathBlock(true);
        
        
        currentDirection = startDirection;
        currentLocation = GameGrid.PLAYER_STARTPOINT + startDirection;
    }
    /**
     * Makes the player move. This method is called by the game thread that controls 
     * movement and bonuscreation, once every turn. How often the method makes
     * the player move depends of the speed (or lack of slowness) of the player.
     */
    public void movePlayer() {
        //Only act if player is alive, turn is set to a positive number and
        //the speed of the player allows.
        if(isAlive && turn > 0 && turn % playerSlownes == 0) {
            int destination = currentLocation + currentDirection;
            
            //If the end of the grid is reached and no deathblock is in the way
            //set the destination to the other side of the screen.
            if(gameGrid.getBlock(destination).getBlockId() < 0) {
                destination = jumpToOtherSide(gameGrid.getBlock(currentLocation));
            }
            //If the destination block is a death block or the startpoint kill the player.
            if(gameGrid.getBlock(destination).isDeathBlock() || gameGrid.getBlock(destination).getBlockId() == GameGrid.PLAYER_STARTPOINT) {
                killPlayer();
                return;
            }
            //Move the player, see if a bonus was taken and handle bonus happenings.
            BuildingBlock moveTo = gameGrid.getBlock(destination);
            moveTo.setBlockColor(playerColor);
            moveTo.setIsDeathBlock();
            handleBonuses(events.getBonus(destination));
            currentLocation = moveTo.getBlockId();
            body.add(0, moveTo);
            mayChangeDirection = true;
            //If the body is longer than it should be, which is the usual case after
            //a new block has been reached, peel the oldest block of. Repeat
            //to enable player to shrink after the make short bonus.
            if (body.size() > currentLength) {
                int blockId = body.peek().getBlockId();
                body.pop().revertDeathBlock(gameGrid.isInSafeZone(blockId));  
            }
            if (body.size() > currentLength) {
                int blockId = body.peek().getBlockId();
                body.pop().revertDeathBlock(gameGrid.isInSafeZone(blockId)); 
            }
        }
        turn ++;
    }
    /**
     * Handles the part of bonuses that apply directly to the player. 
     * @param bonusHappening the static final for the event.
     */
    public void handleBonuses(int bonusHappening) {
        
        switch(bonusHappening) {
            case BonusHandler.REGULAR_BONUS_HAPPENING: makeLonger(3); makeFaster(); score++; break;
            case BonusHandler.MAKE_SHORT_BONUS_HAPPENING: makeShort(); score++; break;
            case BonusHandler.ADD_DEATH_BLOCK_BONUS_HAPPENING: score++; break;
        }
    }
    /**
     * Kill or revive the player. Only alive players move.
     * @param alive set true for alive.
     */
    public void setIsAlive(boolean alive) {
        isAlive = alive;
    }
    /**
     * Empties the stack that holds the player. On screen the body turns black.
     * If the player is in the safe zone, it removes the body and reverts to the correct collor.
     */
    public void erasePlayer() {
        Iterator<BuildingBlock> itr = body.iterator();
        while(itr.hasNext()) {
            BuildingBlock block = itr.next();
            if(gameGrid.isInSafeZone(block.getBlockId())) {
                block.revertDeathBlock(true);
                itr.remove();
            }
                        
        }
        while(body.size() > 0) {
            body.pop().setBlockColor(BonusHandler.DETHBLOCK_COLOR);
        }
    }
    /**
     * Method that helps teleporting the player from one side of the screen, if no death blocks are in the way
     * to the other.
     * @param block the block that the is at the "from" end of the screen.
     * @return block id for the destination.
     */
    public int jumpToOtherSide(BuildingBlock block) {

            return block.getBlockId() - (currentDirection * ((GameGrid.GRID_SIZE / GameGrid.BLOCK_SIZE) - 1));
    }
    /**
     * Kills the player. If the game field is still deminishing or the player is
     * not on minus score the player is recreated and set to a short delay before 
     * moving again.
     */
    public void killPlayer() {
        erasePlayer();
        addToScore(PLAYER_DEATH_PENALTY);
        if(score  < 0 && !gameGrid.isDeathRunning()) {
            setIsAlive(false);
        }
        else {
            createPlayer();
            turn = -500;
        }
    }
    /**
     * Makes the player slightly faster.
     */
    public void makeFaster() {
        if(playerSlownes > 3) {
            playerSlownes--;
        }
    }    
    /**
     * Makes the player longer. This is made by not removing the tale after moving forward 
     * for the next round or rounds.
     * @param addLength the number of BuildingBlocks to add to the player.
     */
    public void makeLonger(int addLength) {
        currentLength += addLength;
    }
    /**
     * The player is shortened to the start length. Used for the bonus that makes the player short
     * and after death.
     */
    public void makeShort() {
        currentLength = PLAYER_START_LENGTH;
    }
    /**
     * Slows the player to startspeed. Used after death.
     */
    public void makeSlow() {
        playerSlownes = PLAYER_START_SLOWNESS;
    }
    /**
     * Returns the player score, as an int for comparability.
     * @return player score.
     */
    public int getScore() {
        return score;
    }
    /**
     * Returns the name of the player.
     * @return player name.
     */
    public String getName() {
        return name;
    }
    /**
     * Returns the color of the player.
     * @return player color.
     */
    public Color getPlayerColor() {
        return playerColor;
    }
    /**
     * Used for the scoreboard.
     * @return a string with the player name and current score.
     */
    public String scoreToString() {
        String scoreString = name + ": " + score;
        return scoreString;
    }
    /**
     * Boolean property for determining if the player contains the block with the
     * given id.
     * @param blockId the id of the block.
     * @return true if the player contains the block.
     */
    public boolean containsBlock(int blockId) {
        boolean isFound = false;
        for(BuildingBlock block: body) {
            if(block.getBlockId() == blockId) {
                isFound = true;
            }
        }
        return isFound;
    }
    
    /**
     * Boolean property to check if the player is still alive.
     * @return true if the player is alive.
     */
    public boolean isAlive() {
        return isAlive;
    }
    /**
     * This method is called whenever a key is pressed in the main game screen. If the
     * key is mapped to the controls of the player and the change of direction is valid
     * it changes the player direction. It also disables further change of direction till
     * the turn is executed.
     * @param pressedKey 
     */
    public void setCurrentDirection(KeyCode pressedKey) {
        int newDirection = keyCodeToDirection(pressedKey);
        if(newDirection != -currentDirection && newDirection != currentDirection && mayChangeDirection) {
            currentDirection = newDirection;
            mayChangeDirection = false;
        }
    }
    /**
     * If the key pressed is to make the player turn, the new direction is return. Otherwise
     * the current direction is returned.
     * @param pressedKey the key pressed on the keyboard.
     * @return new direction for the player.
     */
    public int keyCodeToDirection(KeyCode pressedKey) {
        int newDirection = currentDirection;
        if(controls.containsKey(pressedKey)) {
            newDirection = controls.get(pressedKey);
        }
        return newDirection;
    }
    /**
     * Adds to the players score.
     * @param addToScore points to add.
     */
    public void addToScore(int addToScore) {
        score += addToScore;
    }
}