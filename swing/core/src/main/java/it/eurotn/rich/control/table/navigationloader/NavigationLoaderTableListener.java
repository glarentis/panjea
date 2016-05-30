/**
 *
 */
package it.eurotn.rich.control.table.navigationloader;

import it.eurotn.panjea.rich.factory.navigationloader.AbstractLoaderActionCommand;
import it.eurotn.panjea.rich.factory.navigationloader.CellNavigationLoadersManager;
import it.eurotn.panjea.rich.factory.navigationloader.NavigationLoaderContext;
import it.eurotn.rich.command.JECCommandGroup;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.ITable;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;

import com.jidesoft.grid.DefaultGroupRow;
import com.jidesoft.grid.TableModelWrapperUtils;

/**
 * @author fattazzo
 *
 */
public class NavigationLoaderTableListener extends MouseAdapter {

    private class LoadersPopupListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (loadersPopupMenu.isShowing()) {
                loadersPopupMenu.setVisible(false);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (loadersPopupMenu.isShowing()) {
                loadersPopupMenu.setVisible(false);
            }
        }
    }

    private JTable table;

    private JPopupMenu loadersPopupMenu;
    private LoadersPopupListener loadersPopupListener;

    /**
     * Costruttore.
     *
     * @param table
     *            table
     */
    public NavigationLoaderTableListener(final JTable table) {
        super();
        this.table = table;

        loadersPopupListener = new LoadersPopupListener();
    }

    /**
     * Verifica se ci sono loaders presenti per l'oggetto nella cella al puntatore del mouse.
     *
     * @param mouseEvent
     *            mouseEvent
     * @return <code>true</code> se ci sono loaders presenti
     */
    private boolean checkLoadersPresent(MouseEvent mouseEvent) {

        int rowIndex = table.rowAtPoint(mouseEvent.getPoint());
        int columnIndex = table.columnAtPoint(mouseEvent.getPoint());

        Object value = getCellValue(rowIndex, columnIndex);
        Class<?> valueClass = value != null ? value.getClass() : null;

        NavigationLoaderContext[] navigationLoaderContexts = getNavigationLoaderContexts(rowIndex, columnIndex);

        Rectangle rectSelezione = ((ITable<?>) table).getIconCellRect(rowIndex, columnIndex);
        return CellNavigationLoadersManager.isLoadersPresent(valueClass, navigationLoaderContexts)
                && rectSelezione.contains(mouseEvent.getPoint());
    }

    /**
     * Restituisce l'oggetto contenuto nella cella richiesta.
     *
     * @param rowIndex
     *            riga
     * @param columnIndex
     *            colonna
     * @return oggetto
     */
    private Object getCellValue(int rowIndex, int columnIndex) {
        Object value = null;

        int columnIndexConvert = table.convertRowIndexToModel(table.convertColumnIndexToModel(columnIndex));
        int rowIndexConvert = table.convertRowIndexToModel(table.convertRowIndexToModel(rowIndex));
        value = table.getModel().getValueAt(rowIndexConvert, columnIndexConvert);

        if (value instanceof DefaultGroupRow) {
            value = ((DefaultGroupRow) value).getConditionValue(((DefaultGroupRow) value).getLevel());
        }

        return value;
    }

    /**
     * Resituisce il valore della prima riga utile. ( vedere getFirstRowIndex per capire l'indice usato ).
     *
     * @param rowIndex
     *            indice visuale della riga
     * @param columnIndex
     *            indice visuale della colonna
     * @return valore della riga
     */
    private Object getFirstRowValue(int rowIndex, int columnIndex) {

        Object value = null;

        @SuppressWarnings("rawtypes")
        DefaultBeanTableModel defaultBeanTableModel = (DefaultBeanTableModel) TableModelWrapperUtils
                .getActualTableModel(table.getModel(), DefaultBeanTableModel.class);
        if (defaultBeanTableModel != null) {
            // int[] rowIndexConvert = ((ITable<?>) table).getActualRowsAt(rowIndex, columnIndex);
            // value = defaultBeanTableModel.getElementAt(rowIndexConvert != null ? rowIndexConvert[0] : null);
            int rowIndexConvert = getFirstRoxIndex(rowIndex, columnIndex);
            value = defaultBeanTableModel.getElementAt(rowIndexConvert);
        }

        return value;
    }

    /**
     * Restituisce l'indice della prima riga valida. ( es: se sono su un raggruppamento della aggregate table prendo
     * come riferimento la prima riga del raggruppamento ).
     *
     * @param rowIndex
     *            indice visuale della riga
     * @param columnIndex
     *            indice visuale della colonna
     * @return indice valido
     */
    private int getFirstRoxIndex(int rowIndex, int columnIndex) {
        int[] rowIndexConvert = ((ITable<?>) table).getActualRowsAt(rowIndex, columnIndex);
        int idx = rowIndexConvert != null && rowIndexConvert.length > 0 ? rowIndexConvert[0] : -1;

        return idx;
    }

    /**
     * Resituisce i navigation loaders context configurati per la cella.
     *
     * @param rowIndex
     *            indice visuale della riga
     * @param columnIndex
     *            indice visuale della colonna
     * @return navigation loaders
     */
    private NavigationLoaderContext[] getNavigationLoaderContexts(int rowIndex, int columnIndex) {
        NavigationLoaderContext[] contexts = null;

        @SuppressWarnings("rawtypes")
        DefaultBeanTableModel defaultBeanTableModel = (DefaultBeanTableModel) TableModelWrapperUtils
                .getActualTableModel(table.getModel(), DefaultBeanTableModel.class);
        if (defaultBeanTableModel != null) {
            // int columnIndexConvert = table.convertColumnIndexToModel(columnIndex);
            int columnIndexConvert = ((ITable<?>) table).getActualColumn(columnIndex);
            int rowIndexConvert = getFirstRoxIndex(rowIndex, columnIndex);
            contexts = defaultBeanTableModel.getNavigationLoadersContextAt(rowIndexConvert, columnIndexConvert);
        }

        return contexts;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (checkLoadersPresent(mouseEvent)) {

            int rowIndex = table.rowAtPoint(mouseEvent.getPoint());
            int columnIndex = table.columnAtPoint(mouseEvent.getPoint());
            Object rowValue = getFirstRowValue(rowIndex, columnIndex);
            Object value = getCellValue(rowIndex, columnIndex);
            Class<?> valueClass = value != null ? value.getClass() : null;

            NavigationLoaderContext[] navigationLoaderContexts = getNavigationLoaderContexts(rowIndex, columnIndex);

            JECCommandGroup loadersGroup = CellNavigationLoadersManager.getLoadersCommandGroup(valueClass,
                    navigationLoaderContexts);
            if (loadersGroup.getCommands().size() == 1) {
                ActionCommand command = (ActionCommand) loadersGroup.getCommands().get(0);
                Map<String, Object> params = new HashMap<String, Object>();
                params.put(AbstractLoaderActionCommand.PARAM_LOADER_OBJECT, value);
                params.put(AbstractLoaderActionCommand.PARAM_LOADER_CONTEXT_OBJECT, rowValue);
                command.execute(params);
            } else {
                for (AbstractCommand command : loadersGroup.getCommands()) {
                    ((ActionCommand) command).addParameter(AbstractLoaderActionCommand.PARAM_LOADER_OBJECT, value);
                    ((ActionCommand) command).addParameter(AbstractLoaderActionCommand.PARAM_LOADER_CONTEXT_OBJECT,
                            rowValue);
                }
                if (loadersPopupMenu != null) {
                    loadersPopupMenu.removeMouseListener(loadersPopupListener);
                }
                loadersPopupMenu = loadersGroup.createPopupMenu();
                loadersPopupMenu.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                loadersPopupMenu.addMouseListener(loadersPopupListener);
                loadersPopupMenu.show(table, mouseEvent.getPoint().x - 10, mouseEvent.getPoint().y - 10);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if (checkLoadersPresent(mouseEvent)) {
            table.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }
}
