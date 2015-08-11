/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesnake3;

/**
 *
 * @author johanwendt
 */
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Iterator;

/**
 *
 * @author johanwendt
 */
public class Player {
    private int turn;
    private int currentLocation;
    private int currentDirection;
    public static final int PLAYER_START_LENGTH = 8;
    private int currentLength = PLAYER_START_LENGTH;
    public static final int PLAYER_START_SLOWNESS = 30;
    private int playerSlownes = PLAYER_START_SLOWNESS;
    private boolean isAlive;
    private Color playerColor;
    private GameGrid gameGrid;
    private Stack<BuildingBlock> body = new Stack<>();
    private EventHandler events;
    private boolean mayChangeDirection = true;
    private int startDirection;
    private int score = 0;
    
    
    public Player(int startDirection, Color playerColor, GameGrid gameGrid, EventHandler events) {
        this.startDirection = startDirection;
        this.playerColor = playerColor;
        this.gameGrid = gameGrid;
        this.events = events;
        createPlayer(); 
    }
    public void createPlayer() {
        makeShort();
        makeSlow();
        BuildingBlock startSnake = gameGrid.getBlock(GameGrid.PLAYER_STARTPOINT + (startDirection));
        startSnake.revertDeathBlock(true);
        startSnake.setIsDeathBlock();
        startSnake.setBlockColor(playerColor);
        
        
        body.add(0, startSnake);    
        
        gameGrid.getBlock(GameGrid.PLAYER_STARTPOINT + (startDirection * 2)).revertDeathBlock(true);
        currentDirection = startDirection;
        currentLocation = GameGrid.PLAYER_STARTPOINT + startDirection;
    }
    //mutators

    public void movePlayer() {
        if(isAlive && turn > 0 && turn % playerSlownes == 0) {
            int destination = currentLocation + currentDirection;
            if(gameGrid.getBlock(destination).getBlockId() < 0) {
                destination = jumpToOtherSide(gameGrid.getBlock(currentLocation));
            }
            if(gameGrid.getBlock(destination).isDeathBlock() || gameGrid.getBlock(destination).getBlockId() == GameGrid.PLAYER_STARTPOINT) {
                killPlayer();
                return;
            }
            BuildingBlock moveTo = gameGrid.getBlock(destination);
            moveTo.setBlockColor(playerColor);
            moveTo.setIsDeathBlock();
            handleEvents(events.getEvent(destination));
            currentLocation = moveTo.getBlockId();
            body.add(0, moveTo);
            mayChangeDirection = true;
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
    //Mutators
    public void handleEvents(int eventHappening) {
        
        switch(eventHappening) {
            case EventHandler.REGULAR_EVENT_HAPPENING: makeLonger(); makeFaster(); score++; break;
            case EventHandler.MAKE_SHORT__EVENT_HAPPENING: makeShort(); score++; break;
            case EventHandler.ADD_DEATH_BLOCK_EVENT_HAPPENING: score++; break;
        }
    }
    public void setAlive() {
        isAlive = true;
    }
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
            body.pop().setBlockColor(EventHandler.DETHBLOCK_COLOR);
        }
    }
    public int jumpToOtherSide(BuildingBlock block) {

            return block.getBlockId() - (currentDirection * ((GameGrid.GRID_SIZE / GameGrid.BLOCK_SIZE) - 1));
    }
    public void killPlayer() {
        isAlive = false;
        erasePlayer();
        addToScore(-5);
        createPlayer();
        turn = -500;
        isAlive = true;
    }
    public void makeFaster() {
        if(playerSlownes > 3) {
            playerSlownes--;
        }
    }    
    public void makeLonger() {
        currentLength ++;
    }
    public void makeShort() {
        currentLength = PLAYER_START_LENGTH;
    }
    public void makeSlow() {
        playerSlownes = PLAYER_START_SLOWNESS;
    }
    //Getters
    public BuildingBlock getBlock(int blockId) {
        for(BuildingBlock block: body) {
            if(block.getBlockId() == blockId) {
                return block;
            }
        }
        return body.get(0);
    }
    public int getCurrentLocation() {
        return currentLocation;
    }
    public int getLength() {
        return currentLength;
    }
    public int getScore() {
        return score;
    }
    public boolean containsBlock(int blockId) {
        boolean isFound = false;
        for(BuildingBlock block: body) {
            if(block.getBlockId() == blockId) {
                isFound = true;
            }
        }
        return isFound;
    }
    
    //Setters
    public boolean isAlive() {
        return isAlive;
    }
    public void setLength(int newLength) {
        currentLength = newLength;
    }
    public void setCurrentLocation(int newLocation) {
        currentLocation = newLocation;
    }
    public void setCurrentDirection(int direction) {
        if(direction != -currentDirection && mayChangeDirection) {
            currentDirection = direction;
            mayChangeDirection = false;
        }
    }
    public void setScore() {
        score ++;
    }
    public void addToScore(int addToScore) {
        score += addToScore;
    }
}