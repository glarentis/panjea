package it.eurotn.panjea.anagrafica.documenti.service.exception;

import org.apache.commons.lang3.StringUtils;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.exception.PanjeaPropertyException;

/**
 * Eccezione sollevata in caso di Documento Duplicato.
 *
 * @author adriano
 * @version 1.0, 07/nov/07
 */
public class DocumentoDuplicateException extends Exception implements PanjeaPropertyException {

    private static final long serialVersionUID = 1L;

    /**
     * @uml.property name="documento"
     * @uml.associationEnd
     */
    private final Documento documento;

    /**
     * Costruttore.
     */
    public DocumentoDuplicateException() {
        super();
        documento = null;
    }

    /**
     * @param message
     *            message
     */
    public DocumentoDuplicateException(final String message) {
        this(message, (Documento) null);
    }

    /**
     * @param message
     *            message
     * @param documento
     *            documento
     */
    public DocumentoDuplicateException(final String message, final Documento documento) {
        super(message);
        this.documento = documento;
    }

    /**
     * @param message
     *            message
     * @param cause
     *            cause
     */
    public DocumentoDuplicateException(final String message, final Throwable cause) {
        this(message, cause, null);
    }

    /**
     * @param message
     *            message
     * @param cause
     *            cause
     * @param documento
     *            documento
     */
    public DocumentoDuplicateException(final String message, final Throwable cause, final Documento documento) {
        super(message, cause);
        this.documento = documento;
    }

    /**
     * @param cause
     *            cause
     */
    public DocumentoDuplicateException(final Throwable cause) {
        super(cause);
        documento = null;
    }

    /**
     * @return Returns the documento.
     * @uml.property name="documento"
     */
    public Documento getDocumento() {
        return documento;
    }

    @Override
    public String[] getPropertyVaules() {
        String codiceTipoDocumento = documento != null && documento.getTipoDocumento() != null
                && documento.getTipoDocumento().getCodice() != null ? documento.getTipoDocumento().getCodice() : "";
        String codiceDocumento = documento != null && !StringUtils.isEmpty(documento.getCodice().getCodice())
                ? documento.getCodice().getCodice() : getMessage();
        return new String[] { codiceTipoDocumento, codiceDocumento };
    }

}
