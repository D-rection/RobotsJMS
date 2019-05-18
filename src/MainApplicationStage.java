import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApplicationStage {
    public MainApplicationStage(Stage primaryStage) {
        Pane gameWindow = new GameWindow();
        primaryStage.setScene(new Scene(gameWindow));
    }
}