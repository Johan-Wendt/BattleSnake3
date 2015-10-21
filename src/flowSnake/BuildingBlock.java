
package flowSnake;

/**
 *
 * @author johanwendt
 */
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.*;
import javafx.util.Duration;


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
    private PlayerEnum occupiedBy = null;
    private boolean isMovableObject;
    private int movableObjectDirection;
    
    private Circle circle;
    
   // private static final Color deathBlockColor = new Color(0, 0, 0, 0.5);
    
    
    private static ImagePattern bbGreen;
    private static ImagePattern bbYellow;
    private static ImagePattern bbRed;
    private static ImagePattern bbGrey;
    private static ImagePattern playerOneImage;
    private static ImagePattern playerTwoImage;
    private static ImagePattern playerThreeImage;
    private static ImagePattern playerFourImage;
    private static ImagePattern deathPattern;
    
    private static ImagePattern explosionView;
    
    
    /**
     * Constructor used for creating blocks that only need an id, not a graphical appearance.
     * @param blockId the block id to be set for this building block.
     * @param color the revertColor of the block
     */
    public BuildingBlock(int blockId, Color color) {
        Image bbGreenImage = new Image(getClass().getResourceAsStream(BonusEnum.REGULAR_BONUS.getBonusImage()));
        bbGreen = new ImagePattern(bbGreenImage);
        BonusEnum.REGULAR_BONUS.setBlockImage(bbGreen);
        Image bbYellowImage = new Image(getClass().getResourceAsStream(BonusEnum.MAKE_SHORT_BONUS.getBonusImage()));
        bbYellow = new ImagePattern(bbYellowImage);
        BonusEnum.MAKE_SHORT_BONUS.setBlockImage(bbYellow);
        Image bbRedImage = new Image(getClass().getResourceAsStream(BonusEnum.ADD_DEATH_BLOCK_BONUS.getBonusImage()));
        bbRed = new ImagePattern(bbRedImage);
        BonusEnum.ADD_DEATH_BLOCK_BONUS.setBlockImage(bbRed);
        Image bbGreyImage = new Image(getClass().getResourceAsStream(BonusEnum.DEATH_BLOCK.getBonusImage()));
        bbGrey = new ImagePattern(bbGreyImage);
        BonusEnum.DEATH_BLOCK.setBlockImage(bbGrey);
        Image playerOneImageImage = new Image(getClass().getResourceAsStream(PlayerEnum.PLAYER_ONE.getImage()));
        playerOneImage = new ImagePattern(playerOneImageImage);
        PlayerEnum.PLAYER_ONE.setBlockImage(playerOneImage);
        Image playerTwoImageImage = new Image(getClass().getResourceAsStream(PlayerEnum.PLAYER_TWO.getImage()));
        playerTwoImage = new ImagePattern(playerTwoImageImage);
        PlayerEnum.PLAYER_TWO.setBlockImage(playerTwoImage);
        Image playerThreeImageImage = new Image(getClass().getResourceAsStream(PlayerEnum.PLAYER_THREE.getImage()));
        playerThreeImage = new ImagePattern(playerThreeImageImage);
        PlayerEnum.PLAYER_THREE.setBlockImage(playerThreeImage);
        Image playerFourImageImage = new Image(getClass().getResourceAsStream(PlayerEnum.PLAYER_FOUR.getImage()));
        playerFourImage = new ImagePattern(playerFourImageImage);
        PlayerEnum.PLAYER_FOUR.setBlockImage(playerFourImage);
        Image deathImage = new Image(getClass().getResourceAsStream("cross.png"));
        deathPattern = new ImagePattern(deathImage);
        
       Image eplosionImage = new Image(getClass().getResourceAsStream("explosion.gif"));
       explosionView = new ImagePattern(eplosionImage);
        
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
        double circleAdjuster = size / 2.0;
        circle = new Circle(setX + circleAdjuster, setY + circleAdjuster, circleAdjuster);
        circle.setFill(revertColor);
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
        /**
     * Sets the collor for this building block.
     * @param color new revertColor for the block.
     */
    public void setBlockImage(ImagePattern color) {
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
    public void setPlayerBlock(PlayerEnum player) throws InterruptedException {
        setBlockImage(player.getBlockImage());
        setDeathBlock();
        occupiedBy = player;
    }
    public void setMovableObjectBlock(int direction) {
        setBlockImage(bbRed);
        setDeathBlock();
        isMovableObject = true;
        movableObjectDirection = direction;
    }
    public void removeMovableObject() {
        isMovableObject = false;
        movableObjectDirection = 0;
        if(!isDeathBlockIrrevertible && occupiedBy == null) {
            rectangle.setFill(GameGrid.GAMEGRID_COLOR);
            isDeathBlock = false;
            isDeathBlockIrrevertible = false;
        }
    }
    public PlayerEnum getOccupiedBy() {
        return occupiedBy;
    }
    public void setBonusBlock(BonusEnum bonus) {
        setBlockImage(bonus.getBlockImage());
    }
    public void resetBlock() {
        rectangle.setFill(GameGrid.GAMEGRID_COLOR);
        isDeathBlock = false;
        isDeathBlockIrrevertible = false;
        occupiedBy = null;
        removeMovableObject();
    }
    public void setRevertColor(Color color) {
        revertColor = color;
    }
    /**
     * Returns the shape that is associated with this building block.
     * @return the shape (currently only rectangle)
     */
    public Shape getRectangle() {
        return rectangle;
    }
    public Shape getCircle() {
        return circle;
    }
    public int getMovableObjectDirection() {
        return movableObjectDirection;
    }
    /**
     * Returns true if this block is a death block.
     * @return true if deathblock,
     */
    public boolean isDeathBlock() {
        return isDeathBlock;
    }
        public boolean isDeathBlockIrrevertible() {
        return isDeathBlockIrrevertible;
    }
    /**
     * Sets this building block to a deathblock.
     */
    public void setDeathBlock() {
        isDeathBlock = true;
    }
    public boolean isMovableObject() {
        return isMovableObject;
    }
    public void setPlayerDiedBlock() {
        rectangle.setFill(deathPattern);
    }
    
    /**
     * Sets this building block to a death block and also makes it impossible to
     * remove the death block property.
     */
    public void setDeathBlockIrreveritble() {
        if(!isDeathBlockIrrevertible) {
            isDeathBlock = true;
            setBlockImage(bbGrey);
            isDeathBlockIrrevertible = true;
            occupiedBy = null;
            isMovableObject = false;
        }
    }
    
    public void setStartBlock() {
        rectangle.setEffect(null);
        isDeathBlock = true;
        addBacgoundImage("tLogo.png");
        isDeathBlockIrrevertible = true;
        isMovableObject = false;
    }
    public static ImagePattern playerOneImage() {
        return playerOneImage;
    }
    public static ImagePattern playerTwoImage() {
        return playerTwoImage;
    }
    public static ImagePattern playerThreeImage() {
        return playerThreeImage;
    }
    public static ImagePattern playerFourImage() {
        return playerFourImage;
    }
    public static ImagePattern greenBonusImage() {
        return bbGreen;
    }
    public static ImagePattern yellowBonusImage() {
        return bbYellow;
    }
    public static ImagePattern redBonusImage() {
        return bbRed;
    }
    public static ImagePattern greyBonusImage() {
        return bbGrey;
    }
    public void explosion() {
        circle.setFill(explosionView);
        Timeline timeline = new Timeline(new KeyFrame(
        Duration.millis(1000),
        ae -> circle.setFill(GameGrid.GAMEGRID_COLOR)));
        timeline.play();
    }
    /**
     * If the death block property is not irrevertible this method removes the
 it and sets the correct revertColor for the block depending on where it is in the GameGrid.
     * @param isInSafeZone set true if the block is in the safe zone in the middle of the GameGrid.
     */
    public void revertDeathBlock() {
        if(!isDeathBlockIrrevertible) {
            isDeathBlock = false;
            rectangle.setFill(revertColor);
            occupiedBy = null;
            isMovableObject = false;
        }
    }

}
