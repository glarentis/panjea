package it.eurotn.panjea.anagrafica.rich.editors.azienda.deposito.stampa;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.rich.report.ReportManager;
import it.eurotn.rich.report.StampaCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.util.RcpSupport;

public class StampaValorizzazioneDepositoDialog extends ConfirmationDialog {

	private class StampaDepositoCommand extends StampaCommand {

		/**
		 * Costruttore.
		 */
		public StampaDepositoCommand() {
			super(null);
		}

		@Override
		protected Map<Object, Object> getParametri() {
			Map<Object, Object> params = new HashMap<Object,Object>();

			ParametriDeposito parametriDeposito = (ParametriDeposito) form.getFormObject();
			params.put("dataIniziale", DateFormatUtils.format(parametriDeposito.getPeriodo().getDataIniziale(),"yyyy-MM-dd"));
			params.put("dataFinale", DateFormatUtils.format(parametriDeposito.getPeriodo().getDataFinale(),"yyyy-MM-dd"));

			params.put("idDeposito", deposito.getId());

			return params;
		}

		@Override
		protected String getReportName() {
			return "Stampa valorizzazione deposito";
		}

		@Override
		protected String getReportPath() {
			return "Magazzino/Cantieri/" + ((ParametriDeposito)form.getFormObject()).getReport();
		}

	}

	private ParametriDepositoForm form = new ParametriDepositoForm();

	private Set<String> reports;

	private DepositoLite deposito;

	private StampaCommand stampaCommand;

	/**
	 * Costruttore.
	 */
	public StampaValorizzazioneDepositoDialog() {
		super();

		setTitle("Parametri di stampa");

		stampaCommand = new StampaDepositoCommand();

		form = new ParametriDepositoForm();
		form.setReportDisponibili(caricaStampe());
	}

	/**
	 * Carica tutte le stampe disponibili per la valorizzazione dei depositi.
	 *
	 * @return stampe disponibili
	 */
	private Set<String> caricaStampe() {
		if(reports == null) {
			ReportManager reportManager = RcpSupport.getBean(ReportManager.BEAN_ID);
			reports = reportManager.listReport("Magazzino/Cantieri");
		}

		return reports;
	}



	@Override
	protected JComponent createDialogContentPane() {
		return form.getControl();
	}

	@Override
	protected String getCancelCommandId() {
		return "cancelCommand";
	}

	@Override
	protected String getFinishCommandId() {
		return "okCommand";
	}

	@Override
	protected void onAboutToShow() {
		super.onAboutToShow();

		form.getNewFormObjectCommand().execute();
	}

	@Override
	protected void onCancel() {
		form.revert();
		super.onCancel();
	}

	@Override
	protected void onConfirm() {
		form.commit();
		stampaCommand.execute();
	}

	/**
	 * @param deposito the deposito to set
	 */
	public void setDeposito(DepositoLite deposito) {
		this.deposito = deposito;
	}

}
