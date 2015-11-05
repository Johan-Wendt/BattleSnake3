
package flowSnake;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.util.Duration;
/**
 *
 * @author johanwendt
 */
public abstract class Bonus extends VisibleObject {
    //private final int lifespan;
    private final BonusEnum bonusEnum;
    private final BuildingBlock bonusBlock;
    private boolean isToRemove = false;
    private boolean isTaken = false;
    
    /**
     * Creates a bonus and places it in the field.
     * @param bonusBlock The Building block to be changed to a Bonus. If the color of this block is 
     * not the GameGrid color the bonus is removed directly.
     * @param bonusColor The color of this Bonus.
     * @param lifespan The how long this bonus should be on the GameGrid.
     * @param bonusHappening Int describing what happens when the bonus is taken.
     */
    public Bonus(VisibleObjects details, BuildingBlock bonusBlock, BonusEnum bonusEnum, int lifespan) {
        super(details);
      //  this.lifespan = GameEngine.getTurn() + lifespan;
        this.bonusBlock = bonusBlock;
        this.bonusEnum = bonusEnum;
        bonusBlock.Occupy(details, this, -1);
        bonusBlock.setBlockImage(details.getBlockImage());
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(lifespan),
        ae -> bonusBlock.revertBlock(this)));
        timeline.play();
        GameEngine.addTimeLine(timeline);
    }
    /**
     * Checks if a bonus is to be removed from the GameGrid. If the bonus is
     * to be removed it sets the isToRemove to true. If the bonus was not taken
     * by a player this method also reverts the color to the original color of 
     * the BuildingBlock.
     * @return If the bonus is to be removed.
     */
    /*
    public boolean checkRemove() {
        if(GameEngine.getTurn() > lifespan) {
            isToRemove = true;
            if(!isTaken) bonusBlock.revertBlock();
        }
        return isToRemove;
    }
    */
    /**
     * Returns the block id for the BuildingBlock that constitutes this bonus.
     * @return id for this bonus.
     */
    public int getBonusId() {
        return bonusBlock.getBlockId();
    }
    private void removeBonus(Timeline timeline) {
        bonusBlock.revertBlock(this);
        GameEngine.removeTimeline(timeline);
    }
    /**
     * Returns the bonus happening that lets the player instances know what to make of the bonus.
     * @return happening for this bonus.
     */
    public int getBonusHappening() {
        return bonusEnum.getBonusNumber();
    }
    /**
     * Sets the isTaken property to true and also sets the isToRemove property to true.
     */
    public void setTaken() {
        isTaken = true;
        isToRemove = true;
    }
    /**
     * Method to be overridden by those bonuses that need to be executed 
     * on the GameGrid.
     */
    public void executeBonus() {
    }
    @Override
    public int getDirection() {
        return 0;
    }
    @Override
    public int getSpeed() {
        return 0;
    }
}
