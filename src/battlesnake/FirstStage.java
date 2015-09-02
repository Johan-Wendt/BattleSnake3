/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesnake;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Bloom;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author johanwendt
 */
public class FirstStage {
    private static Stage firstStage;
    private static Scene firstScene;
    private static final GridPane firstPane = new GridPane();
    private static final Text gameInfo = new Text();
    private static Button startButton = new Button();
    private static Button cancelButton = new Button();
    
    
    public FirstStage() {
        firstStage = new Stage();
        firstStage.setAlwaysOnTop(true);
        firstStage.setMaxWidth(510);
        firstStage.setMaxHeight(600);
                
        firstStage.setOnShowing(e -> {
            UserInterface.setDisableUnderMenuFour(true);
        });
        
        firstStage.setOnHiding(e -> {
            UserInterface.setDisableUnderMenuFour(false);
        });
        
        firstStage.initOwner(UserInterface.getBattleStage());
        
        //Adjust the spacing between the different parts of the screen.
        firstPane.getColumnConstraints().addAll(new ColumnConstraints(20), new ColumnConstraints(250), new ColumnConstraints(150));
        firstPane.getRowConstraints().addAll(new RowConstraints(10), new RowConstraints(170), new RowConstraints(90), new RowConstraints(5));
        
        //Create info about how to play the game.
        gameInfo.setText("Battle against your friends. Collect \nbonuses for points, and lose "
        + "them when\nyou die. When the field is reduced to the\ncore, "
        + "the eliminationbegins as snakes\nwith negative scores are terminated.\n" 
        + "Last snake standing wins!");
        gameInfo.setId("info-text");
        gameInfo.setEffect(new Bloom());
        
        //Add info about the objective of the game and info about the winner. 
        //Winner info is empty untill a player has won the game and is
        //emptied again after the game is restarted.
        firstPane.add(gameInfo, 1, 1);
        firstPane.add(UserInterface.getWinnerInfo(), 1, 2);
 
        //Create combo-box woth enables to choose number of players in the game.
        ComboBox<String> chooseNumberOfPlayers = new ComboBox<>();
        ObservableList<String> options = FXCollections.observableArrayList("1 player", "2 players","3 players","4 players");
        
        chooseNumberOfPlayers.getItems().addAll(options);
        chooseNumberOfPlayers.setValue("Select number of players");
        chooseNumberOfPlayers.setPrefWidth(240);
        
        chooseNumberOfPlayers.setOnAction(e -> {
            GameEngine.setNumberOfPlayers(options.indexOf(chooseNumberOfPlayers.getValue()) + 1);
            startButton.setDisable(false);
        });
        
        //Enter has the same effekt as clicking the LET'S DO THIS!-button.
        //Escape has the same effect as pressing the cancel-button.
        chooseNumberOfPlayers.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER) && !chooseNumberOfPlayers.getValue().equals("Select number of players")) {
                GameEngine.restart();
                firstStage.hide();
                cancelButton.setDisable(false);
            }
            if(e.getCode().equals(KeyCode.ESCAPE) && cancelButton.isDisable() == false) {
                GameEngine.setPaused(false);
                firstStage.hide();
            }
        });
        
        //Initiate start and cancelbutton.
        startButton.setText("LET'S DO THIS!");
        startButton.setDisable(true);
        startButton.setPrefWidth(140);
        startButton.setStyle("-fx-base: #009933;");
        startButton.setOnAction(e -> {
            GameEngine.restart();
            UserInterface.setDisableMenuBar(false);
            firstStage.hide();
            cancelButton.setDisable(false);
        });
        startButton.setOnKeyPressed(k -> {
            GameEngine.restart();
            firstStage.hide();
            cancelButton.setDisable(false);
        });
        
        cancelButton.setText("Cancel");
        cancelButton.setCancelButton(true);
        cancelButton.setDisable(true);
        cancelButton.setStyle("-fx-base: #FF3300;");
        cancelButton.setOnAction(e -> {
            GameEngine.setPaused(false);
            firstStage.hide();
        });   
        
        //Add buttons to GUI.
        firstPane.add(chooseNumberOfPlayers, 1, 3);
        firstPane.add(startButton, 2, 3);
        firstPane.add(cancelButton, 3, 3);
        
        
        
        //Make the stage show at startup and disable closing the screen.
        firstScene = new Scene(firstPane, 600, 300);
        firstScene.getStylesheets().add(BattleSnake.class.getResource("BattleSnake.css").toExternalForm());
        firstStage.setScene(firstScene);
        firstStage.setTitle("Set up game");
        
        firstStage.setResizable(false);
        firstStage.show();
    }
    public void showWelcomeScreen(boolean show) {
        if(show) firstStage.show();
        else firstStage.hide();
    }
}
