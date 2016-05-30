package it.eurotn.panjea.bi.rich.editors.analisi;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.dialog.MessageDialog;

import com.jidesoft.grid.TableUtils;
import com.jidesoft.pivot.IPivotDataModel;
import com.jidesoft.pivot.PivotField;
import com.jidesoft.swing.DefaultOverlayable;

import it.eurotn.panjea.bi.domain.analisi.AnalisiBIResult;
import it.eurotn.panjea.bi.domain.analisi.AnalisiBi;
import it.eurotn.panjea.bi.rich.bd.IBusinessIntelligenceBD;
import it.eurotn.panjea.bi.rich.editors.analisi.detail.AnalisiBiDetailTablePage;
import it.eurotn.panjea.bi.rich.editors.analisi.model.AnalisiBiDataSource;
import it.eurotn.panjea.bi.rich.editors.analisi.model.AnalisiBiPivotDataModel;
import it.eurotn.panjea.bi.rich.editors.analisi.model.AnalisiBiTableModel;
import it.eurotn.panjea.bi.rich.editors.analisi.style.PivotCellStyleProvider;
import it.eurotn.panjea.bi.rich.editors.chart.AnalisiBiChartPage;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.magazzino.exception.AnalisiNonPresenteException;
import it.eurotn.rich.dialog.DockingCompositeDialogPage;

public class AnalisiBiEditorController implements PropertyChangeListener {

    private static Logger logger = Logger.getLogger(AnalisiBiEditorController.class);
    public static final String SELECTION_EVENT = "selectionEvent"; // Indica che è cambiata la
                                                                   // selezione in una pagina
    private IPivotDataModel pivotDataModel;
    private final IBusinessIntelligenceBD businessIntelligenceBD;

    private DockingCompositeDialogPage dockingDialogPage;
    protected AnalisiBiDataSource dataSource;
    // Dati dell'analisi corrente
    protected AnalisiBi analisiBi;

    protected AnalisiBiPivotTablePage pivotTablePage;
    protected AnalisiBiDetailTablePage detailTablePage;
    protected AnalisiBiChartPage chartPage;

    private DefaultOverlayable overlay;
    private ToolbarAnalisi toolbarAnalisi;

    /**
     * costruttore.
     *
     * @param paramDatawarehouseBD
     *            business delegate per il datawarehouse
     */
    public AnalisiBiEditorController(final IBusinessIntelligenceBD paramDatawarehouseBD) {
        businessIntelligenceBD = paramDatawarehouseBD;
        dataSource = new AnalisiBiDataSource();
        pivotDataModel = new AnalisiBiPivotDataModel(dataSource);
        analisiBi = new AnalisiBi();
    }

    /**
     * Resize di tutte le colonne dell'analisi
     */
    public void autoResize() {
        pivotTablePage.getPivotTablePane().autoResizeAllColumns();
    }

    /**
     * Carica i dati per un analisi .
     *
     * @param nomeAnalisi
     *            nomeAnalisi
     * @param categoriaAnalisi
     *            categoria dell'analisi
     */
    public void caricaAnalisi(String nomeAnalisi, String categoriaAnalisi) {
        try {
            analisiBi = businessIntelligenceBD.caricaAnalisi(nomeAnalisi, categoriaAnalisi);
        } catch (AnalisiNonPresenteException e) {
            new MessageDialog("ATTENZIONE", "L'analisi che si stà cercando di caricare non è più presente.")
                    .showDialog();
            return;
        }

        pivotTablePage.getPivotTablePane().applyLayout(analisiBi);
        pivotTablePage.setFieldChooserVisible(analisiBi.isShowFieldList());
        chartPage.applyLayout(analisiBi);
        pivotDataModel = pivotTablePage.getPivotTablePane().getPivotDataModel();
        pivotDataModel.setCellStyleProvider(new PivotCellStyleProvider());

        updatePage();
        caricaDati();
    }

    /**
     * Carica i dati dal modello presente.
     */
    public void caricaDati() {
        try {
            setBusy(true);
            overlay.setOverlayVisible(true);
            syncLayoutToAnalisi();
            AnalisiBIResult result = businessIntelligenceBD.eseguiAnalisi(analisiBi);
            dataSource = new AnalisiBiDataSource(new AnalisiBiTableModel(result));
            ((AnalisiBiPivotDataModel) pivotDataModel).setDataSource(dataSource);
            updatePage();
        } catch (Exception e) {
            System.err.println("Errore nell'eseguire i caricamento dati " + e);
            logger.error("-->errore ", e);
            throw new PanjeaRuntimeException(e);
        } finally {
            setBusy(false);
            overlay.setOverlayVisible(false);
        }
    }

    /**
     * Cambia la visibilità del pannello di selezione dei campi.
     */
    public void changeFieldChooserVisibility() {
        pivotTablePage.setFieldChooserVisible(!pivotTablePage.isFieldChooserVisible());
    }

    /**
     * Dispose del controller.
     */
    public void dispose() {
        for (DialogPage dialogPage : dockingDialogPage.getDialogPages()) {
            dialogPage.removePropertyChangeListener(this);
        }
        chartPage = null;
        detailTablePage = null;
        pivotTablePage = null;

        overlay = null;
    }

    /**
     * @param alternativeJoin
     *            the alternativeJoin to set
     */
    public void enableAlternativeJoin(boolean alternativeJoin) {
        analisiBi.setAlternativeJoin(alternativeJoin);
    }

    /**
     * Esporta la pivot in un foglio di excel.
     *
     * @param fileName
     *            nome del file dove esportare i dati *
     * @throws IOException
     *             errore sul file
     */
    public void esportaPivotPane(String fileName) throws IOException {
        pivotTablePage.esportaPivotPane(fileName, analisiBi.getNome());
    }

    /**
     *
     * @return parametri per l'analisi corrente
     */
    public AnalisiBi getAnalisiBi() {
        return analisiBi;
    }

    /**
     *
     * @return pagina contenente il garfico
     */
    public AnalisiBiChartPage getChartPage() {
        return chartPage;
    }

    /**
     *
     * @return pagina con i dati drillThroug
     */
    public AnalisiBiDetailTablePage getDetailTablePage() {
        return detailTablePage;
    }

    /**
     * Azzera tutti i valori e crea una nuova analisi.
     */
    public void nuovo() {
        analisiBi = new AnalisiBi();
        dataSource = new AnalisiBiDataSource();
        pivotDataModel = new AnalisiBiPivotDataModel(dataSource);

        // chartPage.setAnalisiChartParametri(analisiBi.getAnalisiChartParametri());
        detailTablePage.setSelection(-1, -1);
        chartPage.setVisible(false);
        detailTablePage.setVisible(false);
        pivotTablePage.setFieldChooserVisible(true);
        updatePage();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Se cambia la selezione in qualche pagina setto in tutte el pagine la nuova selezione.
        // NB. La setto anche nella pagina che mi ha spedito l'evento
        if (SELECTION_EVENT.equals(evt.getPropertyName())) {
            int[] selection = (int[]) evt.getNewValue();
            int rowSelected = selection[0];
            int columnSelected = selection[1];
            if (!((AbstractDialogPage) evt.getSource()).getId().equals(pivotTablePage.getId())) {
                pivotTablePage.setSelection(rowSelected, columnSelected);
            } else if (!((AbstractDialogPage) evt.getSource()).getId().equals(detailTablePage.getId())) {
                detailTablePage.setSelection(rowSelected, columnSelected);
            }
        }
    }

    /**
     * Salva l'analisi corrente.<br/>
     * I dati salvati sono:<br/>
     * <ul>
     * <li>Layout delle pagine docked</li>
     * <li>layout della tabella (quindi i campi interessati) ed eventuali personalizzazione dei nomi
     * dei campi.</li>
     * <li>Filtri applicati</li>
     * </ul>
     *
     * @param analisiBiToStore
     *            dati dell'analisi da salvare
     */
    public void salvaAnalisi(AnalisiBi analisiBiToStore) {
        logger.debug("--> Enter salvaAnalisi");
        analisiBi = analisiBiToStore;
        syncLayoutToAnalisi();
        analisiBi = chartPage.syncLayout(analisiBi);
        analisiBi = businessIntelligenceBD.salvaAnalisi(analisiBi);
        if (toolbarAnalisi != null) {
            toolbarAnalisi.refreshTitle();
        }
        logger.debug("--> Exit salvaAnalisi");
    }

    /**
     * Setta lo sato del controller.
     *
     * @param busy
     *            busy
     */
    private void setBusy(boolean busy) {
        // chartPage.setBusy(busy);
        toolbarAnalisi.setEnabled(!busy);
    }

    /**
     * Setto la compositeDialogPage che la classe controlla.
     *
     * @param compositeDialogPage
     *            composite page contenuta nell'editor
     */
    public void setDialogPage(DockingCompositeDialogPage compositeDialogPage) {
        // setto le pagine nel controller
        dockingDialogPage = compositeDialogPage;
        dockingDialogPage.getDockingManager().setXmlFormat(true);
        dockingDialogPage.getDockingManager().setXmlEncoding("UTF-8");
        for (DialogPage dialogPage : compositeDialogPage.getDialogPages()) {
            dialogPage.addPropertyChangeListener(this);
            if (dialogPage.getId().equals(AnalisiBiPivotTablePage.PAGE_ID)) {
                pivotTablePage = (AnalisiBiPivotTablePage) dialogPage;
            } else if (dialogPage.getId().equals(AnalisiBiDetailTablePage.PAGE_ID)) {
                detailTablePage = (AnalisiBiDetailTablePage) dialogPage;
                detailTablePage.setVisible(false);
            } else if (dialogPage.getId().equals(AnalisiBiChartPage.PAGE_ID)) {
                chartPage = (AnalisiBiChartPage) dialogPage;
                chartPage.setVisible(false);
                dockingDialogPage.getDockingManager().getFrame(chartPage.getId()).setVisible(false);
            }
        }
        updatePage();
    }

    /**
     * @param overlay
     *            The overlay to set.
     */
    public void setOverlay(DefaultOverlayable overlay) {
        this.overlay = overlay;
        overlay.setOverlayVisible(false);
    }

    /**
     *
     * @param paramToolbarAnalisi
     *            toolbar dell'editor di analisi.
     */
    public void setToolBarAnalisi(ToolbarAnalisi paramToolbarAnalisi) {
        this.toolbarAnalisi = paramToolbarAnalisi;
    }

    /**
     * Swappa gli assi della pivot.
     */
    public void slicing() {

        PivotField[] columnField = pivotDataModel.getColumnFields();
        PivotField[] rowFields = pivotDataModel.getRowFields();

        for (PivotField rowField : rowFields) {
            rowField.setAreaType(1);
        }
        for (PivotField element : columnField) {
            element.setAreaType(0);
        }

        pivotTablePage.getPivotTablePane().fieldsUpdated();

    }

    /**
     * sync il layout nel controller con il layout ne pivot
     */
    public void syncLayoutToAnalisi() {
        analisiBi = pivotTablePage.getPivotTablePane().saveAnalisi(analisiBi);
    }

    /**
     * Setta/rimuove il flat layout.
     */
    public void toogleFlatLayout() {
        pivotTablePage.getPivotTablePane().setFlatLayout(!pivotTablePage.getPivotTablePane().isFlatLayout());
        analisiBi.setFlatLayout(pivotTablePage.getPivotTablePane().isFlatLayout());
    }

    /**
     * aggiorna l'oggetto delle pagine .
     */
    private void updatePage() {
        // Non uso la setCurrentObject della dockingDialogPage perchè cercherebbe di fare una clone
        // prima di spedire l'oggetto e la pivotDataModel non è serializzabile !!!
        pivotTablePage.preSetFormObject(pivotDataModel);
        pivotTablePage.setFormObject(pivotDataModel);
        pivotTablePage.postSetFormObject(pivotDataModel);
        pivotTablePage.getPivotTablePane().setDataFieldFilterable(true);
        pivotTablePage.getPivotTablePane().setFlatLayout(analisiBi.isFlatLayout());
        TableUtils.autoResizeAllColumns(pivotTablePage.getPivotTablePane().getDataTable());

        detailTablePage.setAnalisiBi(getAnalisiBi());
        detailTablePage.preSetFormObject(pivotDataModel);
        detailTablePage.setFormObject(pivotDataModel);
        detailTablePage.postSetFormObject(pivotDataModel);
        // Se cambio il table model al dettaglio setto anche la selezione a -1 per pulire i dati
        detailTablePage.setSelection(-1, -1);

        chartPage.preSetFormObject(pivotDataModel);
        chartPage.setFormObject(pivotDataModel);
        chartPage.postSetFormObject(pivotDataModel);
        if (toolbarAnalisi != null) {
            toolbarAnalisi.refreshTitle();
        }
    }
}
