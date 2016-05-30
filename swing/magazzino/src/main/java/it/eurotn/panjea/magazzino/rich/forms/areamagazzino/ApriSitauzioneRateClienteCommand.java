package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * Command per aprire l'editor per la ricerca rate con impostato i parametri di ricerca per TROVARE IL FIDO UTILIZZATO.
 * 
 * @author Leonardo
 */
public class ApriSitauzioneRateClienteCommand extends ApplicationWindowAwareCommand {

	protected static Logger logger = Logger.getLogger(ApriSitauzioneRateClienteCommand.class);

	public static final String COMMAND_ID = "apriSitauzioneRateClienteCommand";

	private EntitaLite entita;

	/**
	 * Costruttore.
	 */
	public ApriSitauzioneRateClienteCommand() {
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		logger.debug("--> Enter doExecuteCommand");
		LifecycleApplicationEvent event = new OpenEditorEvent(
				ParametriRicercaRate.creaParametriRicercaRateAperte(entita));
		Application.instance().getApplicationContext().publishEvent(event);
		logger.debug("--> Exit doExecuteCommand");
	}

	/**
	 * setter for entitaLite.
	 * 
	 * @param value
	 *            entita da ricercare
	 */
	public void setEntita(EntitaLite value) {
		this.entita = value;
	}

}