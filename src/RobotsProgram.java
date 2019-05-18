
import javafx.application.Application;
import javafx.stage.Stage;

public class RobotsProgram extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Robots");
        primaryStage.setMaxHeight(800);
        primaryStage.setMaxWidth(1250);
        new MainApplicationStage(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
