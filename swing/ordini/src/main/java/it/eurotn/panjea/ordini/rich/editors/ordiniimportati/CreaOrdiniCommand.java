package it.eurotn.panjea.ordini.rich.editors.ordiniimportati;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.ordini.domain.OrdineImportato;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine.STATO_ORDINE;
import it.eurotn.rich.dialog.MessageAlert;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractButton;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

public class CreaOrdiniCommand extends ActionCommand {
	public static final String COMMAND_ID = "creaOrdiniCommand";
	private IOrdiniDocumentoBD ordiniDocumentoBD;
	private ImportazioneOrdiniPage page;

	/**
	 * 
	 * @param ordiniDocumentoBD
	 *            ordiniDocumentoBD
	 * @param importazioneOrdiniPage
	 *            pagina delll'importazione
	 */
	public CreaOrdiniCommand(final IOrdiniDocumentoBD ordiniDocumentoBD,
			final ImportazioneOrdiniPage importazioneOrdiniPage) {
		super(COMMAND_ID);
		RcpSupport.configure(this);
		this.ordiniDocumentoBD = ordiniDocumentoBD;
		this.page = importazioneOrdiniPage;
	}

	@Override
	protected void doExecuteCommand() {

		MessageAlert messageAlert = new MessageAlert(new DefaultMessage("Creazione ordini in corso", Severity.INFO), 0);

		try {
			messageAlert.showAlert();
			Map<OrdineImportato, Map<Integer, RigaOrdineImportata>> map = new HashMap<OrdineImportato, Map<Integer, RigaOrdineImportata>>();

			// raggruppo le righe in base all'ordine nella mappa e poi passo nel chiavi alla conferma oridini importati.
			// NB: nel HashSet ordiniDaConfermare non posso passare direttamente riga.getOrdine() perchè le righe
			// all'interno dell'ordine non sono aggiornate, lo sono solo quelle in tabellla.
			for (RigaOrdineImportata riga : page.getTable().getRows()) {
				if (riga.isSelezionata()) {
					Map<Integer, RigaOrdineImportata> righe = map.get(riga.getOrdine());
					if (righe == null) {
						righe = new HashMap<Integer, RigaOrdineImportata>();
					}
					righe.put(riga.getNumeroRiga(), riga);
					map.put(riga.getOrdine(), righe);
				}
			}

			Collection<OrdineImportato> ordiniDaConfermare = new HashSet<OrdineImportato>();

			for (Entry<OrdineImportato, Map<Integer, RigaOrdineImportata>> entry : map.entrySet()) {
				entry.getKey().setRighe(entry.getValue());
				ordiniDaConfermare.add(entry.getKey());
			}

			if (ordiniDaConfermare.isEmpty()) {
				messageAlert.closeAlert();
				return;
			}

			Long timeStampDataCreazione = ordiniDocumentoBD.confermaOrdiniImportati(ordiniDaConfermare);

			AziendaCorrente aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
			ParametriRicercaAreaOrdine parametri = new ParametriRicercaAreaOrdine();
			parametri.setAnnoCompetenza(aziendaCorrente.getAnnoMagazzino());
			parametri.setDataCreazioneTimeStamp(timeStampDataCreazione);
			parametri.setStatoOrdine(STATO_ORDINE.NON_EVASO);
			parametri.setEffettuaRicerca(true);
			LifecycleApplicationEvent event = new OpenEditorEvent(parametri);
			Application.instance().getApplicationContext().publishEvent(event);
		} finally {
			messageAlert.closeAlert();
		}
	}

	@Override
	protected void onButtonAttached(AbstractButton button) {
		super.onButtonAttached(button);
		button.setName("CreaOrdiniCommand");
	}
}
