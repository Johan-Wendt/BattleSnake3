/**
 * @author johanwendt
 */
package battlesnake;

import javafx.scene.paint.Color;

/**
 * Creates a bonus that makes the player short.
 */
public class MakeShortBonus extends Bonus {

    public MakeShortBonus(BuildingBlock bonusBlock, Color eventColor, int longevity, int eventHappening) {
        super(bonusBlock, eventColor, longevity, eventHappening);
    }
}