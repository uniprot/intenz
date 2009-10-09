package uk.ac.ebi.intenz.webapp.utilities;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

/**
 * This class ...
 *
 * @author Michael Darsow
 * @version 0.1 - 11-May-2004
 */
public class IntEnzMessenger {

    public static final Logger LOGGER = Logger.getLogger(IntEnzMessenger.class);
    
    private static final Properties mailProperties;
    
    static {
    	mailProperties = new Properties();
    	try {
			mailProperties.load(IntEnzMessenger.class.getClassLoader().getResourceAsStream("mail.properties"));
		} catch (IOException e) {
			LOGGER.error("Error loading mail properties", e);
		}
    }

    public static void sendError(String className, String errorMessage, String userName) {
      Date today = new Date();
      Session session = Session.getDefaultInstance(mailProperties, null);
      Message message = new MimeMessage(session);
      try {
        InternetAddress fromAddress = new InternetAddress(mailProperties.getProperty("intenz.mail.from"));
        message.setFrom(fromAddress);
        InternetAddress[] toAddress = InternetAddress.parse(mailProperties.getProperty("intenz.error.mail.to"), false);
        message.setRecipients(Message.RecipientType.TO, toAddress);
        message.setSubject(mailProperties.getProperty("intenz.error.mail.subject"));
        StringBuffer text = new StringBuffer();
        text.append("Class name: ");
        text.append(className);
//        text.append("\nUser name: ");
//        text.append(userName);
        text.append("\nError message: ");
        text.append(errorMessage);
        message.setText(text.toString());
        message.setSentDate(today);
        Transport.send(message);
      } catch (MessagingException e) {
        e.printStackTrace();
      }
    }
  }
