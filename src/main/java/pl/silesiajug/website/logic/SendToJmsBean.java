package pl.silesiajug.website.logic;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import pl.silesiajug.website.model.RegistrationKey;
import pl.silesiajug.website.model.Users;

/**
 *
 * @author Adam Szecowka
 */
@Stateless
public class SendToJmsBean {

    @Resource(mappedName = "jms/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/queue")
    private Queue queue;

    public void wyslijWiadomoscDoKolejki(Users user, RegistrationKey rk) {
        try {

            MessageProducer messageProducer;
            MapMessage mapMsg;
            Connection connection = connectionFactory.createConnection();
            javax.jms.Session session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
            messageProducer = session.createProducer(queue);
            mapMsg = session.createMapMessage();
            mapMsg.setString("USER_NAME", user.getName());
            mapMsg.setString("USER_EMAIL", user.getEmail());
            mapMsg.setString("KEY", rk.getUserKey());
            messageProducer.send(mapMsg);
            messageProducer.close();
            session.close();
            connection.close();
        } catch (JMSException ex) {
            Logger.getLogger(SendToJmsBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
