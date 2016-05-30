package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili.rich.forms.bene.ReportQuoteAnnoForm;
import it.eurotn.panjea.beniammortizzabili2.util.parametriricerca.ParametriRicercaBeni;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.report.StampaCommand;

import java.util.HashMap;
import java.util.Map;

import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.form.Form;

public class ReportQuoteAnnoDialogPage extends PanjeaTitledPageApplicationDialog {

	private class StampaQuoteConsolidateAnnoCommand extends StampaCommand {

		private static final String CONTROLLER_ID = "stampaQuoteConsolidateAnnoCommand";

		/**
		 * Costruttore di default.
		 */
		public StampaQuoteConsolidateAnnoCommand() {
			super(CONTROLLER_ID);
		}

		// @Override
		// protected Map<String, Closure> getActionHyperlinkExecutor() {
		// Closure closureBene = new Closure() {
		//
		// @Override
		// public Object call(Object argument) {
		//
		// @SuppressWarnings("unchecked")
		// List<JRPrintHyperlinkParameter> list = (List<JRPrintHyperlinkParameter>) argument;
		//
		// for (JRPrintHyperlinkParameter parametro : list) {
		//
		// if ("id".equals(parametro.getName())) {
		// BeneAmmortizzabile beneAmmortizzabile = new BeneAmmortizzabile();
		// beneAmmortizzabile.setId((Integer) parametro.getValue());
		// beneAmmortizzabile = beniAmmortizzabiliBD.caricaBeneAmmortizzabile(beneAmmortizzabile);
		// LifecycleApplicationEvent event = new OpenEditorEvent(beneAmmortizzabile);
		// Application.instance().getApplicationContext().publishEvent(event);
		// }
		// }
		//
		// return null;
		// }
		// };
		//
		// Map<String, Closure> mappa = new HashMap<String, Closure>();
		// mappa.put("it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile", closureBene);
		//
		// return mappa;
		// }

		@Override
		protected Map<Object, Object> getParametri() {
			ParametriRicercaBeni parametriRicercaBeni = (ParametriRicercaBeni) form.getFormObject();

			HashMap<Object, Object> parametri = new HashMap<Object, Object>();
			parametri.put("annoQuote", parametriRicercaBeni.getAnno());
			parametri.put("descAzienda", aziendaCorrente.getDenominazione());
			parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
			return parametri;
		}

		@Override
		protected String getReportName() {
			return "Stampa quote annuali";
		}

		@Override
		protected String getReportPath() {
			return "BeniAmmortizzabili/Anagrafica/quoteConsolidateAnnuali";
		}

	}

	private StampaQuoteConsolidateAnnoCommand stampaQuoteConsolidateAnnoCommand;

	private AziendaCorrente aziendaCorrente;

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	private Form form = null;

	/**
	 * Costruttore di default.
	 */
	public ReportQuoteAnnoDialogPage() {
		super(new ReportQuoteAnnoForm(new ParametriRicercaBeni()), null);
		org.springframework.util.Assert.isInstanceOf(FormBackedDialogPage.class, getDialogPage());
		FormBackedDialogPage dialogPage = (FormBackedDialogPage) getDialogPage();
		form = dialogPage.getBackingFormPage();
	}

	/**
	 * Restituisce il comando per la generazione del report.
	 *
	 * @return comando
	 */
	public StampaQuoteConsolidateAnnoCommand getStampaQuoteConsolidateAnnoCommand() {
		if (stampaQuoteConsolidateAnnoCommand == null) {
			stampaQuoteConsolidateAnnoCommand = new StampaQuoteConsolidateAnnoCommand();
		}

		return stampaQuoteConsolidateAnnoCommand;
	}

	@Override
	protected boolean onFinish() {

		getStampaQuoteConsolidateAnnoCommand().execute();

		return true;
	}

	/**
	 * @param aziendaCorrente
	 *            the aziendaCorrente to set
	 */
	public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
		this.aziendaCorrente = aziendaCorrente;
	}

	/**
	 * @param beniAmmortizzabiliBD
	 *            the beniAmmortizzabiliBD to set
	 */
	public void setBeniAmmortizzabiliBD(IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

}
