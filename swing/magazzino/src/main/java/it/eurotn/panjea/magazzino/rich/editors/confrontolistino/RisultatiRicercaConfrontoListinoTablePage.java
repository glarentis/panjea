/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.confrontolistino;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.richclient.settings.Settings;

import com.jidesoft.docking.DockContext;
import com.jidesoft.grid.TableColumnChooser;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.pivot.AggregateTableModel;

import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoAnagraficaBD;
import it.eurotn.panjea.magazzino.util.ConfrontoListinoDTO;
import it.eurotn.panjea.magazzino.util.RigaConfrontoListinoDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaConfrontoListino;
import it.eurotn.rich.editors.AbstractTablePageEditor;
import it.eurotn.rich.editors.table.EEditPageMode;
import it.eurotn.rich.editors.table.EditFrame;

/**
 * @author fattazzo
 *
 */
public class RisultatiRicercaConfrontoListinoTablePage extends AbstractTablePageEditor<RigaConfrontoListinoDTO> {

    public static final String PAGE_ID = "risultatiRicercaConfrontoListinoTablePage";

    private ParametriRicercaConfrontoListino parametriRicercaConfrontoListino;

    private IMagazzinoAnagraficaBD magazzinoAnagraficaBD;

    private ControlloStripeCellStypeProvider controlloStripeCellStypeProvider;

    /**
     * Costruttore.
     */
    protected RisultatiRicercaConfrontoListinoTablePage() {
        super(PAGE_ID, new RisultatiRicercaConfrontoListinoTableModel());
        getTable().getOverlayTable().setShowOptionPanel(false);
        getTable().setAggregatedColumns(new String[] { "articolo.categoria" });
        getTable().getTable().getTableHeader().setReorderingAllowed(false);

        controlloStripeCellStypeProvider = new ControlloStripeCellStypeProvider();
        AggregateTableModel aggregateTableModel = (AggregateTableModel) TableModelWrapperUtils
                .getActualTableModel(getTable().getTable().getModel(), AggregateTableModel.class);
        aggregateTableModel.setCellStyleProvider(controlloStripeCellStypeProvider);
    }

    @Override
    public JComponent getHeaderControl() {
        JPanel panel = getComponentFactory().createPanel(new HorizontalLayout(10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));

        JCheckBox barrePercentualiCheckBox = new JCheckBox("Visualizza barra delle percentuali");
        barrePercentualiCheckBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                RisultatiRicercaConfrontoListinoTableModel tableModel = (RisultatiRicercaConfrontoListinoTableModel) TableModelWrapperUtils
                        .getActualTableModel(getTable().getTable().getModel());
                tableModel.setEnableProgressBar(e.getStateChange() == ItemEvent.SELECTED);
                getTable().getTable().repaint();
            }
        });
        panel.add(barrePercentualiCheckBox);

        JCheckBox columnCellStripeCheckBox = new JCheckBox("Usa i colori per i confronti");
        columnCellStripeCheckBox.setSelected(true);
        columnCellStripeCheckBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                controlloStripeCellStypeProvider
                        .setEnableConfrontoColumnsStripe(e.getStateChange() == ItemEvent.SELECTED);
                getTable().getTable().repaint();
            }
        });
        panel.add(columnCellStripeCheckBox);

        return panel;
    }

    @Override
    public Collection<RigaConfrontoListinoDTO> loadTableData() {

        List<RigaConfrontoListinoDTO> righeConfronto = new ArrayList<RigaConfrontoListinoDTO>();

        if (parametriRicercaConfrontoListino.isEffettuaRicerca()) {
            ConfrontoListinoDTO confrontoListinoDTO = magazzinoAnagraficaBD
                    .caricaConfrontoListino(parametriRicercaConfrontoListino);
            righeConfronto = confrontoListinoDTO.getRigheConfronto();
        }
        return righeConfronto;
    }

    @Override
    public void onPostPageOpen() {

    }

    @Override
    public void processTableData(Collection<RigaConfrontoListinoDTO> results) {
        int numeroDecimaliPrezzo = 0;
        super.processTableData(results);

        numeroDecimaliPrezzo = results.isEmpty() ? 0 : results.iterator().next().getNumeroDecimaliPrezzo();

        RisultatiRicercaConfrontoListinoTableModel tableModel = (RisultatiRicercaConfrontoListinoTableModel) TableModelWrapperUtils
                .getActualTableModel(getTable().getTable().getModel());
        tableModel.setNumeroDecimaliPrezzo(numeroDecimaliPrezzo);

        TableColumnChooser.showAllColumns(getTable().getTable());

        tableModel.fireTableStructureChanged();
    }

    @Override
    public Collection<RigaConfrontoListinoDTO> refreshTableData() {
        return loadTableData();
    }

    /**
     * Sovrascrivo il metodo per non fare il restore state della tabella ma solo della pagina.
     *
     * @param settings
     *            settings
     */
    @Override
    public void restoreState(Settings settings) {
        try {
            if (getEditPages() != null) {
                EditFrame<RigaConfrontoListinoDTO> editFrame = getEditFrame();
                editFrame.setAvailable(true);
                if (settings.contains(PAGE_ID + "." + EditFrame.EDIT_FRAME_ID + ".popupMode")) {
                    if (settings.getBoolean(PAGE_ID + "." + EditFrame.EDIT_FRAME_ID + ".popupMode")) {
                        editPageMode = EEditPageMode.POPUP;
                        editFrame.setEditMode(editPageMode);
                        editFrame.getContext().setCurrentMode(DockContext.MODE_FLOATABLE);
                        holderPanel.getDockingManager().hideFrame(EditFrame.EDIT_FRAME_ID);
                    } else {
                        editPageMode = EEditPageMode.DETAIL;
                        editFrame.setEditMode(editPageMode);
                    }
                }
                updateTableCommands();
            }
        } catch (Exception e) {
            logger.warn("-->Impossibile ripristinare lo stato della pagina " + getId());
        }
    }

    /**
     * Sovrascrivo il metodo per non fare il save state della tabella ma solo della pagina.
     *
     * @param settings
     *            settings
     */
    @Override
    public void saveState(Settings settings) {
        try {
            if (getEditPages() != null) {
                EditFrame<RigaConfrontoListinoDTO> editFrame = getEditFrame();

                settings.setBoolean(PAGE_ID + "." + EditFrame.EDIT_FRAME_ID + ".popupMode",
                        editFrame.getEditMode() == EEditPageMode.POPUP);

            }
        } catch (Exception e) {
            logger.warn("-->Impossibile salvare lo stato della pagina " + getId());
        }
    }

    @Override
    public void setFormObject(Object object) {
        this.parametriRicercaConfrontoListino = (ParametriRicercaConfrontoListino) object;
    }

    /**
     * @param magazzinoAnagraficaBD
     *            the magazzinoAnagraficaBD to set
     */
    public void setMagazzinoAnagraficaBD(IMagazzinoAnagraficaBD magazzinoAnagraficaBD) {
        this.magazzinoAnagraficaBD = magazzinoAnagraficaBD;
    }

}
