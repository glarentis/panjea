package it.eurotn.panjea.magazzino.manager.listinoprezzi;

import java.io.Serializable;
import java.math.BigDecimal;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Sconto;

public class ListinoPrezziDTO implements Serializable {
    private static final long serialVersionUID = 1398096115101640164L;

    private ArticoloLite articolo;
    private BigDecimal prezzo;
    private Sconto sconto;
    private int numeroDecimaliPrezzo;
    private BigDecimal prezzoNetto;
    private BigDecimal prezzoIvato;

    /**
     * @return Returns the articolo.
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return Returns the numeroDecimaliPrezzo.
     */
    public int getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return Returns the prezzo.
     */
    public BigDecimal getPrezzo() {
        return prezzo;
    }

    /**
     * @return Returns the prezzoIvato.
     */
    public BigDecimal getPrezzoIvato() {
        if (prezzoIvato == null && getPrezzoNetto() != null) {
            BigDecimal percApplicazioneIva = articolo.getCodiceIva().getPercApplicazione();
            prezzoIvato = getPrezzoNetto().add(getPrezzoNetto().multiply(percApplicazioneIva).divide(Importo.HUNDRED));
        }
        return prezzoIvato;
    }

    /**
     * @return Returns the prezzoNetto.
     */
    public BigDecimal getPrezzoNetto() {
        if (sconto == null) {
            return prezzo;
        }
        if (prezzoNetto == null && prezzo != null) {
            prezzoNetto = sconto.applica(prezzo, numeroDecimaliPrezzo);
        }
        return prezzoNetto;
    }

    /**
     * @return Returns the sconto.
     */
    public Sconto getSconto() {
        return sconto;
    }

    /**
     * @param articolo
     *            The articolo to set.
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            The numeroDecimaliPrezzo to set.
     */
    public void setNumeroDecimaliPrezzo(int numeroDecimaliPrezzo) {
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
     * @param sconto
     *            The sconto to set.
     */
    public void setSconto(Sconto sconto) {
        this.sconto = sconto;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ListinoPrezziDTO [articolo=" + articolo + ", prezzo=" + prezzo + ", sconto=" + sconto
                + ", numeroDecimaliPrezzo=" + numeroDecimaliPrezzo + ", prezzoNetto=" + prezzoNetto + ", prezzoIvato="
                + prezzoIvato + "]";
    }
}
