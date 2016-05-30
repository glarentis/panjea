package it.eurotn.panjea.mrp.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;

public class ParametriMrpRisultato implements Serializable {

    private static final long serialVersionUID = 9017967288915971261L;

    private Date dataInizio;
    private Integer numBucket;
    private AreaOrdine areaOrdine;
    private boolean evidenziaOrdine;

    private boolean escludiOrdinati;
    private boolean eseguiCalcoloSuApertura;

    /**
     * Costruttore.
     */
    public ParametriMrpRisultato() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        setDataInizio(today.getTime());

        areaOrdine = new AreaOrdine();
        escludiOrdinati = true;
    }

    /**
     * @return Returns the areaOrdine.
     */
    public AreaOrdine getAreaOrdine() {
        return areaOrdine;
    }

    /**
     * @return Returns the dataInizio.
     */
    public Date getDataInizio() {
        return dataInizio;
    }

    /**
     * @return restituisce l'id area da filtrare o null se non è impostata l'area o non ho impostato
     *         il tipo ricerca FILTRA.
     */
    public Integer getIdAreaDaFiltrare() {
        return getAreaOrdine().getId();
    }

    /**
     * @return Returns the numBucket.
     */
    public Integer getNumBucket() {
        return numBucket;
    }

    /**
     * @return the escludiOrdinati
     */
    public boolean isEscludiOrdinati() {
        return escludiOrdinati;
    }

    /**
     * @return Returns the eseguiCalcoloSuApertura.
     */
    public boolean isEseguiCalcoloSuApertura() {
        return eseguiCalcoloSuApertura;
    }

    /**
     * @return Returns the evidenziaOrdine.
     */
    public boolean isEvidenziaOrdine() {
        return evidenziaOrdine;
    }

    /**
     * @param areaOrdine
     *            The areaOrdine to set.
     */
    public void setAreaOrdine(AreaOrdine areaOrdine) {
        if (areaOrdine == null) {
            areaOrdine = new AreaOrdine();
        }
        this.areaOrdine = areaOrdine;
    }

    /**
     * @param dataInizio
     *            The dataInizio to set.
     */
    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
        if (dataInizio != null) {
            DateTime today = new DateTime();
            DateTime dateTime = new DateTime(dataInizio.getTime());
            int days = Days.daysBetween(dateTime, today).getDays();
            numBucket = days;
        }
    }

    /**
     * @param escludiOrdinati
     *            the escludiOrdinati to set
     */
    public void setEscludiOrdinati(boolean escludiOrdinati) {
        this.escludiOrdinati = escludiOrdinati;
    }

    /**
     * @param eseguiCalcoloSuApertura
     *            The eseguiCalcoloSuApertura to set.
     */
    public void setEseguiCalcoloSuApertura(boolean eseguiCalcoloSuApertura) {
        this.eseguiCalcoloSuApertura = eseguiCalcoloSuApertura;
    }

    /**
     * @param evidenziaOrdine
     *            The evidenziaOrdine to set.
     */
    public void setEvidenziaOrdine(boolean evidenziaOrdine) {
        this.evidenziaOrdine = evidenziaOrdine;
    }

    /**
     * @param numBucket
     *            The numBucket to set.
     */
    public void setNumBucket(Integer numBucket) {
        this.numBucket = numBucket;
    }

}