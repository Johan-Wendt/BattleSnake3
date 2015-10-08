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

import java.util.HashMap;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *Creates the stage where the player can set the controls.
 * @author johanwendt
 */
public class ControlsStage extends PopUp{
    private static final GridPane controlsPane = new GridPane();
    
    private static final HashMap<Integer, TextField> playerOneControls = new HashMap<>();
    private static final HashMap<Integer, TextField> playerTwoControls = new HashMap<>();
    private static final HashMap<Integer, TextField> playerThreeControls = new HashMap<>();
    private static final HashMap<Integer, TextField> playerFourControls = new HashMap<>();
    private static TextField pauseKeyField = new TextField();
    
    public ControlsStage(String title, String okMessage) {
        super(title, okMessage, PopUp.STANDARD_PANE_WIDTH);
        setUpInitialControlsInfo();
        setUpInfoTexts();
        makeControlsEditable();
        createButtons();
        addExtraPane(controlsPane);
    }
    private void setUpInitialControlsInfo() {
        
        playerOneControls.put(GameEngine.UP, new TextField());
        playerOneControls.put(GameEngine.RIGHT, new TextField());
        playerOneControls.put(GameEngine.DOWN, new TextField());
        playerOneControls.put(GameEngine.LEFT, new TextField());
        
        playerTwoControls.put(GameEngine.UP, new TextField());
        playerTwoControls.put(GameEngine.RIGHT, new TextField());
        playerTwoControls.put(GameEngine.DOWN, new TextField());
        playerTwoControls.put(GameEngine.LEFT, new TextField());
        
        playerThreeControls.put(GameEngine.UP, new TextField());
        playerThreeControls.put(GameEngine.RIGHT, new TextField());
        playerThreeControls.put(GameEngine.DOWN, new TextField());
        playerThreeControls.put(GameEngine.LEFT, new TextField());
        
        playerFourControls.put(GameEngine.UP, new TextField());
        playerFourControls.put(GameEngine.RIGHT, new TextField());
        playerFourControls.put(GameEngine.DOWN, new TextField());
        playerFourControls.put(GameEngine.LEFT, new TextField());
    }
 
    private void setUpInfoTexts() {
        int fontSize = 20;
        controlsPane.setPadding(new Insets(20));
        controlsPane.setVgap(3);
        controlsPane.setHgap(3);
        //Set up info texts
        Text player1 = new Text(PlayerEnum.PLAYER_ONE.getName() + " ");
        Text player2 = new Text(PlayerEnum.PLAYER_TWO.getName() + " ");
        Text player3 = new Text(PlayerEnum.PLAYER_THREE.getName() + " ");
        Text player4 = new Text(PlayerEnum.PLAYER_FOUR.getName() + " ");
        Text pauseKey = new Text("Pause ");
        
        //ColumnConstraints rightAlign = new ColumnConstraints(150, 150, 150, Priority.NEVER, HPos.RIGHT, true);
        //controlsPane.getColumnConstraints().add(0, rightAlign);
        GridPane.setHalignment(player1, HPos.RIGHT);
        GridPane.setHalignment(player2, HPos.RIGHT);
        GridPane.setHalignment(player3, HPos.RIGHT);
        GridPane.setHalignment(player4, HPos.RIGHT);
        GridPane.setHalignment(pauseKey, HPos.RIGHT);
        
        //Text space = new Text("");
        Text Up = new Text("Up");
        Text Right = new Text("Right");
        Text Down = new Text("Down");
        Text Left = new Text("Left");
        
        player1.setFont(Font.font(fontSize));
        player2.setFont(Font.font(fontSize));
        player3.setFont(Font.font(fontSize));
        player4.setFont(Font.font(fontSize));
        pauseKey.setFont(Font.font(fontSize));
        player1.setFill(PlayerEnum.PLAYER_ONE.getColor());
        player2.setFill(PlayerEnum.PLAYER_TWO.getColor());
        player3.setFill(PlayerEnum.PLAYER_THREE.getColor());
        player4.setFill(PlayerEnum.PLAYER_FOUR.getColor());
        
        Up.setFont(Font.font(fontSize));
        Right.setFont(Font.font(fontSize));
        Down.setFont(Font.font(fontSize));
        Left.setFont(Font.font(fontSize));
  
        controlsPane.addRow(0,new Text(""), Up, Right, Down, Left);
       // controlsPane.addColumn(0, new Text(""), new Text(""),new Text(""), new Text(""), new Text(""));
        controlsPane.addColumn(0, player1, player2, player3, player4, pauseKey);
        
        
        //Add input text fields for editing the controls and display current controls.
        /*
        controlsPane.add(player1, 0, 1);
        controlsPane.add(player2, 0, 2);
        controlsPane.add(player3, 0, 3);
        controlsPane.add(player4, 0, 4);
        controlsPane.add(pauseKey, 0, 5);
        */
        
        controlsPane.add(playerOneControls.get(GameEngine.UP), 1, 1);
        controlsPane.add(playerOneControls.get(GameEngine.RIGHT), 2, 1);
        controlsPane.add(playerOneControls.get(GameEngine.DOWN), 3, 1);
        controlsPane.add(playerOneControls.get(GameEngine.LEFT), 4, 1);
        
        controlsPane.add(playerTwoControls.get(GameEngine.UP), 1, 2);
        controlsPane.add(playerTwoControls.get(GameEngine.RIGHT), 2, 2);
        controlsPane.add(playerTwoControls.get(GameEngine.DOWN), 3, 2);
        controlsPane.add(playerTwoControls.get(GameEngine.LEFT), 4, 2);
        
        controlsPane.add(playerThreeControls.get(GameEngine.UP), 1, 3);
        controlsPane.add(playerThreeControls.get(GameEngine.RIGHT), 2, 3);
        controlsPane.add(playerThreeControls.get(GameEngine.DOWN), 3, 3);
        controlsPane.add(playerThreeControls.get(GameEngine.LEFT), 4, 3);
        
        controlsPane.add(playerFourControls.get(GameEngine.UP), 1, 4);
        controlsPane.add(playerFourControls.get(GameEngine.RIGHT), 2, 4);
        controlsPane.add(playerFourControls.get(GameEngine.DOWN), 3, 4);
        controlsPane.add(playerFourControls.get(GameEngine.LEFT), 4, 4);
        
        controlsPane.add(pauseKeyField, 1, 5);
        
    
        //Make inputfields only show values that are set.
        playerOneControls.forEach((key, value) -> {
            value.setEditable(false);
        });
        playerTwoControls.forEach((key, value) -> {
            value.setEditable(false);
        });
        playerThreeControls.forEach((key, value) -> {
            value.setEditable(false);
        });
        playerFourControls.forEach((key, value) -> {
            value.setEditable(false);
        });
        pauseKeyField.setEditable(false);
    }
    private void makeControlsEditable() {
        
        //Update player controls when pressing a button in the grid.
        playerOneControls.get(GameEngine.UP).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_ONE.getName(), GameEngine.UP, e.getCode());
            playerOneControls.get(GameEngine.RIGHT).requestFocus();
        });
        playerOneControls.get(GameEngine.RIGHT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_ONE.getName(), GameEngine.RIGHT, e.getCode());
            playerOneControls.get(GameEngine.DOWN).requestFocus();
        });
        playerOneControls.get(GameEngine.DOWN).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_ONE.getName(), GameEngine.DOWN, e.getCode());
            playerOneControls.get(GameEngine.LEFT).requestFocus();
        });
        playerOneControls.get(GameEngine.LEFT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_ONE.getName(), GameEngine.LEFT, e.getCode());
            playerTwoControls.get(GameEngine.UP).requestFocus();
        });
        
        playerTwoControls.get(GameEngine.UP).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_TWO.getName(), GameEngine.UP, e.getCode());
            playerTwoControls.get(GameEngine.RIGHT).requestFocus();
        });
        playerTwoControls.get(GameEngine.RIGHT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_TWO.getName(), GameEngine.RIGHT, e.getCode());
            playerTwoControls.get(GameEngine.DOWN).requestFocus();
        });
        playerTwoControls.get(GameEngine.DOWN).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_TWO.getName(), GameEngine.DOWN, e.getCode());
            playerTwoControls.get(GameEngine.LEFT).requestFocus();
        });
        playerTwoControls.get(GameEngine.LEFT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_TWO.getName(), GameEngine.LEFT, e.getCode());
            playerThreeControls.get(GameEngine.UP).requestFocus();
        });
        
        playerThreeControls.get(GameEngine.UP).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_THREE.getName(), GameEngine.UP, e.getCode());
            playerThreeControls.get(GameEngine.RIGHT).requestFocus();
        });
        playerThreeControls.get(GameEngine.RIGHT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_THREE.getName(), GameEngine.RIGHT, e.getCode());
            playerThreeControls.get(GameEngine.DOWN).requestFocus();
        });
        playerThreeControls.get(GameEngine.DOWN).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_THREE.getName(), GameEngine.DOWN, e.getCode());
            playerThreeControls.get(GameEngine.LEFT).requestFocus();
        });
        playerThreeControls.get(GameEngine.LEFT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_THREE.getName(), GameEngine.LEFT, e.getCode());
            playerFourControls.get(GameEngine.UP).requestFocus();
        });
        
        playerFourControls.get(GameEngine.UP).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_FOUR.getName(), GameEngine.UP, e.getCode());
            playerFourControls.get(GameEngine.RIGHT).requestFocus();
        });
        playerFourControls.get(GameEngine.RIGHT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_FOUR.getName(), GameEngine.RIGHT, e.getCode());
            playerFourControls.get(GameEngine.DOWN).requestFocus();
        });
        playerFourControls.get(GameEngine.DOWN).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_FOUR.getName(), GameEngine.DOWN, e.getCode());
            playerFourControls.get(GameEngine.LEFT).requestFocus();
        });
        playerFourControls.get(GameEngine.LEFT).setOnKeyPressed(e ->  {
            GameEngine.setControlKey(PlayerEnum.PLAYER_FOUR.getName(), GameEngine.LEFT, e.getCode());
            pauseKeyField.requestFocus();
        });
        pauseKeyField.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER) || e.getCode().equals(KeyCode.ESCAPE)) {
                showPopUp(false);
            }
            else {
            GameEngine.setPauseKey(e.getCode());
            }
        });
    }
    private void createButtons() {
        //Setup button for using default keys.
        Button deafultButton = new Button("Use default keys");
        addExtraButton(deafultButton, deafultButton.getText().length());
        deafultButton.setOnAction(e -> {
            GameEngine.setUpDefaultControlKeys();
        });
    }
    @Override
    protected void setOnActions() {
        getOkButton().setOnAction(e -> {
            showPopUp(false);
        });
    }
    /**
     * Updates the information about the current controls for the players.
     * @param playerName name of the player
     * @param direction direction to be controlled by the key
     * @param key key used to turn in the given direction
     */
    public static void updateControlText(String playerName, Integer direction, String key) {
        if(playerName.equals(PlayerEnum.PLAYER_ONE.getName())) {
            playerOneControls.get(direction).setText(key);
        }
        if(playerName.equals(PlayerEnum.PLAYER_TWO.getName())) {
            playerTwoControls.get(direction).setText(key);
        }
        if(playerName.equals(PlayerEnum.PLAYER_THREE.getName())) {
            playerThreeControls.get(direction).setText(key);
        }
        if(playerName.equals(PlayerEnum.PLAYER_FOUR.getName())) {
            playerFourControls.get(direction).setText(key);
        }
    }
    public static void updatePausedKeyText(String key) {
        pauseKeyField.setText(key);
    }
}
