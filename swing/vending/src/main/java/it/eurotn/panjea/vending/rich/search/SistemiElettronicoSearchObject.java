package it.eurotn.panjea.vending.rich.search;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.SistemaElettronico.TipoSistemaElettronico;
import it.eurotn.panjea.vending.manager.sistemielettronici.ParametriRicercaSistemiElettronici;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

public class SistemiElettronicoSearchObject extends AbstractSearchObject {
    public static final String TIPO_SISTEMA_PARAM_KEY = "tipoSistemaParam";

    private IVendingAnagraficaBD vendingAnagraficaBD;

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return Collections.emptyList();
    }

    @Override
    public List<?> getData(final String fieldSearch, String valueSearch) {
        final ParametriRicercaSistemiElettronici parametriRicerca = new ParametriRicercaSistemiElettronici();
        switch (fieldSearch) {
        case "codice":
            parametriRicerca.setCodice(valueSearch);
            break;
        case "descrizione":
            parametriRicerca.setDescrizione(valueSearch);
            break;
        default:
            throw new UnsupportedOperationException("ricerca per campo " + fieldSearch + " non suportato");
        }

        final Map<String, Object> parametri = searchPanel.getMapParameters();
        if (parametri.get(TIPO_SISTEMA_PARAM_KEY) != null) {
            parametriRicerca.setTipoSistemaElettronico((TipoSistemaElettronico) parametri.get(TIPO_SISTEMA_PARAM_KEY));
        }

        return vendingAnagraficaBD.ricercaSistemiElettronici(parametriRicerca);
    }

    /**
     * @return Returns the vendingAnagraficaBD.
     */
    public IVendingAnagraficaBD getVendingAnagraficaBD() {
        return vendingAnagraficaBD;
    }

    /**
     * @param vendingAnagraficaBD
     *            The vendingAnagraficaBD to set.
     */
    public void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }

}
