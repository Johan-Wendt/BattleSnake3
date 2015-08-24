
package battlesnake;

/**
 *
 * @author johanwendt
 */
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.*;

/**
 * This class is the foundation for the GameGrid-, the Player- and Bonus-class.
 * Changes in this class will affect appearence for all of theese classes.
 * @author johanwendt
 */
public class BuildingBlock {
    //Regular fields
    private int blockId;
    private Rectangle rectangle;
    private Color color;
    private boolean isDeathBlock = false;
    private boolean isDeathBlockIrrevertible = false;
    
    /**
     * Constructor used for creating blocks that only need an id, not a graphical appearance.
     * @param blockId the block id to be set for this building block.
     */
    public BuildingBlock(int blockId) {
        this.blockId = blockId;
    }
    /**
     * The commonly used constructor. The X and Y is adjusted for 
     * GUI-purposes.
     * @param setX The x-position of the upper left part of the building block.
     * @param setY The y-position of the upper left part of the building block.
     * @param size size of the building block in pixels.
     * @param blockId the block id to be set for this building block.
     */
    public BuildingBlock(int setX, int setY, int size, int blockId) {
        this.blockId = blockId;
        color = GameGrid.GAMEGRID_COLOR;
        createRectangle(setX, setY +6 , size, color);
    }
    private void createRectangle(int X, int Y, int size, Color color) {
        rectangle = new Rectangle(size, size, color);
        rectangle.setX(X);
        rectangle.setY(Y);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeType(StrokeType.INSIDE);
    }
    /**
     * Returns the id for this block.
     * @return id for this block.
     */
    public int getBlockId() {
        return blockId;
    }
    /**
     * Sets the collor for this building block.
     * @param color new color for the block.
     */
    public void setBlockColor(Color color) {
        rectangle.setFill(color);
    }
    /**
     * Returns the current color af this block.
     * @return current block color.
     */
    public Paint getBlockColor() {
        return rectangle.getFill();
    }
    /**
     * Returns the shape that is associated with this building block.
     * @return the shape (currently only rectangle)
     */
    public Shape getShape() {
        return rectangle;
    }
    /**
     * Returns true if this block is a death block.
     * @return true if deathblock,
     */
    public boolean isDeathBlock() {
        return isDeathBlock;
    }
    /**
     * Sets this building block to a deathblock.
     */
    public void setDeathBlock() {
        isDeathBlock = true;
    }
    /**
     * Sets this building block to a death block and also makes it impossible to
     * remove the death block property.
     */
    public void setDeathBlockIrreveritble() {
        isDeathBlock = true;
        setBlockColor(BonusHandler.DETHBLOCK_COLOR);
        isDeathBlockIrrevertible = true;
    }
    /**
     * If the death block property is not irrevertible this method removes the
     * it and sets the correct color for the block depending on where it is in the GameGrid.
     * @param isInSafeZone set true if the block is in the safe zone in the middle of the GameGrid.
     */
    public void revertDeathBlock(boolean isInSafeZone) {
        if(!isDeathBlockIrrevertible) {
            isDeathBlock = false;
            if(isInSafeZone) {
                rectangle.setFill(GameGrid.SAFE_ZONE_COLOR);
            }
            else {
                rectangle.setFill(GameGrid.GAMEGRID_COLOR);         
            }
        }
    }
}
