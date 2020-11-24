//This is terribly-designed code for the beginning of a software-engineering project.
// Author: Kyle Burke <paithanq@gmail.com>

/*
Basic Cleanup Tasks:  If you're not sure about any of these, please come ask Kyle!
  * Fix Indentation/line breaks.  (I recommend 4 spaces, especially since I will be reading your code.)
  * Proper case styling (capitalization).
  * Use meaningful variable/method names.
  * Remove duplicated data.  (There's a very obvious example here.)

Advanced cleanup: (consider these if you finish the others)
  * Choose between Fields vs. local variables.
  * Remove unneccessary Code.
  * Start considering adding other classes... :)
*/

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ToadsAndFrogs extends Application implements GameState, GameInterface {

    //WANT TO HAVE A FIELD and private GameState state;
    //somewhere in the class call this.state.savedGame();

    private static final String[] TEAMS = {"Toad", "Frog"};
    private int turnNumber;
    private int boardSize;
    private Stage stage;
    private String[] boardButtons;
    private CompositeGame compGame;
    private GameState frogstate;
    private GameState toadstate;
    private GameState state;

    public final String saveFile = "tafsave.txt";

    public ToadsAndFrogs() {
        this.frogstate = new FrogState(this);
        this.toadstate = new ToadState(this);

    }

    public void setGameState(GameState state) {
        this.state = state;
    }

    public GameState getFrogstate() {
        return frogstate;
    }

    public void setFrogstate(GameState frogstate) {
        this.frogstate = frogstate;
    }

    public GameState getToadstate() {
        return toadstate;
    }

    public void setToadstate(GameState toadstate) {
        this.toadstate = toadstate;
    }

    @Override
    public void toadSpace() {
        state.toadSpace();
    }

    @Override
    public void frogSpace() {
        state.frogSpace();
    }

    @Override
    public void resumeGame(int[] gameData, int i, int curIndex, String[] boardButtons) {
        state.resumeGame(gameData, i, curIndex, boardButtons);
    }

    @Override
    public void savedGame() {
        state.savedGame();
    }

    @Override
    public String getPlayer(int turnNumber) {
        return state.getPlayer(turnNumber);
    }

    @Override
    public void update() {

    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Sends win message
     *
     * @param turnNumber Defines who is the winner
     */
    //can have a method .getPlayer(){
    //if toad one, returns the string frog
    //alert.setContentTxt(this.state.getOtherPlayer() + have won);
    // to replace this
    private void win(int turnNumber) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("Congratulations!.");
        alert.setContentText(this.state.getPlayer(turnNumber));
        alert.showAndWait();
        this.stage.close();
        this.compGame.removeGame(this, this.stage);
    }

    /**
     * Code that is ran on program start-up
     *
     * @param primaryStage stage for game board
     */
    public void start(Stage primaryStage) {

    }

    /**
     * Resumes game with saved data
     *
     * @param primaryStage stage for game board
     * @param gameData     game data loaded from txt file
     */
    public void resume(Stage primaryStage, int[] gameData) {
        this.boardSize = gameData.length - 1;
        this.boardButtons = new String[this.boardSize];
        //Gets turn number from first number in array
        this.turnNumber = gameData[0];

        if (this.turnNumber == 0) {
            this.state = getToadstate();
        } else {
            this.state = getFrogstate();
        }

        int curIndex = 0;
        //Translates team ids to strings
        for (int i = 1; i < gameData.length; i++) {
            this.state.resumeGame(gameData, i, curIndex, boardButtons);

            switch (gameData[i]) {
                case 1:
                    this.boardButtons[curIndex] = "Frog";
                    break;
                case 2:
                    this.boardButtons[curIndex] = "Toad";
                    break;
                default:
                    this.boardButtons[curIndex] = "    ";
            }


            curIndex++;


        }

        //Deletes Saved Game
        //File oldSaveGame = new File("TAFsave.txt");
        //oldSaveGame.delete();

        updateStage(primaryStage);
    }

    /**
     * Creates board buttons and starts game
     *
     * @param primaryStage stage for game board
     */
    public void startGame(Stage primaryStage, int turnNumber, CompositeGame compGame) {
        this.stage = primaryStage;
        this.compGame = compGame;
        this.boardSize = 10;
        this.turnNumber = turnNumber;
        this.boardButtons = new String[this.boardSize];

        //All buttons start as blank
        for (int i = 0; i < this.boardSize; i++) {
            boardButtons[i] = "    ";
        }

        //Sets up initial positions for frogs & toads
        this.boardButtons[0] = "Toad";
        boardButtons[1] = "Toad";
        this.boardButtons[2] = "Toad";
        boardButtons[boardButtons.length - 3] = "Frog";
        this.boardButtons[boardButtons.length - 2] = "Frog";
        boardButtons[boardButtons.length - 1] = "Frog";

        updateStage(primaryStage);
    }

    /**
     * Switches whose turn it is
     *
     * @param stage game board passed through to allow updates
     */
    public void changeTurn(Stage stage) {
        this.turnNumber = 1 - this.turnNumber;
        if (this.state == getToadstate()) {
            this.state = getFrogstate();
        } else {
            this.state = getToadstate();
        }

        this.updateStage(stage);
    }

    /**
     * Main logic for game, sets title, sets button texts, and adds actions to each button
     *
     * @param stage game board passed through to allow updates
     */
    public void updateStage(Stage stage) {
        stage.setTitle("Amphibian Battle!  " + TEAMS[turnNumber] + "'s Turn.");

        HBox gameBoard = new HBox(10);

        for (int i = 0; i < this.boardSize; i++) {
            String curBox = boardButtons[i];
            Button curButton = new Button();
            curButton.setText(curBox);
            gameBoard.getChildren().add(curButton);
            //Sets actions to buttons
            switch (curBox) {
                case "Toad":
                    toadSpace(curButton, i, stage);
                    Image imgageT = new Image("toad.jpg");
                    ImageView viewT = new ImageView(imgageT);
                    viewT.setFitHeight(20);
                    viewT.setFitWidth(20);
                    curButton.setGraphic(viewT);
                    break;
                case "    ":
                    emptySpace(curButton, i);
                    break;
                case "Frog":
                    frogSpace(curButton, i, stage);
                    Image imgageF = new Image("frog.png");
                    ImageView viewF = new ImageView(imgageF);
                    viewF.setFitHeight(20);
                    viewF.setFitWidth(20);
                    curButton.setGraphic(viewF);
                    break;
            }
        }

        VBox bottomText = new VBox(10);
        bottomText.getChildren().add(gameBoard);
        bottomText.getChildren().add(new Label("If you can't move, then you lose the game."));
        Button saveButton = new Button("Save Game");
        saveButton.setOnAction((event) -> {
            saveGame();
        });

        bottomText.getChildren().add(saveButton);

        MainMenu mainMenu = new MainMenu();
        Button mainMenuButton = new Button("Return to Main Menu");
        mainMenuButton.setOnAction((event) -> {
            mainMenu.start(stage);

        });

        bottomText.getChildren().add(mainMenuButton);
        ScrollPane layout = new ScrollPane(bottomText);
        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    /**
     * Checks to see if any moves can be made by the specific team
     *
     * @param turnNumber defines who has the current turn
     * @return true if moves are available, false if otherwise
     */
    public boolean checkForMoves(int turnNumber) {
        boolean canMove = false;

        //Loops through each button
        for (int i = 0; i < this.boardSize; i++) {
            String curBox = boardButtons[i];
            //Checks only buttons for the specific team
            if (curBox.equals(TEAMS[turnNumber])) {
                //Move checks differ by team
                if (turnNumber == 1) { //Frog check
                    if ((i > 0 && this.boardButtons[i - 1].equals("    ")) || (i > 1 && this.boardButtons[i - 2] == "    " && this.boardButtons[i - 1].equals("Toad"))) {
                        canMove = true;
                    }
                } else { // Toad Check
                    if ((i <= (this.boardSize - 2) && this.boardButtons[i + 1] == "    ") || (i <= (this.boardSize - 3) && this.boardButtons[i + 1].equals("Frog") && this.boardButtons[i + 2] == "    ")) {
                        canMove = true;
                    }
                }
            }
        }

        return canMove;
    }

    /**
     * If a toad button is pressed
     *
     * @param curButton    The button pressed
     * @param buttonNumber The button number pressed
     */
    private void toadSpace(Button curButton, int buttonNumber, Stage stage) {
        curButton.setOnAction((event) -> {
            if (this.turnNumber == 1) {
                System.out.println("It is not your turn, it is the frog's turn!");
                return;
            }

            if (buttonNumber <= 8 && this.boardButtons[buttonNumber + 1].equals("    ")) {
                this.boardButtons[buttonNumber] = "    ";
                this.boardButtons[buttonNumber + 1] = "Toad";
                this.compGame.broadcastChangeTurn();

            } else if (buttonNumber <= 7 && this.boardButtons[buttonNumber + 1] == "Frog" && this.boardButtons[buttonNumber + 2].equals("    ")) {
                this.boardButtons[buttonNumber] = "    ";
                this.boardButtons[buttonNumber + 2] = "Toad";
                this.compGame.broadcastChangeTurn();

            }

            if ((buttonNumber == 6 && this.boardButtons[buttonNumber + 1].equals("    ") && this.boardButtons[buttonNumber + 2].equals("Toad") && this.boardButtons[buttonNumber + 3] == "Toad" &&
                    this.boardButtons[0].equals("Frog") && this.boardButtons[1] == "Frog" && this.boardButtons[2] == "Frog")) {
                win(this.turnNumber);
            }

        });
    }

    /**
     * If a blank button is pressed
     *
     * @param curButton    The button pressed
     * @param buttonNumber The button number pressed
     */
    private void emptySpace(Button curButton, int buttonNumber) {
        curButton.setOnAction((event) -> {
            System.out.println("This square is empty!");

        });
    }

    /**
     * If a frog button is pressed
     *
     * @param curButton    The button pressed
     * @param buttonNumber The button number pressed
     */
    private void frogSpace(Button curButton, int buttonNumber, Stage stage) {
        curButton.setOnAction((event) -> {

            if (this.turnNumber < 1) {
                System.out.println("It is not your turn, it is the toad's turn!");
                return;
            }

            if ((buttonNumber == 3 && this.boardButtons[buttonNumber - 1] == "    " && this.boardButtons[buttonNumber - 2] == "Frog" && this.boardButtons[buttonNumber].equals("Frog") &&
                    this.boardButtons[7] == "Toad" && this.boardButtons[8].equals("Toad") && this.boardButtons[9] == "Toad")) {
                win(this.turnNumber);
            }

            if (buttonNumber > 0 && this.boardButtons[buttonNumber - 1].equals("    ")) {
                this.boardButtons[buttonNumber] = "    ";
                this.boardButtons[buttonNumber - 1] = "Frog";
                this.compGame.broadcastChangeTurn();
            } else if (buttonNumber > 1 && this.boardButtons[buttonNumber - 2].equals("    ") && this.boardButtons[buttonNumber - 1] == "Toad") {
                this.boardButtons[buttonNumber] = "    ";
                this.boardButtons[buttonNumber - 2] = "Frog";
                this.compGame.broadcastChangeTurn();
            }

        });
    }

    /**
     * Saves game data to text file in working directory
     */
    public void saveGame() {
        try {
            File oldSaveGame = new File("tafsave.txt");
            if (oldSaveGame.exists()) {
                oldSaveGame.delete();
            }
            PrintWriter pr = new PrintWriter("tafsave.txt");
            pr.println(this.boardSize + 1);
            pr.println(this.turnNumber);
            for (int i = 0; i < this.boardSize; i++) {
                switch (this.boardButtons[i]) {
                    case "Frog":
                        pr.println(1);
                        break;
                    case "Toad":
                        pr.println(2);
                        break;
                    case "    ":
                        pr.println(0);
                        break;
                }
            }

            pr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}