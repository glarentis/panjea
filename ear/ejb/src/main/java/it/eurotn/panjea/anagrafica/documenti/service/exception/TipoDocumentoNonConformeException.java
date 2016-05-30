/**
 *
 */
package it.eurotn.panjea.anagrafica.documenti.service.exception;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.exception.PanjeaPropertyException;

/**
 * @author Leonardo
 */
public class TipoDocumentoNonConformeException extends Exception implements PanjeaPropertyException {

    private static final long serialVersionUID = -1941671153062329462L;

    /**
     * @uml.property name="tipoDocumento"
     * @uml.associationEnd
     */
    private TipoDocumento tipoDocumento = null;

    /**
     * @param tipoDocumento
     *            tipo documento
     */
    public TipoDocumentoNonConformeException(final TipoDocumento tipoDocumento) {
        super();
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @param tipoDocumento
     *            tipo documento
     * @param message
     *            messaggio
     */
    public TipoDocumentoNonConformeException(final TipoDocumento tipoDocumento, final String message) {
        super(message);
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @param tipoDocumento
     *            tipo documento
     * @param message
     *            messaggio
     * @param cause
     *            causa
     */
    public TipoDocumentoNonConformeException(final TipoDocumento tipoDocumento, final String message,
            final Throwable cause) {
        super(message, cause);
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @param tipoDocumento
     *            tipo documento
     * @param cause
     *            causa
     */
    public TipoDocumentoNonConformeException(final TipoDocumento tipoDocumento, final Throwable cause) {
        super(cause);
        this.tipoDocumento = tipoDocumento;
    }

    @Override
    public String[] getPropertyVaules() {
        String[] values = new String[] { "" };
        if (tipoDocumento != null) {
            values = new String[] { tipoDocumento.getCodice() + " - " + tipoDocumento.getDescrizione() };
        }
        return values;
    }

    /**
     * @return the tipoDocumento
     * @uml.property name="tipoDocumento"
     */
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

}
