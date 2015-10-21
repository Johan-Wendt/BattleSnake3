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


import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *This class creates the stage that lets the player initiate a new game.
 * @author johanwendt
 */
public class RulesStage extends PopUp{
 //   private final static ComboBox chooseNumberOfPlayers = new ComboBox<>();
    private static final Label winnerInfo = new Label();
    

    public RulesStage(String title, String okMessage) {
        super(title, okMessage, PopUp.STANDARD_PANE_WIDTH);
        setUpLowerPart();
        addExtraPane(setUpBonusInformation());
    }
    @Override
    protected void setOnActions() {
        getOkButton().setOnAction(e -> {
            showPopUp(false);
        }); 
    }

    public GridPane setUpBonusInformation() {
        
        GridPane innerBonusPane = new GridPane();
        
        Color textColor = UserInterface.infoColor();
        
        Text regularBonusText = new Text(BonusEnum.REGULAR_BONUS.getBonusDescription());  
        Text makeShortBonusText = new Text(BonusEnum.MAKE_SHORT_BONUS.getBonusDescription());  
        Text addDeathBlockBonusText = new Text(BonusEnum.ADD_DEATH_BLOCK_BONUS.getBonusDescription());
        Text deathBlockText = new Text(BonusEnum.DEATH_BLOCK.getBonusDescription());

        regularBonusText.setFill(textColor);
        makeShortBonusText.setFill(textColor);
        addDeathBlockBonusText.setFill(textColor);
        deathBlockText.setFill(textColor);
                
        Font textFont = new Font("Impact", 25);
        regularBonusText.setFont(textFont);
        makeShortBonusText.setFont(textFont);
        addDeathBlockBonusText.setFont(textFont);
        deathBlockText.setFont(textFont);
        
        //Create the rectangles that show what type of bonus the description is about.
        Rectangle regularBonusColor = new Rectangle(UserInterface.getBlockSize() * 2.5, UserInterface.getBlockSize() * 2.5);
        Image imageRegular = new Image(getClass().getResourceAsStream(BonusEnum.REGULAR_BONUS.getBonusImage()));
        ImagePattern imagePatternRegular = new ImagePattern(imageRegular);  
        regularBonusColor.setFill(imagePatternRegular);
        
        Rectangle makeShortBonusColor = new Rectangle(UserInterface.getBlockSize() * 2.5, UserInterface.getBlockSize() * 2.5);
        Image imageMakeShort = new Image(getClass().getResourceAsStream(BonusEnum.MAKE_SHORT_BONUS.getBonusImage()));
        ImagePattern imagePatternMakeShort = new ImagePattern(imageMakeShort);  
        makeShortBonusColor.setFill(imagePatternMakeShort);
        
        Rectangle addDeathBlockBonusColor = new Rectangle(UserInterface.getBlockSize() * 2.5, UserInterface.getBlockSize() * 2.5);
        Image imageAddDeath = new Image(getClass().getResourceAsStream(BonusEnum.ADD_DEATH_BLOCK_BONUS.getBonusImage()));
        ImagePattern imagePatternAddDeath = new ImagePattern(imageAddDeath);  
        addDeathBlockBonusColor.setFill(imagePatternAddDeath);
        
        Rectangle deathBlockColor = new Rectangle(UserInterface.getBlockSize() * 2.5, UserInterface.getBlockSize() * 2.5);
        Image imageDeath = new Image(getClass().getResourceAsStream(BonusEnum.DEATH_BLOCK.getBonusImage()));
        ImagePattern imagePatternDeath = new ImagePattern(imageDeath);  
        deathBlockColor.setFill(imagePatternDeath);
        
        //Add the bonus information to the pane.
        
        
        innerBonusPane.add(regularBonusColor, 0, 0);
        innerBonusPane.add(makeShortBonusColor, 0, 1);
        innerBonusPane.add(addDeathBlockBonusColor, 0, 2);
        innerBonusPane.add(deathBlockColor, 0, 3);
        
        innerBonusPane.add(regularBonusText, 1, 0);
        innerBonusPane.add(makeShortBonusText, 1, 1);
        innerBonusPane.add(addDeathBlockBonusText, 1, 2);
        innerBonusPane.add(deathBlockText, 1, 3);
        
        //Adjust positioning
        innerBonusPane.setVgap(PopUp.getStandardPadding() / 2);
        innerBonusPane.setHgap(PopUp.getStandardPadding());
        innerBonusPane.setPadding(new Insets(PopUp.getStandardPadding()));
        
        return innerBonusPane;
    }  
}
