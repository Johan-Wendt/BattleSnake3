/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesnake3;

import javafx.scene.paint.Color;

/**
 *
 * @author johanwendt
 */
public class AddDeathBlocksBonus extends Bonus{

    public AddDeathBlocksBonus(BuildingBlock bonusBlock, Color eventColor, int longevity, int eventHappening) {
        super(bonusBlock, eventColor, longevity, eventHappening);
    }
    @Override
    public void executeBonus(GameGrid gameGrid) {
        for(int i = 0; i < 3; i++) {
            BuildingBlock deathBlock = gameGrid.getRandomBlock();
            deathBlock.setIsDeathBlockIrreveritble();
        }
    }
    
}
