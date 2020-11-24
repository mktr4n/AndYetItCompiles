
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CompositeGame implements GameInterface {

    private final List<GameInterface> games = new ArrayList<GameInterface>();
    private final List<String> saveFiles = new ArrayList<>();
    private final List<Stage> stages = new ArrayList<>();
    private int turnNumber;

    @Override
    public void resume(Stage primaryStage, int gameData[]) {
        for (int index = 0; index < games.size(); index++) {
                System.out.println("File name: " + saveFiles.get(index));
                if (getGameData(saveFiles.get(index)).length > 6) {
                    games.get(index).resume(new Stage(), getGameData(saveFiles.get(index)));
                } else {
                    System.out.println("Save data not found");
                }
        }

    }

    @Override
    public void startGame(Stage primaryStage, int turnNumber, CompositeGame compGame) {
        System.out.println("Start Game");

    }

    public void startNewGame() {
        //Create


        FlowPane pane = new FlowPane();

        //Second r
        HBox secondRowHBox = new HBox();
        secondRowHBox.setPadding(new Insets(15, 5, 15, 5));
        secondRowHBox.setSpacing(10);

        Text gameTitle = new Text("Who's playing first?");
        gameTitle.setFont(new Font(20));

        Button firstPlayerButton = new Button("Player 1");
        firstPlayerButton.setPrefSize(100, 20);

        Stage stage = new Stage();
        firstPlayerButton.setOnAction((event) -> {
            this.turnNumber = 0;
            for (int index = 0; index < games.size(); index++) {
                stages.add(new Stage());
                games.get(index).startGame(stages.get(index), this.turnNumber, this);
            }
            stage.close();
        });

        Button secondPlayerButton = new Button("Player 2");
        secondPlayerButton.setPrefSize(100, 20);

        secondPlayerButton.setOnAction((event) -> {
            this.turnNumber = 1;
            for (int index = 0; index < games.size(); index++) {
                stages.add(new Stage());
                games.get(index).startGame(stages.get(index), this.turnNumber, this);
            }
            stage.close();
        });

        secondRowHBox.getChildren().addAll(gameTitle, firstPlayerButton, secondPlayerButton);

        pane.getChildren().addAll(secondRowHBox);

        Scene scene = new Scene(pane); // Creates the scene with created pane
        stage.setTitle("Choose first player"); // Sets the title
        stage.setScene(scene); // Place the scene in the stage
        stage.sizeToScene();
        stage.show(); // Display the stage
    }

    @Override
    public void changeTurn(Stage stage) {

    }

    @Override
    public void updateStage(Stage stage) {

    }

    @Override
    public void saveGame() {

    }

    @Override
    public boolean checkForMoves(int turnNumber) {
        return false;
    }

    public void addGame(GameInterface gameInterface) {
        games.add(gameInterface);
    }

    public void addFile(String fname) {
        saveFiles.add(fname);
    }

    public void broadcastChangeTurn() {
        for (int index = 0; index < games.size(); index++) {
            System.out.println(games.size());
            games.get(index).changeTurn(stages.get(index));
        }

        this.turnNumber = 1 - this.turnNumber;

        boolean movesExist = false;

        for (int index = 0; index < games.size(); index++) {
            if (!movesExist) {
                movesExist = games.get(index).checkForMoves(this.turnNumber);
            }
        }

        if (!movesExist) {
            win(this.turnNumber);
        }
    }

    private void win(int turnNumber) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("Congratulations!.");
        if (1 - turnNumber == 0) {
            alert.setContentText("Player 2 has no more playable moves, Player 1 (Elephants, Red, Toads) has Won the sum of games!");
        } else {
            alert.setContentText("Player 1 has no more playable moves, Player 2 (Rhinos, Blue, Frogs) has Won the sum of games!");
        }
        alert.showAndWait();

        for (Stage stage : stages) {
            stage.close();
        }

    }

    public void removeGame(GameInterface game, Stage stage){
        this.games.remove(game);
        this.stages.remove(stage);
    }

    private int[] getGameData(String fileName) {
        int[] gameData = new int[0];
        File dataFile = new File(fileName);
        if (dataFile.exists()) {
            try {
                Scanner s = new Scanner(new File(fileName));

                gameData = new int[s.nextInt()];

                for (int i = 0; i < gameData.length; i++) {
                    gameData[i] = s.nextInt();
                }

                s.close();
            } catch (FileNotFoundException e) {
                return gameData;
            }
        } else {
            System.out.println("File Doesn't Exist!");
        }

        return gameData;
    }
}
