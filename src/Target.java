import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

class Target extends GameObject {

    private final Timer m_timer = initTimer();
    static volatile LinkedList<String> m_buffer = new LinkedList<>();
    private MessageReceiver m_receiver;
    private final Object m_objForSync = new Object();

    Target(double x, double y, String queueName)
    {
        super(x, y, "apple.png", 30, queueName);
        Direction = - Math.PI / 2;
        m_receiver = new MessageReceiver(queueName, m_buffer);
        m_receiver.startReceiveMessages();

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                bufferChecker();
            }
        }, 0, 2);
    }

    private Timer initTimer() {
        Timer timer = new Timer("messages checker", true);
        return timer;
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
            case "setTargetPosition":
                setTargetPosition(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
                break;
        }

    }

    private void setTargetPosition(double x, double y) {
        X_Position = x;
        Y_Position = y;
    }

}
