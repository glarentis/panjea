package it.eurotn.rich.report.editor.export;

import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.report.JecReport;
import it.eurotn.rich.report.JecReports;
import it.eurotn.rich.report.exception.JecReportErrorsException;
import it.eurotn.rich.report.result.JecReportResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.XlsReportConfiguration;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

public class ExportToXLSCommand extends AbstractExportCommand {

    public static final String COMMAND_ID = "JecJRViewer.exportToXLS";

    /**
     * Costruttore.
     *
     * @param fileExtension
     *            estensione del file
     * @param jecReport
     *            report da esportare
     */
    public ExportToXLSCommand(final String fileExtension, final JecReport jecReport) {
        super(COMMAND_ID, fileExtension, jecReport);
    }

    @Override
    public boolean export(File fileToExport) {

        boolean exportSuccess = true;

        try (OutputStream output = new FileOutputStream(fileToExport)) {

            // rieseguo il report mettendo il parametro IS_IGNORE_PAGINATION a true per ottenere un foglio di
            // calcolo senza interruzioni di pagina
            // Per farlo sincrono utilizzo le genreaReports
            List<JecReport> reports = new ArrayList<JecReport>();
            reports.add(jecReport);
            jecReport.getParameters().put(JRParameter.IS_IGNORE_PAGINATION, true);
            JecReportResult reportsResult = JecReports.generaReports(reports);

            if (reportsResult.getReportErrors().hasErrors()) {
                PanjeaSwingUtil.checkAndThrowException(new JecReportErrorsException(reportsResult.getReportErrors()));
            }
            reports = reportsResult.getReportGenerati();

            jecReport = reports.get(0);
            JRXlsExporter exporterXLS = new JRXlsExporter();
            XlsReportConfiguration configurazione = new SimpleXlsReportConfiguration() {
                @Override
                public Boolean isDetectCellType() {
                    return true;
                }

                @Override
                public Boolean isRemoveEmptySpaceBetweenColumns() {
                    return true;
                }

                @Override
                public Boolean isWhitePageBackground() {
                    return false;
                }
            };

            exporterXLS.setConfiguration(configurazione);
            exporterXLS.setExporterInput(new SimpleExporterInput(jecReport.getJrPrint()));
            exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
            exporterXLS.exportReport();
        } catch (Exception e) {
            logger.error("-->errore nell'esportare il report", e);
            String testoMessaggio = RcpSupport.getMessage("", "percorsoFileNonValido", "message",
                    new Object[] { fileToExport.getAbsolutePath() });
            Message messaggio = new DefaultMessage(testoMessaggio + "\n" + e.getMessage(), Severity.ERROR);
            String title = RcpSupport.getMessage("", "percorsoFileNonValido", "title");
            MessageDialog messageDialog = new MessageDialog(title, messaggio);
            messageDialog.showDialog();
            exportSuccess = false;
        }

        return exportSuccess;
    }

}
