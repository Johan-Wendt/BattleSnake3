
package battlesnake;

import javafx.scene.paint.Color;
/**
 *
 * @author johanwendt
 */
public abstract class Bonus {
    private double longevity;
    private int bonusHappening;
    private double startTime;
    private boolean isToRemove = false;
    private boolean isTaken = false;
    private BuildingBlock bonusBlock;
    
    public Bonus(BuildingBlock bonusBlock, Color eventColor, int longevity, int bonusHappening) {
        this.longevity = longevity;
        this.bonusBlock = bonusBlock;
        this.bonusHappening = bonusHappening;
        if(this.bonusBlock.getBlockColor().equals(GameGrid.GAMEGRID_COLOR)) {
            this.bonusBlock.setBlockColor(eventColor);
            startTime = System.currentTimeMillis();
        }
        else {
            isToRemove = true;
            isTaken = true;
        }
    }
    public boolean isToRemove() {
        if(System.currentTimeMillis() - startTime > longevity) {
            isToRemove = true;
            if(!isTaken) bonusBlock.revertDeathBlock(false);
        }
        return isToRemove;
    }
    public int getEventId() {
        return bonusBlock.getBlockId();
    }
    public int getEventHappening() {
        return bonusHappening;
    }
    public void isTaken() {
        isTaken = true;
        isToRemove = true;
    }
    public void executeBonus(GameGrid gameGrid) {
    }

}
