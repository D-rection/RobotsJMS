import javax.jms.*;
import java.util.LinkedList;

public class MyListener implements MessageListener {

    private volatile LinkedList<String> buffer;

    MyListener(LinkedList<String> curBuffer) {
        buffer = curBuffer;
    }

    public void onMessage(Message m) {
        try{
            TextMessage msg = (TextMessage)m;
            buffer.add(msg.getText());
        }catch(JMSException e){System.out.println(e);}
    }
}
