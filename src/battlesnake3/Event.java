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
public class Event {
    private double longevity;
    private double startTime;
    private boolean isToRemove = false;
    private boolean isTaken = false;
    private Color revertColor;
    private BuildingBlock eventBlock;
    private String eventHappening = "Regular";
    
    public Event(BuildingBlock eventBlock,Color color, Color revertColor, int longevity) {
        this.longevity = longevity;
        this.eventBlock = eventBlock;
        this.revertColor = revertColor;
        eventBlock.getRectangle().setFill(color);
        startTime = System.currentTimeMillis();
    }
    public boolean isToRemove() {
        if(System.currentTimeMillis() - startTime > longevity) {
            isToRemove = true;
            if(!isTaken) eventBlock.getRectangle().setFill(revertColor);
        }
        return isToRemove;
    }
    public String getEventHappening() {
        return eventHappening; 
    }
    public int getEventId() {
        return eventBlock.getBlockId();
    }
    public void isTaken() {
        isTaken = true;
        isToRemove = true;
    }
}
