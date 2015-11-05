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
package flowSnake;

/**
 * @author johanwendt
 */
import java.util.ArrayList;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

/**
 *Creates a player. Every player in the game is an instance of this class. This class hold information
 * about player direction, speed, length, score and so on.
 */
public class Player extends MovableObject{
    //Fields
    private final PlayerEnum playerDetails;
    
    public static final int PLAYER_START_SLOWNESS = 100;
    public static final int PLAYER_DEATH_PENALTY = -1;
    public static final int PLAYER_START_LENGTH = 5;
    
    private static final double INITIAL_BOT_TURNCHANCE = 0.02;
    private static final double INCREASE_BOT_TURNCHANCE = 0.04;
    
    
    private int delayedChangeDirection = 0;
    private boolean isAlive = false;
    private boolean mayChangeDirection = false;
    private HashMap<KeyCode, Integer> controls = new HashMap<>();
    
    private boolean bot = false;
    private double botTurnChance = INITIAL_BOT_TURNCHANCE;
    private double botLeftRightBias = 0.5;
    
    
    private int turn;
    
    
    private final AudioClip bonusSound;
    private final AudioClip deathSound;
    private final AudioClip busySound;
    
    private Random random = new Random();
    
   // private final ArrayList<BuildingBlock> lastBlocks = new ArrayList<>();


    public Player(VisibleObjects details, PlayerEnum playerDetails, HashMap controls) {
        super(details);
        this.playerDetails = playerDetails;
        this.controls = controls;
        setMayMove(false);
        setSpeed(PLAYER_START_SLOWNESS);
        setDirection(playerDetails.getStartDirection());
        setLocation(BuildingBlock.getBlock(GameGrid.getStartPosition() + playerDetails.getStartDirection()));
        createPlayer();
        
        bonusSound = new AudioClip(getClass().getResource(playerDetails.getBonusSound()).toExternalForm());
        deathSound = new AudioClip(getClass().getResource(playerDetails.getDeathSound()).toExternalForm());
        busySound = new AudioClip(getClass().getResource(playerDetails.getBusySound()).toExternalForm());
        
        bonusSound.play(0);
        deathSound.play(0);
        busySound.play(0);
        
    }
    /**
     * This creates the player on game start and recreates the player after death by stripping 
     * the player of it's bonuses and placing it in the startpossition with the startdirection
     * activated.
     */
    public void createPlayer() {
        if(isAlive) {
            setDirection(playerDetails.getStartDirection());
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000),ae -> 
            comeOutAndPlay()
            ));
            timeline.play();
            timeline.setOnFinished(e -> {
                GameEngine.removeTimeline(timeline);
            });
            GameEngine.addTimeLine(timeline);
        }
    }
    public void createPlayer(boolean gameStart) {
        if(isAlive && gameStart) {
            setLength(PLAYER_START_LENGTH);
            botTurnChance = INITIAL_BOT_TURNCHANCE;
            botLeftRightBias = 0.5;
            setDirection(playerDetails.getStartDirection());
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000),ae -> 
            comeOutAndPlay()
            ));
            timeline.play();
        }
    }
    public void comeOutAndPlay() {
        makeSlow();
        setLocation(BuildingBlock.getBlock(GameGrid.getStartPosition()));
        setMayMove(true);
    }
    /**
     * Makes the player move. This method is called by the game thread that controls 
     * movement and bonuscreation, once every turn. How often the method makes
     * the player move depends of the speed (or lack of slowness) of the player.
     * @return 0 if player didnt move and one if it did. 
     */
    public void move() {
        if(isAlive && getMayMove()) {
           // revertLastBlock();
            if(bot && !getLocation().equals(BuildingBlock.getBlock(GameGrid.getStartPosition()))) changeDirectionRandomly();
            BuildingBlock moveTo = getLocation().getAdjacent(getDirection());
            if(moveTo.getOccupant() != null) {
                handleCrash(moveTo);
            }
            if(occupy(moveTo) && getMayMove()) {
                moveTo.setInMotion();
                setMayMove(false);
                setLocation(moveTo);
                mayChangeDirection = true;
                if (delayedChangeDirection != 0) {
                    setDirection(delayedChangeDirection);
                    delayedChangeDirection = 0;
                    mayChangeDirection = false;
                }
            }
        }
        turn ++;
       // if(turn == 30) mayMove = true;
    }
    /**
     * Handles the part of bonuses that apply directly to the player. 
     * @param bonusHappening the static final for the event.
     */
    public void handleBonuses(int bonusHappening, BuildingBlock bonusBlock) {
        bonusBlock.resetBlock();
        switch(bonusHappening) {
            case VisibleObjects.MAKE_LONGER: 
                makeLonger(1); 
                bonusSound.play(playerDetails.getBonusVolume());
                break;
            case VisibleObjects.MAKE_FASTER: 
                makeFaster();
                bonusSound.play(playerDetails.getBonusVolume());
                break;
            case VisibleObjects.SHOOT: 
                busySound.play(0.5);
                ShotShootBonus shot = new ShotShootBonus(VisibleObjects.SHOOT_BONUS, bonusBlock, getDirection(), getSpeed());
                if(bot) {
                    randomNewDirection(1, 0.5);
                    botTurnChance = -0.1;
                }
                break;
        }
    }
    /**
     * Kill or revive the player. Only alive players move.
     * @param alive set true for alive.
     */
    public void setAlive(boolean alive) {
        if(alive) {
        }
        isAlive = alive;
    }
    public void handleCrash(BuildingBlock block) {
        VisibleObject crashObject = block.getOccupant() ;
        if(crashObject instanceof MovableObject || crashObject instanceof Death) {
            
            crashAndRestart();
        }
        else if(crashObject instanceof Bonus) {
            handleBonuses((crashObject.getDetails().getActionNumber()), block);
        }
    }

    private void crashAndRestart() {
        mayChangeDirection = false;
        deathSound.play();
        getLocation().playerDied(this);
        setLength(getLength() -1 );
        if(getLength() < 1) {
            setAlive(false);
            GameEngine.gameOver();
        }
        setMayMove(false);
        createPlayer();
    }
    private void changeDirectionRandomly() {
        BuildingBlock moveTo = getLocation().getAdjacent(getDirection());
        BuildingBlock moveToNext = moveTo.getAdjacent(getDirection());
        BuildingBlock toTheLeft = getLocation().getAdjacent(getLeftFromCurrentDirection());
        BuildingBlock toTheRight = getLocation().getAdjacent(getRightFromCurrentDirection());
        BuildingBlock toTheLeftOfLeft = toTheLeft.getAdjacent(getLeftFromCurrentDirection());
        BuildingBlock toTheRightOfRight = toTheRight.getAdjacent(getRightFromCurrentDirection());
        
        for(Integer direction:GameEngine.getDirections()) {
            if(getLocation().getAdjacent(direction).hasOccupant() && getLocation().getAdjacent(direction).getOccupant() instanceof PowerupBonus) {
                setDirection(direction);
                botTurnChance = INITIAL_BOT_TURNCHANCE;
                return;
            }
        }
        if((moveToNext.hasOccupant() && !(moveToNext.getOccupant() instanceof PowerupBonus)) || (moveTo.hasOccupant() && !(moveTo.getOccupant() instanceof PowerupBonus))) {
            steerClear();
            botTurnChance = INITIAL_BOT_TURNCHANCE;
           // System.out.println("steer clear");
        }
        else if(toTheLeft.hasOccupant() && (!(toTheLeft.getOccupant() instanceof Bonus) && !toTheLeft.getOccupant().equals(this))) {
            randomNewDirection(1, 0);
            botTurnChance = INITIAL_BOT_TURNCHANCE;
        }
        else if(toTheRight.hasOccupant() && (!(toTheRight.getOccupant() instanceof Bonus) && !toTheRight.getOccupant().equals(this))) {
            randomNewDirection(1, 1);
            botTurnChance = INITIAL_BOT_TURNCHANCE;
        }
        else {
            if(toTheLeftOfLeft.hasOccupant() && !((toTheLeftOfLeft.getOccupant() instanceof PowerupBonus) && !toTheLeftOfLeft.getOccupant().equals(this))) {
                botLeftRightBias = 0.0;
                botTurnChance = 0.5;
            }
            else if(toTheRightOfRight.hasOccupant() && !((toTheRightOfRight.getOccupant() instanceof PowerupBonus) && !toTheRightOfRight.getOccupant().equals(this))) {
                botLeftRightBias = 1.0;
                botTurnChance = 0.5;
            }
            randomNewDirection(botTurnChance, botLeftRightBias);
        }
        
    }
    private void steerClear() {
        double chance = random.nextDouble();
        if(chance < 0.5 && !getLocation().getAdjacent(getLeftFromCurrentDirection()).hasOccupant()) {
            setDirection(getLeftFromCurrentDirection());
            botLeftRightBias = 0;
        }
        else if(chance > 0.5 && !getLocation().getAdjacent(getRightFromCurrentDirection()).hasOccupant()) {
            setDirection(getRightFromCurrentDirection());
            botLeftRightBias = 1;
        }
        else if(!getLocation().getAdjacent(getRightFromCurrentDirection()).hasOccupant()) {
            setDirection(getRightFromCurrentDirection());
            botLeftRightBias = 1;
        }
        else if(!getLocation().getAdjacent(getLeftFromCurrentDirection()).hasOccupant()) {
            setDirection(getLeftFromCurrentDirection());
            botLeftRightBias = 0;
        }
        botTurnChance = INITIAL_BOT_TURNCHANCE;
    }
    private void randomNewDirection(double chance, double leftRightBias) {
        System.out.println("Bias" + leftRightBias);
        double randomDouble = random.nextDouble();
        if(randomDouble < (chance * leftRightBias) && !getLocation().getAdjacent(getLeftFromCurrentDirection()).hasOccupant()) {
            setDirection(getLeftFromCurrentDirection());
            botTurnChance = INITIAL_BOT_TURNCHANCE;
            botLeftRightBias = 0;
        }
        else if(randomDouble < chance && !getLocation().getAdjacent(getRightFromCurrentDirection()).hasOccupant()) {
            setDirection(getRightFromCurrentDirection());
            botTurnChance = INITIAL_BOT_TURNCHANCE;
            botLeftRightBias = 1;
        }
        else {
            botTurnChance += INCREASE_BOT_TURNCHANCE;
            normalizeBotLeftRightBias();
        }
    }
    private int getLeftFromCurrentDirection() {
        if(getDirection() == GameEngine.UP) {
            return GameEngine.LEFT;
        }
        if(getDirection() == GameEngine.RIGHT) {
            return GameEngine.UP;
        }
        if(getDirection() == GameEngine.DOWN) {
            return GameEngine.RIGHT;
        }
        else {
            return GameEngine.DOWN;
        }
    }
    private int getRightFromCurrentDirection() {
        if(getDirection() == GameEngine.UP) {
            return GameEngine.RIGHT;
        }
        if(getDirection() == GameEngine.RIGHT) {
            return GameEngine.DOWN;
        }
        if(getDirection() == GameEngine.DOWN) {
            return GameEngine.LEFT;
        }
        else {
            return GameEngine.UP;
        }
    }
    public void setBot(boolean bot) {
        this.bot = bot;
    }
    public boolean isBot() {
        return bot;
    }
    private void normalizeBotLeftRightBias() {
        if(botLeftRightBias < 0.5) {
            botLeftRightBias += 0.05;
        }
        if(botLeftRightBias > 0.5) {
            botLeftRightBias -= 0.05;
        }
    }
    /**
     * Method that helps teleporting the player from one side of the screen, if no death blocks are in the way
     * to the other.
     * @param block the block that the is at the "from" end of the screen.
     * @return block id for the destination.
     */
    /*
    public int jumpToOtherSide(BuildingBlock block) {
            return block.getBlockId() - (currentDirection * ((GameGrid.getGridSize() / GameGrid.getBlockSize()) - 1));
    }
    */

    public void killPlayer() {
        deathSound.play();
        setLength(getLength() -1);

        if(getLength() < 1) {
            getLocation().setPlayerDiedBlock();
            setAlive(false);
            Platform.runLater(() -> {
                GameEngine.gameOver();
            });
        }
        createPlayer();
    }

    /**
     * Makes the player slightly faster.
     */
    public void makeFaster() {
        if(getSpeed() > 10) {
            setSpeed(getSpeed() - 4);
        }
    }    
    /**
     * Makes the player longer. This is made by not removing the tale after moving forward 
     * for the next round or rounds.
     * @param addLength the number of BuildingBlocks to add to the player.
     */
    public void makeLonger(int addLength) {
        setLength(getLength() + addLength);
    }
    /**
     * The player is shortened to the start length. Used after death.
     */
    public void makeShort() {
        setLength(PLAYER_START_LENGTH);
    }
    /**
     * Slows the player to startspeed. Used after death.
     */
    public void makeSlow() {
        setSpeed(PLAYER_START_SLOWNESS);
    }
    /**
     * Returns the enum with name, color, number and start direction of the player.
     * @return player details.
     */
    public PlayerEnum getPlayerDetails() {
        return playerDetails;
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
        if(controls.containsKey(pressedKey)) {
            int newDirection  = controls.get(pressedKey);
            if(mayChangeDirection && newDirection != -getDirection() && newDirection != getDirection()) {
                setDirection(newDirection);
                mayChangeDirection = false;
            }
            if(newDirection != -getDirection() && newDirection != getDirection()) {
                delayedChangeDirection = newDirection;
            }
        }
    }
    @Override
    public int getTurn() {
        return turn;
    }
    /**
    public void addLastBlock(BuildingBlock block) {
        lastBlocks.add(block);
    }
    public void revertLastBlock() {
       // if(!lastBlocks.isEmpty()) {
            for(BuildingBlock block:lastBlocks) {
                block.revertBlock();
            }
      //  }
    }
    **/
}