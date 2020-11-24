import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FrogState implements GameState {

    private final ToadsAndFrogs toadsAndFrogs;

    public FrogState(ToadsAndFrogs toadsAndFrogs) {
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
        
    }

    @Override
    public void savedGame() {
        System.out.println("Player saves game");

    }

    @Override
    public String getPlayer(int turnNumber) {
        return "Frogs have Won";

        /*
        if(turnNumber== 1){
            return "Frogs have Won";
        }else{
            return "Toads have Won";
        }

         */
    }

    @Override
    public void update(){
    }

}
