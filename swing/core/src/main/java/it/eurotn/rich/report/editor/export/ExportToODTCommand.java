package it.eurotn.rich.report.editor.export;

import it.eurotn.rich.report.JecReport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

public class ExportToODTCommand extends AbstractExportCommand {

    public static final String COMMAND_ID = "JecJRViewer.exportToODT";

    /**
     * Costruttore.
     *
     * @param fileExtension
     *            estensione del file
     * @param jecReport
     *            report da esportare
     */
    public ExportToODTCommand(final String fileExtension, final JecReport jecReport) {
        super(COMMAND_ID, fileExtension, jecReport);
    }

    @Override
    public boolean export(File fileToExport) {

        boolean exportSuccess = true;

        try (OutputStream output = new FileOutputStream(fileToExport)) {
            JROdtExporter exporterODT = new JROdtExporter();
            exporterODT.setExporterInput(new SimpleExporterInput(jecReport.getJrPrint()));
            exporterODT.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
            exporterODT.exportReport();
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
