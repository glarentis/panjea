package it.eurotn.panjea.magazzino.rich.editors.fatturazione.consultazione;

import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.service.exception.FatturazioneContabilizzazioneException;
import it.eurotn.panjea.utils.PanjeaSwingUtil;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class ConfermaMovimentiCommand extends ActionCommand {

	private static Logger logger = Logger.getLogger(ConfermaMovimentiCommand.class);
	private static final String COMMAND_ID = "confermaMovimentiCommand";
	public static final String PARAM_MAGAZZINO_DOCUMENTO_BD = "paramMagazzinoDocumentoDB";
	public static final String PARAM_UTENTE = "paramUtente";

	private DatiGenerazione datiGenerazione;

	/**
	 * Costruttore.
	 *
	 */
	public ConfermaMovimentiCommand() {
		super(COMMAND_ID);
		RcpSupport.configure(this);
	}

	@Override
	protected void doExecuteCommand() {
		datiGenerazione = null;

		try {
			IMagazzinoDocumentoBD magazzinoDocumentoBD = (IMagazzinoDocumentoBD) getParameter(
					PARAM_MAGAZZINO_DOCUMENTO_BD, null);
			String utente = (String) getParameter(PARAM_UTENTE, "");
			datiGenerazione = magazzinoDocumentoBD.confermaMovimentiInFatturazione(utente);
		} catch (FatturazioneContabilizzazioneException e) {
			datiGenerazione = e.getDatiGenerazione();
			LifecycleApplicationEvent event = new OpenEditorEvent(e.getContabilizzazioneException());
			Application.instance().getApplicationContext().publishEvent(event);
		} catch (Exception e) {
			// Rilancio l'eccezione prima però chiamo la postExecute che non verrebbe chiamata.
			logger.error("--> Errore nella conferma dei movimenti fatturazione", e);
			onPostExecute();
			PanjeaSwingUtil.checkAndThrowException(e);
		}
	}

	/**
	 * @return the datiGenerazione
	 */
	public DatiGenerazione getDatiGenerazione() {
		return datiGenerazione;
	}
}