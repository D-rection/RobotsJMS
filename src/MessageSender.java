import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageSender {

    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String nameQueue;
    public void sendMessage(String queue, String message) {

        try {
            nameQueue = queue;
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(nameQueue);
            MessageProducer producer = session.createProducer(destination);
            TextMessage sendingMessage = session
                    .createTextMessage(message);
            producer.send(sendingMessage);
            System.out.println("Отправила " + nameQueue + ": '" + sendingMessage.getText() + "'");
            connection.close();
        }
        catch (JMSException e) {
            System.out.println(e);
        }
    }
}
