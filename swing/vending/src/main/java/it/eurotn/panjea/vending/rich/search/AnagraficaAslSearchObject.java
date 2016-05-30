package it.eurotn.panjea.vending.rich.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.AnagraficaAsl;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

public class AnagraficaAslSearchObject extends AbstractSearchObject {

    private static final String SEARCH_OBJECT_ID = "anagraficaAslSearchObject";

    private IVendingAnagraficaBD vendingAnagraficaBD;

    /**
     * Costruttore di default.
     */
    public AnagraficaAslSearchObject() {
        super(SEARCH_OBJECT_ID);
    }

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return new ArrayList<>();
    }

    @Override
    public List<?> getData(final String fieldSearch, String valueSearch) {
        List<AnagraficaAsl> anagrafiche = vendingAnagraficaBD.caricaAnagraficheAsl();
        if (valueSearch == null) {
            return anagrafiche;
        }
        final String valueSearchFinal = valueSearch.substring(0, valueSearch.length() - 1).toLowerCase();
        CollectionUtils.filter(anagrafiche, new Predicate<AnagraficaAsl>() {

            @Override
            public boolean evaluate(AnagraficaAsl anagraficaAsl) {
                if ("codice".equals(fieldSearch)) {
                    return anagraficaAsl.getCodice().toLowerCase().startsWith(valueSearchFinal);
                }
                if ("descrizione".equals(fieldSearch)) {
                    return anagraficaAsl.getDescrizione().toLowerCase().startsWith(valueSearchFinal);
                }
                return false;
            }
        });
        return anagrafiche;
    }

    /**
     * @param vendingAnagraficaBD
     *            the vendingAnagraficaBD to set
     */
    public void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }

}
