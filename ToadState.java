import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ToadState implements GameState {

    private final ToadsAndFrogs toadsAndFrogs;

    public ToadState(ToadsAndFrogs toadsAndFrogs){
        this.toadsAndFrogs = toadsAndFrogs;
    }

    @Override
    public void toadSpace() {
        System.out.println("Player moves left");
    }

    @Override
    public void frogSpace() {
        System.out.println("Player moves right");
    }

    @Override
    public void resumeGame(int[] gameData, int i, int curIndex, String[] boardButtons){
        switch (gameData[i]) {
            case 1:
                boardButtons[curIndex] = "Frog";
                break;
            case 2:
                boardButtons[curIndex] = "Toad";
                break;
            default:
                boardButtons[curIndex] = "    ";
        }

    }


    @Override
    public void savedGame() {
        System.out.println("Player saves game");
    }

    @Override
    public String getPlayer(int turnNumber) {
        return "Toads have Won";
    }

    @Override
    public void update(){
    }

}