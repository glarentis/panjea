package it.eurotn.panjea.rich.factory.table;

import it.eurotn.panjea.rich.factory.JecTable;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.springframework.richclient.factory.TableFactory;

import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.swing.TableSearchable;

public class JXTableFactory implements TableFactory {
    static Logger logger = Logger.getLogger(JXTableFactory.class);

    private Map<Class<Object>, TableCellRenderer> cellRenderers = null;

    private static final Color EVEN_ROW_COLOR = new Color(241, 245, 250);
    private static final Color TABLE_GRID_COLOR = new Color(0xd9d9d9);
    private static final Color SELECTION_ACTIVE_SELECTION_BACKGROUND_COLOR = new Color(0x3d80df);
    private static final Color SELECTION_INACTIVE_SELECTION_BACKGROUND_COLOR = new Color(0xc0c0c0);
    private static final Color SELECTION_ACTIVE_BOTTOM_BORDER_COLOR = new Color(125, 170, 234);
    private static final Color SELECTION_INACTIVE_BOTTOM_BORDER_COLOR = new Color(224, 224, 224);

    private void configureTable(JTable table) {
        ((JXTable) table).setColumnControlVisible(true);

        Highlighter highlighter = HighlighterFactory.createAlternateStriping(JecTable.whiteColor,
                JecTable.alternateColor);
        ((JXTable) table).setHighlighters(highlighter);

        TableSearchable tableSearchable = SearchableUtils.installSearchable(table);
        tableSearchable.setMainIndex(-1); // search for all columns

        table.setDefaultRenderer(String.class, new CustomTableCellRenderer(SwingConstants.LEFT));

        if (cellRenderers != null) {
            // setto tutti i renderer configurati nell'xml
            for (Entry<Class<Object>, TableCellRenderer> entry : cellRenderers.entrySet()) {
                table.setDefaultRenderer(entry.getKey(), entry.getValue());
            }
        } else {
            throw new RuntimeException("Nessun renderer configurato.");
        }

        // installo tutti i comparator per le colonne
        // for (int i = 0; i < ((JXTable) table).getColumnCount(); i++) {
        // ((JXTable) table).getColumnExt(i).setComparator(
        // ((AbstractCustomTableCellRenderer) ((JXTable) table).getColumnExt(i).getCellRenderer()));
        // }

        table.setDefaultEditor(BigDecimal.class, new NumberCellEditor());
        table.setDefaultEditor(Double.class, new NumberCellEditor());

        table.setShowVerticalLines(true);

        table.setOpaque(false);
        table.setGridColor(TABLE_GRID_COLOR);

        // table.getTableHeader().setDefaultRenderer(new ITunesTableHeaderRenderer(table));

        table.setShowHorizontalLines(false);

    }

    @Override
    public JTable createTable() {
        JXTable table = new JecTable();
        configureTable(table);
        return table;
    }

    @Override
    public JTable createTable(TableModel model) {
        JXTable table = new JecTable(model);
        configureTable(table);
        return table;
    }

    public JTable createTreeTable() {
        logger.debug("--> Creo una treeTable");
        JXTreeTable table = new JXTreeTable() {

            private static final long serialVersionUID = 5538282226902003119L;

            /*
             * (non-Javadoc)
             * 
             * @see javax.swing.JTable#getScrollableTracksViewportHeight()
             */
            @Override
            public boolean getScrollableTracksViewportHeight() {
                return getPreferredSize().height < getParent().getHeight();
            }

        };
        configureTable(table);
        return table;
    }

    public void setCellRenderers(Map<Class<Object>, TableCellRenderer> cellRenderers) {
        this.cellRenderers = cellRenderers;
    }
}