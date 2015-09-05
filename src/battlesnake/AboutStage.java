/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesnake;

/**
 *
 * @author johanwendt
 */
public class AboutStage extends PopUp{
    /*
    private static final VBox aboutPane = new VBox();
    private static Scene aboutScene;
    private static Stage aboutStage;
    */
    
    public AboutStage(String title, String infoText, String okMessage) {
        super(title, infoText, okMessage);
        
        getOkButton().setOnAction(e -> {
            GameEngine.setPaused(false);
            showPopUp(false);
        });
    }
                
}
