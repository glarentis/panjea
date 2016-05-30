package it.eurotn.panjea.vending.rich.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.Gettone;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

public class GettoneSearchObject extends AbstractSearchObject {

    private static final String SEARCH_OBJECT_ID = "gettoneSearchObject";

    private IVendingAnagraficaBD vendingAnagraficaBD;

    /**
     * Costruttore di default.
     */
    public GettoneSearchObject() {
        super(SEARCH_OBJECT_ID);
    }

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return new ArrayList<>();
    }

    @Override
    public List<?> getData(final String fieldSearch, String valueSearch) {
        List<Gettone> gettoni = vendingAnagraficaBD.caricaGettoni();
        if (valueSearch == null) {
            return gettoni;
        }
        final String valueSearchFinal = valueSearch.substring(0, valueSearch.length() - 1).toLowerCase();
        CollectionUtils.filter(gettoni, new Predicate<Gettone>() {

            @Override
            public boolean evaluate(Gettone gettone) {
                if ("codice".equals(fieldSearch)) {
                    return gettone.getCodice().toLowerCase().startsWith(valueSearchFinal);
                }
                if ("descrizione".equals(fieldSearch)) {
                    return gettone.getDescrizione().toLowerCase().startsWith(valueSearchFinal);
                }
                return false;
            }
        });
        return gettoni;
    }

    /**
     * @param vendingAnagraficaBD
     *            the vendingAnagraficaBD to set
     */
    public void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }

}
