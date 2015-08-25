
package battlesnake;

import javafx.scene.paint.Color;
/**
 *
 * @author johanwendt
 */
public abstract class Bonus {
    //Regular fields.
    private double longevity;
    private int bonusHappening;
    private double startTime;
    private boolean isToRemove = false;
    private boolean isTaken = false;
    private BuildingBlock bonusBlock;
    
    /**
     * Creates a bonus and places it in the field.
     * @param bonusBlock The Building block to be changed to a Bonus. If the color of this block is 
     * not the GameGrid color the bonus is removed directly.
     * @param bonusColor The color of this Bonus.
     * @param lifespan The how long this bonus should be on the GameGrid.
     * @param bonusHappening Int describing what happens when the bonus is taken.
     */
    public Bonus(BuildingBlock bonusBlock, Color bonusColor, int lifespan, int bonusHappening) {
        this.longevity = lifespan;
        this.bonusBlock = bonusBlock;
        this.bonusHappening = bonusHappening;
        if(this.bonusBlock.getBlockColor().equals(GameGrid.GAMEGRID_COLOR)) {
            this.bonusBlock.setBlockColor(bonusColor);
            this.bonusBlock.setLightingEffect();
            startTime = System.currentTimeMillis();
        }
        else {
            setTaken();
        }
    }
    /**
     * Checks if a bonus is to be removed from the GameGrid. If the bonus is
     * to be removed it sets the isToRemove to true. If the bonus was not taken
     * by a player this method also reverts the color to the original color of 
     * the BuildingBlock.
     * @return If the bonus is to be removed.
     */
    public boolean checkRemove() {
        if(System.currentTimeMillis() - startTime > longevity) {
            isToRemove = true;
            bonusBlock.removeEffect();
            if(!isTaken) bonusBlock.revertDeathBlock(false);
        }
        return isToRemove;
    }
    /**
     * Returns the block id for the BuildingBlock that constitutes this bonus.
     * @return id for this bonus.
     */
    public int getBonusId() {
        return bonusBlock.getBlockId();
    }
    /**
     * Returns the bonus happening that lets the player instances know what to make of the bonus.
     * @return happening for this bonus.
     */
    public int getBonusHappening() {
        return bonusHappening;
    }
    /**
     * Sets the isTaken property to true and also sets the isToRemove property to true.
     */
    public void setTaken() {
        bonusBlock.removeEffect();
        isTaken = true;
        isToRemove = true;
    }
    /**
     * Method to be overridden by those bonuses that need to be executed 
     * on the GameGrid.
     * @param gameGrid 
     */
    public void executeBonus(GameGrid gameGrid) {
    }

}
