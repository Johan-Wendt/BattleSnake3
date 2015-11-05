/*
 * Copyright (C) 2015 johanwendt
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package flowSnake;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

/**
 *
 * @author johanwendt
 */
public enum BonusEnum {
    MAKE_LONG_BONUS (0, "ADD A LICENSE - Become longer", 10),
    MAKE_FASTER_BONUS (1, "ADD MORE FLOW - Become faster", 10),
    SHOOT_BONUS (2, "FLOW SHOT - Shoot you opponents", 5),
    DEATH_BLOCK (3, "LOOSE A LICENSE - Become shorter", 10);
    
    private final String bonusDescription;
    private final int bonusProbabilityFactor;
    private final int bonusNumber;
    
    
    BonusEnum(int bonusNumber , String bonusDescription, int bonusProbabilityFactor) {
    this.bonusDescription = bonusDescription;
    this.bonusProbabilityFactor = bonusProbabilityFactor;
    this.bonusNumber = bonusNumber;
    }

    public String getBonusDescription() {
        return bonusDescription;
    }

    public int getBonusProbabilityFactor() {
        return bonusProbabilityFactor;
    }
    public int getBonusNumber() {
        return bonusNumber;
    }
}
