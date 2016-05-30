package it.eurotn.panjea.anagrafica.rich.editors.riepilogo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class RiepilogoSedeEntitaTablePage extends AbstractTablePageEditor<RiepilogoSedeEntitaDTO> {

    public static final String PAGE_ID = "riepilogoSedeEntitaTablePage";

    private IAnagraficaBD anagraficaBD;

    /**
     *
     * Costruttore.
     *
     */
    protected RiepilogoSedeEntitaTablePage() {
        super(PAGE_ID,
                new String[] { "entita", "entita.tipoEntita", "sedeEntita", "codicePagamento", "causaleTrasporto",
                        "trasportoCura", "aspettoEsteriore", "tipoPorto", "categoriaContabileSedeMagazzino",
                        "categoriaSedeMagazzino", "tipologiaGenerazioneDocumentoFatturazione",
                        "tipologiaCodiceIvaAlternativo", "codiceIvaAlternativo", "raggruppamentoBolle", "calcoloSpese",
                        "stampaPrezzo", "tipoSede", "codiceValuta", "lingua", "zonaGeografica", "nazione", "localita",
                        "cap", "lvl1", "lvl2", "lvl3", "lvl4", "bloccoSede", "listino", "listinoAlternativo", "agente",
                        "dataScadenza", "indirizzoMail", "sedeEntita.sede.indirizzo", "codicePagamentoCodice" },
                RiepilogoSedeEntitaDTO.class);
    }

    @Override
    public Collection<RiepilogoSedeEntitaDTO> loadTableData() {

        List<RiepilogoSedeEntitaDTO> riepilogo = new ArrayList<RiepilogoSedeEntitaDTO>();
        riepilogo = anagraficaBD.caricaRiepilogoSediEntita();

        return riepilogo;
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public Collection<RiepilogoSedeEntitaDTO> refreshTableData() {
        return loadTableData();
    }

    /**
     * @param anagraficaBD
     *            the anagraficaBD to set
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    @Override
    public void setFormObject(Object object) {
    }

}
