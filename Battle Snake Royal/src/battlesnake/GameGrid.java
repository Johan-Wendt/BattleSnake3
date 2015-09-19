
package battlesnake;

/**
 * @author johanwendt
 */
import javafx.scene.paint.Color;
import java.util.HashSet;
import java.util.Random;

/**
 *This class the grid where the players move around. It also creates the block that
 * make the field smaller and smaller.
 */
public class GameGrid {
    private static double screenHeight;
    private static int blockSize;
    private static int gridSize;
    private static int playerStartpoint;
    private static final int SAFE_ZONE_DIAMETER = 8;
    
    public static final Color GAMEGRID_COLOR = Color.web("#0000FF");
    public static final Color SAFE_ZONE_COLOR = Color.web("#4D4DFF");
    
    private static final int DEATH_SLOWNESS = GameEngine.PLAYER_START_SLOWNESS;
    //private static final int DEATH_SLOWNESS = 1;
    private final HashSet<BuildingBlock> gridList = new HashSet<>(GameEngine.BRICKS_PER_ROW * GameEngine.BRICKS_PER_ROW + 10);
    
    //This block is returned from the grid if it gets asked about a grid id that it cannot
    //find. This is used for making the player move from one side to another on the 
    //field if no death blocks are in the way.
    private final static BuildingBlock outsideBlock = new BuildingBlock(-1, Color.BLACK);
    
    //This value is used for the blocks that make tha field smaller and smaller.
    //For convinience this gridsize is normalized to number of blocks.
    private int currentGridSize;
    
    private int deathCounter = 0;
    private int deathLocation = 0;
    private int deathDirection = GameEngine.RIGHT;
    private int deathPause = 1;
    private boolean isDeathRunning = true;

    /**
     * Constructor for the main game field grid. It creates the field from BuildingBlocks.
     */
    public GameGrid() {
        blockSize = UserInterface.getBlockSize();
        gridSize = UserInterface.getGridSize();
        playerStartpoint = (((gridSize - blockSize) * GameEngine.MULIPLIER_X) / blockSize) / 2 + (((gridSize - blockSize) / blockSize) / 2);
        currentGridSize = gridSize / blockSize;

        for(int i = 0; i < gridSize/blockSize; i ++) {
            for(int j = 0; j < gridSize/blockSize; j ++) {
                BuildingBlock block = new BuildingBlock(i * blockSize, j * blockSize, blockSize, (j + i * GameEngine.MULIPLIER_X));
                UserInterface.gameGridPane.getChildren().add(block.getShape());

                if(isInSafeZone(block.getBlockId())) {
                    block.setBlockColor(SAFE_ZONE_COLOR);
                }
                gridList.add(block);
            }
        }
        getBlock(playerStartpoint).setDeathBlockIrreveritble(); 
    }
    /**
     * This creates the black death blocks that make the field smaller and smaller.
     * The method is called once every round by the thread that controlls game events
     * such as movement. The method returns the block id of created death block. This 
     * is used for killing players cought in the death builder.
     * @return id for the deathblock if a new one has been created or -1 if nothing happened.
     */
    public BuildingBlock deathBuilder() {
        BuildingBlock deathReturn = outsideBlock;
        if(isInSafeZone(deathLocation) == true) {
            isDeathRunning = false;
        }
        //Slow down the building progress to match the initial speed of players.
        if(isDeathRunning) {
            if(deathPause % (DEATH_SLOWNESS) == 0) {
                if(!(deathCounter < currentGridSize -1)) {
                    changeDeathDirection();
                }
                deathReturn =  getBlock(deathLocation);
               deathReturn.setDeathBlockIrreveritble();
               // deathReturn = deathLocation;
                deathCounter ++;
                deathLocation += deathDirection;
            }
            deathPause ++; 
        }
        return deathReturn;
    }
    //Checks if the end of the grid has been reached redirects the deathbuilder.
    private void changeDeathDirection() {
        switch (deathDirection) {
            case GameEngine.RIGHT:
                deathDirection = GameEngine.DOWN;
                deathCounter = 0;
                if (deathPause / (DEATH_SLOWNESS) > gridSize / blockSize) {
                    currentGridSize--;
                }
                break;
            case GameEngine.DOWN:
                deathDirection = GameEngine.LEFT;
                deathCounter = 0;
                break;
            case GameEngine.LEFT:
                deathDirection = GameEngine.UP;
                deathCounter = 0;
                currentGridSize--;
                break;
            case GameEngine.UP:
                deathDirection = GameEngine.RIGHT;
                deathCounter = 0;
                break;
        }
    }
    /**
     * Returns true if the death builder is still running and false if not.
     * @return true if death builder is still running.
     */
    public boolean isDeathRunning() {
        return isDeathRunning;
    }
    /**
     * Returns the start position for the players.
     * @return start position given as the Buildiing Block id
     */
    public int getStartPosition() {
        return playerStartpoint;
    }
    /**
     * Returns the size of the grid
     * @return The size of the grid in pixels
     */
    public int getGridSize() {
        return gridSize;
    }
    /**
     * Returns the size of the BuildingBlocks the grid is made up of.
     * @return the size of the BuildingBlocks in pixels.
     */
    public int getBlockSize() {
        return blockSize;
    }
    /**
     * Returns true if a block id belongs to a block that is in the zone
     * in the middle of the game field.
     * @param blockId id of block to be tested.
     * @return true if the block is in the safe zone
     */
    public boolean isInSafeZone(int blockId) {
        if(blockId == playerStartpoint) {
            return false;
        }
        int startPoint = playerStartpoint - SAFE_ZONE_DIAMETER - SAFE_ZONE_DIAMETER * GameEngine.MULIPLIER_X; 
        for(int i = 0; i < 2 * SAFE_ZONE_DIAMETER + 1; i++) {
            for(int j = 0; j < 2 * SAFE_ZONE_DIAMETER + 1; j++) {
                if(blockId == startPoint + i + j * GameEngine.MULIPLIER_X) {
                    return true;
                }  
            }
        }
        return false;
    }
    /**
     * Returns the block with the given block id or the outside block, that has block id -1, if
     * the block is not found.
     * @param blockId id of the requested block.
     * @return the BuildingBlock with the given id.
     */
    public BuildingBlock getBlock(int blockId) {
        for(BuildingBlock block: gridList) {
            if(block.getBlockId() == blockId) {
                return block;
            }
        }
        return outsideBlock;
    }
    /**
     * Used for getting a random block from the field, e.g. to place a bonus in.
     * It does not return blocks that have been occupied by the death builder.
     * @return a random BuildingBlock in the playable field.
     */
    public BuildingBlock getRandomBlock() {
        int adjustToMiddle = ((gridSize / blockSize) - currentGridSize)/2;
        Random random = new Random();
        int randomY = random.nextInt(currentGridSize - 2) + adjustToMiddle + 1;
        int randomX = (random.nextInt(currentGridSize -2) + adjustToMiddle + 1) * GameEngine.MULIPLIER_X;
        BuildingBlock randomBlock = getBlock(randomY + randomX);
        if(randomBlock.getBlockColor().equals(GAMEGRID_COLOR)) {
            return getBlock(randomY + randomX);
        }
        return outsideBlock;
    }
}
