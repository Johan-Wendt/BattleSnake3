
package battlesnake;

/**
 * @author johanwendt
 */
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 *This class the grid where the players move around. It also creates the block that
 * make the field smaller and smaller.
 */
public class GameGrid {
    //Fields
    //Static finals
    public static final int BLOCK_SIZE = (UserInterface.SCREENHEIGHT - (UserInterface.SCREENHEIGHT / 8)) / 47;
    public static final int GRID_SIZE = 2* (BLOCK_SIZE * 23) + BLOCK_SIZE;
    public static final int PLAYER_STARTPOINT = (((GRID_SIZE - BLOCK_SIZE) * GameEngine.MULIPLIER_X) / BLOCK_SIZE) / 2 + (((GRID_SIZE - BLOCK_SIZE) / BLOCK_SIZE) / 2);
    public static final int SAFE_ZONE_DIAMETER = 8;
    public static final Color GAMEGRID_COLOR = Color.web("#0000FF");
    public static final Color SAFE_ZONE_COLOR = Color.web("#4D4DFF");
    
    //Final fields
    private final int DEATH_SLOWNESS = GameEngine.PLAYER_START_SLOWNESS;
    private final ArrayList<BuildingBlock> gridList = new ArrayList<>();
    private final BuildingBlock outsideBlock;
    
    //This value is used for the blocks that make tha field smaller and smaller.
    //For convinience this gridsize is normalized to number of blocks.
    private int currentGridSize = GRID_SIZE / BLOCK_SIZE;
    
    //Regular fields
    private int deathCounter = 0;
    private int deathLocation = 0;
    private int deathDirection = GameEngine.RIGHT;
    private int deathPause = 1;
    private boolean isDeathRunning = true;

    /**
     * Constructor for the main game field grid. It creates the field from BuildingBlocks.
     * @param pane The pane where the field should be built.
     */
    public GameGrid(Pane pane) {

        for(int i = 0; i < GRID_SIZE/BLOCK_SIZE; i ++) {
            for(int j = 0; j < GRID_SIZE/BLOCK_SIZE; j ++) {
                BuildingBlock block = new BuildingBlock(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, (j + i * GameEngine.MULIPLIER_X));
                pane.getChildren().add(block.getShape());

                if(isInSafeZone(block.getBlockId())) {
                    block.setBlockColor(SAFE_ZONE_COLOR);
                }
                gridList.add(block);

            }
        }
        getBlock(PLAYER_STARTPOINT).setDeathBlockIrreveritble();
        
        //This block is returned from the grid if it gets asked about a grid id that it cannot
        //find. This is used for making the player move from one side to another on the 
        //field if no death blocks are in the way.
        outsideBlock = new BuildingBlock(-1);   
    }
    /**
     * This creates the black death blocks that make the field smaller and smaller.
     * The method is called once every round by the thread that controlls game events
     * such as movement. The method returns the block id of created death block. This 
     * is used for killing players cought in the death builder.
     * @return id for the deathblock if a new one has been created or -1 if nothing happened.
     */
    public int deathBuilder() {
        int deathReturn = -1;
        if(isInSafeZone(deathLocation) == true) {
            isDeathRunning = false;
        }
        //Slow down the building progress to match the initial speed of players.
        if(isDeathRunning) {
            if(deathPause % (DEATH_SLOWNESS) == 0) {

                if(deathCounter < currentGridSize -1) {
                    getBlock(deathLocation).setDeathBlockIrreveritble();
                    deathReturn = deathLocation;
                    deathCounter ++;
                    deathLocation += deathDirection;
                }
                else {
                    changeDeathDirection();
                }
            }
            deathPause ++; 
        }
        return deathReturn;
    }
    //Checks if the end of the grid has been reached redirects the deathbuilder.
    private void changeDeathDirection() {
        switch(deathDirection) {
            case GameEngine.RIGHT: deathDirection = GameEngine.DOWN; deathCounter = 0; if(deathPause / (DEATH_SLOWNESS)> GRID_SIZE / BLOCK_SIZE) currentGridSize --; break;
            case GameEngine.DOWN: deathDirection = GameEngine.LEFT; deathCounter = 0; break;
            case GameEngine.LEFT: deathDirection = GameEngine.UP; deathCounter = 0; currentGridSize --; break;          
            case GameEngine.UP: deathDirection = GameEngine.RIGHT; deathCounter = 0; break;
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
     * Returns true if a block id belongs to a block that is in the zone
     * in the middle of the game field.
     * @param blockId id of block to be tested.
     * @return true if the block is in the safe zone
     */
    public boolean isInSafeZone(int blockId) {
        if(blockId == PLAYER_STARTPOINT) {
            return false;
        }
        int startPoint = PLAYER_STARTPOINT - SAFE_ZONE_DIAMETER - SAFE_ZONE_DIAMETER * GameEngine.MULIPLIER_X; 
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
        int adjustToMiddle = ((GRID_SIZE / BLOCK_SIZE) - currentGridSize)/2;
        Random random = new Random();
        int randomY = random.nextInt(currentGridSize - 2) + adjustToMiddle + 1;
        int randomX = (random.nextInt(currentGridSize -2) + adjustToMiddle + 1) * GameEngine.MULIPLIER_X;
        return getBlock(randomY + randomX);
    }
}
