package it.eurotn.panjea.mrp.rich.editors.risultato.command;

import it.eurotn.dao.exception.ProtocolloNonEsistenteException;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.mrp.rich.bd.IMrpBD;
import it.eurotn.panjea.mrp.util.ParametriMrpRisultato;
import it.eurotn.panjea.ordini.exception.TipoAreaOrdineAssenteException;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine;
import it.eurotn.rich.dialog.MessageAlert;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class GeneraOrdiniCommand extends ActionCommand {

	private static final String COMMAND_ID = "generaOrdiniCommand";

	public static final String PARAM_RISULTATI_ID = "paramRisultatiId";

	/**
	 * Costruttore.
	 */
	public GeneraOrdiniCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		ParametriMrpRisultato parametriMrpRisultato = (ParametriMrpRisultato) getParameter("parametriMrp");
		Integer[] risultatiId = (Integer[]) getParameter(PARAM_RISULTATI_ID, null);

		Message messageMrp = new DefaultMessage("Generazione ordini in corso...", Severity.INFO);
		MessageAlert mrpAlert = new MessageAlert(messageMrp);
		mrpAlert.showAlert();
		try {
			IMrpBD mrpBD = RcpSupport.getBean(IMrpBD.BEAN_ID);
			Long timeStmapCreazione = mrpBD.generaOrdini(risultatiId);

			// lancio i parametri mrp per rieseguire la ricerca a generazione avvenuta
			OpenEditorEvent event = new OpenEditorEvent(parametriMrpRisultato);
			Application.instance().getApplicationContext().publishEvent(event);

			// apro la ricerca ordini per data di creazione dei documenti generati
			ParametriRicercaAreaOrdine parametriRicercaAreaOrdine = new ParametriRicercaAreaOrdine();
			parametriRicercaAreaOrdine.setEffettuaRicerca(true);
			parametriRicercaAreaOrdine.setDataCreazioneTimeStamp(timeStmapCreazione);

			OpenEditorEvent eventRicercaOrdini = new OpenEditorEvent(parametriRicercaAreaOrdine);
			Application.instance().getApplicationContext().publishEvent(eventRicercaOrdini);

		} catch (Exception e) {
			if (e.getCause() != null && e.getCause().getCause() instanceof TipoAreaOrdineAssenteException) {
				throw (PanjeaRuntimeException) e;
			}
			if (e.getCause() != null && e.getCause().getCause() != null && e.getCause().getCause().getCause() != null
					&& e.getCause().getCause().getCause().getCause() instanceof ProtocolloNonEsistenteException) {
				throw (ProtocolloNonEsistenteException) e.getCause().getCause().getCause().getCause();
			}
			logger.error("-->errore nel calcolo mrp ", e);
			Message messageErrorMrp = new DefaultMessage("Errore nel calcolo mrp...", Severity.ERROR);
			MessageAlert mrpErrorAlert = new MessageAlert(messageErrorMrp, 5);
			mrpErrorAlert.showAlert();
		} finally {
			mrpAlert.closeAlert();
		}
	}

}
