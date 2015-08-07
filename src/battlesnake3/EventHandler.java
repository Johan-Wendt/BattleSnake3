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
    private static double EVENT_PROBABILITY = 0.03;
    private GameGrid gameGrid;
    
    public EventHandler(GameGrid gameGrid) {
        this.gameGrid = gameGrid;
        
    }
    //Mutators
    public void eventRound() {
        if(EVENT_PROBABILITY > Math.random()) createEvent();
        destroyEvents();
    }
    public void createEvent() { 
        BuildingBlock eventBlock = gameGrid.getRandomBlock();
        Event event = new Event(eventBlock, Color.LIMEGREEN, gameGrid.getColor(), LONGEVITY_MIN + new Random().nextInt(LONGEVITY_MAX));
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
