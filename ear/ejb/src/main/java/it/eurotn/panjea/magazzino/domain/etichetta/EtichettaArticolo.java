package it.eurotn.panjea.magazzino.domain.etichetta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.lotti.domain.Lotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

/**
 * Definisce la riga che serve per generare una etichetta articolo.
 *
 * @author Leonardo
 */
public class EtichettaArticolo implements Serializable {

    private static final long serialVersionUID = 3217348630237813985L;
    private ArticoloLite articolo;
    private Integer numeroCopiePerStampa;
    private BigDecimal prezzoNetto;
    // Prezzo calcolato dal calcolo prezzo. Ripristino quando devo rimuovere l'iva
    private BigDecimal prezzoNettoCalcolato;

    private BigDecimal percApplicazioneCodiceIva;

    // Uso solamente perchè nei form serve.
    private Integer id;

    private boolean aggiungiIva;

    private Integer numeroDecimali;

    private String esitoStampa;

    private Lotto lotto;

    private Date dataDocumento;

    private Double quantita;

    private String descrizione;

    private SedeEntita sedeEntita;

    /**
     * Init degli attributi principali ai valori di default.
     */
    {
        numeroDecimali = 0;
        numeroCopiePerStampa = 1;
        Random random = new Random();
        id = random.nextInt();
        quantita = 0.0;
        prezzoNetto = BigDecimal.ZERO;
    }

    /**
     * Costruttore.
     */
    public EtichettaArticolo() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EtichettaArticolo other = (EtichettaArticolo) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /**
     * @return the articoloLite
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return Returns the dataDocumento.
     */
    public Date getDataDocumento() {
        return dataDocumento;
    }

    /**
     * @return Returns the descrizione.
     */
    public String getDescrizione() {
        if (descrizione != null) {
            return descrizione;
        } else if (articolo != null) {
            return articolo.getDescrizione();
        }
        return "";
    }

    /**
     * @return the esitoStampa
     */
    public String getEsitoStampa() {
        return esitoStampa;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return Returns the lotto.
     */
    public Lotto getLotto() {
        return lotto;
    }

    /**
     * @return the numeroCopiePerStampa
     */
    public Integer getNumeroCopiePerStampa() {
        return numeroCopiePerStampa;
    }

    /**
     * @return the numeroDecimali
     */
    public Integer getNumeroDecimali() {
        return numeroDecimali;
    }

    /**
     * @return the percApplicazioneCodiceIva
     */
    public BigDecimal getPercApplicazioneCodiceIva() {
        return percApplicazioneCodiceIva;
    }

    /**
     * @return the prezzoNetto
     */
    public BigDecimal getPrezzoNetto() {
        return prezzoNetto;
    }

    /**
     * @return the prezzoNettoCalcolato
     */
    public BigDecimal getPrezzoNettoCalcolato() {
        return prezzoNettoCalcolato;
    }

    /**
     * @return Returns the quantita.
     */
    public Double getQuantita() {
        return quantita;
    }

    /**
     * @return Returns the sedeEntita.
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * @return the aggiungiIva
     */
    public boolean isAggiungiIva() {
        return aggiungiIva;
    }

    /**
     * @param aggiungiIva
     *            the aggiungiIva to set
     */
    public void setAggiungiIva(boolean aggiungiIva) {
        this.aggiungiIva = aggiungiIva;
    }

    /**
     * @param articolo
     *            the articoloLite to set
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param dataDocumento
     *            The dataDocumento to set.
     */
    public void setDataDocumento(Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    /**
     * @param descrizione
     *            The descrizione to set.
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param esitoStampa
     *            the esitoStampa to set
     */
    public void setEsitoStampa(String esitoStampa) {
        this.esitoStampa = esitoStampa;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param lotto
     *            The lotto to set.
     */
    public void setLotto(Lotto lotto) {
        this.lotto = lotto;
    }

    /**
     * @param numeroCopiePerStampa
     *            the numeroCopiePerStampa to set
     */
    public void setNumeroCopiePerStampa(Integer numeroCopiePerStampa) {
        this.numeroCopiePerStampa = numeroCopiePerStampa;
    }

    /**
     * @param numeroDecimali
     *            the numeroDecimali to set
     */
    public void setNumeroDecimali(Integer numeroDecimali) {
        this.numeroDecimali = numeroDecimali;
    }

    /**
     * @param percApplicazioneCodiceIva
     *            the percApplicazioneCodiceIva to set
     */
    public void setPercApplicazioneCodiceIva(BigDecimal percApplicazioneCodiceIva) {
        this.percApplicazioneCodiceIva = percApplicazioneCodiceIva;
    }

    /**
     * @param prezzoNetto
     *            the prezzoNetto to set
     */
    public void setPrezzoNetto(BigDecimal prezzoNetto) {
        this.prezzoNetto = prezzoNetto;
    }

    /**
     * @param prezzoNettoCalcolato
     *            the prezzoNettoCalcolato to set
     */
    public void setPrezzoNettoCalcolato(BigDecimal prezzoNettoCalcolato) {
        this.prezzoNettoCalcolato = prezzoNettoCalcolato;
    }

    /**
     * @param quantita
     *            The quantita to set.
     */
    public void setQuantita(Double quantita) {
        this.quantita = quantita;
    }

    /**
     * @param sedeEntita
     *            The sedeEntita to set.
     */
    public void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

}
