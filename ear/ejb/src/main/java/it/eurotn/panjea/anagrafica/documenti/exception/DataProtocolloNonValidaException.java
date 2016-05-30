package it.eurotn.panjea.anagrafica.documenti.exception;

import java.util.Date;

import it.eurotn.panjea.protocolli.domain.Protocollo;

public class DataProtocolloNonValidaException extends RuntimeException {

    private static final long serialVersionUID = -7827373176411942618L;

    private Date data;
    private Integer numeroProtocollo;

    private Date dataDocumento;
    private String numeroDocumento;
    private Integer valoreProtocolloDocumento;
    private Protocollo protocolloDocumento;
    private Date dataRegistrazioneArea;

    /**
     * Costruttore.
     *
     * @param data
     *            data
     * @param numeroProtocollo
     *            numero protocollo
     * @param dataDocumento
     *            data documento
     * @param numeroDocumento
     *            numero documento
     * @param valoreProtocolloDocumento
     *            valore protocollo documento
     * @param codiceProtocollo
     *            codice protocollo
     * @param descrizioneProtocollo
     *            descrizione protocollo
     */
    public DataProtocolloNonValidaException(final Date data, final Integer numeroProtocollo, final Date dataDocumento,
            final String numeroDocumento, final Integer valoreProtocolloDocumento, final String codiceProtocollo,
            final String descrizioneProtocollo, final Date dataRegistrazioneArea) {
        super();
        this.data = data;
        this.numeroProtocollo = numeroProtocollo;
        this.dataDocumento = dataDocumento;
        this.numeroDocumento = numeroDocumento;
        this.valoreProtocolloDocumento = valoreProtocolloDocumento;

        this.protocolloDocumento = new Protocollo();
        protocolloDocumento.setCodice(codiceProtocollo);
        protocolloDocumento.setDescrizione(descrizioneProtocollo);

        this.dataRegistrazioneArea = dataRegistrazioneArea;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @return the dataDocumento
     */
    public Date getDataDocumento() {
        return dataDocumento;
    }

    /**
     * @return the dataRegistrazioneArea
     */
    public Date getDataRegistrazioneArea() {
        return dataRegistrazioneArea;
    }

    /**
     * @return the numeroDocumento
     */
    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * @return the numeroProtocollo
     */
    public Integer getNumeroProtocollo() {
        return numeroProtocollo;
    }

    /**
     * @return the Protocollo
     */
    public Protocollo getProtocolloDocumento() {
        return protocolloDocumento;
    }

    /**
     * @return the valoreProtocolloDocumento
     */
    public Integer getValoreProtocolloDocumento() {
        return valoreProtocolloDocumento;
    }

}
