/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesnake;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

/**
 *
 * @author johanwendt
 */
public class QuitStage extends PopUp {
    
    public QuitStage(String title, String infoText, String okMessage, String cancelMessage) {
        super(title, infoText, okMessage, cancelMessage);
        setOnOkKeyBehaviour();
        setBackgroundImage("noose.jpg");
    }
    private void setOnOkKeyBehaviour() {
        getOkButton().setOnAction(a -> {
            System.exit(0);
        });
        getCancelButton().setOnAction(a -> {
            GameEngine.setPaused(false);
            showPopUp(false);
        });

        getPopUpPane().setOnKeyPressed(k -> {
            if(k.equals(KeyCode.ESCAPE)) {
                showPopUp(false);
            }
        });
    }
    private void addPicture(String name) {
        Image image = new Image(getClass().getResourceAsStream(name));
        ImageView picture = new ImageView(image);
        picture.setFitHeight(150);
        addInfoPicture(picture);
    }
    private void setBackgroundImage(String name) {
        setBackGround(name);
    } 
}
