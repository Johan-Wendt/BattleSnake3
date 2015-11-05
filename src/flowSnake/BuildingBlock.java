
package flowSnake;

/**
 *
 * @author johanwendt
 */
import java.util.HashMap;
import java.util.HashSet;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
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
    private VisibleObjects occupatorDetails = null;
    private VisibleObject occupant = null;
    private boolean isMovableObject;
    private int movableObjectDirection;
    
    private int playerDirection;
    private int animationSpeed;
    
    private Circle circle;
    private int turn;
    private int repeatTimes = 1;
    
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
    private final AudioClip explosionSound;
            
    
    
    private static final HashMap<Integer, BuildingBlock> gridList = new HashMap<>(GameEngine.BRICKS_PER_ROW * GameEngine.BRICKS_PER_ROW + 1); 
    /**
     * Constructor used for creating blocks that only need an id, not a graphical appearance.
     * @param blockId the block id to be set for this building block.
     * @param color the revertColor of the block
     */
    public BuildingBlock(int blockId, Color color) {
        Image bbGreenImage = new Image(getClass().getResourceAsStream(VisibleObjects.MAKE_LONGER_BONUS.getImage()));
        bbGreen = new ImagePattern(bbGreenImage);
        VisibleObjects.MAKE_LONGER_BONUS.setBlockImage(bbGreen);
        Image bbYellowImage = new Image(getClass().getResourceAsStream(VisibleObjects.MAKE_FASTER_BONUS.getImage()));
        bbYellow = new ImagePattern(bbYellowImage);
        VisibleObjects.MAKE_FASTER_BONUS.setBlockImage(bbYellow);
        Image bbRedImage = new Image(getClass().getResourceAsStream(VisibleObjects.SHOOT_BONUS.getImage()));
        bbRed = new ImagePattern(bbRedImage);
        VisibleObjects.SHOOT_BONUS.setBlockImage(bbRed);
        Image bbGreyImage = new Image(getClass().getResourceAsStream(VisibleObjects.DEATH.getImage()));
        bbGrey = new ImagePattern(bbGreyImage);
        VisibleObjects.DEATH.setBlockImage(bbGrey);
        Image playerOneImageImage = new Image(getClass().getResourceAsStream(VisibleObjects.PLAYER_ONE.getImage()));
        playerOneImage = new ImagePattern(playerOneImageImage);
        VisibleObjects.PLAYER_ONE.setBlockImage(playerOneImage);
        Image playerTwoImageImage = new Image(getClass().getResourceAsStream(VisibleObjects.PLAYER_TWO.getImage()));
        playerTwoImage = new ImagePattern(playerTwoImageImage);
        VisibleObjects.PLAYER_TWO.setBlockImage(playerTwoImage);
        Image playerThreeImageImage = new Image(getClass().getResourceAsStream(VisibleObjects.PLAYER_THREE.getImage()));
        playerThreeImage = new ImagePattern(playerThreeImageImage);
        VisibleObjects.PLAYER_THREE.setBlockImage(playerThreeImage);
        Image playerFourImageImage = new Image(getClass().getResourceAsStream(VisibleObjects.PLAYER_FOUR.getImage()));
        playerFourImage = new ImagePattern(playerFourImageImage);
        VisibleObjects.PLAYER_FOUR.setBlockImage(playerFourImage);
        Image deathImage = new Image(getClass().getResourceAsStream("cross.png"));
        deathPattern = new ImagePattern(deathImage);
        
       Image eplosionImage = new Image(getClass().getResourceAsStream("explosion.gif"));
       explosionView = new ImagePattern(eplosionImage);
       
       explosionSound = new AudioClip(getClass().getResource("explosion.wav").toExternalForm());
       
       explosionSound.play(0);
        
        this.blockId = blockId;
        this.revertColor = color;
        rectangle = new Rectangle();
        rectangle.setFill(color);
        occupant = new Death(VisibleObjects.DEATH);
        gridList.put(blockId, this);
    }
    
    public BuildingBlock(int setX, int setY, int size) {
        blockId = 0;
        revertColor = GameGrid.GAMEGRID_COLOR;
        createRectangle(setX, setY , size, revertColor);
        explosionSound = new AudioClip(getClass().getResource("explosion.wav").toExternalForm());
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
        gridList.put(blockId, this);
        explosionSound = new AudioClip(getClass().getResource("explosion.wav").toExternalForm());
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
    public void setBlockImageOnlyIfEmpty(ImagePattern color) {
        if(occupant == null) {
            rectangle.setFill(color);
        }
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
    public boolean Occupy(VisibleObjects details, VisibleObject newOccupant, int turn) {
       // setBlockImage(details.getBlockImage());
        boolean succeeded = false;
        if(occupant == null || newOccupant instanceof Death) {
            this.turn = turn;
            //System.out.println("succes" + blockId);
           // setBlockImage(details.getBlockImage());
            setOccupant(newOccupant);
            occupatorDetails = details;
            succeeded = true;
        }
      //  System.out.println("moveTo succeeded" + succeeded);
        return succeeded;
    }
    
    public void setMovableObjectBlock(int direction) {
        setBlockImage(bbRed);
      //  setDeathBlock();
        isMovableObject = true;
        movableObjectDirection = direction;
    }
    
    public void removeMovableObject() {
        isMovableObject = false;
        movableObjectDirection = 0;
        if(!isDeathBlockIrrevertible && occupatorDetails == null) {
            rectangle.setFill(GameGrid.GAMEGRID_COLOR);
            isDeathBlock = false;
            isDeathBlockIrrevertible = false;
        }
    }
    public VisibleObjects getOccupatorDetails() {
        return occupatorDetails;
    }
    public void setBonusBlock(VisibleObjects details) {
        setBlockImage(details.getBlockImage());
    }
    public void resetBlock() {
        rectangle.setFill(GameGrid.GAMEGRID_COLOR);
        rectangle.setTranslateX(0);
        rectangle.setTranslateY(0);
        isDeathBlock = false;
        isDeathBlockIrrevertible = false;
        occupatorDetails = null;
        occupant = null;
        playerDirection = 0;
        animationSpeed = 0;
        turn = 0;
        
        
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

    public boolean isMovableObject() {
        return isMovableObject;
    }
    public void setPlayerDiedBlock() {
        rectangle.setFill(deathPattern);
    }
    public BuildingBlock getAdjacent(int direction) {
        int askedFor = blockId + direction;
        if(!gridList.containsKey(askedFor)) askedFor = -100;
     //   System.out.println("adjacent recieved" + askedFor);
        return gridList.get(askedFor);
    }
    public void setInMotion() {
        Timeline timeline = new Timeline();
        if(occupant instanceof MovableObject) {
            playerDirection = occupant.getDirection();
            setBlockImage(occupatorDetails.getBlockImage());
            animationSpeed = occupant.getSpeed();

            KeyFrame frame1 = new KeyFrame(Duration.millis(0), new KeyValue (rectangle.translateXProperty(), 0, Interpolator.DISCRETE));
            KeyFrame frame2 = new KeyFrame(Duration.millis(animationSpeed), new KeyValue (rectangle.translateXProperty(), 0, Interpolator.DISCRETE));
            timeline.getKeyFrames().addAll(frame1, frame2);

            GameEngine.addTimeLine(timeline);
            timeline.play();
        }
        
        timeline.setOnFinished(e -> {
            occupant.setMayMove(true);
            repeatMotion(timeline);
        });
       // return timeline;
    }
    public void repeatMotion(Timeline timeline) {
        if(occupant != null && occupant.getLength() > repeatTimes) {

            timeline.setOnFinished(e -> {
                repeatMotion(timeline);
            });
            timeline.play();
            repeatTimes ++;
        }
        else {
            revertBlock();
           // GameEngine.addLastBlock(this);
            GameEngine.removeTimeline(timeline);
            repeatTimes = 1;
        }
    }
    public void setInSmoothMotion() {
        Timeline timeline = new Timeline();
        int direction = occupant.getDirection();
        boolean makeSmooth = getAdjacent(direction).getOccupant() == null;
        if(occupant instanceof MovableObject) {
            rectangle.setTranslateX(0);
            rectangle.setTranslateY(0);
            //int direction = occupant.getDirection();
            setBlockImage(occupatorDetails.getBlockImage());
            animationSpeed = occupant.getSpeed();
            

            if(makeSmooth) {
                if(Math.abs(direction) > 1) {
                    KeyFrame frame1 = new KeyFrame(Duration.millis(0), new KeyValue (rectangle.translateXProperty(), 0, Interpolator.LINEAR));
                    KeyFrame frame2 = new KeyFrame(Duration.millis(animationSpeed), new KeyValue (rectangle.translateXProperty(), ((direction / Math.abs(direction)) * GameGrid.getBlockSize()), Interpolator.LINEAR));
                    timeline.getKeyFrames().addAll(frame1, frame2);
                }
                else {
                    KeyFrame frame1 = new KeyFrame(Duration.millis(0), new KeyValue (rectangle.translateYProperty(), 0, Interpolator.LINEAR));
                    KeyFrame frame2 = new KeyFrame(Duration.millis(animationSpeed), new KeyValue (rectangle.translateYProperty(), ((direction / Math.abs(direction)) * GameGrid.getBlockSize()), Interpolator.LINEAR));
                    timeline.getKeyFrames().addAll(frame1, frame2);
                }
            }
            else {
                KeyFrame frame1 = new KeyFrame(Duration.millis(0), new KeyValue (rectangle.translateXProperty(), 0, Interpolator.DISCRETE));
                KeyFrame frame2 = new KeyFrame(Duration.millis(animationSpeed), new KeyValue (rectangle.translateXProperty(), 0, Interpolator.DISCRETE));
                timeline.getKeyFrames().addAll(frame1, frame2);
            }

            GameEngine.addTimeLine(timeline);
            timeline.play();
        }
        
        timeline.setOnFinished(e -> {
            if(occupant != null) occupant.setMayMove(true);
            rectangle.setTranslateX(0);
            rectangle.setTranslateY(0);
            if(makeSmooth) getAdjacent(direction).setBlockImage(occupatorDetails.getBlockImage());
            revertBlock();
            GameEngine.removeTimeline(timeline);
        });
    }
    public void showStillImage(int time, int times) {
        Rectangle extraRectangle = new Rectangle(rectangle.getX(), rectangle.getY(), rectangle.getHeight(), rectangle.getWidth());
        extraRectangle.setFill(occupatorDetails.getBlockImage());
        Timeline timeline = new Timeline();
        timeline.setCycleCount(times);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(time), new KeyValue (extraRectangle.translateYProperty(), 0)));
        timeline.setOnFinished(e -> {
            revertBlock();
            GameEngine.removeTimeline(timeline);
        });
        timeline.play();
    }
    public void explode() {
        explosion();
    }
    public void explodePlayer() {
        if(occupant instanceof Player) {
            
            for(Integer direction:GameEngine.getDirections()) {
                if(getAdjacent(direction).getTurn() < turn) {
                    getAdjacent(direction).explodePlayer(occupant);
                }
            }
            occupant.setLength(occupant.getLength() - 1);
            revertBlock();
            setDeathBlock();
            explosion();
            
        }
    }
    public boolean hasOccupant() {
        return occupant != null;
    }
    public void explodePlayer(VisibleObject player) {
        if(player.equals(occupant)) {
          //  setDeathBlock();
            for(Integer direction:GameEngine.getDirections()) {
                if(getAdjacent(direction).getTurn() < turn) {
                    getAdjacent(direction).explodePlayer(player);
                }
            }
            occupant.setLength(occupant.getLength() - 1);
            revertBlock();
            setDeathBlock();
        
        }
    }
    public void playerDied(Player player) {
      //  System.out.println("killed" + blockId);
        revertBlock(player);
        getAdjacent(GameEngine.UP).erasePlayer(player);
        getAdjacent(GameEngine.DOWN).erasePlayer(player);
        getAdjacent(GameEngine.LEFT).erasePlayer(player);
        getAdjacent(GameEngine.RIGHT).erasePlayer(player);
        setDeathBlock();
    }
    public void erasePlayer(Player player) {
    //    System.out.println("erasePlayer BlockId " + blockId + "OccupiedBy" + occupant);
        if(player.equals(occupant)) {
      //      System.out.println("killedToo");
            revertBlock(player);
            getAdjacent(GameEngine.UP).erasePlayer(player);
            getAdjacent(GameEngine.DOWN).erasePlayer(player);
            getAdjacent(GameEngine.LEFT).erasePlayer(player);
            getAdjacent(GameEngine.RIGHT).erasePlayer(player);
        }
    }
    public void setDeathBlock() {
        if(!GameGrid.isInSafeZone(blockId) && !(this.equals(BuildingBlock.getBlock(GameGrid.getStartPosition())))) {
            Death death = new Death(VisibleObjects.DEATH);
            death.occupy(this);
            setBlockImage(occupatorDetails.getBlockImage());
        }
    }
    public int getTurn() {
        return turn;
    }

    public static BuildingBlock getBlock(int blockId) {
        if(gridList.containsKey(blockId)) {
            return gridList.get(blockId);
        }
        return null;
    }
    private void setOccupant(VisibleObject occupant) {
        this.occupant = occupant;
    }
    public VisibleObject getOccupant() {
        return occupant;
    }
    
    public void setStartBlock() {
        setOccupant(new Death(VisibleObjects.DEATH));
        addBacgoundImage("tLogo.png");
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
        explosionSound.play();
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
    /*
    public void revertDeathBlock() {
        if(!isDeathBlockIrrevertible) {
            isDeathBlock = false;
            rectangle.setFill(revertColor);
            
            occupatorDetails = null;
            isMovableObject = false;
        }
    }
    */
    public void revertBlock() {
        if(!(occupant instanceof Death)) {
            rectangle.setFill(revertColor);
            occupatorDetails = null;
            occupant = null;
            turn = 0;
        }
    }
    public void revertBlock(VisibleObject reverter) {
        if(occupant != null && occupant.equals(reverter)) {
            rectangle.setFill(revertColor);
            occupatorDetails = null;
            occupant = null;
        }
    }
    public static void resetBuildingBlocks() {
        HashSet<BuildingBlock> resetGrid = new HashSet<>(gridList.values());
        for(BuildingBlock block: resetGrid) {
            block.resetBlock();
        }
        gridList.get(-100).setOccupant(new Death(VisibleObjects.DEATH));
    }

}
