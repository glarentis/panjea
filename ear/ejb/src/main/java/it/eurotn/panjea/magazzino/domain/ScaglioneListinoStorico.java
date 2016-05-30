package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import it.eurotn.entity.EntityBase;

/**
 * @author fattazzo
 *
 */
@Entity
@Table(name = "maga_scaglioni_listini_storico")
public class ScaglioneListinoStorico extends EntityBase {

    private static final long serialVersionUID = 2057513832721731606L;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date data;

    @Column(precision = 19, scale = 6)
    private BigDecimal prezzo;

    private int numeroDecimaliPrezzo;

    @Column(length = 700)
    private String note;

    @ManyToOne
    private ArticoloLite articolo;

    @ManyToOne
    private Listino listino;

    private Integer numeroVersione;

    {
        this.data = Calendar.getInstance().getTime();
    }

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the listino
     */
    public Listino getListino() {
        return listino;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @return the numeroDecimaliPrezzo
     */
    public int getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return the numeroVersione
     */
    public Integer getNumeroVersione() {
        return numeroVersione;
    }

    /**
     * @return the prezzo
     */
    public BigDecimal getPrezzo() {
        return prezzo;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @param listino
     *            the listino to set
     */
    public void setListino(Listino listino) {
        this.listino = listino;
    }

    /**
     * @param note
     *            the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    public void setNumeroDecimaliPrezzo(int numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
    }

    /**
     * @param numeroVersione
     *            the numeroVersione to set
     */
    public void setNumeroVersione(Integer numeroVersione) {
        this.numeroVersione = numeroVersione;
    }

    /**
     * @param prezzo
     *            the prezzo to set
     */
    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }
}
