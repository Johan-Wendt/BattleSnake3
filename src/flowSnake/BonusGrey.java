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

/**
 *
 * @author johanwendt
 */
public class BonusGrey extends Bonus {
     /**
     * Creates a bonus and places it in the field.
     * @param bonusBlock The Building block to be changed to a Bonus. If the color of this block is 
     * not the GameGrid color the bonus is removed directly.
     * @param bonusColor The color of this Bonus.
     * @param lifespan The how long this bonus should be on the GameGrid.
     * @param bonusHappening Int describing what happens when the bonus is taken.
     */
    public BonusGrey(BuildingBlock bonusBlock, BonusEnum bonusEnum, int lifespan) {
        super(bonusBlock, bonusEnum, lifespan);
        bonusBlock.setDeathBlockIrreveritble();
    }
    
}
