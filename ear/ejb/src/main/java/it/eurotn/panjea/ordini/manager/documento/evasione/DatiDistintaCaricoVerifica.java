package it.eurotn.panjea.ordini.manager.documento.evasione;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class DatiDistintaCaricoVerifica implements Serializable {
    private Date dataInizioTrasporto;
    private int ordiniInevasi;
    private int ordiniInevasiConData;
    private int ordiniInProduzioneConData;
    private int ordiniInProduzione;
    private int ordiniEvasiConData;
    private int righeInevase;
    private int righeInevaseConData;
    private int righeInProduzione;
    private int righeInProduzioneConData;
    private int righeInProduzionePronteConData;
    private int righeEvase;

    /**
     * @param dataInizioTrasporto
     *            data inizio trasp
     */
    public DatiDistintaCaricoVerifica(final Date dataInizioTrasporto) {
        super();
        this.dataInizioTrasporto = dataInizioTrasporto;
    }

    /**
     * @return Returns the dataInizioTrasporto.
     */
    public Date getDataInizioTrasporto() {
        return dataInizioTrasporto;
    }

    /**
     * @return Returns the ordiniEvasiConData.
     */
    public int getOrdiniEvasiConData() {
        return ordiniEvasiConData;
    }

    /**
     * @return Returns the ordiniInevasi.
     */
    public int getOrdiniInevasi() {
        return ordiniInevasi;
    }

    /**
     * @return Returns the ordiniInevasiConData.
     */
    public int getOrdiniInevasiConData() {
        return ordiniInevasiConData;
    }

    /**
     * @return Returns the ordiniInProduzione.
     */
    public int getOrdiniInProduzione() {
        return ordiniInProduzione;
    }

    /**
     * @return Returns the ordiniInProduzioneConData.
     */
    public int getOrdiniInProduzioneConData() {
        return ordiniInProduzioneConData;
    }

    /**
     *
     * @return ordini inevasi
     */
    public int getOrdiniNonElaborati() {
        return ordiniInevasi - ordiniInProduzione;
    }

    /**
     *
     * @return ordiniNonElaboratiConData
     */
    public int getOrdiniNonElaboratiConData() {
        return ordiniInevasiConData - ordiniInProduzioneConData;
    }

    /**
     * @return Returns the righeEvase.
     */
    public int getRigheEvase() {
        return righeEvase;
    }

    /**
     * @return Returns the righeInevase.
     */
    public int getRigheInevase() {
        return righeInevase;
    }

    /**
     * @return Returns the righeInevaseConData.
     */
    public int getRigheInevaseConData() {
        return righeInevaseConData;
    }

    /**
     * @return Returns the righeInProduzione.
     */
    public int getRigheInProduzione() {
        return righeInProduzione;
    }

    /**
     * @return Returns the righeInProduzioneConData.
     */
    public int getRigheInProduzioneConData() {
        return righeInProduzioneConData;
    }

    /**
     * @return Returns the righeInProduzionePrinteConData.
     */
    public int getRigheInProduzionePronteConData() {
        return righeInProduzionePronteConData;
    }

    /**
     *
     * @return getRigheNonElaborate
     */
    public int getRigheNonElaborate() {
        return righeInevase - righeInProduzione;
    }

    /**
     *
     * @return getRigheNonElaborate
     */
    public int getRigheNonElaborateConData() {
        return righeInevaseConData - righeInProduzioneConData;
    }

    /**
     * @param ordiniEvasiConData
     *            The ordiniEvasiConData to set.
     */
    public void setOrdiniEvasiConData(int ordiniEvasiConData) {
        this.ordiniEvasiConData = ordiniEvasiConData;
    }

    /**
     * @param ordiniInevasi
     *            The ordiniInevasi to set.
     */
    public void setOrdiniInevasi(int ordiniInevasi) {
        this.ordiniInevasi = ordiniInevasi;
    }

    /**
     * @param ordiniInevasiConData
     *            The ordiniInevasiConData to set.
     */
    public void setOrdiniInevasiConData(int ordiniInevasiConData) {
        this.ordiniInevasiConData = ordiniInevasiConData;
    }

    /**
     * @param ordiniInProduzione
     *            The ordiniInProduzione to set.
     */
    public void setOrdiniInProduzione(int ordiniInProduzione) {
        this.ordiniInProduzione = ordiniInProduzione;
    }

    /**
     * @param ordiniInProduzioneConData
     *            The ordiniInProduzioneConData to set.
     */
    public void setOrdiniInProduzioneConData(int ordiniInProduzioneConData) {
        this.ordiniInProduzioneConData = ordiniInProduzioneConData;
    }

    /**
     * @param righeEvase
     *            The righeEvase to set.
     */
    public void setRigheEvase(int righeEvase) {
        this.righeEvase = righeEvase;
    }

    /**
     * @param righeInevase
     *            The righeInevase to set.
     */
    public void setRigheInevase(int righeInevase) {
        this.righeInevase = righeInevase;
    }

    /**
     * @param righeInevaseConData
     *            The righeInevaseConData to set.
     */
    public void setRigheInevaseConData(int righeInevaseConData) {
        this.righeInevaseConData = righeInevaseConData;
    }

    /**
     * @param righeInProduzione
     *            The righeInProduzione to set.
     */
    public void setRigheInProduzione(int righeInProduzione) {
        this.righeInProduzione = righeInProduzione;
    }

    /**
     * @param righeInProduzioneConData
     *            The righeInProduzioneConData to set.
     */
    public void setRigheInProduzioneConData(int righeInProduzioneConData) {
        this.righeInProduzioneConData = righeInProduzioneConData;
    }

    /**
     * @param righeInProduzionePrinteConData
     *            The righeInProduzionePrinteConData to set.
     */
    public void setRigheInProduzionePronteConData(int righeInProduzionePrinteConData) {
        this.righeInProduzionePronteConData = righeInProduzionePrinteConData;
    }
}
