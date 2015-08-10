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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.*;

/**
 *
 * @author johanwendt
 */
public class BuildingBlock {
    private int blockId;
    private Rectangle rectangle;
    private Color color;
    private boolean isDeathBlock = false;
    
    public BuildingBlock(int blockId) {
        this.blockId = blockId;
    }
    
    public BuildingBlock(int setX, int setY, int size, int blockId) {
        this.blockId = blockId;
        color = GameGrid.GAMEGRID_COLOR;
        createRectangle(setX, setY, size, color);
    }
    public BuildingBlock(int setX, int setY, int size, int blockId, boolean isDeathBlock) {
        this.blockId = blockId;
        color = GameGrid.GAMEGRID_COLOR;
        this.isDeathBlock = isDeathBlock;
        createRectangle(setX, setY, size, color);
    }
    public void createRectangle(int X, int Y, int size, Color color) {
        rectangle = new Rectangle(size, size, color);
        rectangle.setX(X);
        rectangle.setY(Y);
        rectangle.setStroke(Color.BLACK);
    }
    // getters and setter
    public int getYpart() {
        return (int) rectangle.getY()/GameGrid.BLOCK_SIZE;
    }
    public int getXpart() {
        return (int) rectangle.getX()/GameGrid.BLOCK_SIZE;
    }
    public void setBlockId(int id) {
        blockId = id;
    }
    public int getBlockId() {
        return blockId;
    }
    public void setBlockColor(Color color) {
        rectangle.setFill(color);
    }
    public Paint getBlockColor() {
        return rectangle.getFill();
    }
    public Shape addToPane() {
        return rectangle;
    }
    public boolean isDeathBlock() {
        return isDeathBlock;
    }
    public void setIsDeathBlock() {
        isDeathBlock = true;
    }
    public void setIsDeathBlock(boolean makeBlack) {
        isDeathBlock = true;
        if(makeBlack) {
            setBlockColor(EventHandler.DETHBLOCK_COLOR);
        }
    }
    public void setIsNotDeathBlock() {
        isDeathBlock = false;
    }
    public void revertDeathBlock(boolean isInSafeZone) {
        isDeathBlock = false;
        if(isInSafeZone) {
            rectangle.setFill(GameGrid.SAFE_ZONE_COLOR);
        }
        else {
            rectangle.setFill(GameGrid.GAMEGRID_COLOR);         
        }
    }
}
