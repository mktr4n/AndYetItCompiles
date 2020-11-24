import javafx.scene.control.Button;
import javafx.stage.Stage;

public interface GameState {

    void toadSpace();
    void frogSpace();
    void resumeGame(int[] gameData, int i, int curIndex, String[] boardButtons);
    void savedGame();
    String getPlayer(int turnNumber);
    void update();



}
