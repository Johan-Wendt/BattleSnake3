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

/**
 * @author johanwendt
 */
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.stage.Screen;

/**
 *This class is the core game engine. It creates the players, the gamefield and the bonus handler.
 * It takes input from the user and it holds all the GUI-stuff.
 */
public class UserInterface {
   // private static final BorderPane mainPane = new BorderPane();
    private static final Stage battleStage = new Stage();
    private static MenuBar menuBar;
    private static AboutStage aboutStage;
    private static FirstStage firstStage;
    private static ControlsStage controlsStage;
    private static QuitStage quitStage;
    private static RulesStage rulesStage;

    public static final Pane gameGridPane = new Pane();
    
    private static final double menuBarSize = 23;
    private static final double screenHeight = Screen.getPrimary().getVisualBounds().getHeight() - menuBarSize;
    private static final double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    private static int gridSize;
    private static int blockSize;
    private static double standardPadding;
    private static double borderWidth; 
   
    private static double playerScoreSize;
    
    private static final Color infoColor = Color.web("#01c9f3");
            
    /**
     * Creates the grafical interface.
     * @param gameEngine The GameEngine that runs the game.
     */
    public UserInterface (GameEngine gameEngine) {        
        borderWidth = screenHeight / 200.0;
        blockSize = (int) ((screenHeight - (screenHeight * 0.01)) / GameEngine.BRICKS_PER_ROW);
        gridSize = blockSize * GameEngine.BRICKS_PER_ROW;
        standardPadding = (screenHeight - gridSize - menuBarSize) / 2;
        playerScoreSize = 0.9 * screenHeight / standardPadding;
        BorderPane mainPane = new BorderPane();
        setUpMainScreen(mainPane);
        aboutStage = new AboutStage("About Flow Snake", getInfoAboutStage(), "I get it, let's play some more.", 600);
        controlsStage = new ControlsStage("Set player controls", "Back to the battlin'");
        rulesStage = new RulesStage("The Rules", "Ok, that's easy");
      //  RightPane rightPane = new RightPane(mainPane);
      //  LeftPane leftPane = new LeftPane(mainPane);
        firstStage = new FirstStage("Start New Game", getInfoFirstStage(), " LET'S DO THIS! ", "  Cancel  ", gameEngine);
        quitStage = new QuitStage("Quit?!", getInfoQuitStage(), "Just leave me alone!", "Hell no!");
        gameGridPane.setManaged(false);
        
    }
    public static void gameOver(PlayerEnum winner, boolean noScore) {
        firstStage.setUpWinnerInfo(winner, noScore);
        firstStage.showPopUp(true);
    }
    public static void restart() {
        firstStage.setUpWinnerInfo(null, false);
    }
    public static int getGridSize() {
        return gridSize;
    }
    public static int getBlockSize() {
        return blockSize;
    }
    /**
     * This sets upp the main game screen.
     */
    private static void setUpMainScreen(BorderPane mainPane) {
        Scene mainScene = new Scene(mainPane, screenWidth, screenHeight);
        gameGridPane.setPrefSize(gridSize, gridSize);
        gameGridPane.setMaxSize(gridSize, gridSize);
      //  mainPane.setCenter(RightPane.getPane());
        mainPane.setCenter(gameGridPane);
       // battleStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        battleStage.setFullScreen(true);
        
       // BorderPane.setMargin(gameGridPane, new Insets(standardPadding));
        
        
        //Exit the game on closing this window.
        battleStage.setOnCloseRequest(c -> {
            System.exit(0);
        });
        //mainScene = new Scene(mainPane, screenWidth, screenHeight);
        mainScene.getStylesheets().add(FlowSnake.class.getResource("BattleSnake.css").toExternalForm());
        
        //Takes input from the keybord and lets the active player-instances decide
        //to do with the information.
        mainScene.setOnKeyPressed(e -> {
            GameEngine.takePressedKey(e.getCode());
        });
        
        //Create Menubar-system
        buildMenuSystem(mainPane);
        
        //Activate the stage
        battleStage.setScene(mainScene);
       // battleStage.setResizable(false);
        battleStage.setTitle("Flow Snake");
        battleStage.show();  
    }
        
    private static void buildMenuSystem(BorderPane mainPane) {
        Menu menu = new Menu("Flow Snake");
        MenuItem underMenu1 = new MenuItem("Set up game");
        MenuItem underMenu2 = new MenuItem("About");
        MenuItem underMenu3 = new MenuItem("Controls");
        MenuItem underMenu4 = new MenuItem("Quit");
        MenuItem underMenu5 = new MenuItem("Rules");
        menu.getItems().addAll(underMenu1, underMenu2, underMenu3, underMenu4, underMenu5);
        menuBar = new MenuBar();
        menuBar.setPrefHeight(menuBarSize);
        menuBar.getMenus().add(menu);
        menuBar.setPadding(new Insets(0, 0, 0, standardPadding));
        mainPane.setTop(menuBar);
        
        menu.setOnShowing(s -> {
            GameEngine.popUpOpened();          
        });
        menu.setOnHiding(h -> {
            GameEngine.popUpClosed();
        });

        //Set Shortcuts for menus.
        underMenu1.setAccelerator(KeyCombination.keyCombination("CTRL + S"));
        underMenu1.setOnAction(a -> {
            firstStage.showPopUp(true);
        });
        
        underMenu2.setAccelerator(KeyCombination.keyCombination("CTRL + A"));
        underMenu2.setOnAction(a -> {
            aboutStage.showPopUp(true
            );
        });
        
        underMenu3.setAccelerator(KeyCombination.keyCombination("CTRL + C"));
        underMenu3.setOnAction(a -> {
            controlsStage.showPopUp(true);
        });
        underMenu4.setAccelerator(KeyCombination.keyCombination("CTRL + Q"));
        underMenu4.setOnAction(a -> {
            quitStage.showPopUp(true);
        }); 
        underMenu5.setAccelerator(KeyCombination.keyCombination("CTRL + R"));
        underMenu5.setOnAction(a -> {
            rulesStage.showPopUp(true);
        }); 
    }
    public void addBacgoundImage(String outerPicture, String innerPicture, boolean cover) {
        Image imageOuter = new Image(getClass().getResourceAsStream(outerPicture));
        Image imageCenter = new Image(getClass().getResourceAsStream(innerPicture));

        
        
        
        // new BackgroundSize(width, height, widthAsPercentage, heightAsPercentage, contain, cover)
        BackgroundSize backgroundSizeOuter = new BackgroundSize(100, 100, true, true, true, cover);
        BackgroundSize backgroundSizeCenter = new BackgroundSize(UserInterface.getBlockSize() * (GameGrid.SAFE_ZONE_DIAMETER * 2 + 1), UserInterface.getBlockSize() * (GameGrid.SAFE_ZONE_DIAMETER * 2 + 1), false, false, false, false);
        // new BackgroundImage(imageOuter, repeatX, repeatY, position, size)
        BackgroundImage backgroundImageOuter = new BackgroundImage(imageOuter, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, backgroundSizeOuter);
        BackgroundImage backgroundImageCenter = new BackgroundImage(imageCenter, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSizeCenter);

        // new Background(images...)
        Background background = new Background(backgroundImageOuter, backgroundImageCenter);
        
        gameGridPane.setBackground(background);
        
        gameGridPane.setBorder(new Border(new BorderStroke(null, new BorderStrokeStyle(StrokeType.CENTERED, StrokeLineJoin.ROUND, StrokeLineCap.BUTT, 10, 0, null), CornerRadii.EMPTY, new BorderWidths(3))));
        
    }
    public static Stage getBattleStage() {
        return battleStage;
    }
    public static void setDisableMenuBar(boolean disabled) {
        if(disabled) menuBar.setDisable(true);
        else menuBar.setDisable(false);
    }
    public static double getScreenHeight() {
        return screenHeight;
    }
    public static double getScreenWidth() {
        return screenWidth;
    }
    public static double getPlayerScoreSize() {
        return playerScoreSize;
    }
    public static double getStandardPadding() {
        return standardPadding;
    }
    public static Color infoColor() {
        return infoColor;
    }
    private String getInfoFirstStage() {
        return "";
    }
    private String getInfoQuitStage() {
        return "Really?! No more Flow?";
    }
    private String getInfoAboutStage() {
        return " \nCreated by Johan Wendt. \n" 
        + "\n"
        + "johan.wendt1981@gmail.com \n"
        + "\n"
        + "Thank you for playing!";
    }
    public static void showControllStage(boolean show) {
        controlsStage.showPopUp(show);
    }
    public static void showQuitStage(boolean show) {
        quitStage.showPopUp(show);
    }
    public static void showRulesStage(boolean show) {
        rulesStage.showPopUp(show);
    }
}