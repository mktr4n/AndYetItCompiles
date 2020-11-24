/**
 * Author: My Tran
 * <p>
 * Elephants and Rhinos game
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class ElephantsAndRhinos extends Application implements GameInterface {

    private static final String[] TEAMS = {"Elephant", "Rhino"};
    private static final String[] buttonNames = {"    ", "E", "R"};
    private static final String[] teamNames = {"E", "R"};
    private int turnNumber;
    private int boardSize;
    private String[] boardButtons;
    public final String saveFile = "ersave.txt";
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
            alert.setContentText("Elephant has Won");
        } else {
            alert.setContentText("Rhino has Won");
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
        this.boardSize = gameData.length - 1;
        this.boardButtons = new String[this.boardSize];
        //Gets turn number from first number in array
        this.turnNumber = gameData[0];
        int curIndex = 0;
        //Translates team ids to strings
        for (int i = 1; i < gameData.length; i++) {
            switch (gameData[i]) {
                case 1:
                    this.boardButtons[curIndex] = "E";
                    break;
                case 2:
                    this.boardButtons[curIndex] = "R";
                    break;
                default:
                    this.boardButtons[curIndex] = "    ";
            }

            curIndex++;


        }
        updateStage(primaryStage);
    }

    /**
     * Creates board buttons and starts game
     *
     * @param primaryStage stage for game board
     */
    public void startGame(Stage primaryStage, int turnNumber, CompositeGame compGame) {
        this.compGame = compGame;
        this.boardSize = 12;
        this.turnNumber = turnNumber;
        this.boardButtons = new String[this.boardSize];
        this.stage = primaryStage;

        for (int i = 0; i < this.boardSize; i++) {
            String a = this.boardButtons[i] = (buttonNames[getRandomNumber(0, buttonNames.length)]);
            if (!a.contains("E")) {
                this.boardButtons[i] = (buttonNames[getRandomNumber(0, buttonNames.length)]);
            } else if (!a.contains("R")) {
                this.boardButtons[i] = (buttonNames[getRandomNumber(0, buttonNames.length)]);
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
        stage.setTitle("Elephants and Rhinos Battle!  " + TEAMS[turnNumber] + "'s Turn.");

        HBox gameBoard = new HBox(10);

        for (int i = 0; i < this.boardSize; i++) {
            String curBox = boardButtons[i];
            Button curButton = new Button();
            curButton.setText(curBox);
            gameBoard.getChildren().add(curButton);
            //Sets actions to buttons
            switch (curBox) {
                case "R":
                    rhinoSpace(curButton, i, stage);
                    Image imgageR = new Image("rhino.png");
                    ImageView viewR = new ImageView(imgageR);
                    viewR.setFitHeight(20);
                    viewR.setFitWidth(20);
                    curButton.setGraphic(viewR);
                    break;
                case "    ":
                    emptySpace(curButton, i);
                    break;
                case "E":
                    elephantSpace(curButton, i, stage);
                    Image imgageE = new Image("elephant.png");
                    ImageView viewE = new ImageView(imgageE);
                    viewE.setFitHeight(20);
                    viewE.setFitWidth(20);
                    curButton.setGraphic(viewE);
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
            if (curBox.equals(teamNames[turnNumber])) {
                //Move checks differ by team
                if (turnNumber == 1) { //Rhino check
                    if (i > 0 && this.boardButtons[i - 1].equals("    ") && this.boardButtons[i].equals("R")) {
                        canMove = true;
                    }
                } else {
                    if ((i <= (this.boardSize - 1) && i < this.boardButtons.length - 1 && this.boardButtons[i + 1].equals("    ")) && (this.boardButtons[i].equals("E"))) {
                        canMove = true;
                    }
                }
            }
        }
        return canMove;
    }

    /**
     * If a rhino button is pressed
     *
     * @param curButton    The button pressed
     * @param buttonNumber The button number pressed
     */
    private void elephantSpace(Button curButton, int buttonNumber, Stage stage) {
        curButton.setOnAction((event) -> {

            boolean check = false;

            if (this.turnNumber == 1) {
                System.out.println("It is not your turn, it is the rhino's turn!");
                return;
            }

            if (this.boardButtons[buttonNumber].equals("E")) {
                if (buttonNumber < this.boardSize - 1 && this.boardButtons[buttonNumber + 1].equals("    ")) {
                    this.boardButtons[buttonNumber] = "    ";
                    this.boardButtons[buttonNumber + 1] = "E";
                    this.compGame.broadcastChangeTurn();
                } else {
                    System.out.println("Invalid Move");
                }
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
     * If a rhino button is pressed
     *
     * @param curButton    The button pressed
     * @param buttonNumber The button number pressed
     */
    private void rhinoSpace(Button curButton, int buttonNumber, Stage stage) {
        curButton.setOnAction((event) -> {

            boolean check = false;

            if (this.turnNumber < 1) {
                System.out.println("It is not your turn, it is the elephants's turn!");
                return;
            }

            if (this.boardButtons[buttonNumber].equals("R")) {
                if (buttonNumber > 0 && this.boardButtons[buttonNumber - 1].equals("    ")) {
                    this.boardButtons[buttonNumber] = "    ";
                    this.boardButtons[buttonNumber - 1] = "R";
                    this.compGame.broadcastChangeTurn();
                } else {
                    System.out.println("Invalid Move");
                }
            }
        });
    }

    /**
     * Saves game data to text file in working directory
     */
    public void saveGame() {
        try {
            File oldSaveGame = new File("ersave.txt");
            if (oldSaveGame.exists()) {
                oldSaveGame.delete();
            }
            oldSaveGame.createNewFile();

            PrintWriter pr = new PrintWriter("ersave.txt");
            pr.println(this.boardSize + 1);
            pr.println(this.turnNumber);
            for (int i = 0; i < this.boardSize; i++) {
                switch (this.boardButtons[i]) {
                    case "E":
                        pr.println(1);
                        break;
                    case "R":
                        pr.println(2);
                        break;
                    case "    ":
                        pr.println(0);
                        break;
                }
            }

            pr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
