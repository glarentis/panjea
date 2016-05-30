package it.eurotn.rich.report.editor.export;

import it.eurotn.rich.report.JecReport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import net.sf.jasperreports.engine.JasperExportManager;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

public class ExportToPDFCommand extends AbstractExportCommand {

    public static final String COMMAND_ID = "JecJRViewer.exportToPDF";

    /**
     * Costruttore.
     *
     * @param fileExtension
     *            estensione del file
     * @param jecReport
     *            report da esportare
     */
    public ExportToPDFCommand(final String fileExtension, final JecReport jecReport) {
        super(COMMAND_ID, fileExtension, jecReport);
    }

    @Override
    public boolean export(File fileToExport) {

        boolean exportSuccess = true;

        try (OutputStream output = new FileOutputStream(fileToExport)) {
            JasperExportManager.exportReportToPdfStream(jecReport.getJrPrint(), output);
        } catch (Exception e) {
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