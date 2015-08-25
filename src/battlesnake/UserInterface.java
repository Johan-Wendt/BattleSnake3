package battlesnake;

/**
 * @author johanwendt
 */
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.SepiaTone;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;


/**
 *This class is the core game engine. It creates the players, the gamefield and the bonus handler.
 * It takes input from the user and it holds all the GUI-stuff.
 */
public class UserInterface {
    //Fields
    //Panes, scenes and stages.
    private final BorderPane mainPane = new BorderPane();
    public static final Pane PANE = new Pane();
    private final GridPane firstPane = new GridPane();
    private final GridPane rightPane = new GridPane();
    private final VBox scorePane = new VBox();
    private final VBox aboutPane = new VBox();
    private final GridPane controlsPane = new GridPane();
    private Scene firstScene;
    private Scene mainScene;
    private Scene aboutScene;
    private Scene controlsScene;
    private Stage firstStage;
    private Stage aboutStage;
    private Stage controlsStage;
    private final Stage battleStage = new Stage();
    
    //Nodes 
    private MenuBar menuBar = new MenuBar();
    private MenuItem underMenu4 = new MenuItem("Unpause");
    private ChoiceBox chooseNumberOfPlayers;
    private Button startButton;
    private Button cancelButton;
    private Text gameInfo = new Text();
    private Text winnerInfo = new Text();
    private Text playerOneScore;
    private Text playerTwoScore;
    private Text playerThreeScore;
    private Text playerFourScore;
    public TextField player1Up = new TextField();
    public TextField player1Right = new TextField();
    public TextField player1Down = new TextField();
    public TextField player1Left = new TextField();
    public TextField player2Up = new TextField();
    public TextField player2Right = new TextField();
    public TextField player2Down = new TextField();
    public TextField player2Left = new TextField();
    public TextField player3Up = new TextField();
    public TextField player3Right = new TextField();
    public TextField player3Down = new TextField();
    public TextField player3Left = new TextField();
    public TextField player4Up = new TextField();
    public TextField player4Right = new TextField();
    public TextField player4Down = new TextField();
    public TextField player4Left = new TextField();
    private Text regularBonusText = new Text();
    private Text makeShortBonusText = new Text();
    private Text addDeathBlockBonusText = new Text();
    
    
    //Static finals
    //To be able to give every block in the field grid a unique id every 
    //block in y-direction adds 1 to the id while every block in 
    //x-direction adds 1000 (MULIPLIER_X).
    
    public static final Color PLAYER_1_COLOR = Color.web("#B200B2");
    public static final Color PLAYER_2_COLOR = Color.web("#66FF33");
    public static final Color PLAYER_3_COLOR = Color.web("#E68A00");
    public static final Color PLAYER_4_COLOR = Color.YELLOW;
    public static final int PLAYER_SCORE_SIZE = 60;
    
    
    //Game fields
    //private BonusHandler bonusHandler;
    //private GameGrid gameGrid;
    private ScoreEffect scoreEffect = new ScoreEffect();
    private GameEngine newGameEngine;

    public UserInterface (GameEngine newGameEngine) {
        this.newGameEngine = newGameEngine;
    }



    //From here on the rest is GUI-stuff.
    public void setCancelButtonDisabled(boolean disable) {
        cancelButton.setDisable(disable);
    }
    public void showFirstStage(boolean show) {
        if(show) {
            firstStage.show();
        }
        else {
            firstStage.hide();
        }
    }
    /**
     * This sets upp the main game screen.
     */
    public void setUpMainScreen() {
        //Exit the game on closing this window.
        battleStage.setOnCloseRequest(c -> {
            System.exit(0);
        });
        mainScene = new Scene(mainPane, GameGrid.GRID_SIZE + 550, GameGrid.GRID_SIZE + 60);
        mainScene.getStylesheets().add(BattleSnake.class.getResource("BattleSnake.css").toExternalForm());
        
        
        //Takes input from the keybord and lets the active player-instances decide
        //to do with the information.
        mainScene.setOnKeyPressed(e -> {
            newGameEngine.takePressedKey(e.getCode());
        });
        
        //Create Menubar-system
        Menu menu = new Menu("Battle Snake");
        menu.setId("menu");
        MenuItem underMenu1 = new MenuItem("Set up game");
        MenuItem underMenu2 = new MenuItem("About");
        MenuItem underMenu3 = new MenuItem("Controls");
        MenuItem underMenu5 = new MenuItem("Quit");
        menu.getItems().addAll(underMenu1, underMenu2, underMenu3, underMenu4, underMenu5);
        menuBar.setMaxHeight(10);
        menuBar.setBackground(new Background(new BackgroundFill(GameGrid.SAFE_ZONE_COLOR, new CornerRadii(5), Insets.EMPTY)));
        menuBar.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DOTTED, CornerRadii.EMPTY, new BorderWidths(3))));
        menuBar.getMenus().add(menu);
        mainPane.setTop(menuBar);
        menuBar.setEffect(new Lighting(new Light.Distant()));

        menuBar.setId("MBar");

        
        
        //Set Shortcuts for menus.
        underMenu1.setAccelerator(KeyCombination.keyCombination("CTRL + S"));
        underMenu1.setOnAction(a -> {
            newGameEngine.setPaused(true);
            firstStage.show();
        });
        
        underMenu2.setAccelerator(KeyCombination.keyCombination("CTRL + A"));
        underMenu2.setOnAction(a -> {
            newGameEngine.setPaused(true);
            aboutStage.show();
        });
        
        underMenu3.setAccelerator(KeyCombination.keyCombination("CTRL + C"));
        underMenu3.setOnAction(a -> {
            newGameEngine.setPaused(true);
            controlsStage.show();
        });
        
        underMenu4.setAccelerator(KeyCombination.keyCombination("CTRL + P"));
        underMenu4.setOnAction(a -> {
            if(!newGameEngine.isPaused()) {
                newGameEngine.setPaused(true);
            }
            else {
                newGameEngine.setPaused(false);
            }
        });
        
        underMenu5.setAccelerator(KeyCombination.keyCombination("CTRL + Q"));
        underMenu5.setOnAction(a -> {
            System.exit(0);
        });
        
        //Adjust the GUI.

        mainPane.setCenter(rightPane);
        mainPane.setLeft(PANE);
        mainPane.setPadding(new Insets(5, 20, 5, 20));
        
        
        BorderPane.setMargin(PANE, new Insets(5, 5, 5, 5));
        
        //Activate the stage
        battleStage.setScene(mainScene);
        battleStage.setResizable(false);
        battleStage.setTitle("Battle Snake");
        battleStage.show();   
    }
    /**
     * Sets up everything for the first screen where the user can set number of
     * players and start the game. The same stage is used for restarting game
     * in mid game and after game over.
     */
    public void setUpFirstScreen() {
        firstStage = new Stage();
        firstStage.setAlwaysOnTop(true);
        firstStage.setMaxWidth(510);
        firstStage.setMaxHeight(600);
        
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
        firstPane.add(winnerInfo, 1, 2);
 
        //Create combo-box woth enables to choose number of players in the game.
        ComboBox<String> chooseNumberOfPlayers = new ComboBox<>();
        ObservableList<String> options = FXCollections.observableArrayList("1 player", "2 players","3 players","4 players");
        
        chooseNumberOfPlayers.getItems().addAll(options);
        chooseNumberOfPlayers.setValue("Select number of players");
        chooseNumberOfPlayers.setPrefWidth(240);
        
        chooseNumberOfPlayers.setOnAction(e -> {
            newGameEngine.setNumberOfPlayers(options.indexOf(chooseNumberOfPlayers.getValue()) + 1);
            startButton.setDisable(false);
        });
        
        //Enter has the same effekt as clicking the LET'S DO THIS!-button.
        //Escape has the same effect as pressing the cancel-button.
        chooseNumberOfPlayers.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER) && !chooseNumberOfPlayers.getValue().equals("Select number of players")) {
                newGameEngine.restart();
               // try {
                    //thread.sleep(600);
               // } catch (InterruptedException ex) {
                  //  Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
               // }
                firstStage.hide();
                cancelButton.setDisable(false); 
            }
            if(e.getCode().equals(KeyCode.ESCAPE) && cancelButton.isDisable() == false) {
                newGameEngine.setPaused(false);
                firstStage.hide();
            }
        });
        
        //Initiate start and cancelbutton.
        startButton = new Button("LET'S DO THIS!");
        startButton.setDisable(true);
        startButton.setPrefWidth(140);
        startButton.setStyle("-fx-base: #009933;");
        startButton.setOnAction(e -> {
            newGameEngine.restart();
            menuBar.setDisable(false);
        //    try {
         //       thread.sleep(600);
         //   } catch (InterruptedException ex) {
         //       Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
         //   }
            firstStage.hide();
            cancelButton.setDisable(false);
        });
        startButton.setOnKeyPressed(k -> {
            newGameEngine.restart();
   //         try {
     //           thread.sleep(600);
       //     } catch (InterruptedException ex) {
         //       Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
           // }
            firstStage.hide();
            cancelButton.setDisable(false);
        });
        
        cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);
        cancelButton.setDisable(true);
        cancelButton.setStyle("-fx-base: #FF3300;");
        cancelButton.setOnAction(e -> {
            newGameEngine.setPaused(false);
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
    /**
     * Sets up the stage where the user can set the player controls. 
     * This is reached via the menus in the main game stage.
     */
    public void setUpControlsScreen() {
        controlsStage = new Stage();
        controlsStage.setAlwaysOnTop(true);
        controlsStage.setMaxWidth(500);
        controlsStage.setMaxHeight(230);
        controlsStage.setResizable(false);
        
        //Set up info texts
        Text player1 = new Text("Player 1 ");
        Text player2 = new Text("Player 2 ");
        Text player3 = new Text("Player 3 ");
        Text player4 = new Text("Player 4 ");
        
        Text space = new Text("");
        Text Up = new Text("Up");
        Text Right = new Text("Right");
        Text Down = new Text("Down");
        Text Left = new Text("Left");
        
        player1.setFont(Font.font(15));
        player2.setFont(Font.font(15));
        player3.setFont(Font.font(15));
        player4.setFont(Font.font(15));
        Up.setFont(Font.font(15));
        Right.setFont(Font.font(15));
        Down.setFont(Font.font(15));
        Left.setFont(Font.font(15));

        
        controlsPane.addRow(0, space, Up, Right, Down, Left);
        
        //Add input text fields for editing the controls and display current controls.
        
        controlsPane.add(player1, 0, 1);
        controlsPane.add(player2, 0, 2);
        controlsPane.add(player3, 0, 3);
        controlsPane.add(player4, 0, 4);
        
        controlsPane.add(player1Up, 1, 1);
        controlsPane.add(player1Right, 2, 1);
        controlsPane.add(player1Down, 3, 1);
        controlsPane.add(player1Left, 4, 1);
        
        controlsPane.add(player2Up, 1, 2);
        controlsPane.add(player2Right, 2, 2);
        controlsPane.add(player2Down, 3, 2);
        controlsPane.add(player2Left, 4, 2);
        
        controlsPane.add(player3Up, 1, 3);
        controlsPane.add(player3Right, 2, 3);
        controlsPane.add(player3Down, 3, 3);
        controlsPane.add(player3Left, 4, 3);
        
        controlsPane.add(player4Up, 1, 4);
        controlsPane.add(player4Right, 2, 4);
        controlsPane.add(player4Down, 3, 4);
        controlsPane.add(player4Left, 4, 4);
        
        //Make inputfields only show values that are set.
        player1Up.setEditable(false);
        player1Right.setEditable(false);
        player1Down.setEditable(false);
        player1Left.setEditable(false);
        
        player2Up.setEditable(false);
        player2Right.setEditable(false);
        player2Down.setEditable(false);
        player2Left.setEditable(false);
        
        player3Up.setEditable(false);
        player3Right.setEditable(false);
        player3Down.setEditable(false);
        player3Left.setEditable(false);
        
        player4Up.setEditable(false);
        player4Right.setEditable(false);
        player4Down.setEditable(false);
        player4Left.setEditable(false);
        
        //Update player controls when pressing a button in the grid.
        player1Up.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 1", GameEngine.UP, e.getCode());
            player1Right.requestFocus();
    });
        player1Right.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 1", GameEngine.RIGHT, e.getCode());
            player1Down.requestFocus();
    });
        player1Down.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 1", GameEngine.DOWN, e.getCode());
            player1Left.requestFocus();
    });
        player1Left.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 1", GameEngine.LEFT, e.getCode());
            player2Up.requestFocus();
    });
        
        player2Up.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 2", GameEngine.UP, e.getCode());
            player2Right.requestFocus();
    });
        player2Right.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 2", GameEngine.RIGHT, e.getCode());
            player2Down.requestFocus();
    });
        player2Down.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 2", GameEngine.DOWN, e.getCode());
            player2Left.requestFocus();
    });
        player2Left.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 2", GameEngine.LEFT, e.getCode());
            player3Up.requestFocus();
    });
        
        player3Up.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 3", GameEngine.UP, e.getCode());
            player3Right.requestFocus();
    });
        player3Right.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 3", GameEngine.RIGHT, e.getCode());
            player3Down.requestFocus();
    });
        player3Down.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 3", GameEngine.DOWN, e.getCode());
            player3Left.requestFocus();
    });
        player3Left.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 3", GameEngine.LEFT, e.getCode());
            player4Up.requestFocus();
    });
        
        player4Up.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 4", GameEngine.UP, e.getCode());
            player4Right.requestFocus();
    });
        player4Right.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 4", GameEngine.RIGHT, e.getCode());
            player4Down.requestFocus();
    });
        player4Down.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 4", GameEngine.DOWN, e.getCode());
            player4Left.requestFocus();
    });
        player4Left.setOnKeyPressed(e ->  {
            newGameEngine.setControlKey("Player 4", GameEngine.LEFT, e.getCode());
    });

        //Setup confirmation button.
        Button backButton = new Button("Back to the battlin'");
        backButton.setPrefWidth(200);
        backButton.setStyle("-fx-font: 15 arial; -fx-base: #009933;");
        backButton.setOnAction(e -> {
            newGameEngine.setPaused(false);
            controlsStage.hide();
        });
        
        //Setup button for using default keys.
        Button deafultButton = new Button("Use default keys");
        deafultButton.setPrefWidth(200);
        deafultButton.setStyle("-fx-font: 15 arial; -fx-base: #FF00FF;");
        deafultButton.setOnAction(e -> {
            newGameEngine.setUpDefaultControlKeys();
        });
        
        //Add empty row,for spacing in the grid, and button.
        Text emptyRow = new Text("");
        
        controlsPane.add(emptyRow, 2, 5);
        controlsPane.add(deafultButton, 0, 6, 2, 1);
        controlsPane.add(backButton, 3, 6, 2, 1);

        //Make enter and escape return the user to the game. N.B escape 
        //does not reset keys to the state before the stage was brought up..
        controlsPane.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)) {
                newGameEngine.setPaused(false);
                controlsStage.hide();    
            }
            if(e.getCode().equals(KeyCode.ESCAPE)) {
                newGameEngine.setPaused(false);
                controlsStage.hide();
            }
        });
        
        //Add some space
        controlsPane.setPadding(new Insets(20));
        
        //Make the stage ready to be shown upon pressing the menu.
        controlsScene = new Scene(controlsPane, 600, 300);
        controlsStage.setScene(controlsScene);
        controlsStage.setTitle("Set player controls");
    }
    /**
     * Sets up the stage with information about the game.
     */
    public void setUpAboutScreen() {

        aboutStage = new Stage();
        aboutStage.setAlwaysOnTop(true);
        aboutStage.setMaxWidth(260);
        aboutStage.setMaxHeight(250);
        aboutStage.setResizable(false);
              
        //Set about info
        Text aboutInfo = new Text("Version 1.0. \n"
        + "\n"
        + "Created by Johan Wendt. \n" 
        + "\n"
        + "johan.wendt1981@gmail.com \n"
        + "\n"
        + "Thank you for playing!");
        
        aboutInfo.setFont(Font.font(15));
        
        //Create button to get back to the game
        Button okButton = new Button("I get it, let's play some more.");
        okButton.setStyle("-fx-font: 15 arial; -fx-base: #009933;");
        okButton.setOnAction(e -> {
            newGameEngine.setPaused(false);
            aboutStage.hide();
        });
        

        
        //Add info add button
        aboutPane.getChildren().addAll(aboutInfo, okButton);
        
        //Add som spacing
        aboutPane.setSpacing(30);
        aboutPane.setPadding(new Insets(20));
        
        //Make enter and escape take the user back to the game.
        aboutPane.setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.ENTER)) {
                newGameEngine.setPaused(false);
                aboutStage.hide();    
            }
            if(e.getCode().equals(KeyCode.ESCAPE)) {
                newGameEngine.setPaused(false);
                aboutStage.hide();
            }
        });
        
        //Make the stage ready to be shown upon pressing the menu.
        aboutScene = new Scene(aboutPane, 260, 250);
        aboutStage.setScene(aboutScene);
        aboutStage.setTitle("About Battle Snake");
    }
    /**
     * Sets up the right pane that holds the score info.
     */
    public void setUpRightPane() {
        
        //Set color, add the scoreboard and set some space to the part that is to contain the tostring info.
        int fontSize = 17;

        rightPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(3))));
        rightPane.getChildren().add(scorePane);
        rightPane.setConstraints(scorePane, 1, 1);
        rightPane.getColumnConstraints().addAll(new ColumnConstraints(30), new ColumnConstraints(10), new ColumnConstraints(30));
        rightPane.getRowConstraints().addAll(new RowConstraints(10), new RowConstraints(500), new RowConstraints(30), new RowConstraints(30), new RowConstraints(30), new RowConstraints(30), new RowConstraints(30));
        BorderPane.setMargin(rightPane, new Insets(12, 5, 1, 5));

        //Create bonus info and set id for css.
        Text regularBonusText = new Text(BonusHandler.REGULAR_BONUS_DESCRIPTION);
        regularBonusText.setEffect(new Bloom());
        regularBonusText.setId("RBonus");
        Text makeShortBonusText = new Text(BonusHandler.MAKE_SHORT_BONUS_DESCRIPTION);
        makeShortBonusText.setEffect(new Bloom());
        makeShortBonusText.setId("MBonus");
        Text addDeathBlockBonusText = new Text(BonusHandler.ADD_DEATH_BLOCK_BONUS_DESCRIPTION);
        addDeathBlockBonusText.setEffect(new Bloom());
        addDeathBlockBonusText.setId("ABonus");
        
        
        //Create the rectangles that show what type of bonus the description is about.
        Rectangle regularBonusColor = new Rectangle(fontSize, fontSize, BonusHandler.REGULAR_BONUS_COLOR);
        regularBonusColor.setStroke(Color.BLACK);
        regularBonusColor.setEffect(new Lighting());
        Rectangle makeShortBonusColor = new Rectangle(fontSize, fontSize, BonusHandler.MAKE_SHORT_BONUS_COLOR);
        makeShortBonusColor.setStroke(Color.BLACK);
        makeShortBonusColor.setEffect(new Lighting());
        Rectangle addDeathBlockBonusColor = new Rectangle(fontSize, fontSize, BonusHandler.ADD_DEATH_BLOCK_BONUS_COLOR);
        addDeathBlockBonusColor.setStroke(Color.BLACK);
        addDeathBlockBonusColor.setEffect(new Lighting());
        
        //Add the bonus information to the pane.
        rightPane.add(regularBonusText, 3, 4);
        rightPane.add(makeShortBonusText, 3, 5);
        rightPane.add(addDeathBlockBonusText, 3, 6);
        
        rightPane.add(regularBonusColor, 1, 4);
        rightPane.add(makeShortBonusColor, 1, 5);
        rightPane.add(addDeathBlockBonusColor, 1, 6);
        rightPane.setEffect(new Lighting(new Light.Distant()));
        rightPane.setId("RPane");

    }
    /**
     * Sets upp the score board for the right pane.
     */
    public void setUpScoreBoard() {
        //Clear the scores for every new game.
        scorePane.getChildren().clear();
        
        //Add some space
        scorePane.setPadding(new Insets(5, 5, 10, 30));
        
        //Create, add the header nad apply efects.
        Text scoreHeader = new Text("Scores");
        scorePane.getChildren().add(scoreHeader);
        //scoreHeader.setFont(Font.font(STYLESHEET_MODENA, 60));
        scoreHeader.setFill(GameGrid.GAMEGRID_COLOR);
        scoreHeader.setUnderline(true);
        scoreHeader.setEffect(scoreEffect.getEffect(GameGrid.GAMEGRID_COLOR));
    }
    /**
     * Makes the score board only show scores for relevant players.
     */
    public void initiateScoreBoard() {
        switch(newGameEngine.getNumberOfPlayers()) {
            case 4: 
                playerFourScore = new Text();
                playerFourScore.setText(newGameEngine.getPlayer(3).scoreToString());
                scorePane.getChildren().add(1, playerFourScore);
                playerFourScore.setFont(Font.font(PLAYER_SCORE_SIZE));
                playerFourScore.setEffect(scoreEffect.getEffect(PLAYER_4_COLOR));
            case 3: 
                playerThreeScore = new Text();
                playerThreeScore.setText(newGameEngine.getPlayer(2).scoreToString());
                scorePane.getChildren().add(1, playerThreeScore);
                playerThreeScore.setFont(Font.font(PLAYER_SCORE_SIZE));
                playerThreeScore.setEffect(scoreEffect.getEffect(PLAYER_3_COLOR));
            case 2: 
                playerTwoScore = new Text();
                playerTwoScore.setText(newGameEngine.getPlayer(1).scoreToString());
                scorePane.getChildren().add(1, playerTwoScore);
                playerTwoScore.setFont(Font.font(PLAYER_SCORE_SIZE));
                playerTwoScore.setEffect(scoreEffect.getEffect(PLAYER_2_COLOR));
            case 1: 
                playerOneScore = new Text();
                playerOneScore.setText(newGameEngine.getPlayer(0).scoreToString());
                scorePane.getChildren().add(1, playerOneScore);
                playerOneScore.setFont(Font.font(PLAYER_SCORE_SIZE));
                playerOneScore.setEffect(scoreEffect.getEffect(PLAYER_1_COLOR));
        }
 
    }
    /**
     * Recieves the correct score from the player instances.
     */
    public void showScores() {
        switch(newGameEngine.getNumberOfPlayers()) {
            case 4: 
                playerFourScore.setText(newGameEngine.getPlayer(3).scoreToString());
            case 3: 
                playerThreeScore.setText(newGameEngine.getPlayer(2).scoreToString());
            case 2: 
                playerTwoScore.setText(newGameEngine.getPlayer(1).scoreToString());
            case 1: 
                playerOneScore.setText(newGameEngine.getPlayer(0).scoreToString());
        }
    }
    /**
     * Makes the setup stage display either "Good Luck!" or the name
     * of the winner of the game.
     * @param winner 
     */
    public void setUpWinnerInfo(Player winner) {
        if(winner == null) {
            winnerInfo.setText("Good Luck!");
            winnerInfo.setFill(Color.BLACK);
            winnerInfo.setFont(Font.font(15));
        }
        else {
            winnerInfo.setText("The winner is " + winner.getName());
            winnerInfo.setFill(winner.getPlayerColor());
            winnerInfo.setFont(Font.font(30));
        }
    }
    
}