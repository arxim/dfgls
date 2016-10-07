package df.bean.obj.mail;

import java.util.Properties;
import javax.mail.PasswordAuthentication;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class Mail{
      
      public static void main(String[] args) {
    	  Properties props = new Properties();
  		props.put("mail.smtp.host", "mail.scapsolutions.com");
  		//props.put("mail.smtp.socketFactory.port", "25");
  		//props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
  		props.put("mail.smtp.auth", "true");
  		props.put("mail.smtp.port", "25");
   
  		Session session = Session.getDefaultInstance(props,
  			new javax.mail.Authenticator() {
  				protected PasswordAuthentication getPasswordAuthentication() {
  					return new PasswordAuthentication("nopphadon@scapsolutions.com","oiupoi");
  				}
  			});
   
  		try {
   
  			Message message = new MimeMessage(session);
  			message.setFrom(new InternetAddress("nopphadon@scapsolutions.com"));
  			message.setRecipients(Message.RecipientType.CC,
  					InternetAddress.parse("sarunyoo@scapsolutions.com"));
  			message.setRecipients(Message.RecipientType.TO,
  					InternetAddress.parse("s_payaprom@hotmail.com"));

  			message.setSubject("Testing Subject");
  			message.setText("Dear Mail Crawler," +
  					"\n\n No spam to my email, please!");
   
  			Transport.send(message);
   
  			System.out.println("Done");
   
  		} catch (MessagingException e) {
  			throw new RuntimeException(e);
  		}
  	}
}