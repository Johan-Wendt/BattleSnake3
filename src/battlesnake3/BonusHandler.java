/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesnake3;

import java.util.ArrayList;
import java.util.Random;
import javafx.scene.paint.Color;
import java.util.Iterator;

/**
 *
 * @author johanwendt
 */
public class BonusHandler {
    private ArrayList <Bonus> eventList = new ArrayList<>();
    private static int LONGEVITY_MIN = 2000;
    private static int LONGEVITY_MAX = 10000;
    private static double BONUS_PROBABILITY = 0.003;
    private GameGrid gameGrid;
    private Random random;
    
    public static final Color DETHBLOCK_COLOR = Color.BLACK;
    
    public static final int REGULAR_BONUS_HAPPENING = 0;
    public static final Color REGULAR_BONUS_COLOR = Color.ORANGE;
    public static final int REGULAR_BONUS_PROBABILTY_FACTOR = 10;
    public static final String REGULAR_BONUS_DESCRIPTION = "Makes the player longer and faster.";
    public static final int MAKE_SHORT_BONUS_HAPPENING = 1;
    public static final Color MAKE_SHORT_BONUS_COLOR = Color.PINK;
    public static final int MAKE_SHORT_BONUS_PROBABILTY_FACTOR = 1;
    public static final String MAKE_SHORT_BONUS_DESCRIPTION = "Makes the player short.";
    public static final int ADD_DEATH_BLOCK_BONUS_HAPPENING = 4;
    public static final Color ADD_DEATH_BLOCK_BONUS_COLOR = Color.PURPLE;
    public static final int ADD_DEATH_BLOCK_BONUS_PROBABILTY_FACTOR = 2;
    public static final String ADD_DEATH_BLOCK_BONUS_DESCRIPTION = "Adds random deathblocks to the field.";
    
    public BonusHandler(GameGrid gameGrid) {
        this.gameGrid = gameGrid;
        
    }
    //Getters
    public int getEvent(int blockId) {
        for(Bonus event: eventList) {
            if(event.getEventId() == blockId) {
                event.isTaken();
                event.executeBonus(gameGrid);
                return event.getEventHappening();
            }
        }
        return -1;
    }
    //Mutators
    public void eventRound() {
        if(BONUS_PROBABILITY > Math.random()) createRandomBonus();
        destroyBonuses();
    }
    public void createRandomBonus() { 
        int chance = new Random().nextInt(REGULAR_BONUS_PROBABILTY_FACTOR + MAKE_SHORT_BONUS_PROBABILTY_FACTOR + ADD_DEATH_BLOCK_BONUS_PROBABILTY_FACTOR);
        if(chance < MAKE_SHORT_BONUS_PROBABILTY_FACTOR) {
            createMakeShortBonus();
        }
        else if (chance < ADD_DEATH_BLOCK_BONUS_PROBABILTY_FACTOR + MAKE_SHORT_BONUS_HAPPENING) {
            createAddDeathBlocksBonus();
        }
        else {
            createRegularBonus();
        }
    }
    public void createRegularBonus() {
        BuildingBlock bonusBlock = gameGrid.getRandomBlock();
        Bonus bonus = new RegularBonus(bonusBlock,REGULAR_BONUS_COLOR, LONGEVITY_MIN + new Random().nextInt(LONGEVITY_MAX), REGULAR_BONUS_HAPPENING);
        eventList.add(bonus);
    }
    public void createMakeShortBonus() {
        BuildingBlock bonusBlock = gameGrid.getRandomBlock();
        Bonus bonus = new MakeShortBonus(bonusBlock,MAKE_SHORT_BONUS_COLOR, LONGEVITY_MIN + new Random().nextInt(LONGEVITY_MAX), MAKE_SHORT_BONUS_HAPPENING);
        eventList.add(bonus);
    }
    public void createAddDeathBlocksBonus() {
        BuildingBlock bonusBlock = gameGrid.getRandomBlock();
        Bonus bonus = new AddDeathBlocksBonus (bonusBlock,ADD_DEATH_BLOCK_BONUS_COLOR, LONGEVITY_MIN + new Random().nextInt(LONGEVITY_MAX), ADD_DEATH_BLOCK_BONUS_HAPPENING);
        eventList.add(bonus);
    }
    public void destroyBonuses() {
        Iterator itr = eventList.iterator();
        while(itr.hasNext()) {
            Bonus bonus = (Bonus)itr.next();
            if(bonus.isToRemove()) {
                itr.remove();
            }
        }
    }
}
