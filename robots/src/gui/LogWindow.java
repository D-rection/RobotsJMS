package gui;

import java.awt.EventQueue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends FlowPane implements LogChangeListener
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;
    FlowPane logsPane = new FlowPane();

    public LogWindow(LogWindowSource logSource)
    {
        this.setOrientation(Orientation.VERTICAL);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("Журнал с сообщениями");
        Button buttonTest = new Button("Tests");
        buttonTest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                m_logContent.appendText("\nВсё хорошо!");
            }
        });
        logsPane.getChildren().add(m_logContent);
        this.getChildren().addAll(buttonTest, logsPane);
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.appendText(content.toString());
    }

    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
