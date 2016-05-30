/**
 * 
 */
package it.eurotn.panjea.auvend.rich.commands;

import it.eurotn.panjea.auvend.rich.bd.IAuVendBD;
import it.eurotn.panjea.auvend.util.ParametriRecuperoMovimenti;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * Command per l'apertura dell'editor per il recupero dei rifornimenti di AuVend.
 * 
 * @author adriano
 * @version 1.0, 13/gen/2009
 * 
 */
public class OpenRecuperoMovimentiAuVendCommand extends ApplicationWindowAwareCommand {

	private static final String COMMAND_ID = "openRecuperoMovimentiAuVendCommand";

	private Logger logger = Logger.getLogger(OpenRecuperoMovimentiAuVendCommand.class);

	private String auVendBDBeanName;

	/**
	 * 
	 */
	public OpenRecuperoMovimentiAuVendCommand() {
		super(COMMAND_ID);
	}

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		IAuVendBD auVendBD = RcpSupport.getBean(IAuVendBD.BEAN_ID);
		ParametriRecuperoMovimenti parametri = new ParametriRecuperoMovimenti();
		parametri.setDataFine(Calendar.getInstance().getTime());
		parametri.setDataInizio(auVendBD.caricaLetturaFlussoMovimenti().getUltimaLetturaFlussoMovimenti());
		LifecycleApplicationEvent event = new OpenEditorEvent(parametri);
		Application.instance().getApplicationContext().publishEvent(event);
		logger.debug("--> Exit doExecuteCommand");
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
