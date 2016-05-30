/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.simulazioni;

import it.eurotn.panjea.beniammortizzabili.rich.bd.BeniAmmortizzabiliContabilitaBD;
import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliContabilitaBD;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author fattazzo
 *
 */
public class ConfermaAreeContabiliSimulazioneCommand extends ActionCommand {

	public static final String PARAM_SIMULAZIONE_PAGE = "paramSimulazionePage";

	private IBeniAmmortizzabiliContabilitaBD beniAmmortizzabiliContabilitaBD;
	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	/**
	 * Costruttore.
	 */
	public ConfermaAreeContabiliSimulazioneCommand() {
		super("confermaAreeContabiliSimulazioneCommand");
		RcpSupport.configure(this);
		beniAmmortizzabiliContabilitaBD = RcpSupport.getBean(BeniAmmortizzabiliContabilitaBD.BEAN_ID);
		beniAmmortizzabiliBD = RcpSupport.getBean("beniAmmortizzabiliBD");
	}

	@Override
	protected void doExecuteCommand() {

		SimulazionePage simulazionePage = (SimulazionePage) getParameter(PARAM_SIMULAZIONE_PAGE, null);

		if (simulazionePage == null || simulazionePage.getBackingFormPage().getFormObject() == null) {
			return;
		}

		try {
			simulazionePage.setReadOnly(true);

			Simulazione simulazione = (Simulazione) simulazionePage.getBackingFormPage().getFormObject();
			beniAmmortizzabiliContabilitaBD.confermaAreeContaibliSimulazione(simulazione.getId());

			simulazione = beniAmmortizzabiliBD.caricaSimulazione(simulazione);
			LifecycleApplicationEvent event = new OpenEditorEvent(simulazione);
			Application.instance().getApplicationContext().publishEvent(event);
		} finally {
			simulazionePage.setReadOnly(false);
		}

	}

}
