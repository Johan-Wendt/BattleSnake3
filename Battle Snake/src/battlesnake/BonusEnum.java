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
package battlesnake;

import javafx.scene.paint.Color;

/**
 *
 * @author johanwendt
 */
public enum BonusEnum {
    REGULAR_BONUS (0, Color.web("#FF0000"), "Makes the player longer and faster.", 10),
    MAKE_SHORT_BONUS (1, Color.web("#009933"), "Makes the player short.", 1),
    ADD_DEATH_BLOCK_BONUS (2, Color.web("#FFFF00"), "Adds deathblocks to the field.", 18);
    
    private final Color bonusColor;
    private final String bonusDescription;
    private final int bonusProbabilityFactor;
    
    
    BonusEnum(int bonusHappening, Color bonusColor, String bonusDescription, int bonusProbabilityFactor) {
    this.bonusColor = bonusColor;
    this.bonusDescription = bonusDescription;
    this.bonusProbabilityFactor = bonusProbabilityFactor;
    }

    public String getBonusDescription() {
        return bonusDescription;
    }
    public Color getBonusColor() {
        return bonusColor;
    }
    public int getBonusProbabilityFactor() {
        return bonusProbabilityFactor;
    }
}
