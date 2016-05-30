/**
 * 
 */
package it.eurotn.panjea.auvend.rich.commands;

import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.auvend.util.ParametriRecuperoFatture;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * 
 * 
 * @author adriano
 * @version 1.0, 30/gen/2009
 * 
 */
public class OpenRecuperoFattureAuVendCommand extends ApplicationWindowAwareCommand {

	private Logger logger = Logger.getLogger(OpenRecuperoFattureAuVendCommand.class);

	private static final String COMMAND_ID = "openRecuperoFattureAuVendCommand";

	private IAuVendBD auVendBD;

	private String auVendBDBeanName;

	/**
	 * Costruttore.
	 * 
	 */
	public OpenRecuperoFattureAuVendCommand() {
		super(COMMAND_ID);
	}

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		ParametriRecuperoFatture parametriRecuperoFatture = new ParametriRecuperoFatture();
		Calendar calendar = Calendar.getInstance();
		parametriRecuperoFatture.setDataRiferimento(calendar.getTime());
		parametriRecuperoFatture.setLettureFlussoAuVend(getAuVendBD().caricaLettureFlussoAuVend());
		LifecycleApplicationEvent event = new OpenEditorEvent(parametriRecuperoFatture);
		Application.instance().getApplicationContext().publishEvent(event);
		logger.debug("--> Exit doExecuteCommand");
	}

	/**
	 * @return the auVendBD
	 */
	private IAuVendBD getAuVendBD() {
		if (auVendBD == null) {
			auVendBD = (IAuVendBD) Application.instance().getApplicationContext().getBean(auVendBDBeanName);
		}
		return auVendBD;
	}

	/**
	 * @return the auVendBDBeanName
	 */
	public String getAuVendBDBeanName() {
		return auVendBDBeanName;
	}

	/**
	 * @param auVendBDBeanName
	 *            the auVendBDBeanName to set
	 */
	public void setAuVendBDBeanName(String auVendBDBeanName) {
		this.auVendBDBeanName = auVendBDBeanName;
	}
}
