/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesnake;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    
    protected final static int STANDARD_PANE_WIDTH = 600;
    private final static int STANDARD_PANE_HEIGHT = 330;
    private final static int INNER_UPPER_HEIGHT = STANDARD_PANE_HEIGHT - 50;
    private final static int INNER_LOWER_HEIGHT = STANDARD_PANE_HEIGHT - INNER_UPPER_HEIGHT;
    private final static int STANDARD_PADDING = 20;
    
    protected final static int STANDARD_BUTTON_HEIGHT = 70;
    
    public PopUp(String title, String okMessage, int paneWidth) {
        setUpOuterPart(title, paneWidth);
        setUpLowerPart();
        setUpExtraPane();
        setUpUpperPart();
        addOkButton(okMessage);
        standardOnStageKeyBehaviour();
        setOnActions();
        okButton.requestFocus();
    }
    public PopUp(String title, String infoText, String okMessage, int paneWidth) {
        setUpOuterPart(title, paneWidth);
        setUpLowerPart();
        setUpExtraPane();
        setUpUpperPart();
        setUpInfoLabel(infoText);
        addOkButton(okMessage);
        standardOnStageKeyBehaviour();
        setOnActions();
        okButton.requestFocus();
    }
    public PopUp(String title, String infoText, String okMessage, String cancelMessage, int paneWidth) {
        setUpOuterPart(title, paneWidth);
        setUpLowerPart();
        setUpExtraPane();
        setUpUpperPart();
        setUpInfoLabel(infoText);
        addOkButton(okMessage);
        addCancelButton(cancelMessage);
        standardOnStageKeyBehaviour();
        setOnActions();
        okButton.requestFocus();
    }
    private void setUpOuterPart(String title, int paneWidth) {
        pupUpStage.setTitle(title);
        pupUpStage.setAlwaysOnTop(true);
        pupUpStage.initOwner(UserInterface.getBattleStage());
        pupUpStage.setOnCloseRequest(c -> {
            showPopUp(false);
        });
        setBackGround("images.jpg", true);
                
        popUpPane.getChildren().add(popUpPaneInnerUpper);
        popUpPane.getChildren().add(extraPane);
        popUpPane.getChildren().add(popUpPaneInnerLower);
        
        popUpScene = new Scene(popUpPane, paneWidth, STANDARD_PANE_HEIGHT);
        pupUpStage.setScene(popUpScene);
        pupUpStage.setResizable(false);
    }
    protected void setUpUpperPart() { 
        popUpPaneInnerUpper.setPadding(new Insets(STANDARD_PADDING / 2, STANDARD_PADDING, STANDARD_PADDING / 4, STANDARD_PADDING));
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
    protected void setUpInfoLabel(String infoText) {
        infoLabelText.setText(infoText);
        infoLabelText.setWrapText(true);
        infoLabelText.setStyle("-fx-font-size: 25px;");
        VBox.setMargin(infoLabelText, new Insets(0, 0, STANDARD_PADDING, 0));
        
    }
    protected  void setInfoText(String infoText) {
        getInfoLabel().setText(infoText);
    }       
    protected void showPopUp(boolean show) {
        if(show) {
            pupUpStage.show();
            GameEngine.popUpOpened();
        }
        else {
            pupUpStage.hide();
            GameEngine.popUpClosed();
        }
    }
    protected void addOkButton(String okMessage) {
        okButton.setText(okMessage);
        okButton.setPrefWidth(okMessage.length() * 9);
        okButton.setStyle("-fx-base: #009933;");
        okButton.setPrefHeight(STANDARD_BUTTON_HEIGHT);
        popUpPaneInnerLower.getChildren().add(getOkButton());
    }
    protected void addCancelButton(String cancelMessage) { 
        cancelButton.setText(cancelMessage);
        cancelButton.setCancelButton(true);
        cancelButton.setPrefWidth(cancelMessage.length() * 9);
        cancelButton.setStyle("-fx-base: #FF3300;");
        cancelButton.setPrefHeight(STANDARD_BUTTON_HEIGHT);
        popUpPaneInnerLower.getChildren().add(getCancelButton());
    }
    protected void addExtraButton(Control extraButton) {
        extraButton.setStyle("-fx-base: #0000FF; -fx-font-size: 15px;");
        extraButton.setPrefHeight(STANDARD_BUTTON_HEIGHT);
        popUpPaneInnerLower.getChildren().add(0, extraButton);
    }
    protected void addExtraButton(Control extraButton, int StringLength) {
        extraButton.setPrefWidth(StringLength * 9);
        extraButton.setStyle("-fx-base: #0000FF; -fx-font-size: 15px;");
        extraButton.setPrefHeight(STANDARD_BUTTON_HEIGHT);
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
                    showPopUp(false);
                }
        }
        });
    }

    /**
     *Set the actions for pressing buttons.
     */
    protected abstract void setOnActions();
    
    protected void addInfoPicture(ImageView picture) {
        extraLabel.setGraphic(picture);
    }
    protected Label getExtraLabel() {
        return extraLabel;
    }
    protected void setBackGround(String name, boolean cover) {
        Image image = new Image(getClass().getResourceAsStream(name));
        // new BackgroundSize(width, height, widthAsPercentage, heightAsPercentage, contain, cover)
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, cover);
        // new BackgroundImage(image, repeatX, repeatY, position, size)
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, backgroundSize);
        // new Background(images...)
        Background background = new Background(backgroundImage);
        popUpPane.setBackground(background);
    }
    protected void addExtraPane(Pane pane) {
        popUpPaneInnerUpper.getChildren().add(pane);
    }
    protected Stage getPopUpStage() {
        return pupUpStage;
    }
}
