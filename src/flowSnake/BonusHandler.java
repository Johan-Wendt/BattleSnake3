/**
 * @author johanwendt
 */
package flowSnake;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import javafx.scene.paint.Color;
import java.util.Iterator;

/**
 *The BonusHandler creates bonuses randomly in the game field and destroys them
 * after a random period of time. 
 */
public class BonusHandler {
    
    private static final Color deathBlockColor = Color.BLACK;
    public static final int REGULAR_BONUS = 0;
    public static final int MAKE_SHORT_BONUS = 1;
    public static final int ADD_DEATH_BLOCK_BONUS = 2;
    private static final int LIFESPAN_MIN = 500;
    private static final int LIFESPAN_MAX = 2500;
    private static final double BONUS_PROBABILITY = 0.09;
    private final static HashSet <Bonus> eventList = new HashSet<>();    
    //Regular fields
    private Random random;
    
    /**
     * Creates a BonusHandler. 
     */
    public BonusHandler() {
    }
    /**
     * Method called every round by the players to see if a bonus has been taken.
     * If taken the bonus is removed, the gamegrid is notified to see if it should
     * act, and the bonus happening is returned to the player.
     * @param blockId The block id for the BuildingBlock to be checked.
     * @return int representing bonus happening or -1 if no bonus is present.
     */
    public static int getBonus(int blockId) {
        for(Bonus event: eventList) {
            if(event.getBonusId() == blockId) {
                event.setTaken();
                event.executeBonus();
                return event.getBonusHappening();
            }
        }
        return -1;
    }
    /**
     * Method that randomly creates bonuses and places them in the GameGrid, and
     * also destroys bonuses that are to be removed.
     */
    public static void bonusRound() {
        if(BONUS_PROBABILITY * Math.sqrt(GameEngine.getNumberOfPlayers()) > Math.random()) createRandomBonus();
        destroyBonuses();
    }
    /**
     * Method that randomly, but wieghted, creates one of the bonusetypes.
     */
    public static void createRandomBonus() { 
        
        int chance = new Random().nextInt(BonusEnum.REGULAR_BONUS.getBonusProbabilityFactor() + BonusEnum.MAKE_SHORT_BONUS.getBonusProbabilityFactor() + BonusEnum.ADD_DEATH_BLOCK_BONUS.getBonusProbabilityFactor());
        if(chance < BonusEnum.REGULAR_BONUS.getBonusProbabilityFactor()) {
            createRegularBonus();
        }
        else if (chance < BonusEnum.ADD_DEATH_BLOCK_BONUS.getBonusProbabilityFactor() + BonusEnum.REGULAR_BONUS.getBonusProbabilityFactor()) {
            createAddDeathBlocksBonus();
        }
        else {
            createMakeShortBonus();
        }
    }
    /**
     * Creates one Regular bonus and places it on the GameGrid.
     */
    public static void createRegularBonus() {
        BuildingBlock bonusBlock = GameGrid.getRandomBlock();
        Bonus bonus = new RegularBonus(bonusBlock, BonusEnum.REGULAR_BONUS, LIFESPAN_MIN + new Random().nextInt(LIFESPAN_MAX));
        eventList.add(bonus);
    }
    /**
     * Creates one Make short bonus and places it on the GameGrid.
     */
    public static void createMakeShortBonus() {
        BuildingBlock bonusBlock = GameGrid.getRandomBlock();
        Bonus bonus = new MakeShortBonus(bonusBlock,BonusEnum.MAKE_SHORT_BONUS, LIFESPAN_MIN + new Random().nextInt(LIFESPAN_MAX));
        eventList.add(bonus);
    }
    /**
     * Creates one Add death block bonus and places it on the GameGrid.
     */
    public static void createAddDeathBlocksBonus() {
        BuildingBlock bonusBlock = GameGrid.getRandomBlock();
        Bonus bonus = new AddDeathBlocksBonus (bonusBlock,BonusEnum.ADD_DEATH_BLOCK_BONUS, LIFESPAN_MIN + new Random().nextInt(LIFESPAN_MAX));
        eventList.add(bonus);
    }
    /**
     * Removes all bonuses on the GameGrid that are taken or have outlived their lifespan.
     */
    private static void destroyBonuses() {
        Iterator itr = eventList.iterator();
        while(itr.hasNext()) {
            Bonus bonus = (Bonus)itr.next();
            if(bonus.checkRemove()) {
                itr.remove();
            }
        }
    }
}
