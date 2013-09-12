package uk.ac.ebi.intenz.webapp.controller;

import java.io.IOException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import uk.ac.ebi.biobabel.util.StringUtil;
import uk.ac.ebi.intenz.webapp.IntEnzConfig;

public class IntEnzContactServlet extends HttpServlet {

	public static final Logger LOGGER = Logger.getLogger(IntEnzContactServlet.class);
	
	public void init() throws ServletException {
		super.init();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		getServletContext().getRequestDispatcher("/contact.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		if (request.getParameter("intenzMail") == null){
			getServletContext().getRequestDispatcher("/contact.jsp").forward(request, response);
		}

		String from = request.getParameter("email");
//		String subject = request.getParameter("subject");
		String messageText = request.getParameter("message");
		
		if (StringUtil.isNullOrEmpty(from)){
			processError(request, response, "The e-mail address must not be empty!");
			return;
		}
		if (StringUtil.isNullOrEmpty(messageText)){
			processError(request, response, "The message text must not be empty!");
			return;
		}
//		if (StringUtil.isNullOrEmpty(subject)) subject = "IntEnz";
		
		try {
			sendMail(from, messageText, (IntEnzConfig) request.getSession()
			        .getServletContext().getAttribute("intenzConfig"));
			request.setAttribute("sent", Boolean.TRUE);
			getServletContext().getRequestDispatcher("/contact.jsp").forward(request, response);
		} catch (MessagingException e) {
			if (e.getMessage().contains("553")){
				processError(request, response, "Sorry, the provided e-mail address is not valid.");
			} else {
				processError(request, response, "Sorry, the message could not be sent. Please try again later.");
			}
		}
	}

	private void processError(ServletRequest request, ServletResponse response, String errorText)
	throws ServletException, IOException {
		request.setAttribute("sent", Boolean.FALSE);
		request.setAttribute("error", errorText);
		getServletContext().getRequestDispatcher("/contact.jsp").forward(request, response);
	}

	private void sendMail(String from, String messageText, IntEnzConfig config)
	throws MessagingException, IOException{
		try{
            Session mailSession =
                    Session.getInstance(config.getMailProperties());
			MimeMessage message = new MimeMessage(mailSession);
			Address fromAddress = new InternetAddress(from);
			message.setFrom(fromAddress);
			Address[] toAddress =
				InternetAddress.parse(config.getContactMailTo());
			message.setRecipients(Message.RecipientType.TO, toAddress);
			message.setSubject(config.getContactMailSubject());
			message.setText(messageText);
			Transport.send(message);
		} catch (MessagingException e){
			LOGGER.error("Problem sending an email from " + from, e);
			throw e;
        }

    }
}
