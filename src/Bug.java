import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class Bug extends GameObject {

    public final double maxVelocity = 0.1;
    public final double maxAngularVelocity = 0.001;

    private double duration = 10;

    private final Timer m_timer = initTimer();
    static volatile LinkedList<String> m_buffer = new LinkedList<>();
    private MessageReceiver m_receiver;
    private final Object m_objForSync = new Object();

    public Bug(double x, double y, String queueName)
    {
        super(x, y, "bug_1.png", FieldCell.translateFactor, queueName);

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
                onModelUpdateEvent(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
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

    private double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    private double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    private double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private void move(double angularVelocity) {
        double velocity = applyLimits(maxVelocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = X_Position + velocity / angularVelocity *
                (Math.sin(Direction + angularVelocity * duration) -
                        Math.sin(Direction));
        if (!Double.isFinite(newX)) {
            newX = X_Position + velocity * duration * Math.cos(Direction);
        }
        double newY = Y_Position - velocity / angularVelocity *
                (Math.cos(Direction + angularVelocity * duration) -
                        Math.cos(Direction));
        if (!Double.isFinite(newY)) {
            newY = Y_Position + velocity * duration * Math.sin(Direction);
        }
        X_Position = newX;
        Y_Position = newY;
        Direction = asNormalizedRadians(Direction + angularVelocity * duration);
    }

    private void onModelUpdateEvent(double targetX, double targetY) {
        double dist = distance(targetX, targetY, X_Position, Y_Position);
        if (dist < 0.5) {
            return;
        }
        double angleToTarget = angleTo(X_Position, Y_Position, targetX, targetY);
        double angularVelocity = 0;
        if (angleToTarget > Direction) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < Direction) {
            angularVelocity = - maxAngularVelocity;
        }
        move(angularVelocity);
    }
}
