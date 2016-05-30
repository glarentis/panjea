package it.eurotn.panjea.magazzino.rich.forms.listino;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.EditorContext;

import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

public class ScaglioneListinoTableModel extends DefaultBeanEditableTableModel<ScaglioneListino> {
    private static final long serialVersionUID = 4568988934180213694L;
    protected static final Color DOWN = new Color(204, 0, 0);
    private static final CellStyle ALERT_CELL_STYLE;

    static {
        ALERT_CELL_STYLE = new CellStyle();
        ALERT_CELL_STYLE.setText(null);
        ALERT_CELL_STYLE.setToolTipText(null);
        ALERT_CELL_STYLE.setBorder(null);
        ALERT_CELL_STYLE.setIcon(null);
        ALERT_CELL_STYLE.setFontStyle(-1);
        ALERT_CELL_STYLE.setForeground(Color.WHITE);
        ALERT_CELL_STYLE.setBackground(DOWN);
    }

    private EditorContext importoEditorContext = new EditorContext("numeroDecimaliEditorContext", 2);

    private ConverterContext importoConverterContext = new ConverterContext("numeroDecimaliConverterContext", 2);

    private RigaListino rigaListino;

    private BigDecimal valoreUltimoScaglione;

    private ConverterContext qtaContext = new NumberWithDecimalConverterContext(2);

    /**
     * Costruttore.
     */
    public ScaglioneListinoTableModel() {
        super("scaglioneListinoTableModel", new String[] { "quantita", "prezzo" }, ScaglioneListino.class);
    }

    @Override
    protected ScaglioneListino createNewObject() {
        ScaglioneListino scaglioneListino = new ScaglioneListino();
        double ultimoScaglione = 0;
        if (getRowCount() > 0) {
            ultimoScaglione = (Double) getValueAt(getRowCount() - 1, 0);
        }
        if (ScaglioneListino.MAX_SCAGLIONE.equals(ultimoScaglione)) {
            return null;
        }
        scaglioneListino.setQuantita(ScaglioneListino.MAX_SCAGLIONE);
        scaglioneListino.setRigaListino(rigaListino);
        if (valoreUltimoScaglione != null) {
            scaglioneListino.setPrezzo(valoreUltimoScaglione);
        }
        return scaglioneListino;
    }

    @Override
    public CellStyle getCellStyleAt(int row, int column) {
        if (row == getRowCount() - 1) {
            ScaglioneListino scaglioneListino = getObject(row);
            if (ScaglioneListino.MAX_SCAGLIONE.equals(scaglioneListino.getQuantita())
                    && BigDecimal.ZERO.compareTo(scaglioneListino.getPrezzo()) == 0) {
                return ALERT_CELL_STYLE;
            }
        }
        return super.getCellStyleAt(row, column);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int col) {
        switch (col) {
        case 0:
            return qtaContext;
        case 1:
            return importoConverterContext;
        default:
            break;
        }
        return super.getConverterContextAt(row, col);
    }

    @Override
    public EditorContext getEditorContextAt(int row, int col) {
        switch (col) {
        case 1:
            return importoEditorContext;
        default:
            break;
        }
        return super.getEditorContextAt(row, col);
    }

    @Override
    public List<ScaglioneListino> getObjects() {
        return source;
    }

    @Override
    public boolean isCellStyleOn() {
        return true;
    }

    /**
     * @param numeroDecimali
     *            numeriDecimali per formattare il prezzo
     */
    public void setNumeroDecimali(int numeroDecimali) {
        importoEditorContext.setUserObject(numeroDecimali);
        importoConverterContext.setUserObject(numeroDecimali);

        // devo aggiornare il prezzo di tutte le righe
        for (ScaglioneListino scaglione : getObjects()) {
            scaglione.setPrezzo(scaglione.getPrezzo().setScale(numeroDecimali, RoundingMode.FLOOR));
        }

        // devo notificare che i dati sono cambiati
        fireTableDataChanged();
    }

    /**
     * @param numeroDecimaliQta
     *            numero decimali per formattare la quantità
     */
    public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
        qtaContext.setUserObject(numeroDecimaliQta);
    }

    /**
     * @param rigaListino
     *            rigaListino da associare al nuovo scaglione
     */
    public void setRigaListino(RigaListino rigaListino) {
        this.rigaListino = rigaListino;
        valoreUltimoScaglione = BigDecimal.ZERO;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        if (isRowObjectChanged(value, row, column)) {
            return;
        }

        if (ScaglioneListino.MAX_SCAGLIONE.equals(getValueAt(row, 0)) && column == 0) {
            valoreUltimoScaglione = new BigDecimal(getValueAt(row, 1).toString());
            setValueAt(BigDecimal.ZERO, row, 1);
        }
        // Se modifico l'ultima riga ne genero una nuova;
        isNewRow = false;
        EditorContext editorContext = getEditorContextAt(row, column);
        if (editorContext instanceof SearchContext && ((SearchContext) editorContext).getBasePropertyPath() != null) {
            String baseProperty = ((SearchContext) editorContext).getBasePropertyPath();
            ScaglioneListino element = getElementAt(row);
            BeanWrapper elementWrapper = new BeanWrapperImpl(element);
            elementWrapper.setPropertyValue(baseProperty, value);
        } else {
            super.setValueAt(value, row, column);
        }

        if (value != null && row == (getRowCount() - 1) && isAllowInsert()) {
            addNewEmptyRow();
        }
        fireTableDataChanged();
    }
}
