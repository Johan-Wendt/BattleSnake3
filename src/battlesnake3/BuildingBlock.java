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
import javafx.scene.shape.Rectangle;

/**
 *
 * @author johanwendt
 */
public class BuildingBlock {
    private int blockId;
    private Rectangle rectangle;
    private Color color;
    
    public BuildingBlock(int blockId) {
        this.blockId = blockId;
    }
    
    public BuildingBlock(int setX, int setY, int size, int blockId, Color color) {
        this.blockId = blockId;
        this.color = color;
        createRectangle(setX, setY, size, color);
    }
    public void createRectangle(int X, int Y, int size, Color color) {
        rectangle = new Rectangle(size, size, color);
        rectangle.setX(X);
        rectangle.setY(Y);
        rectangle.setStroke(Color.BLACK);
    }
    // getters and setter
    public void setBlockId(int id) {
        blockId = id;
    }
    public int getBlockId() {
        return blockId;
    }
    public void setRectangleColor(Color color) {
        rectangle.setFill(color);
    }
    public Rectangle getRectangle() {
        return rectangle;
    }
    public void setColor(Color color) {
        rectangle.setFill(color);
    }       
}
