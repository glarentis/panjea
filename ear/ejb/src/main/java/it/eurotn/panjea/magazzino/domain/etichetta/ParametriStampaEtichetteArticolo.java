package it.eurotn.panjea.magazzino.domain.etichetta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.Listino;

/**
 * Parametri per la stampa delle etichette.<br>
 * Il listino, il parametro aggiungi Iva e la data con cui calcolare il prezzo e il nome del report per la stampa delle
 * etichette.
 *
 * @author Leonardo
 */
public class ParametriStampaEtichetteArticolo implements Serializable {

    private static final long serialVersionUID = 7590304708771239312L;

    public static final String FOLDER_REPORT_PATH = "Magazzino/Etichette";
    public static final String FOLDER_LOTTI_REPORT_PATH = "Magazzino/Etichette/Lotti";

    /**
     * path all'interno di JasperServer per la stampa del report.
     */
    private LayoutStampaEtichette reportName;

    private Date data;
    private Listino listino;
    private boolean aggiungiIva;
    private List<EtichettaArticolo> etichetteArticolo;
    private boolean gestioneLotti;
    private EntitaLite entita;

    /**
     * Init degli attributi principali ai valori di default.
     */
    {
        this.data = Calendar.getInstance().getTime();
        this.aggiungiIva = true;
        this.etichetteArticolo = new ArrayList<EtichettaArticolo>();
        this.gestioneLotti = false;
    }

    /**
     * Costruttore.
     */
    public ParametriStampaEtichetteArticolo() {
        super();
    }

    /**
     * @return the date
     */
    public Date getData() {
        return data;
    }

    /**
     * 
     * @return entita legata
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return the etichetteArticolo
     */
    public List<EtichettaArticolo> getEtichetteArticolo() {
        return etichetteArticolo;
    }

    /**
     * @return the listino
     */
    public Listino getListino() {
        return listino;
    }

    /**
     * @return the reportName
     */
    public LayoutStampaEtichette getReportName() {
        return reportName;
    }

    /**
     * @return path per il report della stampa etichette.
     */
    public String getReportPath() {
        return (gestioneLotti) ? FOLDER_LOTTI_REPORT_PATH : FOLDER_REPORT_PATH;
    }

    /**
     * @return the aggiungiIva
     */
    public boolean isAggiungiIva() {
        return aggiungiIva;
    }

    /**
     * @return the gestioneLotti
     */
    public boolean isGestioneLotti() {
        return gestioneLotti;
    }

    /**
     * @param aggiungiIva
     *            the aggiungiIva to set
     */
    public void setAggiungiIva(boolean aggiungiIva) {
        this.aggiungiIva = aggiungiIva;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setData(Date date) {
        this.data = date;
    }

    /**
     * 
     * @param entita
     *            entità legata.
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param etichetteArticolo
     *            the etichetteArticolo to set
     */
    public void setEtichetteArticolo(List<EtichettaArticolo> etichetteArticolo) {
        this.etichetteArticolo = etichetteArticolo;
    }

    /**
     * @param gestioneLotti
     *            the gestioneLotti to set
     */
    public void setGestioneLotti(boolean gestioneLotti) {
        this.gestioneLotti = gestioneLotti;
    }

    /**
     * @param listino
     *            the listino to set
     */
    public void setListino(Listino listino) {
        this.listino = listino;
    }

    /**
     * @param reportName
     *            the reportName to set
     */
    public void setReportName(LayoutStampaEtichette reportName) {
        this.reportName = reportName;
    }
}
