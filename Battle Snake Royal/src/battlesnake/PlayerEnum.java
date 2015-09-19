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
public enum PlayerEnum {
    PLAYER_ONE (1, "Moon Unit", Color.web("#B200B2"), GameEngine.RIGHT),
    PLAYER_TWO (2, "Daisy Boo", Color.web("#66FF33"), GameEngine.LEFT),
    PLAYER_THREE (3, "Bronx Mowgli", Color.web("#E68A00"), GameEngine.UP),
    PLAYER_FOUR (4, "Moxie CrimeFighter", Color.web("#00FFFF"), GameEngine.DOWN);
    
    private final int number;
    private final String name;
    private final Color color;
    private final int startDirection;

    PlayerEnum(int playerNumber, String playerName, Color playerColor, int startDirection) {
        this.number = playerNumber;
        this.name = playerName;
        this.color = playerColor;
        this.startDirection = startDirection;
    }
    public int getNumber() {
        return number;
    }
    public String getName() {
        return name;
    }
    public Color getColor() {
        return color;
    }
    public int getStartDirection() {
        return startDirection;
    }
}
