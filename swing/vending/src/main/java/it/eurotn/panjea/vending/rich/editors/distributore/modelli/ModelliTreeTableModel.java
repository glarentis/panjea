package it.eurotn.panjea.vending.rich.editors.distributore.modelli;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ObjectConverterManager;

public class ModelliTreeTableModel extends DefaultTreeTableModel {

    /**
     * Crea il modello dell'albero.
     *
     * @param node
     *            nodo principale dell'albero con i suoi figli
     */
    public ModelliTreeTableModel(final TreeTableNode node) {
        super(node);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Class getColumnClass(int column) {
        if (column == 0) {
            return TreeTableModel.class;
        }
        return super.getColumnClass(column);
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public String getColumnName(final int column) {
        return RcpSupport.getMessage("ModelliTableColonna" + column);
    }

    @Override
    public Object getValueAt(Object node, int column) {
        Object object = ((DefaultMutableTreeTableNode) node).getUserObject();

        if (column == 0) {
            return ObjectConverterManager.toString(object);
        }

        return super.getValueAt(node, column);
    }

    @Override
    public boolean isCellEditable(Object arg0, int arg1) {
        return false;
    }
}