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
public class MakeShortEvent extends Event {

    public MakeShortEvent(BuildingBlock eventBlock, Color eventColor, int longevity, int eventHappening) {
        super(eventBlock, eventColor, longevity, eventHappening);
    }
}