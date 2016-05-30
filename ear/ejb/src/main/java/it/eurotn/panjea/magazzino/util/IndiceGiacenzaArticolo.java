package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

public class IndiceGiacenzaArticolo implements Serializable {
    private static final long serialVersionUID = 5708510740906950324L;
    private Double giacenzaMedia;
    private Double rotazione;
    private ArticoloLite articolo;
    private DepositoLite deposito;

    /**
     * Costruttore
     */
    public IndiceGiacenzaArticolo() {
    }

    /**
     * Costruttore
     *
     * @param giacenzaMedia
     *            giacenza media del periodo
     * @param rotazione
     *            rotazione del periodo
     * @param articolo
     *            articolo
     * @param deposito
     *            deposito
     */
    public IndiceGiacenzaArticolo(final Double giacenzaMedia, final Double rotazione, final ArticoloLite articolo,
            final DepositoLite deposito) {
        super();
        this.giacenzaMedia = giacenzaMedia;
        this.rotazione = rotazione;
        this.articolo = articolo;
        this.deposito = deposito;
    }

    /**
     * @return Returns the articolo.
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return Returns the deposito.
     */
    public DepositoLite getDeposito() {
        return deposito;
    }

    /**
     * @return Returns the giacenzaMedia.
     */
    public Double getGiacenzaMedia() {
        return giacenzaMedia;
    }

    /**
     * @return Returns the rotazione.
     */
    public Double getRotazione() {
        return rotazione;
    }

}
