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
import java.util.Random;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class HotPotato extends Application implements GameInterface {

    private static final String[] TEAMS = {"Player 1", "Player 2"};
    private static final String[] buttonImages = {"    ", "potato.png", "tomato.png"};
    private CompositeGame compGame;
    private int turnNumber;
    private static final int boardColumn = 10;
    private static final int boardRow = 10;
    private final int[][] boardButtons = new int[boardColumn][boardRow];
    public final String saveFile = "psave.txt";
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
            alert.setContentText("Player 1 has Won");
        } else {
            alert.setContentText("Player 2 has Won");
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
        int curIndex = 1;
        //Translates team ids to strings
        for (int i = 0; i < boardColumn; i++) {
            for (int x = 0; x < boardRow; x++) {
                this.boardButtons[i][x] = gameData[curIndex];
                curIndex++;
            }
        }

        //Deletes Saved Game
        File oldSaveGame = new File("csave.txt");
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
                this.boardButtons[i][x] = 1;
            }
        }
        Random rand = new Random();
        //sets poison potato
        this.boardButtons[rand.nextInt(10)][rand.nextInt(10)] = 2;

        updateStage(primaryStage);
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
                    Image potato = new Image(buttonImages[this.boardButtons[i][x]]);
                    ImageView potatos = new ImageView(potato);
                    potatos.setFitWidth(20);
                    potatos.setFitHeight(20);
                    curButton.setGraphic(potatos);
                } else {
                    curButton = new Button("      ");
                }

                switch (this.boardButtons[i][x]) {
                    case 0:
                        emptySpace(curButton);
                        break;
                    case 1:
                        potatoAction(curButton, i, x, stage);
                        break;
                    case 2:
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
     * If a normal potato is pressed
     *
     * @param curButton button pressed
     * @param col       column of button
     * @param row       row of button
     */
    private void potatoAction(Button curButton, int col, int row, Stage stage) {
        curButton.setOnAction((event) -> {
            Boolean lose = false;
            System.out.println("potatoAction");

            for (int i = col; i >= 0; i--) {
                for (int x = row; x < boardRow; x++) {
                    if (this.boardButtons[i][x] == 2) {
                        lose = true;
                    }
                    this.boardButtons[i][x] = 0;
                }
            }

            if (lose) {
                win(1 - this.turnNumber, stage);
            } else {
                this.compGame.broadcastChangeTurn();
            }

        });
    }


    /**
     * If a bad potato is pressed
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
            File oldSaveGame = new File("psave.txt");
            if (oldSaveGame.exists()) {
                oldSaveGame.delete();
            }
            oldSaveGame.createNewFile();
            PrintWriter pr = new PrintWriter("psave.txt");
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

