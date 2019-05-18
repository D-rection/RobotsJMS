package gui;

import javafx.scene.layout.Pane;

public class GameWindow extends Pane {

    private final GameField gameVisualizer;

    public GameWindow(){
        gameVisualizer = new GameField(this);
    }
}
