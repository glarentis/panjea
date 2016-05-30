/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.jms;

import it.eurotn.panjea.rich.jms.IMessageDelegate;
import it.eurotn.rich.dialog.MessageAlert;

import java.util.Locale;

import javax.jms.ObjectMessage;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;

/**
 * @author Leonardo
 * 
 */
public class ContoEntitaNonAssociatiMessageDelegate implements IMessageDelegate {

	private MessageSource messageSource;

	/**
	 * Costruttore.
	 * 
	 */
	public ContoEntitaNonAssociatiMessageDelegate() {
		super();
		messageSource = (MessageSource) Application.services().getService(MessageSource.class);
	}

	@Override
	public void handleMessage(ObjectMessage message) {
		String titolo = messageSource.getMessage("contoEntitaNonAssociati.title", null, Locale.getDefault());
		String messaggio = messageSource.getMessage("contoEntitaNonAssociati.description", null, Locale.getDefault());
		Message msg = new DefaultMessage(titolo + "\n" + messaggio, Severity.INFO);
		MessageAlert messageAlert = new MessageAlert(msg);
		messageAlert.showAlert();
	}
}
