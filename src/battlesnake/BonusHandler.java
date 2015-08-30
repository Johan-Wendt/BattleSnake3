/**
 * @author johanwendt
 */
package battlesnake;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.paint.Color;
import java.util.Iterator;

/**
 *The BonusHandler creates bonuses randomly in the game field and destroys them
 * after a random period of time. 
 */
public class BonusHandler {
    
    //Static final fields
    public static final Color deathBlockColor = Color.BLACK;
    private static final int LIFESPAN_MIN = 2000;
    private static final int LIFESPAN_MAX = 10000;
    private static final double BONUS_PROBABILITY = 0.045;
    public static final int REGULAR_BONUS_HAPPENING = 0;
    public static final Color regularBonusColor = Color.web("#FF0000");
    public static final int REGULAR_BONUS_PROBABILTY_FACTOR = 10;
    public static final String regularBonusDescription = "Makes the player longer and faster.";
    public static final int MAKE_SHORT_BONUS_HAPPENING = 1;
    public static final Color makeShortBonusColor = Color.web("#009933");
    public static final int MAKE_SHORT_BONUS_PROBABILTY_FACTOR = 1;
    public static final String makeShortDescription = "Makes the player short.";
    public static final int ADD_DEATH_BLOCK_BONUS_HAPPENING = 2;
    public static final Color addDeathBlockBonusColor = Color.web("#FFFF00");
    public static final int ADD_DEATH_BLOCK_BONUS_PROBABILTY_FACTOR = 8;
    public static final String addDeathBlockBonusDescription = "Adds deathblocks to the field.";
    
    //final fields 
    private final static ArrayList <Bonus> eventList = new ArrayList<>();    
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
    public int getBonus(int blockId) {
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
    public void bonusRound() {
        if(BONUS_PROBABILITY * Math.sqrt(GameEngine.getNumberOfPlayers()) > Math.random()) createRandomBonus();
        destroyBonuses();
    }
    /**
     * Method that randomly, but wieghted, creates one of the bonusetypes.
     */
    public void createRandomBonus() { 
        int chance = new Random().nextInt(REGULAR_BONUS_PROBABILTY_FACTOR + MAKE_SHORT_BONUS_PROBABILTY_FACTOR + ADD_DEATH_BLOCK_BONUS_PROBABILTY_FACTOR);
        if(chance < MAKE_SHORT_BONUS_PROBABILTY_FACTOR) {
            createMakeShortBonus();
        }
        else if (chance < ADD_DEATH_BLOCK_BONUS_PROBABILTY_FACTOR + MAKE_SHORT_BONUS_PROBABILTY_FACTOR) {
            createAddDeathBlocksBonus();
        }
        else {
            createRegularBonus();
        }
    }
    /**
     * Creates one Regular bonus and places it on the GameGrid.
     */
    public void createRegularBonus() {
        BuildingBlock bonusBlock = GameEngine.getCurrentGameGrid().getRandomBlock();
        Bonus bonus = new RegularBonus(bonusBlock, regularBonusColor, LIFESPAN_MIN + new Random().nextInt(LIFESPAN_MAX), REGULAR_BONUS_HAPPENING);
        eventList.add(bonus);
    }
    /**
     * Creates one Make short bonus and places it on the GameGrid.
     */
    public void createMakeShortBonus() {
        BuildingBlock bonusBlock = GameEngine.getCurrentGameGrid().getRandomBlock();
        Bonus bonus = new MakeShortBonus(bonusBlock,makeShortBonusColor, LIFESPAN_MIN + new Random().nextInt(LIFESPAN_MAX), MAKE_SHORT_BONUS_HAPPENING);
        eventList.add(bonus);
    }
    /**
     * Creates one Add death block bonus and places it on the GameGrid.
     */
    public void createAddDeathBlocksBonus() {
        BuildingBlock bonusBlock = GameEngine.getCurrentGameGrid().getRandomBlock();
        Bonus bonus = new AddDeathBlocksBonus (bonusBlock,addDeathBlockBonusColor, LIFESPAN_MIN + new Random().nextInt(LIFESPAN_MAX), ADD_DEATH_BLOCK_BONUS_HAPPENING);
        eventList.add(bonus);
    }
    /**
     * Removes all bonuses on the GameGrid that are taken or have outlived their lifespan.
     */
    private void destroyBonuses() {
        Iterator itr = eventList.iterator();
        while(itr.hasNext()) {
            Bonus bonus = (Bonus)itr.next();
            if(bonus.checkRemove()) {
                itr.remove();
            }
        }
    }
}
