package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.math.BigDecimal;

import it.eurotn.panjea.magazzino.manager.moduloprezzo.ListinoModuloPrezzoCalculator;

/**
 * Utilizzata per il calcolo del prezzo nel modulo {@link ListinoModuloPrezzoCalculator}<br>
 * . Per performance recupero solamente le informazioni che servono
 *
 * @author giangi
 * @version 1.0, 21/gen/2011
 *
 */
public class RigaListinoCalcolo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String listinoDescrizione;
    private String listinoCodice;
    private Integer numeroDecimaliPrezzo;
    private BigDecimal prezzo;
    private Double quantita;
    private boolean listinoIvato;

    /**
     * @return Returns the listinoCodice.
     */
    public String getListinoCodice() {
        return listinoCodice;
    }

    /**
     * @return Returns the listinoDescrizione.
     */
    public String getListinoDescrizione() {
        return listinoDescrizione;
    }

    /**
     * @return Returns the numeroDecimaliPrezzo.
     */
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return Returns the prezzo.
     */
    public BigDecimal getPrezzo() {
        return prezzo;
    }

    /**
     * @return Returns the quantita.
     */
    public Double getQuantita() {
        return quantita;
    }

    /**
     * @return the listinoIvato
     */
    public boolean isListinoIvato() {
        return listinoIvato;
    }

    /**
     * @param listinoCodice
     *            The listinoCodice to set.
     */
    public void setListinoCodice(String listinoCodice) {
        this.listinoCodice = listinoCodice;
    }

    /**
     * @param listinoDescrizione
     *            The listinoDescrizione to set.
     */
    public void setListinoDescrizione(String listinoDescrizione) {
        this.listinoDescrizione = listinoDescrizione;
    }

    /**
     * @param listinoIvato
     *            the listinoIvato to set
     */
    public void setListinoIvato(boolean listinoIvato) {
        this.listinoIvato = listinoIvato;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            The numeroDecimaliPrezzo to set.
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
    }

    /**
     * @param prezzo
     *            The prezzo to set.
     */
    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    /**
     * @param quantita
     *            The quantita to set.
     */
    public void setQuantita(Double quantita) {
        this.quantita = quantita;
    }
}
