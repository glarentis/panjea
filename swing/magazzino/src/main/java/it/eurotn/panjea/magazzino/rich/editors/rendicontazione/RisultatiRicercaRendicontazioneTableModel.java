package it.eurotn.panjea.magazzino.rich.editors.rendicontazione;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.magazzino.rich.editors.areamagazzino.RisultatiRicercaAreaMagazzinoTablePage;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanTableModel;

public class RisultatiRicercaRendicontazioneTableModel extends DefaultBeanTableModel<AreaMagazzinoRicerca> {

    private static final long serialVersionUID = -6971923108008422202L;
    private static final ConverterContext TOTALE_CONTEXT = new NumberWithDecimalConverterContext();

    {
        TOTALE_CONTEXT.setUserObject(2);
    }

    /**
     * Costruttore.
     */
    public RisultatiRicercaRendicontazioneTableModel() {
        super(RisultatiRicercaAreaMagazzinoTablePage.PAGE_ID,
                new String[] { "selezionata", "documento.codice", "documento.dataDocumento", "documento.tipoDocumento",
                        "documento.totale.importoInValutaAzienda", "entitaDocumento", "depositoOrigine",
                        "depositoDestinazione", "dataRegistrazione", "stato", "note", "sedeEntita" },
                AreaMagazzinoRicerca.class);
    }

    @Override
    public ConverterContext getConverterContextAt(int row, int col) {
        switch (col) {
        case 4:
            return TOTALE_CONTEXT;
        default:
            return null;
        }
    }
}