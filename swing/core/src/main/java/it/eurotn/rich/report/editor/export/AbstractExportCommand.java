package it.eurotn.rich.report.editor.export;

import it.eurotn.rich.report.JecReport;
import it.eurotn.rich.report.editor.export.file.AbstractFileExporter;
import it.eurotn.rich.report.editor.export.file.FileChooserExporter;

import java.io.File;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

public abstract class AbstractExportCommand extends ActionCommand {

    private AbstractFileExporter fileExporter;
    protected String fileExtension;
    private EsportazioneStampaMessageAlert alert;
    protected JecReport jecReport;

    /**
     * Costruttore.
     *
     * @param commandId
     *            id del comando
     * @param fileExtension
     *            estensione del file
     * @param jecReport
     *            report da esportare
     */
    public AbstractExportCommand(final String commandId, final String fileExtension, final JecReport jecReport) {
        super(commandId);
        this.jecReport = jecReport;
        RcpSupport.configure(this);
        if (jecReport != null) {
            this.fileExporter = new FileChooserExporter(jecReport.getReportExportPath(), fileExtension);
        } else {
            this.fileExporter = new FileChooserExporter(null, fileExtension);
        }
        this.fileExtension = fileExtension;
    }

    @Override
    protected void doExecuteCommand() {
        alert = new EsportazioneStampaMessageAlert();

        File fileToExport = fileExporter.getFile();

        if (fileToExport != null) {
            alert.showAlert();
            boolean exportSuccess = export(fileToExport);

            if (exportSuccess) {
                alert.finishExport(fileToExport);
            } else {
                alert.errorExport();
            }
        }
    }

    /**
     * Esporta il file passato come parametro.
     *
     * @param fileToExport
     *            file sul quale esportare il report
     * @return <code>true</code> se l'exportazione è andata a buon fine
     */
    public abstract boolean export(File fileToExport);

    /**
     * @return Returns the fileExtension.
     */
    public String getFileExtension() {
        return fileExtension;
    }

}
