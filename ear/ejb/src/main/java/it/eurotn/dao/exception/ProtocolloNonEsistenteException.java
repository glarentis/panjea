package it.eurotn.dao.exception;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.exception.PanjeaPropertyException;

/**
 * Eccezione per segnalare all'utente che non esiste un protocollo con il codice e anno scelti.
 *
 * @author Leonardo
 */
public class ProtocolloNonEsistenteException extends RuntimeException implements PanjeaPropertyException {

    private static final long serialVersionUID = 498245079502190692L;

    private String codiceProtocollo = null;
    private Integer anno = null;

    /**
     * Costruttore.
     *
     * @param message
     *            messaggio
     */
    public ProtocolloNonEsistenteException(final String message) {
        super(message);
    }

    /**
     * Costruttore.
     *
     * @param codiceProtocollo
     *            codice del protocollo
     * @param anno
     *            anno
     *
     */
    public ProtocolloNonEsistenteException(final String codiceProtocollo, final Integer anno) {
        super();
        this.codiceProtocollo = codiceProtocollo;
        this.anno = anno;
    }

    /**
     * Costruttore.
     *
     * @param message
     *            messaggio
     * @param cause
     *            causa
     */
    public ProtocolloNonEsistenteException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruttore.
     *
     * @param cause
     *            causa
     */
    public ProtocolloNonEsistenteException(final Throwable cause) {
        super(cause);
    }

    /**
     * @return the anno
     */
    public Integer getAnno() {
        return anno;
    }

    /**
     * @return the codiceProtocollo
     */
    public String getCodiceProtocollo() {
        return codiceProtocollo;
    }

    @Override
    public String[] getPropertyVaules() {
        return new String[] { StringUtils.defaultIfBlank(codiceProtocollo, "/"),
                anno != null ? String.valueOf(anno) : "/" };
    }

    /**
     * @param anno
     *            the anno to set
     */
    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    /**
     * @param codiceProtocollo
     *            the codiceProtocollo to set
     */
    public void setCodiceProtocollo(String codiceProtocollo) {
        this.codiceProtocollo = codiceProtocollo;
    }

}
