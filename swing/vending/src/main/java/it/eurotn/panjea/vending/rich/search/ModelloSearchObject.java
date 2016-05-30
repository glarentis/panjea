package it.eurotn.panjea.vending.rich.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

public class ModelloSearchObject extends AbstractSearchObject {

    private static final String SEARCH_OBJECT_ID = "modelloSearchObject";

    private IVendingAnagraficaBD vendingAnagraficaBD;

    /**
     * Costruttore di default.
     */
    public ModelloSearchObject() {
        super(SEARCH_OBJECT_ID);
    }

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return new ArrayList<>();
    }

    @Override
    public List<?> getData(final String fieldSearch, String valueSearch) {
        List<Modello> modelli = vendingAnagraficaBD.caricaModelli();
        if (valueSearch == null) {
            return modelli;
        }
        final String valueSearchFinal = valueSearch.substring(0, valueSearch.length() - 1).toLowerCase();
        CollectionUtils.filter(modelli, new Predicate<Modello>() {

            @Override
            public boolean evaluate(Modello modello) {
                if ("codice".equals(fieldSearch)) {
                    return modello.getCodice().toLowerCase().startsWith(valueSearchFinal);
                }
                if ("descrizione".equals(fieldSearch)) {
                    return modello.getDescrizione().toLowerCase().startsWith(valueSearchFinal);
                }
                return false;
            }
        });
        return modelli;
    }

    /**
     * @param vendingAnagraficaBD
     *            the vendingAnagraficaBD to set
     */
    public void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }

}
