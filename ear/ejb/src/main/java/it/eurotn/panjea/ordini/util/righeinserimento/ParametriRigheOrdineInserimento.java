package it.eurotn.panjea.ordini.util.righeinserimento;

import java.io.Serializable;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaMovimentazione;

public class ParametriRigheOrdineInserimento implements Serializable {

    public enum TipoRicerca {
        ANALISI, ASSORTIMENTO, ULTIMI_ORDINI, ORDINE
    }

    private static final long serialVersionUID = 3627512059083593903L;

    private SedeEntita sedeEntita;

    private ParametriRicercaMovimentazione parametriRicercaMovimentazione;

    private int numeroOrdiniDaConsiderare;

    private Integer idAreaOrdine;

    private TipoRicerca tipoRicerca;

    {
        tipoRicerca = TipoRicerca.ULTIMI_ORDINI;
        numeroOrdiniDaConsiderare = 1;
    }

    /**
     * @return the idAreaOrdine
     */
    public Integer getIdAreaOrdine() {
        return idAreaOrdine;
    }

    /**
     * @return the numeroOrdiniDaConsiderare
     */
    public int getNumeroOrdiniDaConsiderare() {
        return numeroOrdiniDaConsiderare;
    }

    /**
     * @return the parametriRicercaMovimentazione
     */
    public ParametriRicercaMovimentazione getParametriRicercaMovimentazione() {
        return parametriRicercaMovimentazione;
    }

    /**
     * @return the sedeEntita
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @return the tipoRicerca
     */
    public TipoRicerca getTipoRicerca() {
        return tipoRicerca;
    }

    /**
     * @param idAreaOrdine
     *            the idAreaOrdine to set
     */
    public void setIdAreaOrdine(Integer idAreaOrdine) {
        this.idAreaOrdine = idAreaOrdine;
    }

    /**
     * @param numeroOrdiniDaConsiderare
     *            the numeroOrdiniDaConsiderare to set
     */
    public void setNumeroOrdiniDaConsiderare(int numeroOrdiniDaConsiderare) {
        this.numeroOrdiniDaConsiderare = numeroOrdiniDaConsiderare;
    }

    /**
     * @param parametriRicercaMovimentazione
     *            the parametriRicercaMovimentazione to set
     */
    public void setParametriRicercaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione) {
        this.parametriRicercaMovimentazione = parametriRicercaMovimentazione;
    }

    /**
     * @param sedeEntita
     *            the sedeEntita to set
     */
    public void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

    /**
     * @param tipoRicerca
     *            the tipoRicerca to set
     */
    public void setTipoRicerca(TipoRicerca tipoRicerca) {
        this.tipoRicerca = tipoRicerca;
    }
}
