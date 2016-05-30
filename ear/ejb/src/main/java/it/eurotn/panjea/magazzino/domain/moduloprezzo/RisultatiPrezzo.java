package it.eurotn.panjea.magazzino.domain.moduloprezzo;

import java.math.BigDecimal;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import it.eurotn.panjea.magazzino.domain.Sconto;

/**
 * Contiene i risultati del calcolo del prezzo separati per quantità.<br>
 * Vengono utilizzati per contenere i risultati della sezione Prezzo,Sconto ed Incremento
 *
 * @author fattazzo
 *
 */
public class RisultatiPrezzo<T> extends TreeMap<Double, RisultatoPrezzo<T>> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(RisultatiPrezzo.class);

    /**
     * Costruttore di default.
     */
    public RisultatiPrezzo() {
        super(new QtaComparator());
    }

    /**
     * Ricerca nella mappa la quantità del primo scaglione trovato corrispondente ("fino a") e ne restituisce il valore.
     * 
     * @param quantita
     *            Quantità da ricercare
     * @return Valore relativo alla quantità ricercata
     */
    public RisultatoPrezzo<T> getRisultatoPrezzo(Double quantita) {
        LOGGER.debug("--> Enter getValue");

        if (quantita == null) {
            quantita = 0.0;
        }
        // se ho una quantita' negativa la considero come positiva per avere gli stessi scaglioni di prezzo
        // ed evitare di configurare listini per quantita' negative
        quantita = new Double(Math.abs(quantita));

        Entry<Double, RisultatoPrezzo<T>> entryTrovato = null;

        for (Entry<Double, RisultatoPrezzo<T>> prezzoEntry : this.entrySet()) {
            // Se il listino non è a scaglioni il limite dello scaglione è 0,
            // pertanto se la chiave è 0 devo accettare qualunque quantità.
            if (prezzoEntry.getValue().getValue() instanceof BigDecimal
                    && (prezzoEntry.getKey().doubleValue() == 0.0 || quantita.compareTo(prezzoEntry.getKey()) <= 0)) {
                entryTrovato = prezzoEntry;
                break;
            } else if (prezzoEntry.getValue().getValue() instanceof Sconto) {
                // ma se è uno sconto devo considerare che lo sconto va applicato da una certa soglia in poi e non fino
                // ad una certa soglia
                if (prezzoEntry.getKey().doubleValue() == 0.0 || quantita.compareTo(prezzoEntry.getKey()) >= 0) {
                    entryTrovato = prezzoEntry;
                }
            }
        }

        if (entryTrovato == null) {
            // se non trovo la quantità nella mappa restituisco il prezzo a 0
            LOGGER.debug("--> Exit getValue con qta 0, non trovata");
            return null;
        } else {
            LOGGER.debug("--> Exit getValue con qta " + entryTrovato.getValue().getValue());
            return entryTrovato.getValue();
        }
    }

    /**
     * Ricerca nella mappa la quantità dello scaglione "da" (invece che "fino a") più vicino alla quantità ricevuta e ne
     * restituisce il valore.
     * 
     * @param quantita
     *            la quantità di cui cercare il risultato prezzo
     * @return RisultatoPrezzo<T>
     */
    public RisultatoPrezzo<T> getRisultatoPrezzoContratto(Double quantita) {
        if (quantita == null) {
            quantita = 0.0;
        }
        // se ho una quantita' negativa la considero come positiva per avere gli stessi scaglioni di prezzo
        // ed evitare di configurare listini per quantita' negative
        quantita = new Double(Math.abs(quantita));

        Entry<Double, RisultatoPrezzo<T>> entryTrovato = null;

        for (Entry<Double, RisultatoPrezzo<T>> prezzoEntry : this.entrySet()) {
            // Se il listino non è a scaglioni il limite dello scaglione è 0,
            // pertanto se la chiave è 0 devo accettare qualunque quantità.
            if (prezzoEntry.getKey().doubleValue() == 0.0 || quantita.compareTo(prezzoEntry.getKey()) >= 0) {
                entryTrovato = prezzoEntry;
            }
        }

        if (entryTrovato == null) {
            // se non trovo la quantità nella mappa restituisco il prezzo a 0
            LOGGER.debug("--> Exit getValue con qta 0, non trovata");
            return null;
        } else {
            LOGGER.debug("--> Exit getValue con qta " + entryTrovato.getValue().getValue());
            return entryTrovato.getValue();
        }
    }
}
