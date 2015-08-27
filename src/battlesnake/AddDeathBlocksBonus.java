/**
 *
 * @author johanwendt
 */
package battlesnake;

import javafx.scene.paint.Color;

/**
 *Creates a death block bonus that randomly creates three death blocks on the 
 * game grid when taken.
 */
public class AddDeathBlocksBonus extends Bonus{

    public AddDeathBlocksBonus(BuildingBlock bonusBlock, Color eventColor, int longevity, int eventHappening) {
        super(bonusBlock, eventColor, longevity, eventHappening);
    }
    /**
     * Creates three death blocks and places them randomly on the GameGrid. 
     * The death blocks are set to irrevertible with the consequence that they can populate 
     * the safe zone.
     * @param gameGrid 
     */
    @Override
    public void executeBonus(GameGrid gameGrid) {
        for(int i = 0; i < 3; i++) {
            BuildingBlock deathBlock = gameGrid.getRandomBlock();
            if(!gameGrid.isInSafeZone(deathBlock.getBlockId())) {
                deathBlock.setDeathBlockIrreveritble();
            }
        }
    }
    
}
