package it.eurotn.panjea.rich.editors.query;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.swing.JComponent;

import org.apache.commons.lang3.StringUtils;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.rich.editors.query.table.QueryTreeTableComponent;
import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.querybuilder.domain.filter.OperatoreQuery;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

public abstract class QueryEditorForm<T> extends PanjeaAbstractForm {

    private class FilterPropertyChange implements PropertyChangeListener {

        private String path;

        public FilterPropertyChange(final String path) {
            this.path = path;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            addFilter(path, evt.getNewValue());
        }

    }

    public static final String PERIODO_SUFFIX = "$Periodo";

    private QueryTreeTableComponent treeTableComponent;

    /**
     * Costruttore.
     *
     * @param entity
     *            entity da gestire
     */
    public QueryEditorForm(final EntityBase entity) {
        super(PanjeaFormModelHelper.createFormModel(entity, false, entity.getClass().getName() + "FormModel"),
                entity.getClass().getName() + "Form");
    }

    @SuppressWarnings("unchecked")
    private void addFilter(String path, Object value) {
        path = StringUtils.remove(path, PERIODO_SUFFIX);
        String[] pathSplit = path.split("\\.");
        Queue<String> pathToken = new LinkedList<>();
        for (String tok : pathSplit) {
            pathToken.add(tok);
        }

        List<ProprietaEntityRow> rows = treeTableComponent.getTreeTable().getQueryTableModel().getOriginalRows();
        ProprietaEntityRow beanPathFind = null;

        boolean filter = treeTableComponent.getTreeTable().getFilterableTreeTableModel().isFiltersApplied();
        treeTableComponent.getTreeTable().getFilterableTreeTableModel().setFiltersApplied(false);
        while (!pathToken.isEmpty()) {
            path = pathToken.poll();
            for (ProprietaEntityRow beanPathRow : rows) {
                if (path.equals(beanPathRow.getProprieta().getNome())) {
                    beanPathFind = beanPathRow;
                    rows = (List<ProprietaEntityRow>) beanPathRow.getChildren();

                    treeTableComponent.getTreeTable().getQueryTableModel().expandRow(beanPathRow, true);
                    break;
                }
            }
        }

        if (value instanceof Periodo) {
            applyFilterValueForPeriodo(beanPathFind, (Periodo) value);
        } else {
            applyFilterValue(beanPathFind, value);
        }

        treeTableComponent.getTreeTable().getFilterableTreeTableModel().fireTableDataChanged();
        treeTableComponent.getTreeTable().getFilterableTreeTableModel().setFiltersApplied(filter);
    }

    private void applyFilterValue(ProprietaEntityRow row, Object value) {
        OperatoreQuery operatore = value == null ? null : OperatoreQuery.UGUALE;

        row.getProprieta().setFiltro(value);
        row.getProprieta().setOperatore(operatore);
    }

    private void applyFilterValueForPeriodo(ProprietaEntityRow row, Periodo periodo) {

        OperatoreQuery operatore = null;
        String value = "";

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        if (periodo.getDataIniziale() != null && periodo.getDataFinale() != null) {
            String dataIniString = format.format(periodo.getDataIniziale());
            String dataFinString = format.format(periodo.getDataFinale());

            operatore = dataIniString.equals(dataFinString) ? OperatoreQuery.UGUALE : OperatoreQuery.TRA;
            value = dataIniString.equals(dataFinString) ? dataIniString : dataIniString + " | " + dataFinString;
        } else if (periodo.getDataIniziale() != null && periodo.getDataFinale() == null) {
            operatore = OperatoreQuery.MAGGIORE_UGUALE;
            value = format.format(periodo.getDataIniziale());
        } else if (periodo.getDataIniziale() == null && periodo.getDataFinale() != null) {
            operatore = OperatoreQuery.MINORE_UGUALE;
            value = format.format(periodo.getDataFinale());
        }

        row.getProprieta().setFiltro(value);
        row.getProprieta().setOperatore(operatore);
    }

    protected abstract JComponent createAdditionalSearchControl();

    @Override
    protected JComponent createFormControl() {
        FormLayout layout = new FormLayout("fill:pref:grow", "4dlu,pref,4dlu,fill:pref:grow");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        JComponent panelAdditionalControl = createAdditionalSearchControl();
        builder.add(panelAdditionalControl, cc.xy(1, 2));
        builder.add(createTreeSearch(), cc.xy(1, 4));

        @SuppressWarnings("unchecked")
        Set<String> names = getFormModel().getFieldNames();
        for (String propertyPath : names) {
            addFormValueChangeListener(propertyPath, new FilterPropertyChange(propertyPath));
        }
        return builder.getPanel();
    }

    private JComponent createTreeSearch() {
        treeTableComponent = new QueryTreeTableComponent();
        treeTableComponent.getTreeTable().updateModel(getFormObject().getClass(), getDefaultFilterProperties(),
                getDefaultSelectProperties());
        return treeTableComponent;
    }

    protected abstract String[] getDefaultFilterProperties();

    protected abstract String[] getDefaultSelectProperties();

    public List<ProprietaEntity> getSeachRow() {
        List<ProprietaEntityRow> righe = treeTableComponent.getTreeTable().getQueryTableModel().getRows(true);
        List<ProprietaEntity> righeQuery = new ArrayList<>();
        for (ProprietaEntityRow row : righe) {
            righeQuery.add(row.getProprieta());
        }
        return righeQuery;
    }

    /**
     * Applica o rimuove i filtri della tree.
     */
    public void toggleTreePropertyFilter() {
        treeTableComponent.getTreeTable().toggleTreePropertyFilter();
    }

}
