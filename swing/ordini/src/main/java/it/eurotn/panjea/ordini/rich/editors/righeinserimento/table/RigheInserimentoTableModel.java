package it.eurotn.panjea.ordini.rich.editors.righeinserimento.table;

import java.awt.Color;
import java.math.BigDecimal;

import org.apache.commons.lang3.ArrayUtils;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.NavigableTableModel;
import com.jidesoft.grid.StyleTableModel;

import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;
import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;
import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigheOrdineInserimento;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class RigheInserimentoTableModel extends DefaultBeanTableModel<RigaOrdineInserimento>
        implements StyleTableModel, NavigableTableModel {

    private static final long serialVersionUID = -8810733185231411239L;

    // numero di colonne che non si riferiscono agli attributi
    private static final int FIXEDCOLUMNS = 2;

    private static ConverterContext qtaContext;

    private static ConverterContext attributiContext;

    public static final CellStyle ARTICOLO_PRESENTE_STYLE = new CellStyle();

    static {
        qtaContext = new NumberWithDecimalConverterContext();
        qtaContext.setUserObject(2);

        attributiContext = new NumberWithDecimalConverterContext();
        attributiContext.setUserObject(2);

        ARTICOLO_PRESENTE_STYLE.setBackground(new Color(255, 255, 158));
    }

    private String[] attributiPresenti = null;

    /**
     * Costruttore.
     *
     * @param righeOrdineInserimento
     *            riga inserimento
     */
    public RigheInserimentoTableModel(final RigheOrdineInserimento righeOrdineInserimento) {
        super("righeInserimentoTableModel",
                ArrayUtils.addAll(new String[] { "articolo", "qtaInserimento" },
                        righeOrdineInserimento.getAttributiPresenti()
                                .toArray(new String[righeOrdineInserimento.getAttributiPresenti().size()])),
                RigaOrdineInserimento.class);
        this.attributiPresenti = righeOrdineInserimento.getAttributiPresenti()
                .toArray(new String[righeOrdineInserimento.getAttributiPresenti().size()]);
    }

    @Override
    protected String[] createColumnNames(String[] propertyColumnNames) {
        String[] colNames = super.createColumnNames(propertyColumnNames);

        // i nomi delle colonne degli attributi sono il nome stesso dell'attributo
        for (int i = 0; i < attributiPresenti.length; i++) {
            String name = attributiPresenti[i];
            colNames[i + 2] = name;
        }

        return colNames;
    }

    @Override
    public CellStyle getCellStyleAt(int row, int column) {
        return getElementAt(row).isPresenteInOrdine() ? ARTICOLO_PRESENTE_STYLE : null;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column < FIXEDCOLUMNS) {
            return super.getColumnClass(column);
        } else {
            if (getRowCount() > 0) {
                for (RigaOrdineInserimento riga : getObjects()) {
                    // devo ciclare per forza perchè devo trovare la prima riga che ha l'attributo della colonna (righe
                    // diverse possono avere o non avere gli stessi attributi)
                    if (riga.getAttributiInserimento().get(getColumnName(column)) != null) {
                        return riga.getAttributiInserimento().get(getColumnName(column)).getTipoAttributo()
                                .getTipoDato().getJavaType();
                    }
                }
            }
        }
        return String.class;
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        switch (column) {
        case 0:
            return super.getConverterContextAt(row, column);
        case 1:
            qtaContext.setUserObject(getElementAt(row).getNumeroDecimaliQuantita());
            return qtaContext;
        default:
            if (getElementAt(row).getAttributiInserimento().get(getColumnName(column)) != null
                    && getElementAt(row).getAttributiInserimento().get(getColumnName(column)).getTipoAttributo()
                            .getTipoDato() == ETipoDatoTipoAttributo.NUMERICO) {
                AttributoRigaArticolo attributo = getElementAt(row).getAttributiInserimento()
                        .get(getColumnName(column));
                attributiContext.setUserObject(attributo.getTipoAttributo().getNumeroDecimali());
                return attributiContext;
            }
            return super.getConverterContextAt(row, column);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex < FIXEDCOLUMNS) {
            return super.getValueAt(rowIndex, columnIndex);
        } else {
            RigaOrdineInserimento riga = getElementAt(rowIndex);
            if (riga != null && riga.getAttributiInserimento().get(getColumnName(columnIndex)) != null) {
                return riga.getAttributiInserimento().get(getColumnName(columnIndex)).getValoreTipizzato();
            }

            return "";
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {

        if (column == 1) {
            return true;
        } else {
            // sono editabili tutti gli attributi che sono updatable
            RigaOrdineInserimento riga = getElementAt(row);
            if (riga != null && riga.getAttributiInserimento().get(getColumnName(column)) != null) {
                return riga.getAttributiInserimento().get(getColumnName(column)).isUpdatable();
            }
        }

        return false;
    }

    @Override
    public boolean isCellStyleOn() {
        return true;
    }

    @Override
    public boolean isNavigableAt(int row, int column) {
        return isCellEditable(row, column);
    }

    @Override
    public boolean isNavigationOn() {
        return true;
    }

    @Override
    public void setValueAt(Object editedValue, int row, int column) {

        if (column < FIXEDCOLUMNS) {
            super.setValueAt(editedValue, row, column);
        } else {
            AttributoRigaArticolo attributo = getElementAt(row).getAttributiInserimento().get(getColumnName(column));
            if (attributo != null) {
                switch (attributo.getTipoAttributo().getTipoDato()) {
                case NUMERICO:
                    String resultString;
                    try {
                        resultString = new DefaultNumberFormatterFactory("#,##0",
                                attributo.getTipoAttributo().getNumeroDecimali(), BigDecimal.class)
                                        .getDefaultFormatter().valueToString(editedValue);
                    } catch (Exception e) {
                        resultString = "0";
                    }
                    attributo.setValore(resultString);
                    break;
                default:
                    attributo.setValore(editedValue.toString());
                    break;
                }
            }
        }
    }
}
