/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.fatturato;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.report.StampaCommand;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.form.Form;

/**
 * 
 * @author adriano
 * @version 1.0, 01/dic/06
 */
public class FatturatoContabilitaDialogPage extends PanjeaTitledPageApplicationDialog {

	private class StampaFatturatoCommand extends StampaCommand {

		private static final String CONTROLLER_ID = "stampaFatturatoCommand";

		/**
		 * Costruttore.
		 */
		public StampaFatturatoCommand() {
			super(CONTROLLER_ID);
		}

		@Override
		protected Map<Object, Object> getParametri() {
			ParametriRicercaFatturato parametriRicercaFatturato = (ParametriRicercaFatturato) form.getFormObject();

			HashMap<Object, Object> parametri = new HashMap<Object, Object>();
			parametri.put("descAzienda", aziendaCorrente.getDenominazione());
			parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
			parametri.put("ANNO_COMPETENZA", parametriRicercaFatturato.getAnnoCompetenza());
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			parametri.put("DATA__REGISTRAZIONE_INIZIALE",
					dateFormat.format(parametriRicercaFatturato.getPeriodo().getDataIniziale()));
			parametri.put("DATA__REGISTRAZIONE_FINALE",
					dateFormat.format(parametriRicercaFatturato.getPeriodo().getDataFinale()));
			parametri.put("TIPO_ENTITA", parametriRicercaFatturato.getTipoEntitaFatturazioneString());
			parametri.put("FATTURATO_PER_SEDI", parametriRicercaFatturato.isFatturatoPerSedi());

			return parametri;
		}

		@Override
		protected String getReportName() {
			return "Fatturato";
		}

		@Override
		protected String getReportPath() {
			return "Contabilita/fatturato";
		}

	}

	private Form form = null;
	private AziendaCorrente aziendaCorrente = null;

	private StampaFatturatoCommand stampaFatturatoCommand;

	/**
	 * Costruttore.
	 */
	public FatturatoContabilitaDialogPage() {
		super(new FatturatoContabilitaForm(), null);
		FormBackedDialogPage dialogPage = (FormBackedDialogPage) getDialogPage();
		form = dialogPage.getBackingFormPage();
	}

	/**
	 * @return the stampaFatturatoCommand
	 */
	public StampaFatturatoCommand getStampaFatturatoCommand() {
		if (stampaFatturatoCommand == null) {
			stampaFatturatoCommand = new StampaFatturatoCommand();
		}

		return stampaFatturatoCommand;
	}

	@Override
	protected boolean onFinish() {
		ParametriRicercaFatturato parametriRicercaFatturato = (ParametriRicercaFatturato) form.getFormObject();

		if (parametriRicercaFatturato.getPeriodo().getDataIniziale() != null
				&& parametriRicercaFatturato.getPeriodo().getDataFinale() != null) {
			getStampaFatturatoCommand().execute();
			return true;
		}

		MessageDialog dialog = new MessageDialog("ATTENZIONE", "Inserire un periodo corretto");
		dialog.showDialog();

		return false;
	}

	/**
	 * @param aziendaCorrente
	 *            setter of aziendaCorrente
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}
}
