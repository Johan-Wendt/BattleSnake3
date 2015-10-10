
package flowSnake;

/**
 *
 * @author johanwendt
 */
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
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
    private final int blockId;
    private Rectangle rectangle;
    private Color revertColor;
    private boolean isDeathBlock = false;
    private boolean isDeathBlockIrrevertible = false;
    
    private static final Color deathBlockColor = new Color(0, 0, 0, 0.5);
    
    /**
     * Constructor used for creating blocks that only need an id, not a graphical appearance.
     * @param blockId the block id to be set for this building block.
     * @param color the revertColor of the block
     */
    public BuildingBlock(int blockId, Color color) {
        this.blockId = blockId;
        this.revertColor = color;
        rectangle = new Rectangle();
        rectangle.setFill(color);
    }
    
    public BuildingBlock(int setX, int setY, int size) {
        blockId = 0;
        revertColor = GameGrid.GAMEGRID_COLOR;
        createRectangle(setX, setY , size, revertColor);
    }
    /**
     * The commonly used constructor. 
     * @param setX The x-position of the upper left part of the building block.
     * @param setY The y-position of the upper left part of the building block.
     * @param size size of the building block in pixels.
     * @param blockId the block id to be set for this building block.
     */
    public BuildingBlock(int setX, int setY, int size, int blockId) {
        this.blockId = blockId;
        revertColor = GameGrid.GAMEGRID_COLOR;
        createRectangle(setX, setY , size, revertColor);
    }
    /**
     * Creates the square that is the base for the building block.
     * @param X x-position
     * @param Y y-position
     * @param size Base of the square
     * @param color Color of the square
     */
    /**
    private void createCircle(int X, int Y, int size, Color color) {
        shape = new Circle(size/2.0, color);
        ((Circle)shape).setCenterX(X + size/2);
        ((Circle)shape).setCenterY(Y + size/2);
       // rectangle.setStroke(Color.BLACK);
       *      
    }
    * **/
    private void createRectangle(int X, int Y, int size, Color color) {
        rectangle = new Rectangle(size, size, color);
        rectangle.setX(X);
        rectangle.setY(Y);
       // rectangle.setStroke(Color.BLACK);
    }
    /**
     * Apply the lighting effect used for the bonuses
     */
    public void setLightingEffect() {
        rectangle.setEffect(new Lighting());   
    }
    /**
     * Removes any effect applied to the square of this BuildingBlock.
     */
    public void removeEffect() {
        rectangle.setEffect(null);   
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
     * @param color new revertColor for the block.
     */
    public void setBlockColor(Color color) {
        rectangle.setFill(color);
    }
    
    public void addBacgoundImage(String picture) {
        Image image = new Image(getClass().getResourceAsStream(picture));
        
        ImagePattern imagePattern = new ImagePattern(image);
        
        rectangle.setFill(imagePattern);
                
    }
    /**
     * Returns the current revertColor af this block.
     * @return current block revertColor.
     */
    public Paint getBlockColor() {
        return rectangle.getFill();
    }
    public void setPlayerBlock(String playerImage) {
        addBacgoundImage(playerImage);
        setDeathBlock();
    }
    public void resetBlock() {
        rectangle.setFill(GameGrid.GAMEGRID_COLOR);
        isDeathBlock = false;
        isDeathBlockIrrevertible = false;
        rectangle.setEffect(null); 
    }
    public void setRevertColor(Color color) {
        revertColor = color;
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
        if(!isDeathBlockIrrevertible) {
            rectangle.setEffect(null);
            isDeathBlock = true;
            addBacgoundImage("bbGrey.png");
            isDeathBlockIrrevertible = true;
        }
    }
    
    public void setStartBlock() {
        rectangle.setEffect(null);
        isDeathBlock = true;
        addBacgoundImage("centerIcon.png");
        isDeathBlockIrrevertible = true;
    }

    /**
     * If the death block property is not irrevertible this method removes the
 it and sets the correct revertColor for the block depending on where it is in the GameGrid.
     * @param isInSafeZone set true if the block is in the safe zone in the middle of the GameGrid.
     */
    public void revertDeathBlock(boolean isInSafeZone) {
        if(!isDeathBlockIrrevertible) {
            isDeathBlock = false;
            rectangle.setFill(revertColor);
        }
    }
}
