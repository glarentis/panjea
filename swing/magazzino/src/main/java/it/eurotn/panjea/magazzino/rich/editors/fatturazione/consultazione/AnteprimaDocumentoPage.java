package it.eurotn.panjea.magazzino.rich.editors.fatturazione.consultazione;

import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyVetoException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.spring.richclient.docking.editor.EditorDescriptor;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.util.MovimentoFatturazioneDTO;
import it.eurotn.panjea.rich.editors.stampe.StampaAreaDocumentoAction;
import it.eurotn.panjea.rich.editors.stampe.TipoAreaStampeComponent;
import it.eurotn.panjea.rich.stampe.ILayoutStampeManager;
import it.eurotn.panjea.rich.stampe.LayoutStampeManager;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.stampe.domain.LayoutStampaDocumento;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.report.JecReportDocumento;
import it.eurotn.rich.report.ReportManager;
import it.eurotn.rich.report.editor.JecReportEditor;

public class AnteprimaDocumentoPage extends AbstractDialogPage implements IPageLifecycleAdvisor, Observer {

    private class LayoutPane extends CollapsiblePane implements StampaAreaDocumentoAction {
        private static final long serialVersionUID = -7046507190357085914L;
        private AreaMagazzino areaMagazzino;

        /**
         * Costruttore.
         */
        public LayoutPane() {
            setTitle("Layout di stampa");
            setStyle(CollapsiblePane.DROPDOWN_STYLE);
            setSlidingDirection(SwingConstants.SOUTH);
            setLayout(new BorderLayout());
            setEmphasized(true);
            try {
                setCollapsed(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
            setBackground(new Color(204, 204, 214));
            getContentPane().setBackground(UIManager.getColor("JPanel.background"));
            getContentPane().setOpaque(true);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        /**
         * Carica il layout per l'area magazzino.
         * 
         * @param areaMagazzinoParam
         *            areaMagazzino
         */
        public void caricaLayout(AreaMagazzino areaMagazzinoParam) {
            this.areaMagazzino = areaMagazzinoParam;
            if (!isCollapsed()) {
                refreshLayout();
            }
        }

        /**
         * Ricarica layout.
         */
        private void refreshLayout() {
            List<LayoutStampaDocumento> layouts = layoutStampeManager.caricaLayoutStampe(
                    areaMagazzino.getTipoAreaMagazzino(), areaMagazzino.getDocumento().getEntita(),
                    areaMagazzino.getDocumento().getSedeEntita());
            TipoAreaStampeComponent tipoAreaStampeComponent = new TipoAreaStampeComponent(
                    areaMagazzino.getTipoAreaMagazzino(), layouts, true, this);
            getContentPane().removeAll();
            getContentPane().add(tipoAreaStampeComponent);
        }

        @Override
        public void setCollapsed(boolean paramBoolean) throws PropertyVetoException {
            super.setCollapsed(paramBoolean);
            if (!paramBoolean && areaMagazzino != null) {
                refreshLayout();
            }
        }

        @Override
        public void stampa(LayoutStampa layout, boolean showPrintDialog) {
            JecReportDocumento documento = new JecReportDocumento(areaMagazzino);
            documento.setShowPrintDialog(showPrintDialog);
            documento.execute();
        }
    }

    private class ReportViewComponent extends JPanel {

        private static final long serialVersionUID = 5043990146499580747L;
        private JLabel labelAnteprimaInCorso;

        /**
         * Costruttore.
         */
        public ReportViewComponent() {
            labelAnteprimaInCorso = getComponentFactory().createLabel(LABEL_ANTEPRIMA_IN_CORSO);
            labelAnteprimaInCorso.setHorizontalAlignment(SwingConstants.CENTER);
            // addOverlayComponent(labelAnteprimaInCorso);
            setLayout(new BorderLayout());
        }

        /**
         * visualizza il report.
         * 
         * @param editorPrint
         *            editorPrint
         */
        public void showReport(JecReportEditor editorPrint) {
            BorderLayout layout = (BorderLayout) getLayout();
            if (layout.getLayoutComponent(BorderLayout.CENTER) != null) {
                remove(layout.getLayoutComponent(BorderLayout.CENTER));
            }
            add(editorPrint.getControl(), BorderLayout.CENTER);
            repaint();
        }

        /**
         * Visualizza il messaggio di generazione anteprima in corso.
         */
        public void showWait() {
            BorderLayout layout = (BorderLayout) getLayout();
            if (layout.getLayoutComponent(BorderLayout.CENTER) != null) {
                remove(layout.getLayoutComponent(BorderLayout.CENTER));
            }
            add(labelAnteprimaInCorso, BorderLayout.CENTER);
            repaint();
        };
    }

    public static final String PAGE_ID = "anteprimaDocumentoPage";

    public static final String LABEL_ANTEPRIMA_IN_CORSO = PAGE_ID + ".label.anteprima.in.corso";

    public static final String LABEL_ANTEPRIMA_DISATTIVATA = PAGE_ID + ".label.anteprima.disattivata";
    public static final String LABEL_REPORT_NON_IMPOSTATO = PAGE_ID + ".label.report.non.impostato";

    public static final String LABEL_REPORT_NON_TROVATO = PAGE_ID + ".label.report.non.trovato";

    public static final String LABEL_REPORT_EXCEPTION = PAGE_ID + ".label.report.exception";

    private ReportManager reportManager;

    private MovimentoFatturazioneDTO movimentoFatturazioneDTOCorrente;
    private TipoAreaMagazzino tipoAreaMagazzino;
    private LayoutPane layoutCollapsiblePane;
    private ILayoutStampeManager layoutStampeManager;

    private ReportViewComponent reportViewComponent;
    private JLabel labelAnteprimaDisattivata;
    private JLabel labelReportNonImpostato;
    private JLabel labelReportNonTrovato;
    private JLabel labelReportException;
    private ReportViewComponent rootPanel;

    /**
     * Costruttore.
     */
    protected AnteprimaDocumentoPage() {
        super(PAGE_ID);
        setVisible(true);
        this.layoutStampeManager = RcpSupport.getBean(LayoutStampeManager.BEAN_ID);
    }

    @Override
    protected JComponent createControl() {
        rootPanel = new ReportViewComponent();

        layoutCollapsiblePane = new LayoutPane();

        rootPanel.add(layoutCollapsiblePane, BorderLayout.NORTH);

        // rootPanel.add(editorPrint.getControl(), BorderLayout.CENTER);
        return rootPanel;
    }

    @Override
    public void dispose() {
    }

    /**
     * @return the labelAnteprimaDisattivata
     */
    public JLabel getLabelAnteprimaDisattivata() {
        if (labelAnteprimaDisattivata == null) {
            labelAnteprimaDisattivata = getComponentFactory().createLabel(LABEL_ANTEPRIMA_DISATTIVATA);
            labelAnteprimaDisattivata.setHorizontalAlignment(SwingConstants.CENTER);
        }

        return labelAnteprimaDisattivata;
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
     * @return the labelReportNonImpostato
     */
    public JLabel getLabelReportNonImpostato() {
        if (labelReportNonImpostato == null) {
            labelReportNonImpostato = getComponentFactory().createLabel(LABEL_REPORT_NON_IMPOSTATO);
            labelReportNonImpostato.setHorizontalAlignment(SwingConstants.CENTER);
        }

        return labelReportNonImpostato;
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
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
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
    public void restoreState(Settings settings) {
    }

    @Override
    public void saveState(Settings settings) {
    }

    @Override
    public void setFormObject(Object object) {
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

    /**
     * @param reportManager
     *            reportManager
     */
    public void setReportManager(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @Override
    public void update(Observable o, Object obj) {
        movimentoFatturazioneDTOCorrente = null;
        if (obj instanceof MovimentoFatturazioneDTO) {
            movimentoFatturazioneDTOCorrente = (MovimentoFatturazioneDTO) obj;
            if (this.isVisible()) {
                AreaMagazzino areaMagazzino = movimentoFatturazioneDTOCorrente.getAreaMagazzino();
                rootPanel.showWait();

                JecReportDocumento documento = new JecReportDocumento(areaMagazzino);
                documento.generaReport(new Closure() {

                    @Override
                    public Object call(Object paramObject) {
                        JecReportDocumento jecReport = (JecReportDocumento) paramObject;
                        if (movimentoFatturazioneDTOCorrente != null && jecReport.getAreaDocumento()
                                .equals(movimentoFatturazioneDTOCorrente.getAreaMagazzino())) {
                            JecReportEditor editorPrint = (JecReportEditor) ((EditorDescriptor) RcpSupport
                                    .getBean(JecReportEditor.BEAN_ID)).createPageComponent();
                            editorPrint.initialize(jecReport);
                            rootPanel.showReport(editorPrint);
                            layoutCollapsiblePane.caricaLayout((AreaMagazzino) jecReport.getAreaDocumento());
                        }
                        return null;
                    }
                });

            }
        }
    }
}
