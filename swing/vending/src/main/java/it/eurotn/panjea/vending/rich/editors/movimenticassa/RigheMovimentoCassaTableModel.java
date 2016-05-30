package it.eurotn.panjea.vending.rich.editors.movimenticassa;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.vending.domain.RigaMovimentoCassa;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.eurotn.rich.control.table.editor.SearchContext;

public class RigheMovimentoCassaTableModel extends DefaultBeanEditableTableModel<RigaMovimentoCassa> {

    private static final long serialVersionUID = 2706037394279730422L;

    private static ConverterContext qtaContext;

    private static EditorContext qtaEditorContext;

    static {
        qtaContext = new NumberWithDecimalConverterContext();
        qtaContext.setUserObject(2);

        qtaEditorContext = new EditorContext("qtaEditorContext", 2);
    }

    private boolean cassaDestinazionePresente = false;

    /**
     * Costruttore.
     *
     */
    public RigheMovimentoCassaTableModel() {
        super("righeMovimentoCassaTableModel",
                new String[] { "gettone", "gettone.valore", "quantitaEntrata", "quantitaUscita" },
                RigaMovimentoCassa.class);

        cassaDestinazionePresente = false;
    }

    @Override
    protected RigaMovimentoCassa createNewObject() {
        return new RigaMovimentoCassa();
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {

        if (column == 1) {
            return qtaContext;
        }

        return super.getConverterContextAt(row, column);
    }

    @Override
    public EditorContext getEditorContextAt(int row, int col) {
        switch (col) {
        case 0:
            return new SearchContext("codice");
        case 1:
            return qtaEditorContext;
        default:
            break;
        }
        return super.getEditorContextAt(row, col);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (columnIndex == 1) {
            RigaMovimentoCassa riga = getElementAt(rowIndex);
            return riga.getGettone() == null ? null : super.getValueAt(rowIndex, columnIndex);
        }
        return super.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 0 || (column == 2 && !cassaDestinazionePresente) || column == 3;
    }

    /**
     * @param cassaDestinazionePresente
     *            the cassaDestinazionePresente to set
     */
    public void setCassaDestinazionePresente(boolean cassaDestinazionePresente) {
        this.cassaDestinazionePresente = cassaDestinazionePresente;
    }

}
