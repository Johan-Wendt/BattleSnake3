/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesnake;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.xml.soap.Text;

/**
 *
 * @author johanwendt
 */
public abstract class PopUp {
    
    private final Stage pupUpStage = new Stage();
    private Scene popUpScene;
    private final Label infoLabelText = new Label();
    private Label extraLabel = new Label();
    private final Button okButton = new Button();
    private final Button cancelButton = new Button();
    
    
    private final VBox popUpPane = new VBox();
    private final VBox popUpPaneInnerUpper = new VBox();
    private final HBox popUpPaneInnerLower = new HBox();
    private HBox extraPane = new HBox();
    
    private final static int PANE_HEIGHT = 330;
    private final static int INNER_UPPER_HEIGHT = PANE_HEIGHT - 50;
    private final static int INNER_LOWER_HEIGHT = PANE_HEIGHT - INNER_UPPER_HEIGHT;
    private final static int STANDARD_PADDING = 20;
    
    
    public PopUp(String title, String infoText, String okMessage) {
        setUpOuterPart(title);
        setUpLowerPart();
        setUpExtraPane();
        setUpUpperPart(infoText);
        addOkButton(okMessage);
        standardOnStageKeyBehaviour();
        //popUpScene.getStylesheets().add(BattleSnake.class.getResource("BattleSnake.css").toExternalForm());
    }
    public PopUp(String title, String infoText, String okMessage, String cancelMessage) {
        setUpOuterPart(title);
        setUpLowerPart();
        setUpExtraPane();
        setUpUpperPart(infoText);
        addOkButton(okMessage);
        addCancelButton(cancelMessage);
        standardOnStageKeyBehaviour();
        //popUpScene.getStylesheets().add(BattleSnake.class.getResource("BattleSnake.css").toExternalForm());
    }
    private void setUpOuterPart(String title) {
        pupUpStage.setTitle(title);
        pupUpStage.setAlwaysOnTop(true);
        pupUpStage.initOwner(UserInterface.getBattleStage());
        setBackGround("images.jpg");
                
        popUpPane.getChildren().add(popUpPaneInnerUpper);
        popUpPane.getChildren().add(extraPane);
        popUpPane.getChildren().add(popUpPaneInnerLower);
        
        popUpScene = new Scene(popUpPane, 600, PANE_HEIGHT);
        pupUpStage.setScene(popUpScene);
        pupUpStage.setResizable(false);
    }
    protected void setUpUpperPart(String infoText) { 
        infoLabelText.setText(infoText);
        infoLabelText.setWrapText(true);
        infoLabelText.setStyle("-fx-font-size: 25px;");
        //infoLabelText.setEffect(new Bloom());
        popUpPaneInnerUpper.setPadding(new Insets(STANDARD_PADDING / 2, STANDARD_PADDING, STANDARD_PADDING / 4, STANDARD_PADDING));
        VBox.setMargin(infoLabelText, new Insets(0, 0, STANDARD_PADDING, 0));
        popUpPaneInnerUpper.setPrefHeight(INNER_UPPER_HEIGHT);
        popUpPaneInnerUpper.getChildren().add(infoLabelText);
   
    }
    protected void setUpExtraPane() {
        extraPane.setAlignment(Pos.CENTER);
        extraPane.getChildren().add(extraLabel);
    }
    protected void setUpLowerPart() {
        popUpPaneInnerLower.setPrefHeight(INNER_LOWER_HEIGHT);
        popUpPaneInnerLower.setAlignment(Pos.CENTER_RIGHT);
        popUpPaneInnerLower.setPadding(new Insets(STANDARD_PADDING));
        HBox.setMargin(okButton, new Insets(0, STANDARD_PADDING / 4, 0, STANDARD_PADDING / 4));

    }
    protected  void setInfoText(String infoText) {
        getInfoLabel().setText(infoText);
    }       
    protected void showPopUp(boolean show) {
        if(show) pupUpStage.show();
        else pupUpStage.hide();
    }
    protected void addOkButton(String okMessage) {
        okButton.setText(okMessage);
       // okButton.setDisable(false);
        okButton.setPrefWidth(okMessage.length() * 9);
        okButton.setStyle("-fx-base: #009933;");
        okButton.setPadding(new Insets(STANDARD_PADDING / 2));
        popUpPaneInnerLower.getChildren().add(getOkButton());
    }
    protected void addCancelButton(String cancelMessage) { 
        cancelButton.setText(cancelMessage);
        cancelButton.setCancelButton(true);
        //cancelButton.setDisable(false);
        cancelButton.setPrefWidth(cancelMessage.length() * 9);
        cancelButton.setStyle("-fx-base: #FF3300;");
        cancelButton.setPadding(new Insets(STANDARD_PADDING / 2));
        popUpPaneInnerLower.getChildren().add(getCancelButton());
    }
    protected void addExtraButton(Control extraButton) {
        extraButton.setStyle("-fx-base: #0000FF; -fx-font-size: 15px;");
        extraButton.setPadding(new Insets(STANDARD_PADDING / 2 - 4));
        popUpPaneInnerLower.getChildren().add(0, extraButton);
    }
    protected Label getInfoLabel() {
        return infoLabelText;
    }
    protected Button getOkButton() {
        return okButton;
    }
    protected Button getCancelButton() {
        return cancelButton;
    }
    protected VBox getPopUpPane() {
        return popUpPane;
    }
    protected VBox getPopUpPaneInnerUpper() {
        return popUpPaneInnerUpper;
    }
    protected HBox getPopUpPaneInnerLower() {
        return popUpPaneInnerLower;
    }
    protected void standardOnStageKeyBehaviour() {
        okButton.defaultButtonProperty().bind(okButton.focusedProperty());
        cancelButton.defaultButtonProperty().bind(cancelButton.focusedProperty());
        popUpPane.setOnKeyPressed(k -> {
            if(k.getCode().equals(KeyCode.ESCAPE)) {
                if(!cancelButton.isDisabled()) {
                    GameEngine.setPaused(false);
                    pupUpStage.hide();
                }
        }
        });
    }
    protected void addInfoPicture(ImageView picture) {
        extraLabel.setGraphic(picture);
    }
    protected Label getExtraLabel() {
        return extraLabel;
    }
    protected void setBackGround(String name) {
        Image image = new Image(getClass().getResourceAsStream(name));
        // new BackgroundSize(width, height, widthAsPercentage, heightAsPercentage, contain, cover)
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        // new BackgroundImage(image, repeatX, repeatY, position, size)
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, backgroundSize);
        // new Background(images...)
        Background background = new Background(backgroundImage);
        popUpPane.setBackground(background);
    }
}
