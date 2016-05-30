/**
 *
 */
package it.eurotn.panjea.contabilita.rich.editors.riepilogoblacklist;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.report.StampaCommand;

import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.form.Form;

/**
 *
 * @author adriano
 * @version 1.0, 01/dic/06
 */
public class ReportRiepilogoDocumentiBlacklistDialogPage extends PanjeaTitledPageApplicationDialog {

	private class StampaRiepilogoDocumentiBlacklistCommand extends StampaCommand {

		private static final String CONTROLLER_ID = "stampaRiepilogoDocumentiBlacklistCommand";

		/**
		 * Costruttore.
		 */
		public StampaRiepilogoDocumentiBlacklistCommand() {
			super(CONTROLLER_ID);
		}

		@Override
		protected Map<Object, Object> getParametri() {
			ParametriRicercaRiepilogoBlacklist parametriRicerca = (ParametriRicercaRiepilogoBlacklist) form
					.getFormObject();

			HashMap<Object, Object> parametri = new HashMap<Object, Object>();
			parametri.put("descAzienda", aziendaCorrente.getDenominazione());
			parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			parametri.put("dataIniziale", dateFormat.format(parametriRicerca.getDataIniziale()));
			parametri.put("dataFinale", dateFormat.format(parametriRicerca.getDataFinale()));

			return parametri;
		}

		@Override
		protected String getReportName() {
			return "Stampa riepilogo documenti blacklist";
		}

		@Override
		protected String getReportPath() {
			return "Contabilita/DocumentiBlacklist";
		}

	}

	private static Logger logger = Logger.getLogger(ReportRiepilogoDocumentiBlacklistDialogPage.class);

	private Form form = null;
	private AziendaCorrente aziendaCorrente = null;

	private StampaRiepilogoDocumentiBlacklistCommand stampaRiepilogoDocumentiBlacklistCommand;

	/**
	 * Costruttore.
	 */
	public ReportRiepilogoDocumentiBlacklistDialogPage() {
		super(new RiepilogoDocumentiBlacklistForm(), null);
		org.springframework.util.Assert.isInstanceOf(FormBackedDialogPage.class, getDialogPage());
		FormBackedDialogPage dialogPage = (FormBackedDialogPage) getDialogPage();
		form = dialogPage.getBackingFormPage();
		setPreferredSize(new Dimension(400, 100));
	}

	/**
	 * @return the stampaRiepilogoDocumentiBlacklistCommand
	 */
	private StampaRiepilogoDocumentiBlacklistCommand getStampaRiepilogoDocumentiBlacklistCommand() {
		if (stampaRiepilogoDocumentiBlacklistCommand == null) {
			stampaRiepilogoDocumentiBlacklistCommand = new StampaRiepilogoDocumentiBlacklistCommand();
		}

		return stampaRiepilogoDocumentiBlacklistCommand;
	}

	@Override
	protected String getTitle() {
		return "Parametri di ricerca";
	}

	@Override
	protected boolean onFinish() {
		logger.debug("--> Enter onFinish");
		ParametriRicercaRiepilogoBlacklist parametriRicerca = (ParametriRicercaRiepilogoBlacklist) form.getFormObject();

		if (parametriRicerca.getDataIniziale().after(parametriRicerca.getDataFinale())) {
			new MessageDialog("ATTENZIONE", "La data finale deve essere maggiore di quella iniziale").showDialog();
			return false;
		}

		getStampaRiepilogoDocumentiBlacklistCommand().execute();

		logger.debug("--> Exit onFinish");
		return true;
	}

	/**
	 * @param aziendaCorrente
	 *            setter of aziendaCorrente
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}
}
