package it.eurotn.panjea.magazzino.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class DisponibilitaArticolo implements Serializable {

    private static final long serialVersionUID = 6128767510385155407L;

    private Double carico = 0.0;

    private Double fabbisogno = 0.0;

    private Double caricoTotale = 0.0;

    private Double fabbisognoTotale = 0.0;

    private Date dataConsegna;

    private int idArticolo;

    private String codiceDeposito;

    private int idDeposito;

    /**
     *
     * @param dispoDaAggiungere
     *            aggiunge una disponibilità
     */
    public void add(DisponibilitaArticolo dispoDaAggiungere) {
        fabbisogno += dispoDaAggiungere.getFabbisogno();
        carico += dispoDaAggiungere.getCarico();
        calcolaIncrementale(dispoDaAggiungere);
    }

    /**
     * Aggiunge i valori incementali di disponibilitàArticolo.
     *
     * @param dispOther
     *            altra disponibilita da aggingere
     */
    public void calcolaIncrementale(DisponibilitaArticolo dispOther) {
        fabbisognoTotale = fabbisogno + dispOther.getFabbisognoTotale();
        caricoTotale = carico + dispOther.getCaricoTotale();
    }

    /**
     * @return anno
     */
    public int getAnno() {
        if (dataConsegna == null) {
            return -1;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataConsegna);
        return cal.get(Calendar.YEAR);
    }

    /**
     * @return Returns the carico.
     */
    public Double getCarico() {
        return carico;
    }

    /**
     * @return Returns the caricoTotale.
     */
    public Double getCaricoTotale() {
        return caricoTotale;
    }

    /**
     * @return Returns the codiceDeposito.
     */
    public String getCodiceDeposito() {
        return codiceDeposito;
    }

    /**
     * @return Returns the dataConsegna.
     */
    public Date getDataConsegna() {
        return dataConsegna;
    }

    /**
     *
     * @param giacenza
     *            giacenza attuale
     * @return disponibilità dell'articolo
     */
    public double getDisponibilita(double giacenza) {
        return giacenza + getCaricoTotale() - getFabbisognoTotale();
    }

    /**
     * @return Returns the fabbisogno.
     */
    public Double getFabbisogno() {
        return fabbisogno;
    }

    /**
     * @return Returns the fabbisognoTotale.
     */
    public Double getFabbisognoTotale() {
        return fabbisognoTotale;
    }

    /**
     * @return Returns the idArticolo.
     */
    public int getIdArticolo() {
        return idArticolo;
    }

    /**
     * @return Returns the idDeposito.
     */
    public int getIdDeposito() {
        return idDeposito;
    }

    /**
     *
     * @return mese del movimento
     */
    public int getMese() {
        if (dataConsegna == null) {
            return -1;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataConsegna);
        return cal.get(Calendar.MONTH);
    }

    /**
     * @param carico
     *            The carico to set.
     */
    public void setCarico(Double carico) {
        this.carico = carico;
    }

    /**
     * @param caricoTotale
     *            The caricoTotale to set.
     */
    public void setCaricoTotale(Double caricoTotale) {
        this.caricoTotale = caricoTotale;
    }

    /**
     * @param codiceDeposito
     *            The codiceDeposito to set.
     */
    public void setCodiceDeposito(String codiceDeposito) {
        this.codiceDeposito = codiceDeposito;
    }

    /**
     * @param dataConsegna
     *            The dataConsegna to set.
     */
    public void setDataConsegna(Date dataConsegna) {
        this.dataConsegna = dataConsegna;
    }

    /**
     * @param fabbisogno
     *            The fabbisogno to set.
     */
    public void setFabbisogno(Double fabbisogno) {
        this.fabbisogno = fabbisogno;
    }

    /**
     * @param fabbisognoTotale
     *            The fabbisognoTotale to set.
     */
    public void setFabbisognoTotale(Double fabbisognoTotale) {
        this.fabbisognoTotale = fabbisognoTotale;
    }

    /**
     * @param idArticolo
     *            The idArticolo to set.
     */
    public void setIdArticolo(int idArticolo) {
        this.idArticolo = idArticolo;
    }

    /**
     * @param idDeposito
     *            The idDeposito to set.
     */
    public void setIdDeposito(int idDeposito) {
        this.idDeposito = idDeposito;
    }

}
