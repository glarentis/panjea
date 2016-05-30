package it.eurotn.panjea.rich.editors.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.rich.bd.IQueryBuilderBD;
import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.querybuilder.domain.ResultCriteria;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public abstract class RisultatiRicercaQueryEditorTablePage<T> extends AbstractTablePageEditor<T> {

    private class OpenSelectedObjectCommand extends ActionCommand {

        @Override
        protected void doExecuteCommand() {
            openSelectedObject(RisultatiRicercaQueryEditorTablePage.this.getTable().getSelectedObject());
        }
    }

    private List<ProprietaEntity> righeFiltro;
    private Class<T> clazz;

    /**
     * Costruttore
     */
    public RisultatiRicercaQueryEditorTablePage(String id, Class<T> clazz) {
        super("risultatiRicercaAreaRifornimentoTablePage", new DefaultBeanTableModel<>(id, new String[] {}, clazz));
        this.clazz = clazz;
    }

    @Override
    public Collection<T> loadTableData() {
        if (CollectionUtils.isEmpty(righeFiltro)) {
            return new ArrayList<>();
        }
        IQueryBuilderBD bd = RcpSupport.getBean("queryBuilderBD");
        ResultCriteria result = bd.execute(clazz, righeFiltro);
        DefaultBeanTableModel<T> tm = new DefaultBeanTableModel<T>("", result.getColonne(), clazz);
        JideTableWidget<T> resultTable = new JideTableWidget<>("resultQuery", tm);
        setTableWidget(resultTable);
        getTable().setPropertyCommandExecutor(new OpenSelectedObjectCommand());
        // resultTable.setRows((Collection<AreaRifornimento>) result.getRisultati());
        return (Collection<T>) result.getRisultati();
    }

    @Override
    public void onPostPageOpen() {
        //
    }

    protected abstract void openSelectedObject(T selectedObject);

    @Override
    public Collection<T> refreshTableData() {
        return loadTableData();
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof List) {
            righeFiltro = (List<ProprietaEntity>) object;
        }
    }
}
