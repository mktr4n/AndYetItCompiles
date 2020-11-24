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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

public class TopplingDominoes extends Application implements GameInterface {

    private static final String[] buttonNames = {"    ", "Blue", "Red"};
    private static final String[] TEAMS = {"Blue", "Red"};
    private int turnNumber;
    private int boardSize;
    private int[][] boardButtons;
    private final DirectionWindow directionWindow = new DirectionWindow();
    public final String saveFile = "tdsave.txt";
    private CompositeGame compGame;
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Sends win message
     *
     * @param turnNumber Defines who is the winner
     */
    private void win(int turnNumber, Stage stage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("Congratulations!.");
        if (turnNumber == 1) {
            alert.setContentText("Red player has Won");
        } else {
            alert.setContentText("Blue player has Won");
        }

        alert.showAndWait();
        stage.close();
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
        this.boardSize = (gameData.length - 1) / 3;
        this.boardButtons = new int[3][this.boardSize];
        //Gets turn number from first number in array
        this.turnNumber = gameData[0];
        int curIndex = 1;
        //Translates team ids to strings
        for (int i = 0; i < 3; i++) {
            for (int x = 0; x < this.boardSize; x++) {
                this.boardButtons[i][x] = gameData[curIndex];
                curIndex++;
            }
        }

        //Deletes Saved Game
        //File oldSaveGame = new File("TDsave.txt");
        //oldSaveGame.delete();

        updateStage(primaryStage);
    }

    /**
     * Creates board buttons and starts game by updating stage
     *
     * @param primaryStage stage for game board
     */
    public void startGame(Stage primaryStage, int turnNumber, CompositeGame compGame) {
        this.stage = primaryStage;
        this.compGame = compGame;
        this.boardSize = 15;
        this.turnNumber = turnNumber;
        this.boardButtons = new int[3][this.boardSize];
        //Loops through each game row
        for (int i = 0; i < 3; i++) {
            //Loops through each button on row
            for (int x = 0; x < this.boardSize; x++) {
                this.boardButtons[i][x] = getRandomNumber(1, 3);
            }
        }

        updateStage(primaryStage);
    }

    /**
     * Returns random number
     *
     * @param min minimum bounds
     * @param max maximum bounds
     * @return randomized number
     */
    private int getRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    /**
     * Switches whose turn it is
     *
     * @param stage game board passed through to allow updates
     */
    public void changeTurn(Stage stage) {
        this.turnNumber = 1 - this.turnNumber;

        this.updateStage(stage);
    }

    /**
     * Main logic for game, sets title, sets button texts, and adds actions to each button
     *
     * @param stage game board passed through to allow updates
     */
    public void updateStage(Stage stage) {
        stage.setTitle("Let the dominoes fall!  " + TEAMS[turnNumber] + "'s Turn.");

        //First Row
        HBox firstRow = new HBox(10);
        int curRow = 0;

        for (int i = 0; i < this.boardSize; i++) {
            String curBox = buttonNames[boardButtons[curRow][i]];
            Button curButton = new Button();
            curButton.setText(curBox);
            firstRow.getChildren().add(curButton);
            //Set actions to buttons
            switch (curBox) {
                case "Red":
                    curButton.setStyle("-fx-base: #de2323;");
                    redSpace(curButton, curRow, i, stage);
                    break;
                case "    ":
                    curButton.setStyle("-fx-base: #ffffff;");
                    emptySpace(curButton);
                    break;
                case "Blue":
                    curButton.setStyle("-fx-base: #2522e0;");
                    blueSpace(curButton, curRow, i, stage);
                    break;
            }
        }

        //Second Row
        HBox secondRow = new HBox(10);
        curRow = 1;
        for (int i = 0; i < this.boardSize; i++) {
            String curBox = buttonNames[boardButtons[curRow][i]];
            Button curButton = new Button();
            curButton.setText(curBox);
            secondRow.getChildren().add(curButton);
            //Set actions to buttons
            switch (curBox) {
                case "Red":
                    curButton.setStyle("-fx-base: #de2323;");
                    redSpace(curButton, curRow, i, stage);
                    break;
                case "    ":
                    curButton.setStyle("-fx-base: #ffffff;");
                    emptySpace(curButton);
                    break;
                case "Blue":
                    curButton.setStyle("-fx-base: #2522e0;");
                    blueSpace(curButton, curRow, i, stage);
                    break;
            }
        }

        //Third Row
        HBox thirdRow = new HBox(10);
        curRow = 2;
        for (int i = 0; i < this.boardSize; i++) {
            String curBox = buttonNames[boardButtons[curRow][i]];
            Button curButton = new Button();
            curButton.setText(curBox);
            thirdRow.getChildren().add(curButton);
            //Set actions to buttons
            switch (curBox) {
                case "Red":
                    curButton.setStyle("-fx-base: #de2323;");
                    redSpace(curButton, curRow, i, stage);
                    break;
                case "    ":
                    curButton.setStyle("-fx-base: #ffffff;");
                    emptySpace(curButton);
                    break;
                case "Blue":
                    curButton.setStyle("-fx-base: #2522e0;");
                    blueSpace(curButton, curRow, i, stage);
                    break;
            }
        }

        VBox bottomText = new VBox(10);
        bottomText.getChildren().addAll(firstRow, secondRow, thirdRow);
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

        for (int i = 0; i < 3; i++) {
            for (int x = 0; x < this.boardSize; x++) {
                if (boardButtons[i][x] == (turnNumber + 1)) {
                    canMove = true;
                }
            }
        }

        return canMove;
    }

    /**
     * Makes the move for the current turn after getting certain directioni
     *
     * @param rowNumber
     * @param buttonNumber
     */
    private void makeMove(int rowNumber, int buttonNumber) {
        String curDirection = directionWindow.getDirection();
        if (curDirection == "left") {
            for (int i = buttonNumber; i >= 0; i--) {
                this.boardButtons[rowNumber][i] = 0;
            }
        } else {
            for (int i = buttonNumber; i < this.boardSize; i++) {
                this.boardButtons[rowNumber][i] = 0;
            }
        }
    }

    /**
     * Sets action to a red button
     *
     * @param curButton    button object
     * @param rowNumber    row number of button
     * @param buttonNumber button number within row
     * @param stage        stage that game is displayed on
     */
    private void redSpace(Button curButton, int rowNumber, int buttonNumber, Stage stage) {
        curButton.setOnAction((event) -> {

            if (this.turnNumber == 0) {
                System.out.println("It is not your turn!");
                return;
            }

            //Handles move
            this.makeMove(rowNumber, buttonNumber);

            this.compGame.broadcastChangeTurn();
        });
    }

    /**
     * Sets action to a blue button
     *
     * @param curButton    button object
     * @param rowNumber    row number of button
     * @param buttonNumber button number within row
     * @param stage        stage that game is displayed on
     */
    private void blueSpace(Button curButton, int rowNumber, int buttonNumber, Stage stage) {
        curButton.setOnAction((event) -> {

            if (this.turnNumber == 1) {
                System.out.println("It is not your turn!");
                return;
            }

            //Handles move
            this.makeMove(rowNumber, buttonNumber);

            this.compGame.broadcastChangeTurn();
        });
    }

    /**
     * If a blank button is pressed
     *
     * @param curButton The button pressed
     */
    private void emptySpace(Button curButton) {
        curButton.setOnAction((event) -> {
            System.out.println("This square is empty!");
        });
    }

    /**
     * Saves game data to text file in working directory
     */
    public void saveGame() {
        try {
            File oldSaveGame = new File("tdsave.txt");
            if (oldSaveGame.exists()) {
                oldSaveGame.delete();
            }
            PrintWriter pr = new PrintWriter("tdsave.txt");
            pr.println((this.boardSize * 3) + 1);
            pr.println(this.turnNumber);

            for (int i = 0; i < 3; i++) {
                for (int x = 0; x < this.boardSize; x++) {
                    pr.println(this.boardButtons[i][x]);
                }
            }

            pr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}