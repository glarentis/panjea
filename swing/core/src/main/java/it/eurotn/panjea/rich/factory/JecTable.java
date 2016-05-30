package it.eurotn.panjea.rich.factory;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.search.Searchable;

public class JecTable extends JXTable {
    private class JecTableSearchable extends org.jdesktop.swingx.search.TableSearchable {

        /**
         *
         * Costruttore.
         *
         * @param arg0
         *            table
         */
        public JecTableSearchable(final JXTable arg0) {
            super(arg0);
        }
    }

    private static final long serialVersionUID = -5186592067973502814L;

    public static final Color whiteColor = new Color(254, 254, 254);
    public static final Color alternateColor = new Color(237, 243, 254);
    public static final Color selectedColor = new Color(61, 128, 223);

    private static final Logger LOGGER = Logger.getLogger(JecTable.class);

    /**
     * Costruttore.
     */
    public JecTable() {
        super();
    }

    public JecTable(int arg0, int arg1) {
        super(arg0, arg1);

    }

    public JecTable(Object[][] arg0, Object[] arg1) {
        super(arg0, arg1);

    }

    public JecTable(TableModel arg0) {
        super(arg0);

    }

    public JecTable(TableModel arg0, TableColumnModel arg1) {
        super(arg0, arg1);

    }

    public JecTable(TableModel arg0, TableColumnModel arg1, ListSelectionModel arg2) {
        super(arg0, arg1, arg2);

    }

    public JecTable(Vector arg0, Vector arg1) {
        super(arg0, arg1);
    }

    @Override
    public Searchable getSearchable() {
        return new JecTableSearchable(JecTable.this);
    }

    // @Override
    // public void paintComponent(Graphics g) {
    //
    // // Let the table paint the real rows
    // super.paintComponent(g);
    //
    // // Now check if we need to paint some empty ones
    // Dimension s = getSize();
    // int rh = getRowHeight();
    // int n = getRowCount();
    // int th = n * rh;
    // if (th < s.height) {
    // // Paint the empty rows
    // int y = th;
    // while (y < s.height) {
    // g.setColor(n % 2 == 0 ? whiteColor : alternateColor);
    // g.fillRect(0, y, s.width, rh);
    // y += rh;
    // n++;
    // }
    //
    // // Paint the vertical grid lines
    // g.setColor(Color.white);
    // TableColumnModel cm = getColumnModel();
    // n = cm.getColumnCount();
    // y = th;
    // int x = 0;
    // for (int i = 0; i < n; i++) {
    // TableColumn col = cm.getColumn(i);
    // x += col.getWidth();
    // g.drawLine(x - 1, y, x - 1, s.height);
    // }
    // }
    // }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.JComponent#processMouseMotionEvent(java.awt.event.MouseEvent)
     */
    @Override
    protected void processMouseMotionEvent(MouseEvent e) {
        super.processMouseMotionEvent(e);

        // algoritmo commentato perche' se non viene chiamata la super il Drag and Drop non funziona. se invece viene
        // chiamata highlighter della row
        // non funziona correttamente
        // int row = rowAtPoint(e.getPoint());
        // Graphics g = getGraphics();
        //
        // // row changed
        // if (highLightedRow != row) {
        // if (null != dirtyRegion) {
        // paintImmediately(dirtyRegion);
        // }
        // for (int j = 0; j < getRowCount(); j++) {
        // if (row == j) {
        // //highlight
        // Rectangle firstRowRect = getCellRect(row, 0, false);
        // Rectangle lastRowRect = getCellRect(row, getColumnCount() - 1, false);
        // dirtyRegion = firstRowRect.union(lastRowRect);
        // g.setColor(new Color(150, 150, 0, 30));
        // g.fillRect((int) dirtyRegion.getX(), (int) dirtyRegion.getY(), (int) dirtyRegion.getWidth(),
        // (int) dirtyRegion.getHeight());
        // highLightedRow = row;
        // }
        // }
        // }
    }
}
