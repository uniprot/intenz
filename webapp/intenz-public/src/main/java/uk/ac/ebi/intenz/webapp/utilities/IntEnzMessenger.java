package uk.ac.ebi.intenz.webapp.utilities;

import java.util.Date;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import uk.ac.ebi.intenz.webapp.IntEnzConfig;

/**
 * This class ...
 *
 * @author Michael Darsow
 * @version 0.1 - 11-May-2004
 */
public class IntEnzMessenger {

    public static final Logger LOGGER = Logger.getLogger(IntEnzMessenger.class);

    public static void sendError(String className, String errorMessage, String userName) {
      Date today = new Date();
      try {
          IntEnzConfig intenzConfig = IntEnzConfig.getInstance();
          Session session = Session.getDefaultInstance(
                intenzConfig.getMailProperties(), null);
          Message message = new MimeMessage(session);
        InternetAddress fromAddress = new InternetAddress(intenzConfig.getMailFrom());
        message.setFrom(fromAddress);
        InternetAddress[] toAddress = InternetAddress.parse(intenzConfig.getErrorMailTo(), false);
        message.setRecipients(Message.RecipientType.TO, toAddress);
        message.setSubject(intenzConfig.getErrorMailSubject());
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
      } catch (Exception e) {
        LOGGER.error("Unable to send error report", e);
      }
    }
  }
