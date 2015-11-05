/**
 * @author johanwendt
 */
package flowSnake;

import javafx.scene.paint.Color;

/**
 * Creates a bonus that makes the player short.
 */
public class MakeFasterBonus extends PowerupBonus {

    public MakeFasterBonus(VisibleObjects details, BuildingBlock bonusBlock, BonusEnum bonusEnum, int lifespan) {
        super(details, bonusBlock, bonusEnum, lifespan);
    }
}