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
    REGULAR_BONUS (0, "bbGreen.png", "ADD A LICENSE", 10, BuildingBlock.greenBonusImage()),
    MAKE_SHORT_BONUS (1, "bbYellow.png", "ADD MORE FLOW", 10, BuildingBlock.yellowBonusImage()),
    ADD_DEATH_BLOCK_BONUS (2, "bbRed.png", "LOOSE FLOW", 10, BuildingBlock.redBonusImage()),
    DEATH_BLOCK (3, "bbGrey.png", "LOOSE A LICENSE", 10, BuildingBlock.greyBonusImage());
    
    private final String bonusImage;
    private final String bonusDescription;
    private final int bonusProbabilityFactor;
    private final int bonusNumber;
    private ImagePattern blockImage;
    
    
    BonusEnum(int bonusNumber, String bonusImage, String bonusDescription, int bonusProbabilityFactor, ImagePattern blockImage) {
    this.bonusImage = bonusImage;
    this.bonusDescription = bonusDescription;
    this.bonusProbabilityFactor = bonusProbabilityFactor;
    this.bonusNumber = bonusNumber;
    this.blockImage = blockImage;
    }

    public String getBonusDescription() {
        return bonusDescription;
    }
    public String getBonusImage() {
        return bonusImage;
    }
    public int getBonusProbabilityFactor() {
        return bonusProbabilityFactor;
    }
    public int getBonusNumber() {
        return bonusNumber;
    }
    public ImagePattern getBlockImage() {
        return blockImage;
    }
    public void setBlockImage(ImagePattern image) {
        blockImage = image;
    }
}
