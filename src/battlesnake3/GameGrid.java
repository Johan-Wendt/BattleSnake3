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
    private BuildingBlock deathBlock;
    public static final Color GAMEGRID_COLOR = Color.AQUA;
    public static final Color SAFE_ZONE_COLOR = Color.LIGHTBLUE;

    
    public GameGrid(int height, int width, Pane pane, int multiplierX, int blockSize) {

        for(int i = 0; i < width/blockSize; i ++) {
            for(int j = 0; j < height/blockSize; j ++) {
                BuildingBlock block = new BuildingBlock(i * blockSize, j * blockSize, blockSize, (j + i * multiplierX));
                pane.getChildren().add(block.addToPane());
                if(i == 0 || j==0 || i == width/blockSize - 1 || j == height/blockSize - 1) {
                    block.setIsDeathBlock();
                    block.setBlockColor(EventHandler.DETHBLOCK_COLOR);
                }
                if(isInSafeZone(block.getBlockId())) {
                    block.setBlockColor(SAFE_ZONE_COLOR);
                }
                gridList.add(block);

            }
        }
        deathBlock = new BuildingBlock(-1);
        getBlock(MainSnakeBoard.PLAYER_STARTPOINT).isDeathBlock();
        getBlock(MainSnakeBoard.PLAYER_STARTPOINT).setBlockColor(EventHandler.DETHBLOCK_COLOR);
    }
    /**
     * Returns the BuildingBlock with the given ID. If the ID is not in the list
     * it returns the deathBlock
     * @param blockId The ID of the BuildingBlock.
     * @return BuildingBlock with requested ID or first block added.
     */
    public boolean isInSafeZone(int blockId) {
        int startPoint = MainSnakeBoard.PLAYER_STARTPOINT - Player.PLAYER_START_LENGTH - Player.PLAYER_START_LENGTH * MainSnakeBoard.MULIPLIER_X; 
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
        return deathBlock;
    }
    public BuildingBlock getRandomBlock() {
        Random random = new Random();
        int randomY = random.nextInt(MainSnakeBoard.GRID_HEIGTH/MainSnakeBoard.BLOCK_SIZE);
        int randomX = random.nextInt(MainSnakeBoard.GRID_WIDTH/MainSnakeBoard.BLOCK_SIZE) * MainSnakeBoard.MULIPLIER_X;
        return getBlock(randomY + randomX);
    }
}
