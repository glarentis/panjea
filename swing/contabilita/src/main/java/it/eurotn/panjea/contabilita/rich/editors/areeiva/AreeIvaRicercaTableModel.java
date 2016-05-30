package it.eurotn.panjea.contabilita.rich.editors.areeiva;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.iva.util.RigaIvaRicercaDTO;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class AreeIvaRicercaTableModel extends DefaultBeanTableModel<RigaIvaRicercaDTO> {

    private static final long serialVersionUID = -6971923108008422202L;
    private static final ConverterContext TOTALE_CONTEXT = new NumberWithDecimalConverterContext();

    {
        TOTALE_CONTEXT.setUserObject(2);
    }

    /**
     * Costruttore.
     */
    public AreeIvaRicercaTableModel() {
        super("areeIvaRicercaTableModel",
                new String[] { "codiceDocumento", "totaleDocumento", "dataDocumento", "dataRegistrazione",
                        "tipoDocumento", "registroIva", "entita", "codiceIva", "imponibileRiga", "impostaRiga" },
                RigaIvaRicercaDTO.class);
    }
}
