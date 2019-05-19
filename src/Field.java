import java.util.*;
import java.lang.String.*;

public class Field{
    public final String QueueName;

    private HashSet<FieldCell> badCells = new HashSet<>();
    private Bug bug;
    private Target target;
    private  HashSet<FieldCell> allCells = new HashSet<>();

    private MessageSender m_sender = new MessageSender();

    private final Timer m_timer = initTimer();
    static volatile LinkedList<String> m_buffer = new LinkedList<>();
    private MessageReceiver m_receiver;
    private final Object m_objForSync = new Object();

    public Field(Bug bug, Target target, String queueName, Wall[] walls, Mine[] mines)
    {
        this.bug = bug;
        this.target = target;
        QueueName = queueName;
        for(Wall wall: walls){
            FieldCell cell = FieldCell.getCell(wall.X_Position, wall.Y_Position);
            badCells.add(cell);
        }

        for(Mine mine: mines){
            FieldCell cell = FieldCell.getCell(mine.X_Position, mine.Y_Position);
            badCells.add(cell);
        }

        m_receiver = new MessageReceiver(queueName, m_buffer);
        m_receiver.startReceiveMessages();

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                bufferChecker();
            }
        }, 0, 1);
    }

    private void bufferChecker() {
        synchronized (m_objForSync) {
            if (m_buffer.size() != 0) {
                for (int i = 0; i < m_buffer.size(); i++) {
                    parseMessages(m_buffer.get(i));
                }
                m_buffer.clear();
            }
        }
    }

    private void parseMessages(String message) {

        StringTokenizer st = new StringTokenizer(message, " (,)");
        String[] elements = new String[st.countTokens()];
        int i = 0;
        while(st.hasMoreTokens()) {
            elements[i] = st.nextToken();
            i++;
        }

        switch (elements[0]) {
            case "onModelUpdateEvent":
                onModelUpdateEvent();
                break;
            case "setTargetPosition":
                setTargetPosition(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
                break;
            case "draw":
                draw();
                break;
        }

    }

    private Timer initTimer() {
        Timer timer = new Timer("messages checker", true);
        return timer;
    }


    private void setTargetPosition(double x, double y)
    {
        FieldCell cell = FieldCell.getCell(x, y);
        if (!badCells.contains(cell)) {
            String textMessage = "setTargetPosition(" + String.valueOf(x) + "," + String.valueOf(y);
            m_sender.sendMessage(target.QueueName, textMessage);
        }
        else {
            System.out.println("Bad Cell!" + cell.X + " " + cell.Y);
            System.out.println(x + " " + y);
        }
    }

    private boolean isSmash()
    {
        return badCells.contains(FieldCell.getCell(bug.X_Position, bug.Y_Position));
    }

    private void onModelUpdateEvent(){
        String textMessage = "onModelUpdateEvent(" +
                String.valueOf(target.X_Position) + "," + String.valueOf(target.Y_Position);
        m_sender.sendMessage(bug.QueueName, textMessage);
        if (isSmash()) {
            System.out.println("Bug is dead...");
            System.exit(0);
        }
    }

    private void draw()
    {
        m_sender.sendMessage(bug.QueueName, "draw");
        m_sender.sendMessage(target.QueueName, "draw");
    }
}
