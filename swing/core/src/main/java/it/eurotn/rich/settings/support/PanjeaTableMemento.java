/**
 * 
 */
package it.eurotn.rich.settings.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.support.ArrayUtil;
import org.springframework.richclient.settings.support.Memento;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Non ho esteso TableMemento dato che quei furbi di spring rcp non hanno fatto metodi protected (cmq la classe è nel
 * package sandbox), a causa degli errori sul restore della riga selezionata, ho preferito copiare per intero la classe,
 * aggiungendo in più il salvataggio delle righe visualizzate. Tutti i metodi sul restoreState sono sotto blocco
 * try-catch per evitare in caso di errori particolari il blocco del programma.
 * 
 * @author Leonardo
 */
public class PanjeaTableMemento implements Memento {

    private static Logger logger = Logger.getLogger(PanjeaTableMemento.class);

    protected static final String COLUMN_WIDTHS = "columnWidths";
    protected static final String COLUMN_ORDER = "columnOrder";
    protected static final String SELECTED_ROWS = "selectedRows";
    protected static final String ANCHOR = "anchor";
    protected static final String LEAD = "lead";
    protected static final String VISIBLE_COLUMNS = "visibleColumns";

    /**
     * Returns the position of the column for the given model index. The model index remains constant, but the position
     * changes as the columns are moved.
     * 
     * @param table
     *            the table
     * @param modelIndex
     *            the modelIndex
     * @return the position
     */
    private static int getPosition(JTable table, int modelIndex) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            if (column.getModelIndex() == modelIndex) {
                return i;
            }
        }
        throw new IllegalArgumentException("No column with modelIndex " + modelIndex + " found");
    }

    private JTable table = null;
    private String key = null;

    /**
     * Costruttore.
     * 
     * @param table
     *            la tabella di cui salvare le impostazioni
     * @param key
     *            la chiave con cui salvare le impostazioni della tabella
     */
    public PanjeaTableMemento(JTable table, String key) {
        Assert.notNull(table, "Table cannot be null");
        Assert.isTrue(StringUtils.hasText(key) || StringUtils.hasText(table.getName()),
                "Key is empty or table has no name");

        if (!StringUtils.hasText(key)) {
            key = table.getName();
        }

        this.table = table;
        this.key = key;
    }

    /**
     * @return key
     */
    protected String getKey() {
        return key;
    }

    /**
     * @return table
     */
    public JTable getTable() {
        return table;
    }

    /**
     * Esegue il ripristino dell'ordine delle colonne.
     * 
     * @param settings
     *            i settings da cui recuperare le impostazioni salvate
     */
    protected void restoreColumnOrder(Settings settings) {
        table.getSelectionModel().clearSelection();
        String orderSetting = settings.getString(key + "." + COLUMN_ORDER);
        if (StringUtils.hasText(orderSetting)) {
            String[] stringColumns = orderSetting.split(",");

            try {
                int[] columns = ArrayUtil.toIntArray(stringColumns);

                if (columns.length == table.getColumnCount()) {
                    for (int i = 0; i < columns.length; i++) {
                        table.moveColumn(getPosition(table, columns[i]), i);
                    }
                } else {
                    logger.warn("Unable to restore column order, table has " + table.getColumnCount() + " columns, "
                            + columns.length + " columns stored in settings");
                }
            } catch (IllegalArgumentException e) {
                logger.warn("Unable to restore column order.", e);
            }
        }
    }

    /**
     * Esegue il ripristino della larghezza delle colonne.
     * 
     * @param settings
     *            i settings da cui recuperare le impostazioni salvate
     */
    protected void restoreColumnWidths(Settings settings) {
        table.getSelectionModel().clearSelection();
        String widthSetting = settings.getString(key + "." + COLUMN_WIDTHS);
        if (StringUtils.hasText(widthSetting)) {

            String[] stringWidths = widthSetting.split(",");

            try {
                int[] widths = ArrayUtil.toIntArray(stringWidths);

                if (widths.length == table.getColumnCount()) {
                    for (int i = 0; i < widths.length; i++) {
                        table.getColumnModel().getColumn(i).setWidth(widths[i]);
                        table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
                    }
                } else {
                    logger.warn("Unable to restore column widths, table has " + table.getColumnCount() + " columns, "
                            + widths.length + " columns stored in settings");
                }
            } catch (IllegalArgumentException e) {
                logger.warn("Unable to restore column widths", e);
            }
        }
    }

    /**
     * Esegue il ripristino delle righe selezionate.
     * 
     * @param settings
     *            i settings da cui recuperare le impostazioni salvate
     */
    protected void restoreSelectedRows(Settings settings) {
        table.getSelectionModel().clearSelection();
        if (settings.contains(key + "." + SELECTED_ROWS)) {
            String selection = settings.getString(key + "." + SELECTED_ROWS);
            if (StringUtils.hasText(selection)) {
                String[] parts = selection.split(",");

                // find max row, so we can check before restoring row selections
                String lastPart = parts[parts.length - 1];
                int maxRow = -1;
                if (lastPart.indexOf('-') >= 0) {
                    maxRow = Integer.parseInt(lastPart.substring(lastPart.indexOf('-')));
                } else {
                    maxRow = Integer.parseInt(lastPart);
                }
                if (maxRow <= table.getRowCount() - 1) {
                    for (String part : parts) {
                        if (part.indexOf('-') >= 0) {
                            String[] tmp = part.split("-");
                            table.addRowSelectionInterval(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
                        } else {
                            int index = Integer.parseInt(part);
                            table.addRowSelectionInterval(index, index);
                        }
                    }
                } else {
                    logger.warn("Unable to restore row selection, table has " + table.getRowCount()
                            + " rows, setting has max row " + maxRow);
                }
            }
        } else {
            if (table.getRowCount() > 0) {
                table.addRowSelectionInterval(0, 0);
            }
        }

        if (settings.contains(key + "." + ANCHOR)) {
            table.getSelectionModel().setAnchorSelectionIndex(settings.getInt(key + "." + ANCHOR));
        }
        if (settings.contains(key + "." + LEAD)) {
            table.getSelectionModel().setLeadSelectionIndex(settings.getInt(key + "." + LEAD));
        }
    }

    @Override
    public void restoreState(Settings settings) {
        // il primo da chiamare � il visibleColumns dato che il save salva le info sulle colonne visualizzate
        try {
            restoreVisibleColumns(settings);
        } catch (Exception e) {
            logger.error("--> Errore nel ripristinare le colonne visualizzate");
        }
        try {
            restoreColumnOrder(settings);
        } catch (Exception e) {
            logger.error("--> Errore nel ripristinare l'ordine delle colonne");
        }
        try {
            // restoreColumnWidths(settings);
        } catch (Exception e) {
            logger.error("--> Errore nel ripristinare la larghezza delle colonne");
        }
        try {
            // restoreSelectedRows(settings);
        } catch (Exception e) {
            logger.debug("--> Errore nel ripristinare la riga selezionata");
        }
    }

    /**
     * Esegue il ripristino delle colonne visibili.
     * 
     * @param settings
     *            i settings da cui recuperare le impostazioni salvate
     */
    protected void restoreVisibleColumns(Settings settings) {
        table.getSelectionModel().clearSelection();
        String visibleSetting = settings.getString(key + "." + VISIBLE_COLUMNS);
        if (StringUtils.hasText(visibleSetting)) {
            logger.debug("--> restore delle colonne " + visibleSetting);
            // array di colonne presenti
            String[] stringWidths = visibleSetting.split(",");
            // list di colonne presenti
            List<String> strings = Arrays.asList(stringWidths);
            logger.debug("--> lista colonne da tenere visualizzate " + strings.size());
            logger.debug("--> lista colonne totali della tabella " + table.getColumnCount());
            int cols = table.getColumnCount();
            List<TableColumn> columnsToRemove = new ArrayList<TableColumn>();
            // ciclo sulle colonne della tabella (tutte quelle presenti di default)
            for (int i = 0; i < cols; i++) {
                Object header = table.getColumnModel().getColumn(i).getHeaderValue();
                logger.debug("--> la colonna " + header + " � presente nella lista di colonne da visualizzare "
                        + strings.contains(header));
                // se la colonna corrente non � presente nella list di colonne visibili la aggiungo alla
                // lista di colonne da eliminare
                if (!strings.contains(header)) {
                    TableColumn col = table.getColumnModel().getColumn(i);
                    columnsToRemove.add(col);
                }
            }

            for (TableColumn tableColumnToRemove : columnsToRemove) {
                if (table instanceof JXTable) {
                    // non posso fare la remove della colonna perchè la JXTable con la remove toglie le colonne
                    // anche dalla tabella e dal popup
                    try {
                        TableColumnExt tableColumnExt = ((JXTable) table)
                                .getColumnExt(tableColumnToRemove.getIdentifier());
                        tableColumnExt.setVisible(false);
                    } catch (Exception e) {
                        logger.error("--> errore in restoreVisibleColumns", e);
                    }
                } else {
                    table.removeColumn(tableColumnToRemove);
                }
            }
        }
    }

    /**
     * Salva le impostazioni dell'ordine delle colonne.
     * 
     * @param settings
     *            i settings su cui salvare le impostazioni
     */
    protected void saveColumnOrder(Settings settings) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            sb.append(column.getModelIndex());
            if (i < table.getColumnModel().getColumnCount() - 1) {
                sb.append(",");
            }
        }
        settings.setString(key + "." + COLUMN_ORDER, sb.toString());
    }

    /**
     * Salva le impostazioni della larghezza delle colonne.
     * 
     * @param settings
     *            i settings su cui salvare le impostazioni
     */
    protected void saveColumnWidths(Settings settings) {
        StringBuffer sb = new StringBuffer();
        int columnCount = table.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            sb.append(table.getColumnModel().getColumn(i).getWidth());
            if (i < columnCount - 1) {
                sb.append(",");
            }
        }
        settings.setString(key + "." + COLUMN_WIDTHS, sb.toString());
    }

    /**
     * Salva le impostazioni delle righe selezionate.
     * 
     * @param settings
     *            i settings su cui salvare le impostazioni
     */
    protected void saveSelectedRows(Settings settings) {
        String settingsKey = key + "." + SELECTED_ROWS;
        if (settings.contains(settingsKey)) {
            settings.remove(settingsKey);
        }

        if (table.getSelectedRowCount() > 0) {
            settings.setInt(key + "." + ANCHOR, table.getSelectionModel().getAnchorSelectionIndex());
            settings.setInt(key + "." + LEAD, table.getSelectionModel().getLeadSelectionIndex());
        }

        String selectionString = ArrayUtil.asIntervalString(table.getSelectedRows());
        if (selectionString.length() > 0) {
            settings.setString(settingsKey, selectionString);
        }
    }

    @Override
    public void saveState(Settings settings) {
        saveSelectedRows(settings);
        saveColumnOrder(settings);
        saveColumnWidths(settings);
        saveVisibleColumns(settings);
    }

    /**
     * Salva le impostazioni delle colonne visibili.
     * 
     * @param settings
     *            i settings su cui salvare le impostazioni
     */
    protected void saveVisibleColumns(Settings settings) {
        StringBuffer sb = new StringBuffer();
        int columnCount = table.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            sb.append(table.getColumnModel().getColumn(i).getHeaderValue());
            if (i < columnCount - 1) {
                sb.append(",");
            }
        }
        settings.setString(key + "." + VISIBLE_COLUMNS, sb.toString());
    }

}