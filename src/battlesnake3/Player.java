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
    private int moves;
    private int playerSlownes = 30;
    private int length;
    private int currentLocation;
    private int currentDirection;
    private boolean isAlive;
    private Color color;
    private GameGrid gameGrid;
    private Stack<BuildingBlock> body = new Stack<>();
    private EventHandler events;
    
    public Player(int startPoint, int length, Color color, GameGrid gameGrid, EventHandler events) {
        this.length = length;
        this.color = color;
        this.gameGrid = gameGrid;
        this.events = events;
        createPlayer(startPoint, length, MainSnakeBoard.MULIPLIER_X, color);
        currentLocation = startPoint + length * MainSnakeBoard.MULIPLIER_X - MainSnakeBoard.MULIPLIER_X;   
    }
    public void createPlayer(int startPoint, int length, int mulitplierX, Color color) {
        for(int j = startPoint; j < startPoint + MainSnakeBoard.MULIPLIER_X*length; j += MainSnakeBoard.MULIPLIER_X) {
            BuildingBlock startSnake = gameGrid.getBlock(j);
            startSnake.setColor(color);
            body.add(0, startSnake);
            setCurrentDirection(MainSnakeBoard.RIGHT);
        }
    }
    //mutators

    public void movePlayer() {
        if(isAlive && moves % playerSlownes == 0) {
            int destination = currentLocation + currentDirection;           
            if(gameGrid.getBlock(destination).getBlockId() < 0 || containsBlock(destination)) {
                isAlive = false;
                System.out.println("Dead");
                return;
            }
            BuildingBlock moveTo = gameGrid.getBlock(destination);
            moveTo.setColor(color);
            handleEvents(events.getEvent(destination));
            currentLocation = moveTo.getBlockId();
            body.add(0, moveTo);
            if(body.size() > length) {
                body.pop().setColor(gameGrid.getColor());  
            }
        }
        moves ++;
    }
    //Mutators
    public void handleEvents(String eventHappening) {
        switch(eventHappening) {
        case "Regular": length += 2; makeFaster(); break;
            
        }
    }
    public void setAlive() {
        isAlive = true;
    }
    public void erasePlayer() {
        while(body.size() > 0) {
            body.pop().setColor(gameGrid.getColor());
        }
    }
    public void makeFaster() {
        if(playerSlownes > 3) {
            playerSlownes--;
        }
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
        return length;
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
        length = newLength;
    }
    public void setCurrentLocation(int newLocation) {
        currentLocation = newLocation;
    }
    public void setCurrentDirection(int direction) {
        if(direction != -currentDirection) {
            currentDirection = direction;
        }
    }
}