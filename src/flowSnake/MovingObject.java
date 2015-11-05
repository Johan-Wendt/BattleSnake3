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

import java.util.HashSet;
import java.util.Iterator;
import javafx.scene.media.AudioClip;

/**
 *
 * @author johanwendt
 */
public class MovingObject {
    private int turn = -1;
    private int currentLocation;
    private int currentDirection;
    private int objectSlowness;
    private static final HashSet<MovingObject> movingObjects= new HashSet<>();
    private boolean isToBeRemoved = false;
    private final AudioClip bounceSound;
    private final AudioClip hitSound;
    
    public MovingObject(int currentLocation,int currentDirection, int objectSlowness) {
        this.currentLocation = currentLocation;
        this.currentDirection = currentDirection;
        this.objectSlowness = objectSlowness - 1;
       // if(!BuildingBlock.getBlock(currentLocation + currentDirection).isDeathBlockIrrevertible()) this.currentLocation += 2 * currentDirection;
        movingObjects.add(this);
        BuildingBlock.getBlock(this.currentLocation).setMovableObjectBlock(currentDirection);
        
        bounceSound = new AudioClip(getClass().getResource("bounce.wav").toExternalForm());
        hitSound = new AudioClip(getClass().getResource("explosion.wav").toExternalForm());
    }
    public void move() {

        if(turn % objectSlowness == 0) {
            checkToBeRemoved();
            if(!isToBeRemoved) {
                int destination = currentLocation + currentDirection;
            //    if(BuildingBlock.getBlock(destination).isDeathBlockIrrevertible() || BuildingBlock.getBlock(destination).isMovableObject()) {
            //        currentDirection = -currentDirection;
            //        bounceSound.play();
            //        turn++;
           //         return;
            //    }
                if((BuildingBlock.getBlock(destination).getOccupatorDetails() != null)) {
                    hitPlayer(destination);
                    turn++;
                    return;
                }
                BuildingBlock moveTo = BuildingBlock.getBlock(destination);

                BuildingBlock.getBlock(currentLocation).removeMovableObject();
                moveTo.setMovableObjectBlock(currentDirection);

                currentLocation = destination;
            }
        }
        turn ++;
    }
    public static HashSet getMovingObjects() {
        return movingObjects;
    }
    private void checkToBeRemoved() {
        if(!BuildingBlock.getBlock(currentLocation).isMovableObject() || isToBeRemoved) {
            BuildingBlock.getBlock(currentLocation).removeMovableObject();
            removeFromMovingObjects();
        }
    }
    private void removeFromMovingObjects() {

        Iterator<MovingObject> it = movingObjects.iterator();

        while(it.hasNext()){
            MovingObject movingObject = it.next();
            if(movingObject.equals(this)) {
                it.remove();
            }
        }

    }
    private void hitPlayer(int destination) {
        hitSound.play();
        BuildingBlock hitBlock = BuildingBlock.getBlock(destination);
        hitBlock.explosion();
     //   GameEngine.killPlayer(hitBlock.getOccupatorDetails(), destination);
        BuildingBlock.getBlock(currentLocation).removeMovableObject();
        isToBeRemoved = true;
    }
}
