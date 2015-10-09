/**
 * @author johanwendt
 */
package flowSnake;

import javafx.scene.paint.Color;

/**
 * Creates a bonus that makes the player short.
 */
public class MakeShortBonus extends Bonus {

    public MakeShortBonus(BuildingBlock bonusBlock, BonusEnum bonusEnum, int lifespan) {
        super(bonusBlock, bonusEnum, lifespan);
    }
}