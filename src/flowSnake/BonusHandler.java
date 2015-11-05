/**
 * @author johanwendt
 */
package flowSnake;

import java.util.Random;
import javafx.scene.paint.Color;

/**
 *The BonusHandler creates bonuses randomly in the game field and destroys them
 * after a random period of time. 
 */
public class BonusHandler {
    
  //  private static final Color deathBlockColor = Color.BLACK;
    private static final int LIFESPAN_MIN = 5000;
    private static final int LIFESPAN_MAX = 20000;
    private static final double BONUS_PROBABILITY = 0.03;
   // private final static HashSet<Bonus> eventList = new HashSet<>();    
    //Regular fields
    //private Random random;
    
    /**
     * Creates a BonusHandler. 
     */
    public BonusHandler() {
    }
    /**
     * Method that randomly creates bonuses and places them in the GameGrid, and
     * also destroys bonuses that are to be removed.
     */
    public static void bonusRound() {
        if(BONUS_PROBABILITY * Math.sqrt(GameEngine.getNumberOfPlayers()) > Math.random()) createRandomBonus();
    }
    /**
     * Method that randomly, but wieghted, creates one of the bonusetypes.
     */
    public static void createRandomBonus() {
        BuildingBlock bonusBlock = GameGrid.getRandomBlock();
        if(bonusBlock.getOccupant() == null) {
            int chance = new Random().nextInt(BonusEnum.MAKE_LONG_BONUS.getBonusProbabilityFactor() + BonusEnum.MAKE_FASTER_BONUS.getBonusProbabilityFactor() + BonusEnum.SHOOT_BONUS.getBonusProbabilityFactor());
            if(chance < BonusEnum.MAKE_LONG_BONUS.getBonusProbabilityFactor()) {
                createMakeLongerBonus(bonusBlock);
            }
            else if (chance < BonusEnum.SHOOT_BONUS.getBonusProbabilityFactor() + BonusEnum.MAKE_LONG_BONUS.getBonusProbabilityFactor()) {
                createShootBonus(bonusBlock);
            }
            else {
                createMakeFasterBonus(bonusBlock);
            }
        }
    }
    /**
     * Creates one Regular bonus and places it on the GameGrid.
     */
    public static void createMakeLongerBonus(BuildingBlock bonusBlock) {
        Bonus bonus = new MakeLongerBonus(VisibleObjects.MAKE_LONGER_BONUS, bonusBlock, BonusEnum.MAKE_LONG_BONUS, LIFESPAN_MIN + new Random().nextInt(LIFESPAN_MAX));
    }
    /**
     * Creates one Make short bonus and places it on the GameGrid.
     */
    public static void createMakeFasterBonus(BuildingBlock bonusBlock) {
        Bonus bonus = new MakeFasterBonus(VisibleObjects.MAKE_FASTER_BONUS, bonusBlock,BonusEnum.MAKE_FASTER_BONUS, LIFESPAN_MIN + new Random().nextInt(LIFESPAN_MAX));
    }
    /**
     * Creates one Add death block bonus and places it on the GameGrid.
     */
    public static void createShootBonus(BuildingBlock bonusBlock) {
        Bonus bonus = new ShootBonus(VisibleObjects.SHOOT_BONUS, bonusBlock, BonusEnum.SHOOT_BONUS, LIFESPAN_MIN + new Random().nextInt(LIFESPAN_MAX));
    }
}
