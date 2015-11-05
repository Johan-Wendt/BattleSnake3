/**
 *
 * @author johanwendt
 */
package flowSnake;

import javafx.scene.paint.Color;

/**
 *Creates a death block bonus that randomly creates three death blocks on the 
 * game grid when taken.
 */
public class ShootBonus extends Bonus{

    /**
     * Creates a bonus and places it in the field.
     * @param bonusBlock The Building block to be changed to a Bonus. If the color of this block is 
     * not the GameGrid color the bonus is removed directly.
     * @param bonusColor The color of this Bonus.
     * @param lifespan The how long this bonus should be on the GameGrid.
     * @param bonusHappening Int describing what happens when the bonus is taken.
     */
    public ShootBonus(VisibleObjects details, BuildingBlock bonusBlock, BonusEnum bonusEnum, int lifespan) {
        super(details, bonusBlock, bonusEnum, lifespan);
    }
    /**
    /**
     * Creates three death blocks and places them randomly on the GameGrid. 
     * The death blocks are set to irrevertible with the consequence that they can populate 
     * the safe zone.
     
    @Override
    public void executeBonus() {
        for(int i = 0; i < 3; i++) {
            BuildingBlock deathBlock = GameGrid.getRandomBlock();
            if(!GameGrid.isInSafeZone(deathBlock.getBlockId())) {
                deathBlock.setDeathBlockIrreveritble();
            }
        }
    }
    **/
    
}
