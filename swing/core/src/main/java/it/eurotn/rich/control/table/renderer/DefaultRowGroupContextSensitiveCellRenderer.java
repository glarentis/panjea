package it.eurotn.rich.control.table.renderer;

import it.eurotn.rich.control.table.DefaultBeanTableModel;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.jidesoft.grid.CellRendererManager;
import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.TableModelWrapperUtils;

public class DefaultRowGroupContextSensitiveCellRenderer extends IconContextSensitiveCellRenderer {
    private static final long serialVersionUID = -951604079126791504L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        DefaultGroupRow groupRow = (DefaultGroupRow) value;
        Object objectToRender = groupRow.getConditionValue(groupRow.getLevel());
        JLabel label;
        if (objectToRender != null) {
            Class<?> classRender = objectToRender.getClass();
            DefaultBeanTableModel<?> tableModel = (DefaultBeanTableModel<?>) TableModelWrapperUtils
                    .getActualTableModel(table.getModel());
            label = (JLabel) CellRendererManager.getRenderer(classRender, tableModel.getEditorContextAt(row, column))
                    .getTableCellRendererComponent(table, objectToRender, isSelected, hasFocus, row, column);

            if (label.getIcon() == null) {
                label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        } else {
            label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        label.setHorizontalAlignment(SwingConstants.LEFT);
        StringBuilder sb = new StringBuilder();
        sb.append("<html><b><i>");
        sb.append(label.getText());
        sb.append("</b></i>");
        sb.append(" ( num elementi: ");
        sb.append(groupRow.getChildrenCount());
        sb.append(" )</html>");

        label.setText(sb.toString());
        return label;
    }

}
