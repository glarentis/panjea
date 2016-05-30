package it.eurotn.panjea.magazzino.rich.editors.validazionerighe;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.EditorContext;

import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class ValidazioneRigheTableModel extends DefaultBeanTableModel<RigaArticoloLite> {

    private static final long serialVersionUID = 2295149140303444094L;
    public static final int COL_IMPORTO = 9;

    private static ConverterContext numberContext;
    private static EditorContext numberPrezzoEditorContext;

    static {
        numberContext = new NumberWithDecimalConverterContext();
        numberContext.setUserObject(new Integer(6));
        numberPrezzoEditorContext = new EditorContext("DecimalNumberEditorContext", 6);
    }

    /**
     * Costruttore.
     */
    public ValidazioneRigheTableModel() {
        super("validazioneRigheTableModel",
                new String[] { "rulesValidationError", "areaMagazzino.documento.entita",
                        "areaMagazzino.documento.sedeEntita", "areaMagazzino.documento",
                        "areaMagazzino.dataRegistrazione", "areaMagazzino.documento.totale.importoInValuta", "articolo",
                        "descrizione", "qta", "prezzoUnitario.importoInValuta", "prezzoDeterminato",
                        "articolo.unitaMisura.codice" },
                RigaArticoloLite.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int column) {
        switch (column) {
        case 5:
        case 8:
        case 9:
        case 10:
            return numberContext;
        default:
            return null;
        }
    }

    @Override
    public EditorContext getEditorContextAt(int row, int column) {
        switch (column) {
        case COL_IMPORTO:
            return numberPrezzoEditorContext;
        default:
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
        case COL_IMPORTO:
            return true;
        default:
            return false;
        }
    }
}
