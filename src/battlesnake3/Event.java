/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesnake3;

import javafx.scene.paint.Color;
/**
 *
 * @author johanwendt
 */
public abstract class Event {
    private double longevity;
    private int eventHappening;
    private double startTime;
    private boolean isToRemove = false;
    private boolean isTaken = false;
    private BuildingBlock eventBlock;
    
    public Event(BuildingBlock eventBlock, Color eventColor, int longevity, int eventHappening) {
        this.longevity = longevity;
        this.eventBlock = eventBlock;
        this.eventHappening = eventHappening;
        if(eventBlock.getBlockColor().equals(GameGrid.GAMEGRID_COLOR)) {
            eventBlock.setBlockColor(eventColor);
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
            if(!isTaken) eventBlock.setBlockColor(GameGrid.GAMEGRID_COLOR);
        }
        return isToRemove;
    }
    public int getEventId() {
        return eventBlock.getBlockId();
    }
    public int getEventHappening() {
        return eventHappening;
    }
    public void isTaken() {
        isTaken = true;
        isToRemove = true;
    }
    public void executeEvent(GameGrid gameGrid) {
    }
}
