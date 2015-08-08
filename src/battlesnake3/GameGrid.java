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

    
    public GameGrid(int height, int width, Pane pane, int multiplierX, int blockSize) {

        for(int i = 0; i < width/blockSize; i ++) {
            for(int j = 0; j < height/blockSize; j ++) {
                BuildingBlock block = new BuildingBlock(i * blockSize, j * blockSize, blockSize, (j + i * multiplierX));
                pane.getChildren().add(block.getRectangle());
                if(i == 0 || j==0 || i == width/blockSize - 1 || j == height/blockSize - 1) {
                    block.setIsDeathBlock();
                    block.setRectangleColor(Color.BLACK);
                }
                gridList.add(block);

            }
        }
        deathBlock = new BuildingBlock(-1);
    }
    /**
     * Returns the BuildingBlock with the given ID. If the ID is not in the list
     * it returns the deathBlock
     * @param blockId The ID of the BuildingBlock.
     * @return BuildingBlock with requested ID or first block added.
     */
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
