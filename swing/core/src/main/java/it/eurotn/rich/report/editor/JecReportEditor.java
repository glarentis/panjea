/**
 *
 */
package it.eurotn.rich.report.editor;

import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.report.JecReport;
import it.eurotn.rich.report.editor.export.AbstractExportCommand;
import it.eurotn.rich.report.editor.export.ExportToMailCommand;
import it.eurotn.rich.report.editor.export.ExportToODTCommand;
import it.eurotn.rich.report.editor.export.ExportToPDFCommand;
import it.eurotn.rich.report.editor.export.ExportToXLSCommand;
import it.eurotn.rich.report.editor.export.PrintCommand;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.progress.ProgressMonitor;

import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;

/**
 * Editor che utilizza un'istanza di <code>JecReport</code> per visualizzarne l'anteprima all'interno di un
 * <code>JPanel</code>.
 * 
 * 
 * @author adriano
 * @version 1.0, 14/set/06
 */
public class JecReportEditor extends AbstractEditor {

    private class EditorControlFactory extends AbstractControlFactory {

        /**
         * Default constructor.
         */
        public EditorControlFactory() {
            setSingleton(false);
        }

        @Override
        public JComponent createControl() {
            reportViewer = new JecJRViewer(jecReport.getJrPrint());
            reportViewer.setToolbar(createToolbarCommandGroup().createToolBar());
            return reportViewer;
        }
    }

    public static final String BEAN_ID = "reportEditor";
    private JecJRViewer reportViewer;
    private static Logger logger = Logger.getLogger(JecReportEditor.class);
    private JPanel reportPanel = null;
    private JecReport jecReport = null;

    private EditorControlFactory factory = new EditorControlFactory();

    /**
     * Default JecReportEditor constructor.
     */
    public JecReportEditor() {
        super();
        reportPanel = new JPanel(new BorderLayout());
    }

    @Override
    public boolean canClose() {
        return true;
    }

    /**
     * Crea i comandi di exportazione e stampa di default.
     * 
     * @return command group
     */
    private JECCommandGroup createDefaultExportCommandGroup() {
        if (jecReport.getExportCommandGroup() != null) {
            return jecReport.getExportCommandGroup();
        }

        ExportToPDFCommand exportToPDFCommand = new ExportToPDFCommand("pdf", jecReport);
        ExportToXLSCommand exportToXLSCommand = new ExportToXLSCommand("xls", jecReport);

        JECCommandGroup group = new JECCommandGroup();
        group.add(exportToPDFCommand);
        group.add(exportToXLSCommand);
        group.add(new ExportToODTCommand("odt", jecReport));
        ExportToMailCommand exportToMailCommand = new ExportToMailCommand(
                new AbstractExportCommand[] { exportToPDFCommand, exportToXLSCommand });
        exportToMailCommand.setParametri(jecReport.getParametriMail());
        group.add(exportToMailCommand);
        return group;
    }

    /**
     * 
     * @return toolbar per l'editor
     */
    public CommandGroup createToolbarCommandGroup() {
        JECCommandGroup commandGroup = new JECCommandGroup();
        commandGroup.add(reportViewer.getSaveCommand());

        for (AbstractCommand commandExport : createDefaultExportCommandGroup().getCommands()) {
            commandGroup.add(commandExport);
        }
        commandGroup.addSeparator();
        commandGroup.add(new PrintCommand(jecReport));
        commandGroup.addSeparator();
        commandGroup.add(reportViewer.getFirstPageCommand());
        commandGroup.add(reportViewer.getPreviousPageCommand());
        commandGroup.add(reportViewer.getNextPageCommand());
        commandGroup.add(reportViewer.getLastPageCommand());
        commandGroup.addSeparator();
        commandGroup.add(reportViewer.getActualSizeCommand());
        commandGroup.add(reportViewer.getFitPageCommand());
        commandGroup.add(reportViewer.getFitWidthCommand());
        commandGroup.add(reportViewer.getZoomInCommand());
        // commandGroup.add(new ComponentGroupMember(reportViewer.getZoomBar()).getCommand());
        commandGroup.add(reportViewer.getZoomOutCommand());
        commandGroup.addGlue();
        commandGroup.add(new LayoutPreferenceEditorCommand(jecReport));
        return commandGroup;
    }

    @Override
    public void dispose() {
        logger.debug("--> Enter dispose editor " + this.hashCode());
        jecReport = null;
        super.dispose();
    }

    @Override
    public JComponent getControl() {
        logger.debug("--> Enter getControl");
        reportPanel.add(reportViewer, BorderLayout.CENTER);
        return reportPanel;
    }

    @Override
    public String getDisplayName() {
        String displayName = getJecReport().getReportName();
        Object editorInput = getEditorInput();
        if (editorInput instanceof JecReport) {
            displayName = ((JecReport) editorInput).getDisplayName();
        }
        return displayName;
    }

    @Override
    public String getId() {
        return jecReport.getReportName();
    }

    /**
     * @return Returns the jecReport.
     */
    public JecReport getJecReport() {
        return jecReport;
    }

    @Override
    public void initialize(Object editorObject) {
        org.springframework.util.Assert.isInstanceOf(JecReport.class, editorObject);
        jecReport = (JecReport) editorObject;
        // se i controlli non sono ancora stati creati sono nella situazione standard
        // di apertura dell'editor altrimenti mi trovo nella situazione in cui ho a false la property
        // closeAndReopenEditor di workspaceView e quindi l'editor rimane aperto
        if (reportPanel != null) {
            refreshReportContent(jecReport);
        }
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isEnableCache() {
        return false;
    }

    /**
     * Aggiorno i controlli contenuti nel reportPanel, pannello di appoggio per aggiornare il report.
     * 
     * @param paramJecReport
     *            report da visualizzare
     */
    private void refreshReportContent(JecReport paramJecReport) {
        reportPanel.removeAll();
        reportPanel.add(factory.getControl(), BorderLayout.CENTER);
    }

    @Override
    public void save(ProgressMonitor saveProgressTracker) {
    }

    /**
     * @param jecReport
     *            The jecReport to set.
     */
    public void setJecReport(JecReport jecReport) {
        this.jecReport = jecReport;
    }
}
