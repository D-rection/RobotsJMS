import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageReceiver {

    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    private static String nameQueue;

    public MessageReceiver(String queueName) {

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

            MyListener listener = new MyListener();

            consumer.setMessageListener(listener);

            System.out.println(nameQueue + " is ready, waiting for messages...");
        }
        catch (JMSException e) {
            System.out.println(e);
        }

    }
}
