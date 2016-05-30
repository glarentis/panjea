/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili.rich.forms.bene.ReportRegistroBeniForm;
import it.eurotn.panjea.beniammortizzabili2.util.parametriricerca.ParametriRicercaBeni;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;
import it.eurotn.rich.report.StampaCommand;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.form.Form;

/**
 *
 * @author adriano
 * @version 1.0, 12/dic/06
 */
public class ReportVenditaBeniDialogPage extends PanjeaTitledPageApplicationDialog {

	private class StampaVenditeBeniCommand extends StampaCommand {

		private static final String CONTROLLER_ID = "stampaVenditeBeniCommand";

		/**
		 * Costruttore.
		 */
		public StampaVenditeBeniCommand() {
			super(CONTROLLER_ID);
		}

		// @Override
		// protected Map<String, Closure> getActionHyperlinkExecutor() {
		//
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
			parametri.put("descAzienda", aziendaCorrente.getDenominazione());
			parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
			parametri.put("anno", parametriRicercaBeni.getAnno());
			parametri.put("specie", new Integer(-1));
			parametri.put("sottospecie", new Integer(-1));

			if (parametriRicercaBeni.getSpecie() != null) {
				parametri.put("spece", parametriRicercaBeni.getSpecie().getId());
			}
			if (parametriRicercaBeni.getSottoSpecie() != null) {
				parametri.put("sottospecie", parametriRicercaBeni.getSottoSpecie().getId());
			}
			return parametri;
		}

		@Override
		protected String getReportName() {
			return "Stampa vendite annuali beni";
		}

		@Override
		protected String getReportPath() {
			return "BeniAmmortizzabili/Anagrafica/venditeAnnuali";
		}

	}

	private static Logger logger = Logger.getLogger(ReportVenditaBeniDialogPage.class);

	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;

	private String reportId = null;

	private Form form = null;
	private AziendaCorrente aziendaCorrente = null;

	private StampaVenditeBeniCommand stampaVenditeBeniCommand;

	/**
	 * Costruttore.
	 *
	 */
	public ReportVenditaBeniDialogPage() {
		super(new ReportRegistroBeniForm(new ParametriRicercaBeni()), null);
		org.springframework.util.Assert.isInstanceOf(FormBackedDialogPage.class, getDialogPage());
		FormBackedDialogPage dialogPage = (FormBackedDialogPage) getDialogPage();
		form = dialogPage.getBackingFormPage();
	}

	/**
	 * @return Returns the reportId.
	 */
	public String getReportId() {
		return reportId;
	}

	/**
	 * @return the stampaVenditeBeniCommand
	 */
	public StampaVenditeBeniCommand getStampaVenditeBeniCommand() {
		if (stampaVenditeBeniCommand == null) {
			stampaVenditeBeniCommand = new StampaVenditeBeniCommand();
		}

		return stampaVenditeBeniCommand;
	}

	@Override
	protected boolean onFinish() {
		logger.debug("--> Enter onFinish");

		getStampaVenditeBeniCommand().execute();

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

	/**
	 * @param beniAmmortizzabiliBD
	 *            the beniAmmortizzabiliBD to set
	 */
	public void setBeniAmmortizzabiliBD(IBeniAmmortizzabiliBD beniAmmortizzabiliBD) {
		this.beniAmmortizzabiliBD = beniAmmortizzabiliBD;
	}

	/**
	 * @param reportId
	 *            The reportId to set.
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
}
