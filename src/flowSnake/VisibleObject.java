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
public abstract class VisibleObject {
    private VisibleObjects details;
    private int length;
    
    public VisibleObject(VisibleObjects details) {
        this.details = details;
    }
    public VisibleObjects getDetails() {
        return details;
    }
    protected boolean occupy(BuildingBlock block) {
        return block.Occupy(details, this, getTurn());
    }
    public abstract int getDirection();
    public abstract int getSpeed();
   // public abstract Enum getExtendedDetails();
    
   // public abstract int getActionNumber();
    public void setMayMove(boolean mayMove) {
        
    }
    public int getLength() {
        return length;
    }
    public void setLength(int newLength) {
        length = newLength;
    }
    public int getTurn() {
        return -1;
    }
}
