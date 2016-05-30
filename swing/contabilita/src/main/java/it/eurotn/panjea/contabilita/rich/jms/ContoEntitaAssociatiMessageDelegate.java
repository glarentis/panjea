/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.jms;

import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.rich.jms.IMessageDelegate;
import it.eurotn.rich.dialog.MessageAlert;

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
public class ContoEntitaAssociatiMessageDelegate implements IMessageDelegate {

	private static Logger logger = Logger.getLogger(ContoEntitaAssociatiMessageDelegate.class);
	private MessageSource messageSource;

	/**
	 * 
	 */
	public ContoEntitaAssociatiMessageDelegate() {
		super();
		messageSource = (MessageSource) Application.services().getService(MessageSource.class);
	}

	@Override
	public void handleMessage(ObjectMessage message) {
		try {
			SottoConto sottoConto = (SottoConto) message.getObject();
			String titolo = messageSource.getMessage("contoEntitaAssociati.title", null, Locale.getDefault());
			String messaggio = messageSource.getMessage("contoEntitaAssociati.description",
					new String[] { sottoConto.getSottoContoCodice() }, Locale.getDefault());
			Message msg = new DefaultMessage(titolo + "\n" + messaggio, Severity.INFO);
			MessageAlert messageAlert = new MessageAlert(msg);
			messageAlert.showAlert();
		} catch (JMSException e) {
			logger.error("--> errore in handleMessage", e);
		}
	}

}
