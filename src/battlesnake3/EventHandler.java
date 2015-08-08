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
public class EventHandler {
    private ArrayList <Event> eventList = new ArrayList<>();
    private static int LONGEVITY_MIN = 2000;
    private static int LONGEVITY_MAX = 10000;
    private static double EVENT_PROBABILITY = 0.003;
    private GameGrid gameGrid;
    private Random random;
    
    public static final int REGULAR_EVENT_HAPPENING = 0;
    public static final Color REGULAR_EVENT_COLOR = Color.YELLOW;
    public static final int REGULAR_EVENT_PROBABILTY_FACTOR = 10;
    public static final int MAKE_SHORT__EVENT_HAPPENING = 1;
    public static final Color MAKE_SHORT_EVENT_COLOR = Color.PINK;
    public static final int MAKE_SHORT__EVENT_PROBABILTY_FACTOR = 1;
    public static final int ADD_DEATH_BLOCK_EVENT_HAPPENING = 4;
    public static final Color ADD_DEATH_BLOCK_EVENT_COLOR = Color.WHITE;
    public static final int ADD_DEATH_BLOCK_EVENT_PROBABILTY_FACTOR = 2;
    
    public EventHandler(GameGrid gameGrid) {
        this.gameGrid = gameGrid;
        
    }
    //Getters
    public int getEvent(int blockId) {
        for(Event event: eventList) {
            if(event.getEventId() == blockId) {
                event.isTaken();
                event.executeEvent(gameGrid);
                return event.getEventHappening();
            }
        }
        return -1;
    }
    //Mutators
    public void eventRound() {
        if(EVENT_PROBABILITY > Math.random()) createRandomEvent();
        destroyEvents();
    }
    public void createRandomEvent() { 
        int chance = new Random().nextInt(REGULAR_EVENT_PROBABILTY_FACTOR + MAKE_SHORT__EVENT_PROBABILTY_FACTOR + ADD_DEATH_BLOCK_EVENT_PROBABILTY_FACTOR);
        if(chance < MAKE_SHORT__EVENT_PROBABILTY_FACTOR) {
            createMakeShortEvent();
        }
        else if (chance < ADD_DEATH_BLOCK_EVENT_PROBABILTY_FACTOR + MAKE_SHORT__EVENT_HAPPENING) {
            createAddDeathBlocksEvent();
        }
        else {
            createRegularBonusEvent();
        }
    }
    public void createRegularBonusEvent() {
        BuildingBlock eventBlock = gameGrid.getRandomBlock();
        Event event = new RegularBonusEvent(eventBlock,REGULAR_EVENT_COLOR, LONGEVITY_MIN + new Random().nextInt(LONGEVITY_MAX), REGULAR_EVENT_HAPPENING);
        eventList.add(event);
    }
    public void createMakeShortEvent() {
        BuildingBlock eventBlock = gameGrid.getRandomBlock();
        Event event = new MakeShortEvent(eventBlock,MAKE_SHORT_EVENT_COLOR, LONGEVITY_MIN + new Random().nextInt(LONGEVITY_MAX), MAKE_SHORT__EVENT_HAPPENING);
        eventList.add(event);
    }
    public void createAddDeathBlocksEvent() {
        BuildingBlock eventBlock = gameGrid.getRandomBlock();
        Event event = new AddDeathBlocksEvent (eventBlock,ADD_DEATH_BLOCK_EVENT_COLOR, LONGEVITY_MIN + new Random().nextInt(LONGEVITY_MAX), ADD_DEATH_BLOCK_EVENT_HAPPENING);
        eventList.add(event);
    }
    public void destroyEvents() {
        Iterator itr = eventList.iterator();
        while(itr.hasNext()) {
            Event event = (Event)itr.next();
            if(event.isToRemove()) {
                itr.remove();
            }
        }
    }
}
