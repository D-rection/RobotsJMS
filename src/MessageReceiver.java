import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.LinkedList;

class MessageReceiver {

    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    private static String nameQueue;

    private volatile LinkedList<String> buffer;

    MessageReceiver(String queueName, LinkedList<String> curBuffer) {

        buffer = curBuffer;
        nameQueue = queueName;
    }

    public void startReceiveMessages(){

        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue(nameQueue);
            MessageConsumer consumer = session.createConsumer(destination);
            MyListener listener = new MyListener(buffer);
            consumer.setMessageListener(listener);

        }
        catch (JMSException e) {
            System.out.println(e);
        }

    }
}
