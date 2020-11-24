/**
 * Author: Dillon Therrien, My Tran
 * <p>
 * Main menu GUI Allows you to Select between games and resume previously saved games
 */


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MainMenu extends Application {
    private Stage primaryStage;
    private static final CompositeGame compGame = new CompositeGame();
    private Label currentlySelected = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        //Label
        Label label = new Label("Select Game(s)");
        label.setUnderline(true);
        label.setTextAlignment(TextAlignment.CENTER);

        //Game Buttons
        HBox first = new HBox();
        first.setSpacing(10);
        Label firstGame = new Label("Chomp");
        Button firstButton = new Button("Add Game");
        firstButton.setOnAction(e -> addGame("Chomp"));
        first.getChildren().addAll(firstGame, firstButton);

        HBox second = new HBox();
        second.setSpacing(10);
        Label secondGame = new Label("Elephants and Rhinos");
        Button secondButton = new Button("Add Game");
        secondButton.setOnAction(e -> addGame("Elephants and Rhinos"));
        second.getChildren().addAll(secondGame, secondButton);

        HBox third = new HBox();
        third.setSpacing(10);
        Label thirdGame = new Label("Hot Potato");
        Button thirdButton = new Button("Add Game");
        thirdButton.setOnAction(e -> addGame("Hot Potato"));
        third.getChildren().addAll(thirdGame, thirdButton);

        HBox fourth = new HBox();
        fourth.setSpacing(10);
        Label fourthGame = new Label("Red Blue Chomp");
        Button fourthButton = new Button("Add Game");
        fourthButton.setOnAction(e -> addGame("Red Blue Chomp"));
        fourth.getChildren().addAll(fourthGame, fourthButton);

        HBox fifth = new HBox();
        fifth.setSpacing(10);
        Label fifthGame = new Label("Toads And Frogs");
        Button fifthButton = new Button("Add Game");
        fifthButton.setOnAction(e -> addGame("Toads And Frogs"));
        fifth.getChildren().addAll(fifthGame, fifthButton);

        HBox sixth = new HBox();
        sixth.setSpacing(10);
        Label sixthGame = new Label("Toppling Dominoes");
        Button sixthButton = new Button("Add Game");
        sixthButton.setOnAction(e -> addGame("Toppling Dominoes"));
        sixth.getChildren().addAll(sixthGame, sixthButton);

        Separator topSeparator = new Separator();

        Label cgTitle = new Label("Currently Selected Games:");

        Separator bottomSeparator = new Separator();

        //Button: select which game to play
        Button button = new Button("New Game");
        button.setOnAction(e -> handleStart());

        //Button: Resume the game
        Button buttonResume = new Button("Resume");
        buttonResume.setOnAction(event -> handleResume());

        //Layout
        VBox root = new VBox(10);

        root.setPadding(new Insets(20, 20, 20, 20));
        root.getChildren().addAll(label, first, second, third, fourth, fifth, sixth, topSeparator, cgTitle, this.currentlySelected, bottomSeparator, button, buttonResume);
        root.setSpacing(5);

        Scene scene = new Scene(root);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Welcome to Burke's Games!"); // Sets the title
        this.primaryStage.setScene(scene);
        this.primaryStage.sizeToScene();
        this.primaryStage.show(); // Display the stage

    }

    private void addGame(String game) {
        switch (game) {
            case "Chomp":
                Chomp chomp = new Chomp();
                compGame.addGame(chomp);
                compGame.addFile(chomp.saveFile);
                break;
            case "Elephants and Rhinos":
                ElephantsAndRhinos elephantsAndRhinos = new ElephantsAndRhinos();
                compGame.addGame(elephantsAndRhinos);
                compGame.addFile(elephantsAndRhinos.saveFile);
                break;
            case "Hot Potato":
                HotPotato hotPotato = new HotPotato();
                compGame.addGame(hotPotato);
                compGame.addFile(hotPotato.saveFile);
                break;
            case "Red Blue Chomp":
                RedBlueChomp redBlueChomp = new RedBlueChomp();
                compGame.addGame(redBlueChomp);
                compGame.addFile(redBlueChomp.saveFile);
                break;
            case "Toads And Frogs":
                ToadsAndFrogs toadsAndFrogs = new ToadsAndFrogs();
                compGame.addGame(toadsAndFrogs);
                compGame.addFile(toadsAndFrogs.saveFile);
                break;
            case "Toppling Dominoes":
                TopplingDominoes topplingDominoes = new TopplingDominoes();
                compGame.addGame(topplingDominoes);
                compGame.addFile(topplingDominoes.saveFile);
                break;
        }

        if (this.currentlySelected.getText() == "") {
            this.currentlySelected.setText(game);
        } else {
            this.currentlySelected.setText(this.currentlySelected.getText() + ", " + game);
        }
        this.primaryStage.sizeToScene();
        this.primaryStage.show();
    }

    //handle checkbox for game options
    private void handleResume() {
        if (this.currentlySelected.getText() != "") {
            compGame.resume(primaryStage, null);
            primaryStage.close();
        } else {
            System.out.println("No game(s) selected!");
        }
    }

    private void handleStart() {
        if (this.currentlySelected.getText() != "") {
            primaryStage.close();
            compGame.startNewGame();
        } else {
            System.out.println("No game(s) selected!");
        }

    }
}
