/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesnake;

import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

/**
 *
 * @author johanwendt
 */
public class ScoreEffect {
    
    public void ScoreEffect() {
        
    }
    
    public Blend getEffect(Color color) {
        
        Blend blend1 = new Blend();
        blend1.setMode(BlendMode.MULTIPLY);

        DropShadow dropShadow1 = new DropShadow();
        dropShadow1.setColor(color);
        dropShadow1.setRadius(20);
        dropShadow1.setSpread(0.2);

        Blend blend3 = new Blend();
        blend3.setMode(BlendMode.MULTIPLY);

        InnerShadow innerShadow1 = new InnerShadow();
        innerShadow1.setColor(Color.web("#FF0000"));
        innerShadow1.setRadius(9);
        innerShadow1.setChoke(0.8);
        blend3.setBottomInput(innerShadow1);

        InnerShadow innerShadow2 = new InnerShadow();
        innerShadow2.setColor(Color.web("#CC0066"));
        innerShadow2.setRadius(5);
        innerShadow2.setChoke(0.4);
        blend3.setTopInput(innerShadow2);

        Blend blend2 = new Blend();
        blend2.setMode(BlendMode.MULTIPLY);
        blend2.setBottomInput(dropShadow1);
        blend2.setTopInput(blend3);

        blend1.setTopInput(blend2);

        return blend1;
    }
}
