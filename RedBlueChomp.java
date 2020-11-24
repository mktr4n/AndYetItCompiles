/**
 * Author: My Tran
 * <p>
 * Chompp game
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class RedBlueChomp extends Application implements GameInterface {

    private static final String[] TEAMS = {"Blue", "Red"};
    private static final String[] buttonImages = {"    ", "cookie.png", "cookie2.png", "bad_cookie.png"};
    private int turnNumber;
    private static final int boardColumn = 10;
    private static final int boardRow = 10;
    private int[][] boardButtons = new int[this.boardColumn][this.boardRow];
    public final String saveFile = "rbcsave.txt";
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
            alert.setContentText("Blue has Won");
        } else {
            alert.setContentText("Red has Won");
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

    //NOT SURE
    public void resume(Stage primaryStage, int[] gameData) {
        //Gets turn number from first number in array
        this.turnNumber = gameData[0];
        System.out.println(gameData[0] + "   " + gameData[100]);
        int curIndex = 1;
        //Translates team ids to strings
        for (int i = 0; i < boardColumn; i++) {
            for (int x = 0; x < boardRow; x++) {
                this.boardButtons[i][x] = gameData[curIndex];
                curIndex++;
            }
        }

        //Deletes Saved Game
        File oldSaveGame = new File("rbcsave.txt");
        oldSaveGame.delete();

        updateStage(primaryStage);
    }

    /**
     * Creates board buttons and starts game
     *
     * @param primaryStage stage for game board
     */
    public void startGame(Stage primaryStage, int turnNumber, CompositeGame compGame) {
        this.turnNumber = turnNumber;
        this.compGame = compGame;
        this.stage = primaryStage;
        for (int i = 0; i < boardColumn; i++) {
            for (int x = 0; x < boardRow; x++) {
                this.boardButtons[i][x] = getRandomNumber(1, 3);
            }
        }
        //sets poison cookie
        this.boardButtons[0][9] = 3;

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

        stage.setTitle(TEAMS[turnNumber] + "'s Turn.");


        GridPane gridPane = new GridPane();
        gridPane.setHgap(5); //padding
        gridPane.setVgap(5);  //padding

        for (int i = 0; i < boardColumn; i++) {
            for (int x = 0; x < boardRow; x++) {

                Button curButton;

                if (this.boardButtons[i][x] != 0) {
                    curButton = new Button();
                    Image cookie = new Image(buttonImages[this.boardButtons[i][x]]);
                    ImageView cookies = new ImageView(cookie);
                    cookies.setFitWidth(20);
                    cookies.setFitHeight(20);
                    curButton.setGraphic(cookies);
                } else {
                    curButton = new Button("      ");
                }

                switch (this.boardButtons[i][x]) {
                    case 0:
                        emptySpace(curButton);
                        break;
                    case 1:
                        cookieAction(curButton, i, x, stage);
                        curButton.setStyle("-fx-base: #de2323;");
                        break;
                    case 2:
                        cookieAction1(curButton, i, x, stage);
                        curButton.setStyle("-fx-base: #2522e0;");
                        break;
                    case 3:
                        poisonAction(curButton, stage);
                        break;
                }

                gridPane.add(curButton, i, x);

            }
        }

        VBox bottomText = new VBox(10);
        bottomText.getChildren().add(gridPane);
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
     * If a normal cookie is pressed
     *
     * @param curButton button pressed
     * @param col       column of button
     * @param row       row of button
     */
    private void cookieAction(Button curButton, int col, int row, Stage stage) {
        curButton.setOnAction((event) -> {

            System.out.println("cookieAction"); //RED

            if (this.turnNumber == 0) {
                System.out.println("It is not your turn!");
                return;
            }
            for (int i = col; i < boardColumn; i++) {
                for (int x = row; x >= 0; x--) {
                    if (this.boardButtons[i][x] == 3) {
                        win(1 - this.turnNumber, stage);
                    } else {
                        this.boardButtons[i][x] = 0;
                    }
                }
            }
            this.compGame.broadcastChangeTurn();
        });
    }

    private void cookieAction1(Button curButton, int col, int row, Stage stage) {
        curButton.setOnAction((event) -> {

            System.out.println("cookieAction1");

            if (this.turnNumber == 1) {
                System.out.println("It is not your turn!");
                return;
            }
            for (int i = col; i < boardColumn; i++) {
                for (int x = row; x >= 0; x--) {
                    if (this.boardButtons[i][x] == 3) {
                        win(1 - this.turnNumber, stage);
                    } else {
                        this.boardButtons[i][x] = 0;
                    }
                }
            }

            this.compGame.broadcastChangeTurn();
        });
    }


    /**
     * If a bad cookie is pressed
     *
     * @param curButton button pressed
     * @param stage     stage of game
     */
    private void poisonAction(Button curButton, Stage stage) {
        curButton.setOnAction((event) -> {
            win(this.turnNumber, stage);
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
            File oldSaveGame = new File("rbcsave.txt");
            if (oldSaveGame.exists()) {
                oldSaveGame.delete();
            }
            oldSaveGame.createNewFile();
            PrintWriter pr = new PrintWriter("rbcsave.txt");
            pr.println(101);
            pr.println(this.turnNumber);
            for (int i = 0; i < boardColumn; i++) {
                for (int x = 0; x < boardRow; x++) {
                    pr.println(this.boardButtons[i][x]);
                }
            }
            pr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkForMoves(int turnNumber) {
        return true;
    }


}