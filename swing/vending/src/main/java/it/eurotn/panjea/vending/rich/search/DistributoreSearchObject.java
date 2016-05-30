package it.eurotn.panjea.vending.rich.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.vending.manager.distributore.ParametriRicercaDistributore;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

public class DistributoreSearchObject extends AbstractSearchObject {

    public static final String ENTITA_PARAM_KEY = "entitaParam";
    public static final String PROPRIETA_CLIENTE_PARAM_KEY = "proprietaClienteParam";
    public static final String SEDE_ENTITA_PARAM_KEY = "sedeEntitaParam";
    // Con disponibili si indicano quelli che non sono ancora associati ad una installazione
    public static final String SOLO_DISPONIBILI_PARAM_KEY = "soloDisponibiliParam";

    private IVendingAnagraficaBD vendingAnagraficaBD;

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return new ArrayList<>();
    }

    @Override
    public List<?> getData(String fieldSearch, String valueSearch) {
        final ParametriRicercaDistributore parametriRicerca = new ParametriRicercaDistributore();
        switch (fieldSearch) {
        case "codice":
            parametriRicerca.setCodice(valueSearch);
            break;
        case "descrizione":
            parametriRicerca.setDescrizione(valueSearch);
            break;
        case "datiVending.modello":
            parametriRicerca.setDescrizioneModello(valueSearch);
            break;
        default:
            throw new UnsupportedOperationException("ricerca per campo " + fieldSearch + " non suportato");
        }

        final Map<String, Object> parametri = searchPanel.getMapParameters();
        if (parametri.get(PROPRIETA_CLIENTE_PARAM_KEY) != null) {
            parametriRicerca.setSoloProprietaCliente((boolean) parametri.get(PROPRIETA_CLIENTE_PARAM_KEY));
        }
        if (parametri.get(SOLO_DISPONIBILI_PARAM_KEY) != null) {
            parametriRicerca.setSoloDisponibili((boolean) parametri.get(SOLO_DISPONIBILI_PARAM_KEY));
        }

        if (parametri.get(ENTITA_PARAM_KEY) != null) {
            final EntitaLite entita = (EntitaLite) parametri.get(ENTITA_PARAM_KEY);
            parametriRicerca.setIdCliente(entita.getId());
        }

        if (parametri.get(SEDE_ENTITA_PARAM_KEY) != null) {
            final SedeEntita sedeEntita = (SedeEntita) parametri.get(SEDE_ENTITA_PARAM_KEY);
            parametriRicerca.setIdSedeCliente(sedeEntita.getId());
        }

        return vendingAnagraficaBD.ricercaDistributori(parametriRicerca);
    }

    /**
     * @param vendingAnagraficaBD
     *            the vendingAnagraficaBD to set
     */
    public final void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }
}
