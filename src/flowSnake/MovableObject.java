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
public abstract class MovableObject extends VisibleObject {
    private int direction;
    private BuildingBlock location;
    private int speed;
    private boolean mayMove;

    public MovableObject(VisibleObjects details) {
        super(details);
    }
    public abstract void move();
    public void setDirection(int newDirection) {
        direction = newDirection;
    }
    @Override
    public int getDirection() {
        return direction;
    }
    public void setLocation(BuildingBlock newLocation) {
        location = newLocation;
    }
    public BuildingBlock getLocation() {
        return location;
    }
    public void setSpeed(int newSpeed) {
        speed = newSpeed;
    }
    @Override
    public int getSpeed() {
        return speed;
    }
    public void setMayMove(boolean mayMove) {
        this.mayMove = mayMove;
    }
    public boolean getMayMove() {
        return mayMove;
    }
    public abstract void handleCrash(BuildingBlock block);
    
}
