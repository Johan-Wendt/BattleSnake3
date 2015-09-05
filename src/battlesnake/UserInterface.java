package battlesnake;

/**
 * @author johanwendt
 */
import com.sun.javafx.scene.control.behavior.KeyBinding;
import java.util.ArrayList;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;

/**
 *This class is the core game engine. It creates the players, the gamefield and the bonus handler.
 * It takes input from the user and it holds all the GUI-stuff.
 */
public class UserInterface {
    //Fields
    private static final BorderPane mainPane = new BorderPane();
    private static Scene mainScene;
    private static final Stage battleStage = new Stage();
    private static MenuBar menuBar;
    private static Menu menu;
    private static AboutStage aboutStage;
    private static FirstStage firstStage;
    private static RightPane rightPane;
    private static ControlsStage controlsStage;
    private static QuitStage quitStage;

    public static final Pane gameGridPane = new Pane();
    
    private static final double screenHeight = Screen.getPrimary().getVisualBounds().getHeight() - 23;
    private static final double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    private static int gridSize;
    private static int blockSize;
    private static double standardPadding;
    private static double borderWidth; 
    private static double menuBarSize;
   
    private static double playerScoreSize;
        
    
    
    //Regular fields

    /**
     * Creates the grafical interface.
     * @param gameEngine The GameEngine that runs the game.
     */
    public UserInterface () {        
        menuBarSize = 23;
        borderWidth = screenHeight / 200.0;
        blockSize = (int) ((screenHeight - (screenHeight * 0.06)) / GameEngine.BRICKS_PER_ROW);
        gridSize = blockSize * GameEngine.BRICKS_PER_ROW;
        standardPadding = (screenHeight - gridSize - menuBarSize) / 2;
        playerScoreSize = 1.4 * screenHeight / standardPadding;
        setUpMainScreen();
        aboutStage = new AboutStage("About Battle Snake", getInfoAboutStage(), "I get it, let's play some more.");
        controlsStage = new ControlsStage();
        rightPane = new RightPane();
        firstStage = new FirstStage("Start New Game", getInfoFirstStage(), " LET'S DO THIS! ", "  Cancel  ");
        quitStage = new QuitStage("Quit?!", getInfoQuitStage(), "Just leave me alone!", "Hell no!");
    }
    public static void gameOver(Player winner) {
        firstStage.setUpWinnerInfo(winner);
        firstStage.showPopUp(true);
    }
    public static void restart() {
        firstStage.setUpWinnerInfo(null);
        RightPane.setUpScoreBoard();
        RightPane.initiateScoreBoard();         
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
    private void setUpMainScreen() {
        mainScene = new Scene(mainPane, screenWidth, screenHeight);
        
        //Adjust the GUI.
        gameGridPane.setPrefSize(gridSize, gridSize);
        mainPane.setCenter(RightPane.getPane());
        mainPane.setLeft(gameGridPane);
        
        BorderPane.setMargin(gameGridPane, new Insets(standardPadding));
        
        //Exit the game on closing this window.
        battleStage.setOnCloseRequest(c -> {
            System.exit(0);
        });
        //mainScene = new Scene(mainPane, screenWidth, screenHeight);
        mainScene.getStylesheets().add(BattleSnake.class.getResource("BattleSnake.css").toExternalForm());
        
        //Takes input from the keybord and lets the active player-instances decide
        //to do with the information.
        mainScene.setOnKeyPressed(e -> {
            GameEngine.takePressedKey(e.getCode());
        });
        
        //Create Menubar-system
        buildMenuSystem();
        
        //Activate the stage
        battleStage.setScene(mainScene);
        battleStage.setResizable(false);
        battleStage.setTitle("Battle Snake");
        battleStage.show();  
    }
        
    private void buildMenuSystem() {
        menu = new Menu("Battle Snake");
        menu.setId("menu");
        MenuItem underMenu1 = new MenuItem("Set up game");
        MenuItem underMenu2 = new MenuItem("About");
        MenuItem underMenu3 = new MenuItem("Controls");
        MenuItem underMenu4 = new MenuItem("Quit");
        menu.getItems().addAll(underMenu1, underMenu2, underMenu3, underMenu4);
        menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        menuBar.setPadding(new Insets(0, 0, 0, standardPadding));
        mainPane.setTop(menuBar);
        
        menu.setOnShowing(s -> {
            GameEngine.setPaused(true);
          
        });
        menu.setOnHiding(h -> {
                GameEngine.setPaused(false);
        });

        //Set Shortcuts for menus.
        underMenu1.setAccelerator(KeyCombination.keyCombination("CTRL + S"));
        underMenu1.setOnAction(a -> {
            GameEngine.setPaused(true);
            firstStage.showPopUp(true);
        });
        
        underMenu2.setAccelerator(KeyCombination.keyCombination("CTRL + A"));
        underMenu2.setOnAction(a -> {
            GameEngine.setPaused(true);
            aboutStage.showPopUp(true);
        });
        
        underMenu3.setAccelerator(KeyCombination.keyCombination("CTRL + C"));
        underMenu3.setOnAction(a -> {
            GameEngine.setPaused(true);
            ControlsStage.showStage(true);
        });
        underMenu4.setAccelerator(KeyCombination.keyCombination("CTRL + Q"));
        underMenu4.setOnAction(a -> {
            GameEngine.setPaused(true);
            quitStage.showPopUp(true);
        }); 
    }
    public static Stage getBattleStage() {
        return battleStage;
    }
    public void restartGameEngine() {
        GameEngine.restart();
    }
    public void setPauseGameEngine(boolean pause) {
        if(pause) GameEngine.setPaused(true);
        else GameEngine.setPaused(false);
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
    private String getInfoFirstStage() {
        return "Battle against your friends. Collect bonuses for points, and lose "
        + "them when you die. When the field is reduced to the core, "
        + "the eliminationbegins as snakes with negative scores are terminated." 
        + "\nLast snake standing wins!";
    }
    private String getInfoQuitStage() {
        return "Really quit being a snake and return to your boring life"
                + " as a corporate drone??";
    }
    private String getInfoAboutStage() {
        return "Created by Johan Wendt. \n" 
        + "\n"
        + "johan.wendt1981@gmail.com \n"
        + "\n"
        + "Thank you for playing!";
    }
}