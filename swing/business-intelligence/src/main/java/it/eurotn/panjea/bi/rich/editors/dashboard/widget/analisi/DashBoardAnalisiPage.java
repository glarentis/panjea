package it.eurotn.panjea.bi.rich.editors.dashboard.widget.analisi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.settings.Settings;

import com.jidesoft.pivot.HeaderTableModel;
import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotField;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.swing.DefaultOverlayable;
import com.jidesoft.swing.InfiniteProgressPanel;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIResult;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.domain.analisi.AnalisiValueSelected;
import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;
import it.eurotn.panjea.bi.domain.dashboard.DashBoard.TIPO_VISUALIZZAZIONE;
import it.eurotn.panjea.bi.domain.dashboard.DashBoardAnalisi;
import it.eurotn.panjea.bi.rich.bd.IBusinessIntelligenceBD;
import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiEditorController;
import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiPersistenceUtil;
import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiPivotTablePane;
import it.eurotn.panjea.bi.rich.editors.analisi.model.AnalisiBiDataSource;
import it.eurotn.panjea.bi.rich.editors.analisi.model.AnalisiBiPivotDataModel;
import it.eurotn.panjea.bi.rich.editors.analisi.model.AnalisiBiTableModel;
import it.eurotn.panjea.bi.rich.editors.analisi.style.PivotCellStyleProvider;
import it.eurotn.panjea.bi.rich.editors.chart.AnalisiBiChartPage;
import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardPage;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.magazzino.exception.AnalisiNonPresenteException;
import it.eurotn.rich.components.WrapLayout;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class DashBoardAnalisiPage extends DashBoardPage implements IPageLifecycleAdvisor, PropertyChangeListener {

    public static final String ASSOCIAZIONE_FILTER_PROPERTY = "associazioneFilterProperty";
    private final IBusinessIntelligenceBD businessIntelligenceBD;
    private AnalisiBiDataSource dataSource;
    private DefaultOverlayable mainPanel;
    private InfiniteProgressPanel progressPanel;
    private DashBoardAnalisi analisi;
    private AnalisiBi analisiBi;
    private IPivotDataModel filterPivotDataModel;
    protected AnalisiBiPivotTablePane pivotTablePane;
    private Map<String, AnalisiValueSelected> filtriOriginale;
    private AnalisiBiChartPage chartPage;

    private JPanel descrizioneFiltriPanel;
    private JComponent controlComponent;

    /**
     *
     * Costruttore.
     *
     * @param analisi
     *            .g analisi da visualizzare
     * @param paramDatawarehouseBD
     *            bd per la gestione dele analisi
     * @throws AnalisiNonPresenteException
     *             Rilanciata se l'analisi non esiste
     */
    protected DashBoardAnalisiPage(final DashBoardAnalisi analisi, final IBusinessIntelligenceBD paramDatawarehouseBD)
            throws AnalisiNonPresenteException {
        super(analisi.getFrameKey());
        this.businessIntelligenceBD = paramDatawarehouseBD;
        this.analisi = analisi;
        filtriOriginale = new HashMap<String, AnalisiValueSelected>();
        initControl();
    }

    /**
     * Aggiorna i filtri della pagina. Se sono cambiati effettua una refresh
     *
     * @param filterPivotDataModel
     *            pivotDataModel con i fields filtrati
     */

    private void applyDashBoardFilter() {
        if (filterPivotDataModel == null) {
            return;
        }

        for (PivotField filterPivotField : filterPivotDataModel.getFields()) {
            PivotField pivotFieldCurrent = pivotTablePane.getPivotDataModel().getField(filterPivotField.getName());
            if (pivotFieldCurrent != null) {
                if (filtriOriginale.containsKey(pivotFieldCurrent.getName())) {
                    pivotFieldCurrent
                            .setSelectedPossibleValues(filtriOriginale.get(pivotFieldCurrent.getName()).getParameter());
                } else if (analisi.getAssociazioneFiltriApplyFields().contains(pivotFieldCurrent.getName())) {
                    pivotFieldCurrent.setSelectedPossibleValues(filterPivotField.getSelectedPossibleValues());
                }
            }
        }
        // risalvo i filtri nell'analisi per considerarli nella execute
        analisiBi.getFiltri().clear();
        analisiBi.getFiltri().putAll(AnalisiBiPersistenceUtil.creaFiltri(getPivotDataModel()));

        updateDescrizioneFiltriPanel();
    }

    /**
     * Apre l'analisi nell'editor dell'analisi.
     */
    @Override
    public void apriAnalisi() {
        AnalisiBi analisiBiToOpen = new AnalisiBi();
        analisiBiToOpen.setCategoria(analisiBi.getCategoria());
        analisiBiToOpen.setNome(analisiBi.getNome());
        LifecycleApplicationEvent event = new OpenEditorEvent(analisiBi);
        Application.instance().getApplicationContext().publishEvent(event);
    }

    /**
     * cambia la visualizzazione da grafico a tabella e contrario.
     */
    @Override
    public void cambiaTipoVisualizzazione() {

        mainPanel.getActualComponent().remove(controlComponent);
        switch (analisi.getTipoVisualizzazione()) {
        case GRAFICO:
            analisi.setTipoVisualizzazione(TIPO_VISUALIZZAZIONE.TABELLA);
            controlComponent = pivotTablePane;
            break;

        default:
            analisi.setTipoVisualizzazione(TIPO_VISUALIZZAZIONE.GRAFICO);
            controlComponent = chartPage.getControl();
            chartPage.applyLayout(analisiBi);
            chartPage.setFormObject(pivotTablePane.getPivotDataModel());
            break;
        }
        mainPanel.getActualComponent().add(controlComponent, BorderLayout.CENTER);
        mainPanel.repaint();
        updateDescrizioneFiltriPanel();
    }

    /**
     * Carica la struttura dell'analisi (misure e colonne).
     *
     * @throws AnalisiNonPresenteException
     *             Rilanciata se l'analisi non esiste
     */
    @Override
    public void caricaAnalisi() throws AnalisiNonPresenteException {
        try {
            analisiBi = businessIntelligenceBD.caricaAnalisi(analisi.getNomeAnalisi(), analisi.getCategoriaAnalisi());
            filtriOriginale = new HashMap<String, AnalisiValueSelected>();
            filtriOriginale.putAll(analisiBi.getFiltri());
            analisiBi.setShowFieldList(false);
            pivotTablePane.applyLayout(analisiBi);
            updateDescrizioneFiltriPanel();
        } catch (AnalisiNonPresenteException e) {
            logger.error("-->errore nel caricare l'analisi. ", e);
            throw new RuntimeException(e);
        }
        caricaDati();
    }

    /**
     * Carica i dati della dashboard.
     */
    @Override
    public void caricaDati() {

        startAnalisi();
        applyDashBoardFilter();
        SwingWorker<AnalisiBIResult, Void> worker = new SwingWorker<AnalisiBIResult, Void>() {

            @Override
            protected AnalisiBIResult doInBackground() throws Exception {
                System.out.println("DEBUG:DashBoardPage.caricaDati()" + Thread.currentThread().getName());
                return businessIntelligenceBD.eseguiAnalisi(analisiBi);
            }

            @Override
            protected void done() {
                try {
                    AnalisiBIResult result = get();
                    dataSource = new AnalisiBiDataSource(new AnalisiBiTableModel(result));
                    pivotTablePane.setPivotDataModel(new AnalisiBiPivotDataModel(dataSource));
                    pivotTablePane.getPivotDataModel().setCellStyleProvider(new PivotCellStyleProvider());
                    analisiBi.setShowFieldList(false);
                    pivotTablePane.applyLayout(analisiBi);
                    AnalisiBiPersistenceUtil.autoResizeAllColumns(pivotTablePane, false);
                    if (analisi.getTipoVisualizzazione() == TIPO_VISUALIZZAZIONE.GRAFICO) {
                        chartPage.applyLayout(analisiBi);
                        chartPage.setFormObject(pivotTablePane.getPivotDataModel());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } finally {
                    stopAnalisi();
                }
            }
        };
        worker.execute();
    }

    /**
     * Carica i dati della dashboard.
     *
     * @param newFilterDataModel
     *            The filterPivotDataModel to set.
     */
    @Override
    public void caricaDati(IPivotDataModel newFilterDataModel) {
        this.filterPivotDataModel = newFilterDataModel;

        boolean reloadAnalisi = false;
        // verifico se ci sono filtri applicati per la dashboard
        if (analisi.getAssociazioneFiltriApplyFields() != null
                && !analisi.getAssociazioneFiltriApplyFields().isEmpty()) {
            // carico i filtri usati finora e costruisco quelli nuovi provenienti dal
            // filterableDataModel per poter
            // confrontare l'eventuale cambio di valori
            Map<String, AnalisiValueSelected> oldFiltri = analisiBi.getFiltri();
            // creo i filtri dal AnalisiBiPersistenceUtil perchè in questo modo il metodo
            // getParameter() mi restituisce
            // i valori come vengono salvati sul db. Questo perchè altrimenti il metodo
            // PivotField.getSelectedPossibleValues e PivotField.getFilter mi restituirebbe un array
            // tipizzato e dovrei
            // farmi tutti i controlli.
            Map<String, AnalisiValueSelected> newFiltri = AnalisiBiPersistenceUtil.creaFiltri(filterPivotDataModel);

            // verifico solo per i filtri applicati alla dashboard se sono cambiati i valori
            for (String filtro : analisi.getAssociazioneFiltriApplyFields()) {
                AnalisiValueSelected oldValueSelected = oldFiltri.get(filtro);
                AnalisiValueSelected newValueSelected = newFiltri.get(filtro);

                Object[] oldValue = oldValueSelected != null ? oldValueSelected.getParameter() : new Object[] {};
                Object[] newValue = newValueSelected != null ? newValueSelected.getParameter() : new Object[] {};

                // se almeno un filtro cambia di valore ricarico tutto
                if (!Arrays.equals(oldValue, newValue)) {
                    reloadAnalisi = true;
                    break;
                }
            }
        }

        // se sono cambiati i valori dei filtri applicabili all'analisi ricarico i dati
        if (reloadAnalisi) {
            caricaDati();
        }
    }

    @Override
    protected JComponent creaAnalisiControl() {
        controlComponent = null;
        if (analisi.getTipoVisualizzazione() == TIPO_VISUALIZZAZIONE.TABELLA) {
            controlComponent = pivotTablePane;
        } else {
            controlComponent = chartPage.getControl();
        }

        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
        rootPanel.add(controlComponent, BorderLayout.CENTER);

        descrizioneFiltriPanel = getComponentFactory().createPanel(new WrapLayout(FlowLayout.LEFT, 10, 4));
        rootPanel.add(descrizioneFiltriPanel, BorderLayout.NORTH);
        updateDescrizioneFiltriPanel();

        return rootPanel;
    }

    @Override
    protected JComponent createControl() {
        mainPanel = new DefaultOverlayable(creaAnalisiControl());
        progressPanel = new InfiniteProgressPanel(5.0);
        mainPanel.addOverlayComponent(progressPanel);
        progressPanel.stop();
        mainPanel.setOverlayVisible(false);
        return mainPanel;
    }

    @Override
    public void dispose() {
        if (mainPanel != null) {
            mainPanel.removeOverlayComponent(progressPanel);
        }
        mainPanel.removeAll();
        pivotTablePane.removePropertyChangeListener(this);
        chartPage.removePropertyChangeListener(this);

        mainPanel = null;
        progressPanel = null;
        dataSource = null;
        analisiBi = null;
        analisi = null;
        pivotTablePane = null;
        chartPage = null;
    }

    /**
     * Esporta il pivot pane.
     *
     * @return nome del file temporaneo dove è stato esportato.
     */
    @Override
    public String esportaPivotPane() {
        File fileExport = null;
        try {
            fileExport = File.createTempFile("panjeaDashBoard", ".xls");
            // pivotTablePage.esportaPivotPane(fileExport.getAbsolutePath(),
            // analisi.getNomeAnalisi());
        } catch (IOException e) {
            logger.error("-->errore nel generare il pivot panel per l'analisi " + analisi.getNomeAnalisi(), e);
            throw new PanjeaRuntimeException(e);
        }
        return fileExport.getAbsolutePath();
    }

    /**
     * @return Returns the analisi.
     */
    @Override
    public DashBoardAnalisi getAnalisi() {
        return analisi;
    }

    @Override
    public String getId() {
        return analisi.getFrameKey();
    }

    /**
     * @return Returns the pivotTablePane.getPivotDataModel().
     */
    @Override
    public IPivotDataModel getPivotDataModel() {
        return pivotTablePane.getPivotDataModel();
    }

    private void initControl() {
        pivotTablePane = new AnalisiBiPivotTablePane();
        pivotTablePane.setPivotDataModel(new AnalisiBiPivotDataModel(new AnalisiBiDataSource()));
        pivotTablePane.setFilterFieldAreaVisible(false);
        pivotTablePane.setColumnFieldAreaVisible(false);
        pivotTablePane.setRowFieldAreaVisible(false);
        pivotTablePane.setDataFieldAreaVisible(false);
        pivotTablePane.setFieldChooserVisible(false);
        pivotTablePane.addPropertyChangeListener(AnalisiBiEditorController.SELECTION_EVENT, this);

        chartPage = new AnalisiBiChartPage();
        chartPage.addPropertyChangeListener(AnalisiBiEditorController.SELECTION_EVENT, this);
        chartPage.setReadOnly(true);
    }

    /**
     * True se l'analisi è stata caricata.
     *
     * @return true se l'analisi è stata caricata
     */
    @Override
    public boolean isAnalisiCaricata() {
        return analisiBi != null;
    }

    @Override
    public void loadData() {
        if (isControlCreated()) {
            caricaDati();
        }
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
    public void propertyChange(PropertyChangeEvent evt) {

        if (evt.getNewValue() != null && !progressPanel.isVisible()) {
            int[] cellSelection = (int[]) evt.getNewValue();
            int rowSelected = cellSelection[0];
            int columnSelected = cellSelection[1];

            if (rowSelected == -1 || columnSelected == -1) {
                return;
            }

            Map<Colonna, Object[]> fields = new HashMap<Colonna, Object[]>();
            HeaderTableModel headerColumn = pivotTablePane.getPivotDataModel().getColumnHeaderTableModel();
            if (pivotTablePane.getPivotDataModel().getColumnFields().length > 0) {
                for (int i = 0; i < pivotTablePane.getPivotDataModel().getColumnFields().length; i++) {
                    boolean ricercaValore = i > 0 ? headerColumn.isExpanded(i - 1, columnSelected) : true;
                    if (ricercaValore) {
                        Colonna colonna = analisiBi.getAnalisiLayout().getFields()
                                .get(pivotTablePane.getPivotDataModel().getColumnFields()[i].getName()).getColonna();
                        fields.put(colonna, new Object[] { headerColumn.getValueAt(i, columnSelected) });
                    } else {
                        // Il dettaglio deve venire filtrato se sul field, anche se non è visibile,
                        // ci sono dei filtri.
                        // Inserisco eventuali filtri sul field
                        Colonna colonna = analisiBi.getAnalisiLayout().getFields()
                                .get(pivotTablePane.getPivotDataModel().getColumnFields()[i].getName()).getColonna();
                        fields.put(colonna,
                                pivotTablePane.getPivotDataModel().getColumnFields()[i].getSelectedPossibleValues());
                    }
                }
            }

            HeaderTableModel headerRow = pivotTablePane.getPivotDataModel().getRowHeaderTableModel();
            if (pivotTablePane.getPivotDataModel().getRowFields().length > 0) {
                for (int i = 0; i < headerRow.getColumnCount(); i++) {
                    // Se la colonna precedente è espanda allora elaboro il valore del campo,
                    // altrimenti
                    // passo il valore di eventuali filtri.
                    // Il primo campo lo considero sempre
                    boolean ricercaValore = i > 0 ? headerRow.isExpanded(rowSelected, i - 1) : true;
                    if (ricercaValore) {
                        Colonna colonna = analisiBi.getAnalisiLayout().getFields()
                                .get(pivotTablePane.getPivotDataModel().getRowFields()[i].getName()).getColonna();
                        fields.put(colonna, new Object[] { headerRow.getValueAt(rowSelected, i) });
                    } else {
                        // Il dettaglio deve venire filtrato se sul field, anche se non è visibile,
                        // ci sono dei filtri.
                        // Inserisco eventuali filtri sul field
                        Colonna colonna = analisiBi.getAnalisiLayout().getFields()
                                .get(pivotTablePane.getPivotDataModel().getRowFields()[i].getName()).getColonna();
                        fields.put(colonna,
                                pivotTablePane.getPivotDataModel().getRowFields()[i].getSelectedPossibleValues());
                    }
                }
            }

            // lancio l'evento solo dei field configurati nell'associazione
            if (analisi.getAssociazioneFiltriFields() != null) {
                Map<String, Object[]> fieldsAssociazione = new HashMap<String, Object[]>();

                for (Entry<Colonna, Object[]> entry : fields.entrySet()) {
                    for (String filtroAssociazioneKey : analisi.getAssociazioneFiltriFields()) {
                        if (filtroAssociazioneKey.equals(entry.getKey().getKey())) {
                            fieldsAssociazione.put(filtroAssociazioneKey, entry.getValue());
                            break;
                        }
                    }
                }

                if (!fieldsAssociazione.isEmpty()) {
                    firePropertyChange(ASSOCIAZIONE_FILTER_PROPERTY, null, fieldsAssociazione);
                }
            }
        }

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

    /**
     * @param filterPivotDataModel
     *            The filterPivotDataModel to set.
     */
    @Override
    public void setFilterPivotDataModel(IPivotDataModel filterPivotDataModel) {
        this.filterPivotDataModel = filterPivotDataModel;
    }

    @Override
    public void setFormObject(Object object) {
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

    /**
     * Visualizza o nasconde i filtri utilizzati per l'analisi.
     *
     * @param showFilterDescription
     *            true per filtrare la decrizione
     */
    @Override
    public void showFilterDescription(boolean showFilterDescription) {
        analisi.setVisualizzaDesrizioneFiltriApplicati(showFilterDescription);
        updateDescrizioneFiltriPanel();
    }

    private void updateDescrizioneFiltriPanel() {
        descrizioneFiltriPanel.setVisible(analisi.isVisualizzaDesrizioneFiltriApplicati());
        descrizioneFiltriPanel.removeAll();
        descrizioneFiltriPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 10, 4));

        Color bgColor;
        Color fgColor;
        switch (analisi.getTipoVisualizzazione()) {
        case GRAFICO:
            if (chartPage.getChart().getPanel().getGradient().getVisible()) {
                bgColor = chartPage.getChart().getPanel().getGradient().getStartColor();
            } else {
                bgColor = chartPage.getChart().getPanel().getColor();
            }
            fgColor = chartPage.getChart().getAxes().getLeft().getLabels().getFont().getColor();
            break;
        default:
            bgColor = pivotTablePane.getBackground();
            fgColor = pivotTablePane.getForeground();
            break;
        }

        descrizioneFiltriPanel.setBackground(bgColor);

        if (analisi.isVisualizzaDesrizioneFiltriApplicati()) {
            for (PivotField field : getPivotDataModel().getFields()) {
                if (field.getSelectedPossibleValues() != null) {
                    StringBuilder sb = new StringBuilder(50);
                    sb.append("<html><b>");
                    sb.append(field.getTitle());
                    sb.append(":</b><i>");
                    for (Object value : field.getSelectedPossibleValues()) {
                        sb.append(value != null ? value.toString() : "");
                        if (value != field.getSelectedPossibleValues()[field.getSelectedPossibleValues().length - 1]) {
                            sb.append(" - ");
                        }
                    }
                    sb.append("</i></html>");

                    JLabel valueLabel = new JLabel(sb.toString());
                    valueLabel.setBackground(bgColor);
                    valueLabel.setForeground(fgColor);
                    descrizioneFiltriPanel.add(valueLabel);
                }
            }

            // se non ci sono filtri impostati non faccio comunque vedere il pannello
            descrizioneFiltriPanel.setVisible(descrizioneFiltriPanel.getComponentCount() > 0);
        }

        descrizioneFiltriPanel.repaint();
    }
}