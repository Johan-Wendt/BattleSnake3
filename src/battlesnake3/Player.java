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
    private int PLAYER_START_LENGTH = 8;
    private int currentLength = PLAYER_START_LENGTH;
    private int PLAYER_START_SLOWNESS = 30;
    private int playerSlownes = PLAYER_START_SLOWNESS;
    private boolean isAlive;
    private Color color;
    private GameGrid gameGrid;
    private Stack<BuildingBlock> body = new Stack<>();
    private EventHandler events;
    private boolean mayChangeDirection = true;
    private int startPoint;
    private int startDirection;
    
    
    public Player(int startPoint, int startDirection, Color color, GameGrid gameGrid, EventHandler events) {
        this.startPoint = startPoint;
        this.startDirection = startDirection;
        this.color = color;
        this.gameGrid = gameGrid;
        this.events = events;
        createPlayer(); 
    }
    public void createPlayer() {
        makeShort();
        makeSlow();
        for(int i = 0; i < PLAYER_START_LENGTH; i ++) {
            BuildingBlock startSnake = gameGrid.getBlock(startPoint + startDirection * i);
            startSnake.revertDeathBlock();
            startSnake.setRectangleColor(color);
            body.add(0, startSnake);    
        }
        gameGrid.getBlock(startPoint + startDirection*(PLAYER_START_LENGTH)).revertDeathBlock();
        currentDirection = startDirection;
        currentLocation = startPoint + PLAYER_START_LENGTH * startDirection - startDirection;
    }
    //mutators

    public void movePlayer() {
        if(isAlive && turn % playerSlownes == 0) {
            int destination = currentLocation + currentDirection; 
            if(gameGrid.getBlock(destination).isDeathBlock()) {
                isAlive = false;
                killPlayer();
                return;
            }
            BuildingBlock moveTo = gameGrid.getBlock(destination);
            moveTo.setRectangleColor(color);
            moveTo.setIsDeathBlock();
            handleEvents(events.getEvent(destination));
            currentLocation = moveTo.getBlockId();
            body.add(0, moveTo);
            mayChangeDirection = true;
            if (body.size() > currentLength) {
                body.pop().revertDeathBlock();  
            }
            if (body.size() > currentLength) {
                body.pop().revertDeathBlock();  
            }
        }
        turn ++;
    }
    //Mutators
    public void handleEvents(int eventHappening) {
        
        switch(eventHappening) {
        case EventHandler.REGULAR_EVENT_HAPPENING: makeLonger(); makeFaster(); break;
        case EventHandler.MAKE_SHORT__EVENT_HAPPENING: makeShort();   
        }
    }
    public void setAlive() {
        isAlive = true;
    }
    public void erasePlayer() {
        while(body.size() > 0) {
            body.pop().setRectangleColor(GameGrid.GAMEGRID_COLOR);
        }
    }
    public void killPlayer() {
        while(body.size() > 0) {
            body.pop().setRectangleColor(Color.BLACK);
        }
        createPlayer();
        turn = 1;
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
}