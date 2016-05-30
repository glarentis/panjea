/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.simulazioni;

import it.eurotn.panjea.beniammortizzabili.exception.SottocontiBeniNonValidiException;
import it.eurotn.panjea.beniammortizzabili.rich.bd.BeniAmmortizzabiliContabilitaBD;
import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliContabilitaBD;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

/**
 * @author fattazzo
 *
 */
public class CreaAreeContabiliSimulazioneCommand extends ActionCommand {

	public static final String PARAM_SIMULAZIONE_PAGE = "paramSimulazionePage";

	private IBeniAmmortizzabiliContabilitaBD beniAmmortizzabiliContabilitaBD;
	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	private SottocontiBeniExceptionDialog sottocontiBeniExceptionDialog;

	/**
	 * Costruttore.
	 */
	public CreaAreeContabiliSimulazioneCommand() {
		super("creaAreeContabiliSimulazioneCommand");
		RcpSupport.configure(this);
		beniAmmortizzabiliContabilitaBD = RcpSupport.getBean(BeniAmmortizzabiliContabilitaBD.BEAN_ID);
		beniAmmortizzabiliBD = RcpSupport.getBean("beniAmmortizzabiliBD");

		sottocontiBeniExceptionDialog = new SottocontiBeniExceptionDialog();
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
			beniAmmortizzabiliContabilitaBD.creaAreeContabili(simulazione.getId());

			// ricarico la simulazione per avere simulazione e politiche aggiornate con i link alle aree contabili
			simulazione = beniAmmortizzabiliBD.caricaSimulazione(simulazione);
			LifecycleApplicationEvent event = new OpenEditorEvent(simulazione);
			Application.instance().getApplicationContext().publishEvent(event);

			// apro l'editor per visualizzare le aree contabili generate
			ParametriRicercaMovimentiContabili parametri = new ParametriRicercaMovimentiContabili();
			parametri.setAnnoCompetenza(simulazione.getAnno().toString());
			parametri.getDataRegistrazione().setTipoPeriodo(TipoPeriodo.DATE);
			parametri.getDataRegistrazione().setDataIniziale(simulazione.getData());
			parametri.getDataRegistrazione().setDataFinale(simulazione.getData());
			parametri.getStatiAreaContabile().clear();
			parametri.getStatiAreaContabile().add(StatoAreaContabile.SIMULATO);
			parametri.setEffettuaRicerca(true);
			event = new OpenEditorEvent(parametri);
			Application.instance().getApplicationContext().publishEvent(event);
		} catch (SottocontiBeniNonValidiException e) {
			sottocontiBeniExceptionDialog.setSottocontiBeniException(e);
			sottocontiBeniExceptionDialog.showDialog();
		} finally {
			simulazionePage.setReadOnly(false);
		}
	}

}
