import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public interface GameInterface {

    void resume(Stage primaryStage, int[] gameData);
    void startGame(Stage primaryStage, int turnNumber, CompositeGame compGame);
    void changeTurn(Stage stage);
    void updateStage(Stage stage);
    void saveGame();
    boolean checkForMoves(int turnNumber);

}
