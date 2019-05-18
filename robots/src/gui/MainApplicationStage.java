package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import log.LogWindowSource;
import java.util.HashMap;

public class MainApplicationStage {

    private String gameWindowName = "GameWindow";
    private String logWindowName = "LogWindow";

    public MainApplicationStage(Stage primaryStage,LogWindowSource source) {
        Pane gameWindow = new GameWindow();
        Pane logWindow = new LogWindow(source);

        HashMap<String, Scene> scenes = getScenes(gameWindow, logWindow);

        ChoiceBox<String> choiceBox = getChoiceBox(gameWindowName);
        ChoiceBox<String> choiceBox2 = getChoiceBox(logWindowName);
        gameWindow.getChildren().add(choiceBox);
        logWindow.getChildren().add(choiceBox2);
        choiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(scenes.get(choiceBox.getValue()));
                choiceBox2.setValue(logWindowName);
            }
        });

        choiceBox2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { ;
                primaryStage.setScene(scenes.get(choiceBox2.getValue()));
                choiceBox.setValue(gameWindowName);
            }
        });
        primaryStage.setScene(scenes.get(gameWindowName));
    }

    private HashMap<String, Scene> getScenes(Pane gameWindow, Pane logWindow) {
        HashMap<String, Scene> scenes = new HashMap<String, Scene>();
        scenes.put(gameWindowName, new Scene(gameWindow));
        scenes.put(logWindowName, new Scene(logWindow));
        return scenes;
    }

    private ChoiceBox<String> getChoiceBox(String startPosition) {
        ObservableList<String> langs = FXCollections.observableArrayList("GameWindow", "LogWindow");
        ChoiceBox<String> choiceBox = new ChoiceBox<String>(langs);
        choiceBox.setValue(startPosition);
        return choiceBox;
    }
}