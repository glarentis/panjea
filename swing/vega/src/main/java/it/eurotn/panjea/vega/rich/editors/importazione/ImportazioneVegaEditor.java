package it.eurotn.panjea.vega.rich.editors.importazione;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.factory.AbstractControlFactory;
import org.springframework.richclient.progress.ProgressMonitor;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;

import it.eurotn.panjea.vega.rich.bd.importazione.IVegaImportaBD;
import it.eurotn.rich.binding.PanjeaTextFieldDateEditor;

public class ImportazioneVegaEditor extends AbstractEditor {

    private class ImportaCommand extends ApplicationWindowAwareCommand {

        public ImportaCommand() {
            super("importaCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            String tipiDocumento = StringUtils.join(tipiDocumentoList.getSelectedValuesList(), ",");
            dateChooserDataInizio.setDateFormatString(tipiDocumento);
            String log = importaBD.importa(dateChooserDataInizio.getDate(), dateChooserDataFine.getDate(),
                    tipiDocumento);
            htmlLabel.setText(log);
        }
    }

    private final AbstractControlFactory factory = new AbstractControlFactory() {

        @Override
        protected JComponent createControl() {

            htmlLabel = new JEditorPane();
            htmlLabel.setContentType("text/html");
            htmlLabel.setEditable(false);
            JScrollPane jsp = new JScrollPane(htmlLabel);

            JPanel toolbarPanel = getComponentFactory().createPanel(new VerticalLayout());

            String dateFormat = "dd/MM/yyyy";
            String maskFormat = "dd/MM/yyyy";
            IDateEditor textFieldDateEditor = new PanjeaTextFieldDateEditor(dateFormat, maskFormat, '_');
            dateChooserDataInizio = new JDateChooser(textFieldDateEditor);
            IDateEditor textFieldDateEditorDataFine = new PanjeaTextFieldDateEditor(dateFormat, maskFormat, '_');
            dateChooserDataFine = new JDateChooser(textFieldDateEditorDataFine);

            tipiDocumentoList = new JList<>(tipiDocumento);
            tipiDocumentoList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
            tipiDocumentoList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            toolbarPanel.add(tipiDocumentoList);
            toolbarPanel.add(dateChooserDataInizio);
            toolbarPanel.add(dateChooserDataFine);
            toolbarPanel.add(new ImportaCommand().createButton());

            JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout(0, 0));
            rootPanel.add(toolbarPanel, BorderLayout.WEST);
            rootPanel.add(jsp, BorderLayout.CENTER);

            GuiStandardUtils.attachBorder(rootPanel);
            return rootPanel;
        }

    };

    private IVegaImportaBD importaBD;

    private JList<String> tipiDocumentoList;

    private String editorId;
    private JDateChooser dateChooserDataInizio;
    private JDateChooser dateChooserDataFine;
    private JEditorPane htmlLabel;
    private String[] tipiDocumento = new String[] { "SCAF", "SCDEP", "POZZ", "TRF", "RMDR", "RMNC", "RFURG", "BUONO",
            "RMDI" };

    @Override
    public JComponent getControl() {
        return factory.getControl();
    }

    @Override
    public String getId() {
        return editorId;
    }

    @Override
    public void initialize(Object editorObject) {
    }

    @Override
    public void save(ProgressMonitor arg0) {
    }

    /**
     * @param editorId
     *            the editorId to set
     */
    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    /**
     * @param importaBD
     *            the importaBD to set
     */
    public void setImportaBD(IVegaImportaBD importaBD) {
        this.importaBD = importaBD;
    }

}
