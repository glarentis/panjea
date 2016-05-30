package it.eurotn.panjea.rich.bd;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaService;

/**
 * Carica le valute e le inserisce in cache.<br/>
 * Le valute vengono caricate non dal bd perchè la chiamata non può essere spostata fuori da un eventuale thread awt
 *
 * @author giangi
 * @version 1.0, 25/ago/2011
 *
 */
public class ValutaAziendaCache {
    public static final String BEAN_ID = "valutaCache";

    private static final Logger LOGGER = Logger.getLogger(ValutaAziendaCache.class);

    private AnagraficaService anagraficaService;

    private Map<String, ValutaAzienda> valuteCached;

    private ValutaAzienda valutaAziendale;

    /**
     * Costruttore.
     */
    public ValutaAziendaCache() {
        valuteCached = Collections.synchronizedMap(new HashMap<String, ValutaAzienda>());
    }

    /**
     * Carica la valuta richiesta. La valuta rimane in cache per un tempo pari a CACHED_TIME
     *
     * @param codiceValuta
     *            codiceValuta da caricare
     * @return valutaCaricata
     */
    public ValutaAzienda caricaValutaAzienda(String codiceValuta) {

        if (codiceValuta == null) {
            return null;
        }

        if (codiceValuta.isEmpty()) {
            return null;
        }

        if (codiceValuta.equals(caricaValutaAziendaCorrente().getCodiceValuta())) {
            return caricaValutaAziendaCorrente();
        }
        ValutaAzienda result = valuteCached.get(codiceValuta);

        if (result == null) {
            try {
                result = anagraficaService.caricaValutaAzienda(codiceValuta);
                valuteCached.put(codiceValuta, result);
            } catch (Exception e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("--> valuta " + codiceValuta + " non trovata in cache", e);
                }
                result = null;
            }
        }
        return result;
    }

    /**
     * Carica la valuta aziendale. Una volta caricata la valuta rimane in cache perennemente. Se vendono effettuate
     * modifiche far ripartire panjea.
     *
     * @return valuta aziendale.
     */
    public ValutaAzienda caricaValutaAziendaCorrente() {
        if (valutaAziendale == null) {
            valutaAziendale = anagraficaService.caricaValutaAziendaCorrente();
        }
        return valutaAziendale;
    }

    /**
     * @param anagraficaService
     *            The anagraficaService to set.
     */
    public void setAnagraficaService(AnagraficaService anagraficaService) {
        this.anagraficaService = anagraficaService;
    }

}
