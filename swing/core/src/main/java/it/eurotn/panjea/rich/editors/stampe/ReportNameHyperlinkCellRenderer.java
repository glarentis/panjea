package it.eurotn.panjea.rich.editors.stampe;

import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.HyperlinkTableCellEditorRenderer;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.swing.JideButton;

public class ReportNameHyperlinkCellRenderer extends HyperlinkTableCellEditorRenderer {

    private class PredefinitoActionListener implements ActionListener {

        private LayoutStampa layoutStampa;
        private int row;
        private TableModel tableModel;

        /**
         * Costruttore.
         * 
         * @param layoutStampa
         *            layout
         * @param row
         *            riga
         * @param tableModel
         *            table model
         */
        public PredefinitoActionListener(final LayoutStampa layoutStampa, final int row, final TableModel tableModel) {
            super();
            this.layoutStampa = layoutStampa;
            this.row = row;
            this.tableModel = tableModel;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!layoutStampa.getPredefinito()) { // FIXME && layoutStampa.getEntita() == null
                final DefaultBeanTableModel innerModel = (DefaultBeanTableModel) TableModelWrapperUtils
                        .getActualTableModel(tableModel);
                tableModel.setValueAt(true, row, (int) innerModel.getColumnPropertyPosition().get("predefinito"));
            }
        }

    }

    private static final long serialVersionUID = -6789676317230472240L;

    public static final EditorContext CONTEXT = new EditorContext("reportNameHyperlinkCellRenderer");

    @SuppressWarnings("rawtypes")
    @Override
    public void configureTableCellEditorRendererComponent(JTable jtable, Component component, boolean flag, Object obj,
            boolean flag1, boolean flag2, final int i, int k) {
        super.configureTableCellEditorRendererComponent(jtable, component, flag, obj, flag1, flag2, i, k);

        if (component instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) component;

            final DefaultBeanTableModel innerModel = (DefaultBeanTableModel) TableModelWrapperUtils
                    .getActualTableModel(jtable.getModel());
            final int roxInner = TableModelWrapperUtils.getActualRowAt(jtable.getModel(), i);

            Font font = button.getFont();
            button.setFont(new Font(font.getName(), Font.PLAIN, font.getSize()));
            if (roxInner != -1) {
                final LayoutStampa layoutStampa = (LayoutStampa) innerModel.getElementAt(roxInner);

                if (layoutStampa.getPredefinito()) {
                    button.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
                }

                button.addActionListener(new PredefinitoActionListener(layoutStampa, roxInner, innerModel));
            }
            button.setIcon(RcpSupport.getIcon(LayoutStampa.class.getName()));
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Component getTableCellEditorComponent(JTable table, Object obj, boolean flag, int row, int j) {
        JideButton button = (JideButton) super.getTableCellEditorComponent(table, obj, flag, row, j);

        final DefaultBeanTableModel innerModel = (DefaultBeanTableModel) TableModelWrapperUtils
                .getActualTableModel(table.getModel());
        final int roxInner = TableModelWrapperUtils.getActualRowAt(table.getModel(), row);

        Font font = button.getFont();
        button.setFont(new Font(font.getName(), Font.PLAIN, font.getSize()));
        if (roxInner != -1) {
            final LayoutStampa layoutStampa = (LayoutStampa) innerModel.getElementAt(roxInner);

            if (layoutStampa.getPredefinito()) {
                button.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
            }
        }
        button.setIcon(RcpSupport.getIcon(LayoutStampa.class.getName()));

        return button;
    }
}
