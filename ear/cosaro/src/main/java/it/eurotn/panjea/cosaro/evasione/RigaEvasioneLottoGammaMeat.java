package it.eurotn.panjea.cosaro.evasione;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class RigaEvasioneLottoGammaMeat {
    private String tipoRecord;
    private String numeroRiga;
    private String codiceArticoloSostitutivo;
    private String codiceArticolo;
    private Double quantita;

    private Double peso;

    private String confezioni;
    private String colli;

    private String codiceLotto;

    private Date dataScadenza;
    private String forzato;

    /**
     * @return Returns the codiceArticolo.
     */
    public String getCodiceArticolo() {
        return codiceArticolo;
    }

    /**
     * @return Returns the codiceArticoloSostitutivo.
     */
    public String getCodiceArticoloSostitutivo() {
        return codiceArticoloSostitutivo;
    }

    /**
     * @return Returns the codiceLotto.
     */
    public String getCodiceLotto() {
        return codiceLotto;
    }

    /**
     * @return Returns the colli.
     */
    public String getColli() {
        return colli;
    }

    /**
     * @return Returns the confezioni.
     */
    public String getConfezioni() {
        return confezioni;
    }

    /**
     * @return Returns the dataScadenza.
     */
    public Date getDataScadenza() {
        return dataScadenza;
    }

    /**
     * @return Returns the forzato.
     */
    public String getForzato() {
        return forzato;
    }

    /**
     * @return Returns the idRigaDistinta.
     */
    public int getIdRigaDistinta() {
        return Integer.parseInt(StringUtils.left(numeroRiga, numeroRiga.length() - 10));
    }

    /**
     *
     * @return numero documento
     */
    public String getNumeroOrdine() {
        return StringUtils.right(numeroRiga, 10);
    }

    /**
     * @return Returns the numeroRiga.
     */
    public String getNumeroRiga() {
        return numeroRiga;
    }

    /**
     * @return Returns the peso.
     */
    public Double getPeso() {
        return peso;
    }

    /**
     * @return Returns the quantita.
     */
    public Double getQuantita() {
        return quantita;
    }

    /**
     * @return Returns the tipoRecord.
     */
    public String getTipoRecord() {
        return tipoRecord;
    }

    /**
     * @param codiceArticolo
     *            The codiceArticolo to set.
     */
    public void setCodiceArticolo(String codiceArticolo) {
        this.codiceArticolo = codiceArticolo;
    }

    /**
     * @param codiceArticoloSostitutivo
     *            The codiceArticoloSostitutivo to set.
     */
    public void setCodiceArticoloSostitutivo(String codiceArticoloSostitutivo) {
        this.codiceArticoloSostitutivo = codiceArticoloSostitutivo;
    }

    /**
     * @param codiceLotto
     *            The codiceLotto to set.
     */
    public void setCodiceLotto(String codiceLotto) {
        this.codiceLotto = codiceLotto;
    }

    /**
     * @param colli
     *            The colli to set.
     */
    public void setColli(String colli) {
        this.colli = colli;
    }

    /**
     * @param confezioni
     *            The confezioni to set.
     */
    public void setConfezioni(String confezioni) {
        this.confezioni = confezioni;
    }

    /**
     * @param dataScadenza
     *            The dataScadenza to set.
     */
    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    /**
     * @param forzato
     *            The forzato to set.
     */
    public void setForzato(String forzato) {
        this.forzato = forzato;
    }

    /**
     * @param numeroRiga
     *            The numeroRiga to set.
     */
    public void setNumeroRiga(String numeroRiga) {
        this.numeroRiga = numeroRiga;
    }

    /**
     * @param peso
     *            The peso to set.
     */
    public void setPeso(Double peso) {
        this.peso = peso;
    }

    /**
     * @param quantita
     *            The quantita to set.
     */
    public void setQuantita(Double quantita) {
        this.quantita = quantita;
    }

    /**
     * @param tipoRecord
     *            The tipoRecord to set.
     */
    public void setTipoRecord(String tipoRecord) {
        this.tipoRecord = tipoRecord;
    }
}
