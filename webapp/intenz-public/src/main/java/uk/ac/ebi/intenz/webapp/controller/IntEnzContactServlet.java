package uk.ac.ebi.intenz.webapp.controller;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
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

public class IntEnzContactServlet extends HttpServlet {

	public static final Logger LOGGER = Logger.getLogger(IntEnzContactServlet.class);
	
	private Properties mailProperties;
	
	public void init() throws ServletException {
		mailProperties = new Properties();
		try {
			mailProperties.load(this.getClass().getClassLoader().getResourceAsStream("intenz-public-mail.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			sendMail(from, messageText);
			request.setAttribute("sent", Boolean.TRUE);
			getServletContext().getRequestDispatcher("/contact.jsp").forward(request, response);
		} catch (MessagingException e) {
			if (e.getMessage().indexOf("553") > -1){
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

	private void sendMail(String from, String messageText)
	throws MessagingException{
		try{
			Session mailSession = Session.getInstance(mailProperties, null);
			MimeMessage message = new MimeMessage(mailSession);
			Address fromAddress = new InternetAddress(from);
			message.setFrom(fromAddress);
			Address[] toAddress = InternetAddress.parse(mailProperties.getProperty("intenz.mail.to"));
			message.setRecipients(Message.RecipientType.TO, toAddress);
			message.setSubject(mailProperties.getProperty("intenz.mail.subject"));
			message.setText(messageText);
			Transport.send(message);
		} catch (MessagingException e){
			LOGGER.error("There has been an error sending an email from " + from, e);
			throw e;
		}
		
	}
}
