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

import javafx.application.Platform;
import javafx.scene.media.AudioClip;

/**
 *
 * @author johanwendt
 */
public class ShotShootBonus extends MovableObject {
    private final AudioClip bounceSound;
    private final AudioClip hitSound;

    public ShotShootBonus(VisibleObjects details) {
        super(details);
        
        bounceSound = new AudioClip(getClass().getResource("bounce.wav").toExternalForm());
        hitSound = new AudioClip(getClass().getResource("explosion.wav").toExternalForm());
    }
    public ShotShootBonus(VisibleObjects details, BuildingBlock location, int direction, int speed) {
        super(details);
        setLocation(location);
        setDirection(direction);
      //  occupy(location);
        setSpeed(speed);
        setMayMove(true);
        setLength(1);
        GameEngine.addProjectile(this);
        
        bounceSound = new AudioClip(getClass().getResource("bounce.wav").toExternalForm());
        hitSound = new AudioClip(getClass().getResource("explosion.wav").toExternalForm());
    }

    @Override
    public void move() {
        if(getMayMove()) {
            BuildingBlock moveTo = getLocation().getAdjacent(getDirection());
            if(moveTo.getOccupant() != null) {
                handleCrash(moveTo);
            }
            if(occupy(moveTo) && getMayMove()) {
                moveTo.setInSmoothMotion();
                setMayMove(false);
                setLocation(moveTo);
            }
        }
    }

    @Override
    public void handleCrash(BuildingBlock block) {
        VisibleObject crashObject = block.getOccupant() ;
        if(crashObject instanceof Bonus || crashObject instanceof Death || crashObject instanceof ShotShootBonus) {
            bounceSound.play();
            setDirection(-getDirection());
          //  block = block.getAdjacent(getDirection());
            
        }
        if(crashObject instanceof Player) {
            block.explodePlayer();
           // Platform.runLater(() -> {
            removeShot();
          //  });
           // removeShot();
        }
    }
    private void removeShot() {
        setMayMove(false);
       // GameEngine.removeProjectile(this);
        Platform.runLater(() -> {
                GameEngine.removeProjectile(this);
            });
    }
    /**
    else if(crashObject instanceof Bonus) {
        handleBonuses((crashObject.getDetails().getActionNumber()), block);
    }
    * **/
}
