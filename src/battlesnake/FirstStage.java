/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesnake;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author johanwendt
 */
public class FirstStage extends PopUp{
    private final ComboBox chooseNumberOfPlayers = new ComboBox<>();
    private static final Label winnerInfo = new Label();
    
    public FirstStage(String title, String infoText, String okMessage, String cancelMessage) {
        super(title, infoText, okMessage, cancelMessage);
        
        getOkButton().setDisable(true);
        getCancelButton().setDisable(true);
        setUpWinnerInfo(null);
        setUpButtons();
        setOnActions();
        setUpLowerPart();        
        showPopUp(true);
    }

    private void setUpButtons() {
        addComboBox();
    }

    private void addComboBox() {
        ObservableList<String> options = FXCollections.observableArrayList("1 player", "2 players","3 players","4 players");
        chooseNumberOfPlayers.getItems().addAll(options);
        chooseNumberOfPlayers.setValue("Select number of players");
        chooseNumberOfPlayers.setPrefWidth(260);
        
        chooseNumberOfPlayers.setOnAction(e -> {
            GameEngine.setNumberOfPlayers(options.indexOf(chooseNumberOfPlayers.getValue()) + 1);
            getOkButton().setDisable(false);
        });
        
        //Enter has the same effekt as clicking the LET'S DO THIS!-button.
        //Escape has the same effect as pressing the cancel-button.
        chooseNumberOfPlayers.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER) && !chooseNumberOfPlayers.getValue().equals("Select number of players")) {
                GameEngine.restart();
                showPopUp(false);
                getCancelButton().setDisable(false);
            }
            if(e.getCode().equals(KeyCode.ESCAPE) && !getCancelButton().isDisable()) {
                GameEngine.setPaused(false);
                showPopUp(false);
            }
        });
        addExtraButton(chooseNumberOfPlayers);
    }
    private void setOnActions() {
        getOkButton().setOnAction(e -> {
            GameEngine.restart();
            UserInterface.setDisableMenuBar(false);
            showPopUp(false);
            getCancelButton().setDisable(false);
        });

        getCancelButton().setOnAction(e -> {
            GameEngine.setPaused(false);
            showPopUp(false);
        });   
        
    }
    //Add info about the objective of the game and info about the winner. 
        //Winner info is empty untill a player has won the game and is
        //emptied again after the game is restarted.
    public void setUpWinnerInfo(Player winner) {
        if(winner == null) {
            getExtraLabel().setText("May the best squamata win!");
            getExtraLabel().setStyle("-fx-text-fill: #000000; -fx-font-size: 30px;" );
        }
        else {
            System.out.println(winner.getPlayerNumber());
            switch(winner.getPlayerNumber()) {
                case 1: 
                    getExtraLabel().setStyle("-fx-text-fill: #B200B2; -fx-font-size: 30px;");
                    break;
                case 2: 
                    getExtraLabel().setStyle("-fx-text-fill: #66FF33; -fx-font-size: 30px;");
                    break;
                case 3: 
                    getExtraLabel().setStyle("-fx-text-fill: #E68A00; -fx-font-size: 30px;");
                    break;
                case 4: 
                    getExtraLabel().setStyle("-fx-text-fill: #00FFFF; -fx-font-size: 30px;");
                    break;
            }
            getExtraLabel().setText("The winner is " + winner.getName());
        }
    }
}
