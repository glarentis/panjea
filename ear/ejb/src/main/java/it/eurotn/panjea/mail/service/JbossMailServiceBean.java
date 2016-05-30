package it.eurotn.panjea.mail.service;

import java.io.File;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Address;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

@Stateless(name = "Panjea.Jboss.Mail.Service")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.Jboss.Mail.Service")
public class JbossMailServiceBean implements JbossMailService {

	private static Logger logger = Logger.getLogger(JbossMailServiceBean.class);

	@Resource(mappedName = "java:/Mail")
	private javax.mail.Session session;

	@Override
	public boolean send(String subject, String bodyText, File[] attachments) {
		try {
			String mailUser = session.getProperty("mail.user");
			if (mailUser != null && "nobody".equals(mailUser)) {
				logger.error("Impossibile inviare la mail,controllare la configurazione nel file mail-service.xml");
				return false;
			}

			Address[] to = InternetAddress.parse(mailUser, false);
			// create message
			javax.mail.Message message = new MimeMessage(session);
			message.setFrom();
			message.setRecipients(javax.mail.Message.RecipientType.TO, to);
			message.setSubject(subject);
			message.setSentDate(new Date());

			// nel caso ci sia file allegato
			if (attachments != null) {
				MimeBodyPart messagePart = new MimeBodyPart();
				messagePart.setContent(bodyText, "text/html");
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messagePart);

				for (final File file : attachments) {
					MimeBodyPart attachmentPart = new MimeBodyPart();

					FileDataSource fileDataSource = new FileDataSource(file) {

						@Override
						public String getContentType() {
							return "text/plain";
						}

					};
					attachmentPart.setDataHandler(new DataHandler(fileDataSource));
					attachmentPart.setFileName(file.getName());
					multipart.addBodyPart(attachmentPart);
				}

				message.setContent(multipart);
			} else {
				message.setContent(bodyText.toString(), "text/html");
			}

			// Send message
			Transport.send(message);
			logger.debug("Mail spedita");
		} catch (Exception e) {
			logger.error("Errore nell'invio della mail,controllare la configurazione nel file mail-service.xml", e);
			return false;
		}
		return true;
	}

}
