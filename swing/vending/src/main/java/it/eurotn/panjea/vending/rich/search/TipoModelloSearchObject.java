package it.eurotn.panjea.vending.rich.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.TipoModello;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

public class TipoModelloSearchObject extends AbstractSearchObject {

    private static final String SEARCH_OBJECT_ID = "tipoModelloSearchObject";

    private IVendingAnagraficaBD vendingAnagraficaBD;

    /**
     * Costruttore di default.
     */
    public TipoModelloSearchObject() {
        super(SEARCH_OBJECT_ID);
    }

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return new ArrayList<>();
    }

    @Override
    public List<?> getData(final String fieldSearch, String valueSearch) {
        List<TipoModello> tipiModelli = vendingAnagraficaBD.caricaTipiModello();
        if (valueSearch == null) {
            return tipiModelli;
        }
        final String valueSearchFinal = valueSearch.substring(0, valueSearch.length() - 1).toLowerCase();
        CollectionUtils.filter(tipiModelli, new Predicate<TipoModello>() {

            @Override
            public boolean evaluate(TipoModello tipoModello) {
                if ("codice".equals(fieldSearch)) {
                    return tipoModello.getCodice().toLowerCase().startsWith(valueSearchFinal);
                }
                if ("descrizione".equals(fieldSearch)) {
                    return tipoModello.getDescrizione().toLowerCase().startsWith(valueSearchFinal);
                }
                return false;
            }
        });
        return tipiModelli;
    }

    /**
     * @param vendingAnagraficaBD
     *            the vendingAnagraficaBD to set
     */
    public void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }

}
