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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author johanwendt
 */
public class GameGrid {
    private ArrayList<BuildingBlock> gridList = new ArrayList<>();
    private BuildingBlock outsideBlock;
    public static final int BLOCK_SIZE = 16;
    public static final int GRID_SIZE = 768;
    public static final int PLAYER_STARTPOINT = ((GRID_SIZE * MainSnakeBoard.MULIPLIER_X) / BLOCK_SIZE) / 2 + ((GRID_SIZE / BLOCK_SIZE) / 2);
    public static final Color GAMEGRID_COLOR = Color.AQUA;
    public static final Color SAFE_ZONE_COLOR = Color.LIGHTBLUE;
    private int currentGridSize = GRID_SIZE / BLOCK_SIZE;
    private int deathCounter = 0;
    private int deathLocation = 0;
    private int deathDirection = MainSnakeBoard.RIGHT;
    private int deathPause = 0;
    private static final int DEATH_SLOWNESS = 10;

    
    public GameGrid(Pane pane) {

        for(int i = 0; i < GRID_SIZE/BLOCK_SIZE; i ++) {
            for(int j = 0; j < GRID_SIZE/BLOCK_SIZE; j ++) {
                BuildingBlock block = new BuildingBlock(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, (j + i * MainSnakeBoard.MULIPLIER_X));
                pane.getChildren().add(block.addToPane());
             //   if(i == 0 || j==0 || i == GRID_SIZE/BLOCK_SIZE - 1 || j == GRID_HEIGTH/BLOCK_SIZE - 1) {
               //     block.setIsDeathBlock();
                 //   block.setBlockColor(EventHandler.DETHBLOCK_COLOR);
                //}
                if(isInSafeZone(block.getBlockId())) {
                    block.setBlockColor(SAFE_ZONE_COLOR);
                }
                gridList.add(block);

            }
        }
        getBlock(PLAYER_STARTPOINT).setBlockColor(EventHandler.DETHBLOCK_COLOR);
        outsideBlock = new BuildingBlock(-1);   
    }
    public void deathBuilder() {
        if(currentGridSize > 2 * Player.PLAYER_START_LENGTH + 1) {
            if(deathPause % DEATH_SLOWNESS == 0) {
                if(deathCounter < currentGridSize - 1) {
                    getBlock(deathLocation).setIsDeathBlock(true);
                    deathCounter ++;
                    deathLocation += deathDirection;
                }
                else {
                    changeDeathDirection();
                }
            }
            deathPause ++;
        }
    }
    public void changeDeathDirection() {
        switch(deathDirection) {
            case MainSnakeBoard.RIGHT: deathDirection = MainSnakeBoard.DOWN; deathCounter = 0; if (deathPause > (GRID_SIZE / BLOCK_SIZE) * DEATH_SLOWNESS) currentGridSize --; break;
            case MainSnakeBoard.DOWN: deathDirection = MainSnakeBoard.LEFT; deathCounter = 0; break;
            case MainSnakeBoard.LEFT: deathDirection = MainSnakeBoard.UP; deathCounter = 0; currentGridSize --; break;          
            case MainSnakeBoard.UP: deathDirection = MainSnakeBoard.RIGHT; deathCounter = 0; break;
        }
        
    }
    /**
     * Returns the BuildingBlock with the given ID. If the ID is not in the list
     * it returns the deathBlock
     * @param blockId The ID of the BuildingBlock.
     * @return BuildingBlock with requested ID or first block added.
     */
    public boolean isInSafeZone(int blockId) {
        if(blockId == PLAYER_STARTPOINT) {
            BuildingBlock block = getBlock(blockId);
            return false;
        }
        int startPoint = PLAYER_STARTPOINT - Player.PLAYER_START_LENGTH - Player.PLAYER_START_LENGTH * MainSnakeBoard.MULIPLIER_X; 
        for(int i = 0; i < 2 * Player.PLAYER_START_LENGTH + 1; i++) {
            for(int j = 0; j < 2 * Player.PLAYER_START_LENGTH + 1; j++) {
                if(blockId == startPoint + i + j * MainSnakeBoard.MULIPLIER_X) {
                    return true;
                }
                
            }
        }
        return false;
    }
    public BuildingBlock getBlock(int blockId) {
        for(BuildingBlock block: gridList) {
            if(block.getBlockId() == blockId) {
                return block;
            }
        }
        return outsideBlock;
    }
    public BuildingBlock getRandomBlock() {
        Random random = new Random();
        int randomY = random.nextInt(GRID_SIZE/BLOCK_SIZE);
        int randomX = random.nextInt(GRID_SIZE/BLOCK_SIZE) * MainSnakeBoard.MULIPLIER_X;
        return getBlock(randomY + randomX);
    }
}
