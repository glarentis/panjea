/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.commands;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.report.ReportManager;
import it.eurotn.rich.report.exception.ReportNonTrovatoException;
import it.eurotn.rich.report.jasperserver.ReportEmptyDataException;

import java.awt.print.PrinterJob;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.util.RcpSupport;

/**
 * @author fattazzo
 * 
 */
public class StampaEtichettaBeniCommand extends ActionCommand {

	public static final String PARAM_BENI = "PARAM_BENI";

	private ReportManager reportManager;
	private AziendaCorrente aziendaCorrente;

	/**
	 * Costruttore.
	 */
	public StampaEtichettaBeniCommand() {
		super("stampaEtichettaBeniCommand");
		RcpSupport.configure(this);

		reportManager = RcpSupport.getBean(ReportManager.BEAN_ID);
		this.aziendaCorrente = (AziendaCorrente) Application.instance().getApplicationContext()
				.getBean("aziendaCorrente");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doExecuteCommand() {
		List<BeneAmmortizzabileLite> beni = (List<BeneAmmortizzabileLite>) getParameter(PARAM_BENI, null);

		final String reportName = "BeniAmmortizzabili/Anagrafica/etichetta";

		// sincornizzo il set perchè lo uso nel thread di foxTrot
		final List<BeneAmmortizzabileLite> etichetteBeni = Collections.synchronizedList(beni);
		Message message = new DefaultMessage("Stampa etichette in corso...", Severity.INFO);
		final MessageAlert messageAlert = new MessageAlert(message, 0);
		messageAlert.showAlert();
		AsyncWorker.post(new AsyncTask() {

			@Override
			public void failure(Throwable error) {
				Message message = null;

				if (error instanceof ReportNonTrovatoException) {
					ReportNonTrovatoException exception = (ReportNonTrovatoException) error;
					message = new DefaultMessage("Impossibile trovare il report con il path " + exception.getMessage(),
							Severity.ERROR);
				} else {
					message = new DefaultMessage("Stampa dell'etichetta non riuscita \n " + error.getMessage(),
							Severity.ERROR);
					// stampo l'errore del defautl error per visualizzarlo al "volo"
					logger.error("-->errore nel stamparele etichette ", error);
				}
				new MessageAlert(message).showAlert();
			}

			@Override
			protected void finish() {
				messageAlert.closeAlert();
				super.finish();
			}

			private void printReport(JasperPrint jasperPrint) {
				PrinterJob pj = PrinterJob.getPrinterJob();
				if (pj.printDialog()) {
					// mando in stampa
					PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
					printRequestAttributeSet.add(new JobName("Panjea - Stampa etichette", null));
					net.sf.jasperreports.engine.export.JRPrintServiceExporter exporter;
					exporter = new net.sf.jasperreports.engine.export.JRPrintServiceExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
							printRequestAttributeSet);
					exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, pj.getPrintService());
					exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
					exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
					try {
						exporter.exportReport();
					} catch (Exception e) {
						if (e instanceof IllegalArgumentException) {
							throw new ReportEmptyDataException("Nessun dato caricato");
						}
						throw new RuntimeException(e);
					}
				}
			}

			@Override
			public Object run() throws Exception {

				StringBuilder idBeni = new StringBuilder();
				for (BeneAmmortizzabileLite beneAmmortizzabileLite : etichetteBeni) {
					idBeni.append(beneAmmortizzabileLite.getId()).append(",");
				}

				Map<Object, Object> parametri = new HashMap<Object, Object>();
				parametri.put("idbeni", StringUtils.chop(idBeni.toString()));
				parametri.put("azienda", aziendaCorrente.getCodice());
				parametri.put("utente", PanjeaSwingUtil.getUtenteCorrente().getUserName());

				JasperPrint jasperReportEtchetta = reportManager.runReport(reportName, parametri);
				printReport(jasperReportEtchetta);
				return true;
			}

			@Override
			public void success(Object arg0) {
			}
		});
	}
}
