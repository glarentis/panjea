package it.eurotn.panjea.magazzino.domain.moduloprezzo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import it.eurotn.panjea.magazzino.domain.Sconto;

/**
 * Contiene i valori del prezzo, incremento e sconti che sono stati calcolati al passaggio dei moduli. Ogni modulo
 * riceve i risultati del modulo precendente, esegue i suoi controlli per la determinazione del prezzo e restituisce i
 * risultati.
 *
 * @author fattazzo
 */
public class PoliticaPrezzo implements Serializable {

    private static final long serialVersionUID = -6861278207772141277L;

    private RisultatiPrezzo<BigDecimal> prezzi;

    private RisultatiPrezzo<Sconto> variazioni;

    private RisultatiPrezzo<BigDecimal> provvigioni;

    private boolean sconto1Bloccato;

    private boolean prezzoIvato;

    private boolean politicaScontiPresenti;

    /**
     * Costruttore di default.
     */
    public PoliticaPrezzo() {
        this.prezzi = new RisultatiPrezzo<BigDecimal>();
        this.variazioni = new RisultatiPrezzo<Sconto>();
        this.provvigioni = new RisultatiPrezzo<BigDecimal>();
        this.sconto1Bloccato = Boolean.FALSE;
        this.politicaScontiPresenti = Boolean.FALSE;
        this.prezzoIvato = Boolean.FALSE;
    }

    /**
     * @return the prezzi
     */
    public RisultatiPrezzo<BigDecimal> getPrezzi() {
        return prezzi;
    }

    /**
     * Restituisce il prezzo netto.
     * 
     * @param quantita
     *            qta per il calcolo del prezzo finale
     * @param percApplicazioneIva
     *            percApplicazioneIva
     * @return il prezzo finale calcolato per la qta richiesta. Considera la stategia prezzo e la stategia sconto
     */
    public BigDecimal getPrezzoNetto(double quantita, BigDecimal percApplicazioneIva) {
        RisultatoPrezzo<BigDecimal> prezzo = prezzi.getRisultatoPrezzo(quantita);
        RisultatoPrezzo<Sconto> variazione = variazioni.getRisultatoPrezzo(quantita);
        BigDecimal prezzoNetto = BigDecimal.ZERO;
        if (prezzo != null) {
            prezzoNetto = prezzo.getValue();
            if (isPrezzoIvato()) {
                // scorporo il prezzo e "aggiusto se serve l'imponibile"
                prezzoNetto = prezzo.getValue().divide(
                        BigDecimal.ONE.add(percApplicazioneIva.divide(new BigDecimal(100))), prezzo.getNumeroDecimali(),
                        RoundingMode.HALF_UP);

                // l'imposta va sempre arrotondata a 2 decimali
                BigDecimal imposta = prezzoNetto.multiply(percApplicazioneIva.divide(new BigDecimal(100))).setScale(2,
                        RoundingMode.HALF_UP);

                if (prezzoNetto.add(imposta).compareTo(prezzo.getValue()) != 0) {
                    prezzoNetto = prezzo.getValue().subtract(imposta);
                }
            }
            if (variazione != null && prezzo.getValue() != null) {
                prezzoNetto = variazione.getValue().applica(prezzoNetto, prezzo.getNumeroDecimali());
            }
        }
        return prezzoNetto;
    }

    /**
     * @return the provvigioni
     */
    public RisultatiPrezzo<BigDecimal> getProvvigioni() {
        return provvigioni;
    }

    /**
     * @return set degli scaglioni configurati sia per il prezzo che per lo sconto.<br/>
     *         <b>NB.</b>Uno scaglione portebbe avere sia il prezzo che lo sconto o solamente uno dei due.
     */
    public Set<Double> getScaglioni() {
        Set<Double> result = new TreeSet<Double>(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return o1.compareTo(o2);
            }
        });
        result.addAll(prezzi.keySet());
        result.addAll(variazioni.keySet());
        result.addAll(provvigioni.keySet());
        return result;
    }

    /**
     * @return the sconti
     */
    public RisultatiPrezzo<Sconto> getSconti() {
        return variazioni;
    }

    /**
     * @return true se non ho nessun risultato calcolato.
     */
    public boolean isEmpty() {
        return prezzi.isEmpty() && variazioni.isEmpty() && provvigioni.isEmpty();
    }

    /**
     * @return the politicaScontiPresenti
     */
    public boolean isPoliticaScontiPresenti() {
        return politicaScontiPresenti;
    }

    /**
     * @return the prezzoIvato
     */
    public boolean isPrezzoIvato() {
        return prezzoIvato;
    }

    /**
     * @return the sconto1Bloccato
     */
    public boolean isSconto1Bloccato() {
        return sconto1Bloccato;
    }

    /**
     * @param politicaScontiPresenti
     *            the politicaScontiPresenti to set
     */
    public void setPoliticaScontiPresenti(boolean politicaScontiPresenti) {
        this.politicaScontiPresenti = politicaScontiPresenti;
    }

    /**
     * @param prezzi
     *            the prezzi to set
     */
    public void setPrezzi(RisultatiPrezzo<BigDecimal> prezzi) {
        this.prezzi = prezzi;
    }

    /**
     * @param prezzoIvato
     *            the prezzoIvato to set
     */
    public void setPrezzoIvato(boolean prezzoIvato) {
        this.prezzoIvato = prezzoIvato;
    }

    /**
     * @param provvigioni
     *            the provvigioni to set
     */
    public void setProvvigioni(RisultatiPrezzo<BigDecimal> provvigioni) {
        this.provvigioni = provvigioni;
    }

    /**
     * @param sconti
     *            the sconti to set
     */
    public void setSconti(RisultatiPrezzo<Sconto> sconti) {
        this.variazioni = sconti;
    }

    /**
     * @param sconto1Bloccato
     *            the sconto1Bloccato to set
     */
    public void setSconto1Bloccato(boolean sconto1Bloccato) {
        this.sconto1Bloccato = sconto1Bloccato;
    }

    /**
     * @return numero di elementi di tutti i risultati (prezzo,sconto,incrementi)
     */
    public int size() {
        return prezzi.entrySet().size() + variazioni.entrySet().size() + provvigioni.entrySet().size();
    }
}
