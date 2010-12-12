/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.silesiajug.website.logic.mdb;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Adam Szecowka
 */
@MessageDriven(mappedName = "jms/SilesiaJUGMailConfirmationQueue", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class MDBSendMail implements MessageListener {

    @Resource(name = "mail/SilesiaJUGMailSession")
    private javax.mail.Session mailSession;

    public MDBSendMail() {
    }

    @Override
    public void onMessage(Message message) {
        try {
            final String EMAIL_SUBJECT = "Silesia JUG - Potwierdzenie";
            MapMessage mapMessage = (MapMessage) message;
            String userName = mapMessage.getString("USER_NAME");
            String userEmail = mapMessage.getString("USER_EMAIL");
            String key = mapMessage.getString("KEY");
            String URL = "http://localhost:8080/SilesiaJUG/website/potwierdzenie.xhtml?key=" + key;
            javax.mail.Message mailMsg = new MimeMessage(mailSession);
            try {
                mailMsg.setSubject(EMAIL_SUBJECT);
                mailMsg.setRecipient(RecipientType.TO,
                        new InternetAddress(userEmail,
                        userName));
                mailMsg.setText("Witaj " + userName + "!\n Aby zakonczyć proces rejestracji na stronie Silesia JUG kliknij w poniższy link:\n" + URL + "\n\nPozdrawiamy\nSilesia JUG");
                Transport.send(mailMsg);
            } catch (MessagingException me) {
                // manage exception
                Logger.getLogger(MDBSendMail.class.getName()).log(Level.SEVERE, null, me);
            } catch (UnsupportedEncodingException uee) {
                // manage exception
                Logger.getLogger(MDBSendMail.class.getName()).log(Level.SEVERE, null, uee);
            }
        } catch (JMSException ex) {
            Logger.getLogger(MDBSendMail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
