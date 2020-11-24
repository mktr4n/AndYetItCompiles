import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DirectionWindow {
    private String direction;
public String getDirection() {
        this.direction = "";
        Stage directionWindow = new Stage();
        directionWindow.setTitle("Choose a Direction");
        HBox buttonRow = new HBox();

        Button leftDirection = new Button("←");
        leftDirection.setStyle("-fx-font: 80 arial; -fx-base: #b6e7c9;");
        leftDirection.setOnAction((event) -> {
            this.direction = "left";
            directionWindow.close();
        });

        Button rightDirection = new Button("→");
        rightDirection.setStyle("-fx-font: 80 arial; -fx-base: #e7b6c4;");
        rightDirection.setOnAction((event) -> {
            this.direction = "right";
            directionWindow.close();
        });

        buttonRow.getChildren().addAll(leftDirection, rightDirection);
        Pane pane = new Pane(buttonRow);
        Scene scene = new Scene(pane);
        directionWindow.setScene(scene);
        directionWindow.sizeToScene();
        directionWindow.showAndWait();
        return this.direction;
    }

}

