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
    private int length;
    private int mulitplierX;
    private int currentLocation;
    private int currentDirection;
    private boolean isAlive;
    private Color color;
    private GameGrid gameGrid;
    private Stack<BuildingBlock> body = new Stack<>();
    
    public Player(int startPoint, int length, int mulitplierX, Color color, GameGrid gameGrid) {
        this.length = length;
        this.color = color;
        this.gameGrid = gameGrid;
        this.mulitplierX = mulitplierX;
        createPlayer(startPoint, length, mulitplierX, color);
        currentLocation = startPoint + length * mulitplierX -mulitplierX;   
    }
    public void createPlayer(int startPoint, int length, int mulitplierX, Color color) {
        for(int j = startPoint; j < startPoint + mulitplierX*length; j += mulitplierX) {
            BuildingBlock startSnake = gameGrid.getBlock(j);
            startSnake.setColor(color);
            body.add(0, startSnake);
            setCurrentDirection("Right");
        }
    }
    //mutators

    public void movePlayer() {
        if(isAlive) {
            int destination = currentLocation + currentDirection;
            if(gameGrid.getBlock(destination).getBlockId() < 0 || containsBlock(destination)) {
                isAlive = false;
                System.out.println("Dead");
                return;
            }
            BuildingBlock moveTo = gameGrid.getBlock(destination);
            moveTo.setColor(color);
            currentLocation = moveTo.getBlockId();
            body.add(0, moveTo);
            if(body.size() > length) {
                body.pop().setColor(gameGrid.getColor());  
            }
        }
    }
    //Mutators
    public void setAlive() {
        isAlive = true;
    }
    public void erasePlayer() {
        while(body.size() > 0) {
            body.pop().setColor(gameGrid.getColor());
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
    public void setCurrentDirection(String direction) {
        switch (direction) {
        case "Right": currentDirection = mulitplierX; break;
        case "Left": currentDirection = -mulitplierX; break;
        case "Down": currentDirection = 1; break;
        case "Up": currentDirection = -1; break;
        
    }
    }
}