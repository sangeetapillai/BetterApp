package utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class EmailUtility {

	

	private static String userName = "viq.betting@gmail.com";
	private static String password = "csolapar";
	private static String emailSubject = "Activation link";
	private static String host = "smtp.gmail.com";
	private static String port = "25";
	private static String appLink = "http://13.232.96.213/resetPassword";	
	public boolean sendEmail( String toEmailAddress) {            
		boolean success = false;		
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port",port);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.required", "true");
//			Session session = Session.getDefaultInstance(props, null);
			Session session = Session.getDefaultInstance(props,    
	                new javax.mail.Authenticator() {    
	                protected PasswordAuthentication getPasswordAuthentication() {    
	                return new PasswordAuthentication(userName,password);  
	                }    
	               });
			// Create email message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(userName));
			String[] recipientList = toEmailAddress.split(",");
			InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
			int counter = 0;
			for (String recipient: recipientList) {
				recipientAddress[counter] = new InternetAddress(recipient.trim());
				counter++;
			}
			message.setRecipients(Message.RecipientType.TO, recipientAddress);
			message.setSubject(emailSubject);
			message.setContent(getEmailContent(toEmailAddress), "text/html");
			// Send smtp message
			Transport tr = session.getTransport("smtp");
			tr.connect(host, 25, userName, password);
			message.saveChanges();
			tr.sendMessage(message, message.getAllRecipients());
			tr.close();
			success = true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return success;
	}

	public String getEmailContent(String userEmail){
		StringBuilder builder = new StringBuilder();
		builder.append(appLink);
		builder.append("?userEmail=");
		builder.append(userEmail);
		builder.append("&code=");		
		builder.append(getSecretCode(userEmail));	
		return builder.toString();
	}
	private int getSecretCode(String userEmail){
		HashCodeBuilder b = new HashCodeBuilder(17, 37);
		return b.append(userEmail).toHashCode();
	}
	
	public boolean validateSecretCode(String userEmail,int code){
		boolean valid = false;
		int secCode = getSecretCode(userEmail);
		if(secCode == code){
			valid = true; 
		}
		return valid;		
	}
	public static void main(String args[]){
		EmailUtility e = new EmailUtility();
		e.sendEmail("suma.nair@nielsen.com");
	}

}
