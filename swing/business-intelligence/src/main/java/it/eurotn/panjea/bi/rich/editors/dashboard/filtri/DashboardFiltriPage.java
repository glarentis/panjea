package it.eurotn.panjea.bi.rich.editors.dashboard.filtri;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.jdesktop.swingx.VerticalLayout;
import org.springframework.richclient.dialog.AbstractDialogPage;
import org.springframework.richclient.settings.Settings;

import com.jidesoft.pivot.PivotDataModel;
import com.jidesoft.pivot.PivotField;

import it.eurotn.panjea.bi.domain.dashboard.DashBoard.LayoutFiltri;
import it.eurotn.panjea.bi.rich.editors.analisi.AnalisiBiPivotTablePane;
import it.eurotn.panjea.bi.rich.editors.dashboard.DashBoardPage;
import it.eurotn.rich.components.WrapLayout;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class DashboardFiltriPage extends AbstractDialogPage implements IPageLifecycleAdvisor, PropertyChangeListener {

    public static final String PAGE_ID = "dashboardFiltriPage";
    private JPanel filtriPanel;
    private PivotDataModel dataModel;

    private LayoutFiltri layoutFiltri;
    private JScrollPane filtriScrollPane;

    private JPanel buttonPanel;

    private Set<String> filtriPrivati;

    /**
     *
     * Costruttore.
     *
     */
    public DashboardFiltriPage() {
        super(PAGE_ID);
    }

    /**
     * Applica i filtri presenti.
     */
    public void applyFilter() {
        // uso tutti i fileds perchè eventuali fields rimossi dovranno esserlo anche da tutti quelli
        // che ricevono
        // l'evento
        Map<String, Object[]> filtriSelezionati = new HashMap<String, Object[]>();
        for (PivotField pivotField : dataModel.getFields()) {
            filtriSelezionati.put(pivotField.getName(), pivotField.getSelectedPossibleValues());
        }

        DashboardFiltriPage.this.firePropertyChange(DashBoardPage.ASSOCIAZIONE_FILTER_PROPERTY, null,
                filtriSelezionati);
    }

    /**
     * Applica il layout specificato alla pagina dei filtri.
     *
     * @param paramLayoutFiltri
     *            layout da applicare
     */
    public void applyLayout(LayoutFiltri paramLayoutFiltri) {
        this.layoutFiltri = paramLayoutFiltri;

        JPanel rootPanel = (JPanel) getControl();

        switch (paramLayoutFiltri) {
        case ORIZZONTALE:
            filtriPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 4));
            filtriScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            filtriScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            break;
        case VERTICALE:
            filtriPanel.setLayout(new VerticalLayout(4));
            filtriScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            filtriScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            break;
        default:
            filtriPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 0, 4));
            filtriScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            filtriScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            break;
        }

        getControl().repaint();
    }

    @Override
    protected JComponent createControl() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());

        filtriPanel = getComponentFactory().createPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        filtriPanel.setBorder(BorderFactory.createEmptyBorder());

        filtriScrollPane = new JScrollPane(filtriPanel);
        filtriScrollPane.setBorder(BorderFactory.createEmptyBorder());
        rootPanel.add(filtriScrollPane, BorderLayout.CENTER);

        return rootPanel;
    }

    @Override
    public void dispose() {
    }

    /**
     * @return the dataModel
     */
    public PivotDataModel getDataModel() {
        return dataModel;
    }

    /**
     * @return the layoutFiltri
     */
    public LayoutFiltri getLayoutFiltri() {
        return layoutFiltri;
    }

    @Override
    public void loadData() {
        updateFiltriPanel(dataModel);
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
        PivotField pivotField = (PivotField) evt.getNewValue();

        Map<String, Object[]> filtriSelezionati = new HashMap<String, Object[]>();
        filtriSelezionati.put(pivotField.getName(), pivotField.getSelectedPossibleValues());
        firePropertyChange(DashBoardPage.ASSOCIAZIONE_FILTER_PROPERTY, null, filtriSelezionati);

    }

    @Override
    public void refreshData() {
        loadData();
    }

    @Override
    public void restoreState(Settings arg0) {
    }

    @Override
    public void saveState(Settings arg0) {
    }

    /**
     * Setta i pivot filelds passati come parametro come filtri togliendo tutti gli altri
     * eventualmente presenti.
     *
     * @param pivotFields
     *            fields
     */
    public void setFilter(List<PivotField> pivotFields) {
        Map<String, Object[]> filtriSelezionati = new HashMap<String, Object[]>();

        // aggiungo i pivotField da aggiungere
        for (PivotField pivotField : pivotFields) {
            filtriSelezionati.put(pivotField.getName(), pivotField.getSelectedPossibleValues());
        }

        // aggiungo i pivotFiled già presenti
        for (PivotField pivotField : dataModel.getFields()) {
            Object[] values = filtriSelezionati.get(pivotField.getName());
            // aggiungo anche tutti gli altri fields perchè potrei averli rimossi
            filtriSelezionati.put(pivotField.getName(), values);
        }

        // aggiorno i pivotFiled del modello
        for (PivotField field : dataModel.getFields()) {
            if (!filtriPrivati.contains(field.getName()) && filtriSelezionati.containsKey(field.getName())) {
                Object[] values = filtriSelezionati.get(field.getName());
                if (values == null) {
                    field.setFilter(null);
                }
                field.setSelectedPossibleValues(values);
            }
        }

        // devo passare i pivot field perchè quelli aggiunti non hanno ancora possible values quindi
        // usando i fields del
        // datamodel non verrebbero aggiunti
        updateFiltriPanel(pivotFields.toArray(new PivotField[pivotFields.size()]), false);
    }

    /**
     * @param filtriPrivati
     *            the filtriPrivati to set
     */
    public void setFiltriPrivati(Set<String> filtriPrivati) {
        this.filtriPrivati = filtriPrivati;
    }

    @Override
    public void setFormObject(Object object) {
        dataModel = (PivotDataModel) object;
    }

    @Override
    public void setReadOnly(boolean readOnly) {

    }

    private void updateFiltriPanel(PivotDataModel pivotDataModel) {
        updateFiltriPanel(pivotDataModel.getFields(), true);
    }

    private void updateFiltriPanel(PivotField[] fields, boolean onlyWithFilteredValues) {
        if (filtriPanel != null) {
            filtriPanel.removeAll();
            filtriPanel.repaint();

            if (!filtriPrivati.isEmpty() || (fields != null && fields.length > 0)) {
                AnalisiBiPivotTablePane pivotTablePane = new AnalisiBiPivotTablePane();
                pivotTablePane.setPivotDataModel(dataModel);
                pivotTablePane.setShowFilterButtonsOnMouseOver(false);

                // aggiungo per primi gli eventuali filtri privati
                for (String filtroPrivato : filtriPrivati) {
                    PivotField field = dataModel.getField(filtroPrivato);
                    if (field.getSelectedPossibleValues() != null) {
                        PivotFieldFiltroComponent component = new PivotFieldFiltroComponent(field, pivotTablePane);
                        component.setReadOnly(true);
                        filtriPanel.add(component);
                    }
                }

                // aggiungo tutti gli altri escludendo quelli privati perchè già aggiunti
                for (PivotField field : fields) {
                    if (!filtriPrivati.contains(field.getName())
                            && (!onlyWithFilteredValues || field.getSelectedPossibleValues() != null)) {
                        PivotFieldFiltroComponent component = new PivotFieldFiltroComponent(field, pivotTablePane);
                        filtriPanel.add(component);
                    }
                }
            }
        }
    }

}