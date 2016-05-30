package it.eurotn.panjea.rich.editors.query;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.springframework.richclient.command.support.ApplicationWindowAwareCommand;
import org.springframework.richclient.progress.ProgressMonitor;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.grid.TreeTableModel;
import com.jidesoft.spring.richclient.docking.editor.AbstractEditor;

import it.eurotn.panjea.rich.bd.IQueryBuilderBD;
import it.eurotn.panjea.rich.editors.query.table.QueryTreeTable;
import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.querybuilder.domain.ResultCriteria;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.JideTableWidget;

public class QueryEditor extends AbstractEditor {

    private class ExecuteCommand extends ApplicationWindowAwareCommand {

        @Override
        public void attach(AbstractButton button) {
            super.attach(button);
            button.setText("ESEGUI QUERY");
        }

        @Override
        protected void doExecuteCommand() {
            TreeTableModel model = (TreeTableModel) TableModelWrapperUtils.getActualTableModel(table.getModel(),
                    TreeTableModel.class);
            List<ProprietaEntityRow> righe = model.getRows();
            IQueryBuilderBD bd = RcpSupport.getBean("queryBuilderBD");
            List<ProprietaEntity> righeQuery = new ArrayList<>();
            for (ProprietaEntityRow row : righe) {
                righeQuery.add(row.getProprieta());
            }
            ResultCriteria result = bd.execute((Class<?>) entityComboBox.getSelectedItem(), righeQuery);
            System.out.println(result.toString());

            DefaultBeanTableModel<?> tm = new DefaultBeanTableModel<>("", result.getColonne(), result.getType());
            if (resultTable != null) {
                resultTable.dispose();
                resultTable = null;
                BorderLayout layout = (BorderLayout) querycontrol.getLayout();
                querycontrol.remove(layout.getLayoutComponent(BorderLayout.CENTER));
            }
            resultTable = new JideTableWidget<>("resultQuery", tm);
            querycontrol.add(resultTable.getComponent(), BorderLayout.CENTER);
            resultTable.setRows(result.getRisultati());
            // try {
            // QueryEditorForm form = new QueryEditorForm(Listino.class.newInstance(), righeQuery);
            // JComponent formControl = form.createFormControl();
            // BorderLayout layout = (BorderLayout) querycontrol.getLayout();
            // if (layout.getLayoutComponent(BorderLayout.CENTER) != null) {
            // querycontrol.remove(layout.getLayoutComponent(BorderLayout.CENTER));
            // }
            // querycontrol.add(formControl, BorderLayout.CENTER);
            // formControl.revalidate();
            // querycontrol.revalidate();
            // } catch (InstantiationException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // } catch (IllegalAccessException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }

        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setText("ESEGUI QUERY");
        }

        private void stampaProperty(ProprietaEntity prop, String parent) {
            if (prop.isInSelect()) {
                System.out
                        .println(parent + "." + prop.getNome() + " : " + prop.isInSelect() + " : " + prop.getFiltro());
            }
            if (prop.getProprieta() != null) {
                for (ProprietaEntity prop2 : prop.getProprieta()) {
                    stampaProperty(prop2, parent + "." + prop.getNome());
                }
            }
        }

    }

    private QueryTreeTable table;

    private JideTableWidget resultTable;
    private JPanel querycontrol;
    private JPanel rootPanel;

    private JComboBox<Class<?>> entityComboBox = new JComboBox<>();

    @Override
    public JComponent getControl() {
        if (rootPanel != null) {
            return rootPanel;
        }
        rootPanel = new JPanel(new BorderLayout());
        querycontrol = new JPanel(new BorderLayout());
        table = new QueryTreeTable();
        // do not select row when expanding a row.

        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        entityComboBox = new JComboBox<>();
        panel.add(entityComboBox, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        querycontrol.add(panel, BorderLayout.NORTH);
        // resultTable = new JideTableWidget("id", new String[] { "id", "userInsert" },
        // Listino.class);
        // querycontrol.add(resultTable.getComponent());
        rootPanel.add(querycontrol, BorderLayout.CENTER);

        ExecuteCommand command = new ExecuteCommand();
        rootPanel.add(command.createButton(), BorderLayout.SOUTH);
        return rootPanel;
    }

    @Override
    public String getId() {
        return "queryEditor";
    }

    @Override
    public void initialize(Object editorObject) {
        final IQueryBuilderBD bd = RcpSupport.getBean("queryBuilderBD");

        List<Class<?>> entity = bd.caricaAllEntityQuerable();
        DefaultComboBoxModel<Class<?>> model = new DefaultComboBoxModel<>(entity.toArray(new Class<?>[entity.size()]));
        entityComboBox.setModel(model);
        entityComboBox.setSelectedIndex(0);
        entityComboBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    table.updateModel((Class<?>) event.getItem(), new String[] {}, new String[] {});
                }
            }
        });
    }

    @Override
    public void save(ProgressMonitor arg0) {
    }

}
