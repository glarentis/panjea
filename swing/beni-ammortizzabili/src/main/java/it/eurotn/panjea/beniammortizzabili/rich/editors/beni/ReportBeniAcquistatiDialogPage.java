/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

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
 * @version 1.0, 04/dic/06
 */
public class ReportBeniAcquistatiDialogPage extends PanjeaTitledPageApplicationDialog {

	private class StampaBeniAcquistatiCommand extends StampaCommand {

		private static final String CONTROLLER_ID = "stampaBeniAcquistatiCommand";

		/**
		 * Costruttore.
		 *
		 */
		public StampaBeniAcquistatiCommand() {
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
			parametri.put("descAzienda", aziendaCorrente.getDenominazione());
			parametri.put("azienda", aziendaCorrente.getCodice().toLowerCase());
			parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());
			parametri.put("fornitore", new Integer(-1));
			if (parametriRicercaBeni.getFornitoreLite() != null) {
				parametri.put("fornitore", parametriRicercaBeni.getFornitoreLite().getId());
			}
			parametri.put("specie", new Integer(-1));
			if (parametriRicercaBeni.getSpecie() != null) {
				parametri.put("specie", parametriRicercaBeni.getSpecie().getId());
			}
			parametri.put("sottospecie", new Integer(-1));
			if (parametriRicercaBeni.getSottoSpecie() != null) {
				parametri.put("sottospecie", parametriRicercaBeni.getSottoSpecie().getId());
			}
			parametri.put("anno", parametriRicercaBeni.getAnno());
			parametri.put("visualizzaEliminati", parametriRicercaBeni.isVisualizzaEliminati());
			parametri.put("htmlParametri", parametriRicercaBeni.getHtmlParametersAcquistoBeni());
			parametri.put("raggruppaUbicazione", parametriRicercaBeni.isRaggruppaUbicazione());

			StringBuffer groupBy = new StringBuffer("group by ");
			if (parametriRicercaBeni.isRaggruppaUbicazione()) {
				groupBy.append("bamm_ubicazioni.id ASC, ");
			}
			groupBy.append("bamm_sottospecie.id ASC, ");
			groupBy.append("bamm_specie.id ASC, ");
			groupBy.append("bamm_bene_ammortizzabile.id ASC ");

			parametri.put("groupBy", groupBy.toString());

			return parametri;
		}

		@Override
		protected String getReportName() {
			return "Stampa acquisti annuali";
		}

		@Override
		protected String getReportPath() {
			return "BeniAmmortizzabili/Anagrafica/acquistoBeniAnnuale";
		}

	}

	private static Logger logger = Logger.getLogger(ReportBeniAcquistatiDialogPage.class);
	private IReportBeniAmmortizzabiliBD reportBeniAmmortizzabiliBD = null;
	private IBeniAmmortizzabiliBD beniAmmortizzabiliBD;
	private String reportId = null;
	private Form form = null;
	private AziendaCorrente aziendaCorrente = null;

	private StampaBeniAcquistatiCommand stampaBeniAcquistatiCommand;

	/**
	 * Costruttore.
	 *
	 */
	public ReportBeniAcquistatiDialogPage() {
		super(new ReportBeniAcquistatiForm(new ParametriRicercaBeni()), null);
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
	 * @return stampaBeniAcquistatiCommand
	 */
	public StampaBeniAcquistatiCommand getStampaBeniAcquistatiCommand() {
		if (stampaBeniAcquistatiCommand == null) {
			stampaBeniAcquistatiCommand = new StampaBeniAcquistatiCommand();
		}

		return stampaBeniAcquistatiCommand;
	}

	@Override
	protected boolean onFinish() {
		logger.debug("--> Enter onFinish");

		getStampaBeniAcquistatiCommand().execute();

		logger.debug("--> Exit onFinish");
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
