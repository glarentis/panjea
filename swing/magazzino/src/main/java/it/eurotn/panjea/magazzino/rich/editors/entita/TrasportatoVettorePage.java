package it.eurotn.panjea.magazzino.rich.editors.entita;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jidesoft.spring.richclient.docking.editor.EditorDescriptor;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.rich.forms.trasportatovettore.TrasportatoVettoreForm;
import it.eurotn.panjea.magazzino.rich.forms.trasportatovettore.TrasportatoVettorePM;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.report.JecReport;
import it.eurotn.rich.report.editor.JecReportEditor;

public class TrasportatoVettorePage extends AbstractDialogPage implements IPageLifecycleAdvisor, Focussable {

    private class CaricaReportCommand extends ApplicationWindowAwareCommand {

        public static final String COMMAND_ID = "stampaCommand";

        /**
         * Costruttore.
         */
        public CaricaReportCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            TrasportatoVettorePM trasportatoVettorePM = (TrasportatoVettorePM) trasportatoVettoreForm.getFormObject();
            if (trasportatoVettorePM.getPeriodo().getDataFinale() == null
                    || trasportatoVettorePM.getPeriodo().getDataIniziale() == null) {
                Message message = new DefaultMessage("Impostare le date", Severity.WARNING);
                MessageDialog dialog = new MessageDialog("Attenzione", message);
                dialog.showDialog();
                return;
            }
            Map<Object, Object> parameters = new HashMap<Object, Object>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Periodo periodo = trasportatoVettorePM.getPeriodo();
            parameters.put("DATA_1", format.format(periodo.getDataIniziale()));
            parameters.put("DATA_2", format.format(periodo.getDataFinale()));
            parameters.put("tuttiVettori", false);
            parameters.put("idVettore", trasportatoVettorePM.getVettore().getId());
            List<Integer> tipiDoc = new ArrayList<Integer>();
            if (trasportatoVettorePM.getTipiAree() != null && !trasportatoVettorePM.getTipiAree().isEmpty()) {
                for (TipoAreaMagazzino tam : trasportatoVettorePM.getTipiAree()) {
                    tipiDoc.add(tam.getTipoDocumento().getId());
                }
            }
            parameters.put("tipiDoc", tipiDoc);
            JecReport documento = new JecReport("Magazzino/TrasportatoVettori", parameters);
            documento.setReportName("Trasportato per vettore");
            documento.generaReport(new Closure() {
                @Override
                public Object call(Object paramObject) {

                    try {
                        JecReport jecReport = (JecReport) paramObject;
                        JecReportEditor editorPrint = (JecReportEditor) ((EditorDescriptor) RcpSupport
                                .getBean(JecReportEditor.BEAN_ID)).createPageComponent();
                        editorPrint.initialize(jecReport);
                        BorderLayout layout = (BorderLayout) rootPanel.getLayout();
                        if (layout.getLayoutComponent(BorderLayout.CENTER) != null) {
                            rootPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
                        }
                        rootPanel.add(editorPrint.getControl(), BorderLayout.CENTER);
                        rootPanel.repaint();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return null;
                }
            });
        }

    }

    public static final String PAGE_ID = "trasportatoVettorePage";

    public static final String LABEL_REPORT_NON_TROVATO = PAGE_ID + ".label.report.non.trovato";

    public static final String LABEL_REPORT_EXCEPTION = PAGE_ID + ".label.report.exception";

    private Entita entita;
    private TrasportatoVettoreForm trasportatoVettoreForm;
    private JLabel labelReportNonTrovato;
    private JLabel labelReportException;

    private CaricaReportCommand caricaReportCommand;

    private JPanel rootPanel;

    {
        trasportatoVettoreForm = new TrasportatoVettoreForm();
    }

    /**
     * Costruttore.
     */
    public TrasportatoVettorePage() {
        super(PAGE_ID);
    }

    @Override
    public JComponent createControl() {

        JECCommandGroup commandGroup = new JECCommandGroup();
        commandGroup.add(getCaricaReportCommand());
        JPanel headPanel = getComponentFactory().createPanel(new BorderLayout());
        headPanel.add(commandGroup.createToolBar(), BorderLayout.NORTH);
        // headPanel.setBorder(new FrameBorder());

        JPanel topPanel = getComponentFactory().createPanel(new BorderLayout());
        topPanel.add(headPanel, BorderLayout.NORTH);

        topPanel.setMaximumSize(new Dimension(800, 300));
        GuiStandardUtils.attachDialogBorder(trasportatoVettoreForm.getControl());
        topPanel.add(trasportatoVettoreForm.getControl(), BorderLayout.CENTER);

        rootPanel = getComponentFactory().createPanel(new BorderLayout());
        rootPanel.add(new JScrollPane(topPanel), BorderLayout.NORTH);

        return rootPanel;
    }

    @Override
    public void dispose() {
        trasportatoVettoreForm.dispose();
    }

    /**
     * @return the caricaReportCommand
     */
    public CaricaReportCommand getCaricaReportCommand() {
        if (caricaReportCommand == null) {
            caricaReportCommand = new CaricaReportCommand();
        }

        return caricaReportCommand;
    }

    /**
     * @return the labelReportException
     */
    public JLabel getLabelReportException() {
        if (labelReportException == null) {
            labelReportException = getComponentFactory().createLabel(LABEL_REPORT_EXCEPTION);
            labelReportException.setHorizontalAlignment(SwingConstants.CENTER);
        }

        return labelReportException;
    }

    /**
     * @return the labelReportNonTrovato
     */
    public JLabel getLabelReportNonTrovato() {
        if (labelReportNonTrovato == null) {
            labelReportNonTrovato = getComponentFactory().createLabel(LABEL_REPORT_NON_TROVATO);
            labelReportNonTrovato.setHorizontalAlignment(SwingConstants.CENTER);
        }

        return labelReportNonTrovato;
    }

    @Override
    public void grabFocus() {
        trasportatoVettoreForm.getControl().requestFocusInWindow();
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        boolean initializePage = true;
        if (entita.isNew()) {
            initializePage = false;
            MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
                    .getService(MessageSourceAccessor.class);
            String titolo = messageSourceAccessor.getMessage("entita.null.messageDialog.title", new Object[] {},
                    Locale.getDefault());
            String messaggio = messageSourceAccessor.getMessage(
                    "entita.null.messageDialog.message", new Object[] { messageSourceAccessor
                            .getMessage(entita.getDomainClassName(), new Object[] {}, Locale.getDefault()) },
                    Locale.getDefault());
            new MessageDialog(titolo, messaggio).showDialog();
        }
        return initializePage;
    }

    @Override
    public void postSetFormObject(Object object) {
    }

    @Override
    public void preSetFormObject(Object object) {
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void restoreState(Settings arg0) {
    }

    @Override
    public void saveState(Settings arg0) {
    }

    @Override
    public void setFormObject(Object object) {
        this.entita = (Entita) object;
        trasportatoVettoreForm.setVettore((VettoreLite) entita.getEntitaLite());
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

}
