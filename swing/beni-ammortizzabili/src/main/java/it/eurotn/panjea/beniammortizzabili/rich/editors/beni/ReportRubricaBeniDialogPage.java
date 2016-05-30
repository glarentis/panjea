/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.beniammortizzabili.rich.bd.IBeniAmmortizzabiliBD;
import it.eurotn.panjea.beniammortizzabili.rich.bd.IReportBeniAmmortizzabiliBD;
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
 * @version 1.0, 01/dic/06
 */
public class ReportRubricaBeniDialogPage extends PanjeaTitledPageApplicationDialog {

	private class StampaRubricaBeniCommand extends StampaCommand {

		private static final String CONTROLLER_ID = "stampaRubricaBeniCommand";

		/**
		 * Costruttore.
		 *
		 */
		public StampaRubricaBeniCommand() {
			super(CONTROLLER_ID);
		}

		// @Override
		// protected Map<String, Closure> getActionHyperlinkExecutor() {
		//
		// Closure closureFornitore = new Closure() {
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
		// Fornitore fornitore = new Fornitore();
		// fornitore.setId((Integer) parametro.getValue());
		// fornitore = (Fornitore) anagraficaBD.caricaEntita(fornitore);
		// LifecycleApplicationEvent event = new OpenEditorEvent(fornitore);
		// Application.instance().getApplicationContext().publishEvent(event);
		// }
		// }
		//
		// return null;
		// }
		// };
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
		// mappa.put("it.eurotn.panjea.anagrafica.domain.Fornitore", closureFornitore);
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

			parametri.put("specie", -1);
			if (parametriRicercaBeni.getSpecie() != null) {
				parametri.put("specie", parametriRicercaBeni.getSpecie().getId());
			}

			parametri.put("sottospecie", -1);
			if (parametriRicercaBeni.getSottoSpecie() != null) {
				parametri.put("sottospecie", parametriRicercaBeni.getSottoSpecie().getId());
			}

			parametri.put("ubicazione", -1);
			if (parametriRicercaBeni.getUbicazione() != null) {
				parametri.put("ubicazione", parametriRicercaBeni.getUbicazione().getId());
			}
			parametri.put("visualizzafigli", parametriRicercaBeni.isVisualizzaFigli());
			parametri.put("stampaRaggruppamento", parametriRicercaBeni.isStampaRaggruppamento());
			parametri.put("htmlParametri", parametriRicercaBeni.getHtmlParametersRubrica());
			return parametri;
		}

		@Override
		protected String getReportName() {
			return "Stampa rubrica beni";
		}

		@Override
		protected String getReportPath() {
			return "BeniAmmortizzabili/Anagrafica/rubricaBeni";
		}

	}

	private static Logger logger = Logger.getLogger(ReportRubricaBeniDialogPage.class);
	private IReportBeniAmmortizzabiliBD reportBeniAmmortizzabiliBD = null;
	private IAnagraficaBD anagraficaBD;
	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;
	private String reportId = null;
	private Form form = null;
	private AziendaCorrente aziendaCorrente = null;

	private StampaRubricaBeniCommand stampaRubricaBeniCommand;

	/**
	 *
	 */
	public ReportRubricaBeniDialogPage() {
		super(new ReportRubricaBeniForm(new ParametriRicercaBeni()), null);
		org.springframework.util.Assert.isInstanceOf(FormBackedDialogPage.class, getDialogPage());
		FormBackedDialogPage dialogPage = (FormBackedDialogPage) getDialogPage();
		form = dialogPage.getBackingFormPage();
	}

	/**
	 * @return Returns the reportBeniAmmortizzabiliBD.
	 */
	public IReportBeniAmmortizzabiliBD getReportBeniAmmortizzabiliBD() {
		return reportBeniAmmortizzabiliBD;
	}

	/**
	 * @return Returns the reportId.
	 */
	public String getReportId() {
		return reportId;
	}

	/**
	 * @return stampaRubricaBeniCommand
	 */
	public StampaRubricaBeniCommand getStampaRubricaBeniCommand() {
		if (stampaRubricaBeniCommand == null) {
			stampaRubricaBeniCommand = new StampaRubricaBeniCommand();
		}

		return stampaRubricaBeniCommand;
	}

	@Override
	protected boolean onFinish() {
		logger.debug("--> Enter onFinish");

		getStampaRubricaBeniCommand().execute();

		logger.debug("--> Exit onFinish");
		return true;
	}

	/**
	 * @param anagraficaBD
	 *            the anagraficaBD to set
	 */
	public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
		this.anagraficaBD = anagraficaBD;
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

	/**
	 * @param reportBeniAmmortizzabiliBD
	 *            The reportBeniAmmortizzabiliBD to set.
	 */
	public void setReportBeniAmmortizzabiliBD(IReportBeniAmmortizzabiliBD reportBeniAmmortizzabiliBD) {
		this.reportBeniAmmortizzabiliBD = reportBeniAmmortizzabiliBD;
	}

	/**
	 * @param reportId
	 *            The reportId to set.
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
}
