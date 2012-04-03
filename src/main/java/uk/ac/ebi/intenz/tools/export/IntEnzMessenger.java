package uk.ac.ebi.intenz.tools.export;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.util.Date;
import java.util.Properties;
import java.io.File;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Transport;

/**
 * This class ...
 * 
 * @author Michael Darsow
 * @version 0.1 - 11-May-2004
 */
public class IntEnzMessenger {

  private static final String SMTP_SERVER = "mailserv.ebi.ac.uk";
  private static final String SENDER = "mdarsow@ebi.ac.uk";

  public static void sendError(String className, String errorMessage, String userName) {
    Date today = new Date();
    Properties properties = System.getProperties();
    properties.put("mail.smtp.host", SMTP_SERVER);
    Session session = Session.getDefaultInstance(properties, null);
    Message message = new MimeMessage(session);
    try {
      message.setFrom(new InternetAddress(SENDER));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(SENDER, false));
      message.setSubject("Notification error " + today.toString());
      StringBuffer text = new StringBuffer();
      text.append("Location: ");
      text.append(className);
      text.append("\nUser name: ");
      text.append(userName);
      text.append("\nError message: ");
      text.append(errorMessage);
      message.setText(text.toString());
      message.setSentDate(today);
      Transport.send(message);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  public static void sendNotificationWithAttachment(String from, String to, String subject, String messageText, File singleAttachment) {
    Date today = new Date();
    Properties properties = System.getProperties();
    properties.put("mail.smtp.host", SMTP_SERVER);
    Session session = Session.getDefaultInstance(properties, null);
    Message message = new MimeMessage(session);
    try {
      message.setFrom(new InternetAddress(from));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
      message.setSubject(subject);
      MimeMultipart mp = new MimeMultipart();
      MimeBodyPart mbp = new MimeBodyPart();
      mbp.setDisposition(Part.INLINE);
      mbp.setContent(messageText, "text/plain");
      mp.addBodyPart(mbp);

      BodyPart file_part = new MimeBodyPart();
      FileDataSource fds = new FileDataSource(singleAttachment);
      DataHandler dh = new DataHandler(fds);
      file_part.setFileName(singleAttachment.getName());
      file_part.setDisposition(Part.ATTACHMENT);
      file_part.setDescription("Attached file: " + singleAttachment.getName());
      file_part.setDataHandler(dh);
      mp.addBodyPart(file_part);

      message.setContent(mp);
      message.setSentDate(today);
      Transport.send(message);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
