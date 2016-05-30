package it.eurotn.panjea.vending.rich.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.vending.domain.TipoComunicazione;
import it.eurotn.panjea.vending.rich.bd.IVendingAnagraficaBD;
import it.eurotn.rich.search.AbstractSearchObject;

public class TipoComunicazioneSearchObject extends AbstractSearchObject {

    private static final String SEARCH_OBJECT_ID = "tipoComunicazioneSearchObject";

    private IVendingAnagraficaBD vendingAnagraficaBD;

    /**
     * Costruttore di default.
     */
    public TipoComunicazioneSearchObject() {
        super(SEARCH_OBJECT_ID);
    }

    @Override
    public List<AbstractCommand> getCustomCommands() {
        return new ArrayList<>();
    }

    @Override
    public List<?> getData(final String fieldSearch, final String valueSearch) {
        List<TipoComunicazione> tipiComunicazione = vendingAnagraficaBD.caricaTipiComunicazione();
        if (valueSearch == null) {
            return tipiComunicazione;
        }

        final String valueSearchFinal = valueSearch.substring(0, valueSearch.length() - 1).toLowerCase();
        CollectionUtils.filter(tipiComunicazione, new Predicate<TipoComunicazione>() {

            @Override
            public boolean evaluate(TipoComunicazione tipoComunicazione) {
                switch (fieldSearch) {
                case "codice":
                    return tipoComunicazione.getCodice().toLowerCase().startsWith(valueSearchFinal);
                case "descrizione":
                    return tipoComunicazione.getDescrizione().toLowerCase().startsWith(valueSearchFinal);
                default:
                    return true;
                }
            }
        });
        return tipiComunicazione;
    }

    /**
     * @param vendingAnagraficaBD
     *            the vendingAnagraficaBD to set
     */
    public void setVendingAnagraficaBD(IVendingAnagraficaBD vendingAnagraficaBD) {
        this.vendingAnagraficaBD = vendingAnagraficaBD;
    }

}
