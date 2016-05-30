/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.jms;

import it.eurotn.panjea.contabilita.domain.Giornale;
import it.eurotn.panjea.rich.jms.IMessageDelegate;
import it.eurotn.rich.dialog.MessageAlert;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;

/**
 * @author Leonardo
 * 
 */
public class LibroGiornaleInvalidatoMessageDelegate implements IMessageDelegate {

	private static Logger logger = Logger.getLogger(LibroGiornaleInvalidatoMessageDelegate.class);
	private final MessageSource messageSource;

	/**
	 * 
	 */
	public LibroGiornaleInvalidatoMessageDelegate() {
		super();
		messageSource = (MessageSource) Application.services().getService(MessageSource.class);

	}

	@Override
	public void handleMessage(ObjectMessage message) {
		try {
			Giornale giornale = (Giornale) message.getObject();

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, giornale.getAnno());
			calendar.set(Calendar.MONTH, giornale.getMese() - 1);

			Date date = calendar.getTime();
			Format format = new SimpleDateFormat("MMMMM yyyy");
			String dateString = format.format(date);

			String titolo = messageSource.getMessage("libroGiornaleInvalidato.title", null, Locale.getDefault());
			String messaggio = messageSource.getMessage("libroGiornaleInvalidato.description",
					new String[] { dateString }, Locale.getDefault());
			Message msg = new DefaultMessage(titolo + "\n" + messaggio, Severity.INFO);
			MessageAlert messageAlert = new MessageAlert(msg);
			messageAlert.showAlert();
		} catch (JMSException e) {
			logger.error("--> errore in handleMessage", e);
		}
	}

}
