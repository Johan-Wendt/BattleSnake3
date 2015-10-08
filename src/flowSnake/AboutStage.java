
package flowSnake;


/**
 * @author johanwendt
 * This class creates a stage with information about the game.
 */
public class AboutStage extends PopUp{

    /**
     *Creates an aboutstage with a title, informationtext about the game and
     * a confirmationbutton to return to the game.
     * @param title Title of the stage.
     * @param infoText Inormation about the game.
     * @param okMessage Text to display on the confirmationbutton.
     */
    public AboutStage(String title, String infoText, String okMessage, int paneWidth) {
        super(title, infoText, okMessage, paneWidth); 
    }
    @Override
    protected void setOnActions() {
        getOkButton().setOnAction(e -> {
            showPopUp(false);
        });
    }
}
