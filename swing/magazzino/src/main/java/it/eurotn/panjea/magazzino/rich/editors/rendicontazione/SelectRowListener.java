package it.eurotn.panjea.magazzino.rich.editors.rendicontazione;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import com.jidesoft.grid.TableModelWrapperUtils;

import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.rich.control.table.DefaultBeanTableModel;
import it.eurotn.rich.control.table.ITable;

public class SelectRowListener extends MouseAdapter implements KeyListener {

    private JTable table;
    private DefaultBeanTableModel<AreaMagazzinoRicerca> tableModel;
    private long lastEvent;

    /**
     * Costruttore.
     *
     * @param table
     *            tabella da agganciare al listener
     */
    @SuppressWarnings("unchecked")
    public SelectRowListener(final JTable table) {
        super();
        this.table = table;
        table.addMouseListener(this);
        table.addKeyListener(this);
        tableModel = (DefaultBeanTableModel<AreaMagazzinoRicerca>) TableModelWrapperUtils
                .getActualTableModel(table.getModel());
    }

    @Override
    public void keyPressed(KeyEvent keyevent) {
    }

    @Override
    public void keyReleased(KeyEvent keyevent) {
        if (keyevent.getKeyCode() == 32) {
            int rowIndex = table.getSelectedRow();
            int columnIndex = table.getSelectedColumn();
            if (columnIndex == -1 || rowIndex == -1) {
                return;
            }

            @SuppressWarnings("unchecked")
            ITable<AreaMagazzinoRicerca> itable = (ITable<AreaMagazzinoRicerca>) table;
            if (itable.getActualColumn(columnIndex) == 0) {
                int[] indiciRighe = itable.getActualRowsAt(rowIndex, columnIndex);
                updateAreaMagazzino(indiciRighe);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent keyevent) {
    }

    @Override
    public void mouseClicked(MouseEvent mouseevent) {
        // Non so perchè l'evento viene chiamato due volte. (ved. MouseClicked
        // su eventMulticaster).
        // controllo che l'evento sia generato in tempi diversi

        if (mouseevent.getWhen() == lastEvent) {
            return;
        }
        lastEvent = mouseevent.getWhen();

        @SuppressWarnings("unchecked")
        ITable<AreaMagazzinoRicerca> itable = (ITable<AreaMagazzinoRicerca>) table;
        if (itable.checkColumn(mouseevent, 0)) {
            int[] indiciRighe = itable.getActualRowsAt(table.rowAtPoint(mouseevent.getPoint()),
                    table.columnAtPoint(mouseevent.getPoint()));
            updateAreaMagazzino(indiciRighe);
        }
    }

    /**
     *
     * @param indiciRighe
     *            indici delle righe da aggiornare
     */
    private void updateAreaMagazzino(int[] indiciRighe) {
        for (int i : indiciRighe) {
            AreaMagazzinoRicerca areaMagazzinoRicerca = tableModel.getObject(i);
            areaMagazzinoRicerca.setSelezionata(!areaMagazzinoRicerca.isSelezionata());
        }
        table.repaint();
    }
}
