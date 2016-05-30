package it.eurotn.panjea.vending.rich.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.Cassa;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

public class CassaSearchObject extends AbstractSearchObject {

    private static final String SEARCH_OBJECT_ID = "cassaSearchObject";

    private IVendingAnagraficaBD vendingAnagraficaBD;

    /**
     * Costruttore di default.
     */
    public CassaSearchObject() {
        super(SEARCH_OBJECT_ID);
    }

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return new ArrayList<>();
    }

    @Override
    public List<?> getData(final String fieldSearch, String valueSearch) {
        List<Cassa> casse = vendingAnagraficaBD.ricercaCasse();
        if (valueSearch == null) {
            return casse;
        }
        final String valueSearchFinal = valueSearch.substring(0, valueSearch.length() - 1).toLowerCase();
        CollectionUtils.filter(casse, new Predicate<Cassa>() {

            @Override
            public boolean evaluate(Cassa cassa) {
                if ("codice".equals(fieldSearch)) {
                    return cassa.getCodice().toLowerCase().startsWith(valueSearchFinal);
                }
                if ("descrizione".equals(fieldSearch)) {
                    return cassa.getDescrizione().toLowerCase().startsWith(valueSearchFinal);
                }
                return false;
            }
        });
        return casse;
    }

    /**
     * @param vendingAnagraficaBD
     *            the vendingAnagraficaBD to set
     */
    public void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }

}
