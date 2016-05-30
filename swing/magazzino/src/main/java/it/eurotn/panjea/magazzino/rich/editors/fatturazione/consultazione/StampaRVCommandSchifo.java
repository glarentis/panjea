package it.eurotn.panjea.magazzino.rich.editors.fatturazione.consultazione;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.util.MovimentoFatturazioneDTO;
import it.eurotn.rich.control.SplitButton;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.report.ReportManager;
import it.eurotn.rich.report.exception.ReportNonTrovatoException;
import it.eurotn.rich.report.jasperserver.ReportEmptyDataException;

import java.awt.event.ActionEvent;
import java.awt.print.PrinterJob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.JideButton;

public class StampaRVCommandSchifo extends ApplicationWindowAwareCommand {

	public static final String COMMAND_ID = "stampaRVSelCommand";

	public static final String PARAM_AREE_MAGAZZINO = "paramAreeMagazzino";
	public static final String PARAM_STAMPA_FATTURAZIONE = "paramStampaFatturazione";
	public static final String PARAM_DATI_GENERAZIONE = "paramDatiGenerazione";

	private ReportManager reportManager;

	private DatiGenerazione datiGenerazione;

	private boolean stampaRaggruppate = false;

	private SplitButton splitButton;

	/**
	 * Costruttore.
	 * 
	 */
	public StampaRVCommandSchifo() {
		super(COMMAND_ID);
		RcpSupport.configure(this);

		reportManager = (ReportManager) Application.instance().getApplicationContext().getBean("reportManager");
	}

	@Override
	public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
			CommandButtonConfigurer buttonConfigurer) {

		JideButton mainButton = (JideButton) super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);
		splitButton = new SplitButton(mainButton, SwingConstants.SOUTH);

		JidePopup popup = new JidePopup();
		popup.getContentPane().setLayout(new VerticalLayout());
		popup.getContentPane().add(createMenuComponent());
		splitButton.setMenu(popup);
		splitButton.setVisible(StampaRVCommandSchifo.this.isVisible());

		return splitButton;
	}

	/**
	 * Crea i componenti per la scelta del layout da utilizzare.
	 * 
	 * @return componenti creati
	 */
	private JComponent createMenuComponent() {
		JPanel buttonPanel = getComponentFactory().createPanel(new VerticalLayout(4));

		JButton buttonRaggruppate = getComponentFactory().createButton("Rate raggruppate");
		buttonRaggruppate.setAction(new AbstractAction() {
			private static final long serialVersionUID = -5897052013973164302L;

			@Override
			public void actionPerformed(ActionEvent e) {
				stampaRaggruppate = Boolean.TRUE;
				StampaRVCommandSchifo.this.execute();
			}
		});
		buttonRaggruppate.setText("Rate raggruppate");
		buttonRaggruppate.setIcon(RcpSupport.getIcon("report"));
		buttonPanel.add(buttonRaggruppate);

		return buttonPanel;
	}

	@Override
	protected void doExecuteCommand() {
		final String reportName = "Stampa richiesta di versamento";
		final String reportPath = getReportPath();

		this.datiGenerazione = (DatiGenerazione) getParameter(PARAM_DATI_GENERAZIONE, null);

		@SuppressWarnings("unchecked")
		final List<MovimentoFatturazioneDTO> areeMagazzino = (List<MovimentoFatturazioneDTO>) getParameter(
				PARAM_AREE_MAGAZZINO, new ArrayList<MovimentoFatturazioneDTO>());
		if (areeMagazzino.isEmpty()) {
			return;
		}
		final boolean stampaFatturazione = (Boolean) getParameter(PARAM_STAMPA_FATTURAZIONE, false);

		if (!reportManager.reportExist(reportPath.split("/")[0], reportPath.split("/")[1])) {
			return;
		}

		Message message = new DefaultMessage("Stampa del documento \n" + reportName + "\n in corso...", Severity.INFO);
		final MessageAlert messageAlert = new MessageAlert(message, 0);
		messageAlert.showAlert();

		AsyncWorker.post(new AsyncTask() {

			@Override
			public void failure(Throwable error) {
				stampaRaggruppate = Boolean.FALSE;
				messageAlert.closeAlert();
				Message message = null;
				logger.error("-->errore nello stampare il report per le richieste versamento", error);
				if (error instanceof ReportNonTrovatoException) {
					ReportNonTrovatoException exception = (ReportNonTrovatoException) error;
					message = new DefaultMessage("Impossibile trovare il report con il path " + exception.getMessage(),
							Severity.ERROR);
				} else if (error instanceof ReportEmptyDataException) {
					message = new DefaultMessage("Nessun dato presente", Severity.INFO);
				} else {
					message = new DefaultMessage("Stampa del documento " + reportName + " non riuscita", Severity.ERROR);
				}
				new MessageAlert(message).showAlert();
			}

			@Override
			public Object run() throws Exception {

				JasperPrint documento = null;

				if (stampaFatturazione) {
					documento = reportManager.runReport(reportPath, getParametriFatturazione());
				} else {
					documento = reportManager.runReport(reportPath, getParametri(areeMagazzino));
				}

				return documento;
			}

			@Override
			public void success(Object documento) {
				try {
					PrinterJob pj = PrinterJob.getPrinterJob();
					if (pj.printDialog()) {
						// mando in stampa
						PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
						printRequestAttributeSet.add(new JobName("Panjea - Stampa RV rata", null));
						net.sf.jasperreports.engine.export.JRPrintServiceExporter exporter;
						exporter = new net.sf.jasperreports.engine.export.JRPrintServiceExporter();
						exporter.setParameter(JRExporterParameter.JASPER_PRINT, documento);
						exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
								printRequestAttributeSet);
						exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, pj.getPrintService());
						exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
						exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
						try {
							exporter.exportReport();
						} catch (Exception e) {
							if (e instanceof IllegalArgumentException) {
								logger.error("Nessun dato caricato");
								throw new ReportEmptyDataException("Nessun dato caricato");
							}
							logger.error("Errore nella stampa del documento", e);
							throw new RuntimeException(e);
						}
					}
				} finally {
					stampaRaggruppate = Boolean.FALSE;
					messageAlert.closeAlert();
				}
			}
		});
	}

	/**
	 * Crea i parametri per il report per la stampa di un'area magazzino.
	 * 
	 * @param areeMagazzino
	 *            aree di cui stampare la RV
	 * @return parametri parametri creati
	 */
	private Map<Object, Object> getParametri(List<MovimentoFatturazioneDTO> areeMagazzino) {

		HashMap<Object, Object> parametri = new HashMap<Object, Object>();

		String paramName = stampaRaggruppate ? "areeMagazzinoIdRaggruppata" : "areeMagazzinoId";

		StringBuilder sb = new StringBuilder();
		for (MovimentoFatturazioneDTO movimentoFatturazioneDTO : areeMagazzino) {
			sb.append(movimentoFatturazioneDTO.getAreaMagazzino().getId());
			sb.append(",");
		}

		String idAree = StringUtils.chop(sb.toString());
		parametri.put(paramName, idAree);

		return parametri;
	}

	/**
	 * Crea i parametri per il report per la stampa dell'intera fatturazione.
	 * 
	 * @return parametri parametri creati
	 */
	private Map<Object, Object> getParametriFatturazione() {
		HashMap<Object, Object> parametri = new HashMap<Object, Object>();
		String paramName = stampaRaggruppate ? "dataCreazioneRaggruppata" : "dataCreazione";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		parametri.put(paramName, dateFormat.format(datiGenerazione.getDataCreazione()));
		parametri.put("azienda", reportManager.getCodiceAzienda().toLowerCase());
		parametri.put("aliasDataSource", "");

		return parametri;
	}

	/**
	 * @return report path
	 */
	private String getReportPath() {
		String path = "Tesoreria/RichiestaVersamentoRate";
		if (stampaRaggruppate) {
			path = "Tesoreria/RichiestaVersamentoRateRaggruppate";
		}

		return path;
	}

	@Override
	public void setVisible(boolean value) {
		super.setVisible(value);
		if (splitButton != null) {
			splitButton.setVisible(value);
		}
	}

}