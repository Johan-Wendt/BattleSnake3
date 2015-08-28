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
    public static final Color DETHBLOCK_COLOR = Color.BLACK;
    private static final int LIFESPAN_MIN = 2000;
    private static final int LIFESPAN_MAX = 10000;
    private static final double BONUS_PROBABILITY = 0.045;
    public static final int REGULAR_BONUS_HAPPENING = 0;
    public static final Color REGULAR_BONUS_COLOR = Color.web("#FF0000");
    public static final int REGULAR_BONUS_PROBABILTY_FACTOR = 10;
    public static final String REGULAR_BONUS_DESCRIPTION = "Makes the player longer and faster.";
    public static final int MAKE_SHORT_BONUS_HAPPENING = 1;
    public static final Color MAKE_SHORT_BONUS_COLOR = Color.web("#009933");
    public static final int MAKE_SHORT_BONUS_PROBABILTY_FACTOR = 1;
    public static final String MAKE_SHORT_BONUS_DESCRIPTION = "Makes the player short.";
    public static final int ADD_DEATH_BLOCK_BONUS_HAPPENING = 2;
    public static final Color ADD_DEATH_BLOCK_BONUS_COLOR = Color.web("#FFFF00");
    public static final int ADD_DEATH_BLOCK_BONUS_PROBABILTY_FACTOR = 8;
    public static final String ADD_DEATH_BLOCK_BONUS_DESCRIPTION = "Adds random deathblocks to the field.";
    
    //final fields 
    private final ArrayList <Bonus> eventList = new ArrayList<>();
    private final GameGrid gameGrid;
    
    //Regular fields
    private final double numberOfPlayers;
    private Random random;
    
    /**
     * Creates a BonusHandler. 
     * @param gameGrid The gameGrid that the bonuses should be placed on.
     * @param numberOfPlayers number of players in the game.
     */
    public BonusHandler(GameGrid gameGrid, double numberOfPlayers) {
        this.gameGrid = gameGrid;    
        this.numberOfPlayers = numberOfPlayers;
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
                event.executeBonus(gameGrid);
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
        if(BONUS_PROBABILITY * Math.sqrt(numberOfPlayers) > Math.random()) createRandomBonus();
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
        BuildingBlock bonusBlock = gameGrid.getRandomBlock();
        Bonus bonus = new RegularBonus(bonusBlock, REGULAR_BONUS_COLOR, LIFESPAN_MIN + new Random().nextInt(LIFESPAN_MAX), REGULAR_BONUS_HAPPENING);
        eventList.add(bonus);
    }
    /**
     * Creates one Make short bonus and places it on the GameGrid.
     */
    public void createMakeShortBonus() {
        BuildingBlock bonusBlock = gameGrid.getRandomBlock();
        Bonus bonus = new MakeShortBonus(bonusBlock,MAKE_SHORT_BONUS_COLOR, LIFESPAN_MIN + new Random().nextInt(LIFESPAN_MAX), MAKE_SHORT_BONUS_HAPPENING);
        eventList.add(bonus);
    }
    /**
     * Creates one Add death block bonus and places it on the GameGrid.
     */
    public void createAddDeathBlocksBonus() {
        BuildingBlock bonusBlock = gameGrid.getRandomBlock();
        Bonus bonus = new AddDeathBlocksBonus (bonusBlock,ADD_DEATH_BLOCK_BONUS_COLOR, LIFESPAN_MIN + new Random().nextInt(LIFESPAN_MAX), ADD_DEATH_BLOCK_BONUS_HAPPENING);
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
