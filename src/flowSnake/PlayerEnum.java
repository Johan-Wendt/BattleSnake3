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

/**
 *
 * @author johanwendt
 */
public enum PlayerEnum {
    PLAYER_ONE (1, "Flow Free", "flowFree.png", GameEngine.RIGHT, 2, "death.wav", "notificationLouder.wav", 0.5, Color.web("#01c9f3")),
    PLAYER_TWO (2, "Flow Fixed", "flowFixed.png", GameEngine.LEFT, 4, "death.wav", "oldPhone.wav", 0.3, Color.web("#ffc02b")),
    PLAYER_THREE (3, "Flow Mobile", "flowMobile.png", GameEngine.UP, 6, "death.wav", "silenceMobile.wav", 0.3, Color.web("#77ad37")),
    PLAYER_FOUR (4, "PBX-monster", "pbx.png", GameEngine.DOWN, 8, "death.wav", "mikMik.wav", 0.3, Color.web("#594f63"));
    
    private final int number;
    private final String name;
    private final String playerImage;
    private final int startDirection;
    private final int moveTurn;
    private final String deathSound;
    private final String bonusSound;
    private final double bonusVolume;
    private final Color scoreColor;

    PlayerEnum(int playerNumber, String playerName, String playerImage, int startDirection, int moveTurn, String deathSound, String bonusSound, double bonusVolume, Color scoreColor) {
        this.number = playerNumber;
        this.name = playerName;
        this.playerImage = playerImage;
        this.startDirection = startDirection;
        this.moveTurn = moveTurn;
        this.deathSound = deathSound;
        this.bonusSound = bonusSound;
        this.bonusVolume = bonusVolume;
        this.scoreColor = scoreColor;
    }
    public int getNumber() {
        return number;
    }
    public String getName() {
        return name;
    }
    public String getImage() {
        return playerImage;
    }
    public int getStartDirection() {
        return startDirection;
    }
    public int getMoveTurn() {
        return moveTurn;
    }
    public String getDeathSound() {
        return deathSound;
    }
    public String getBonusSound() {
        return bonusSound;
    }
    public double getBonusVolume() {
        return bonusVolume;
    }
    public Color getScoreColor() {
        return scoreColor;
    }
}
