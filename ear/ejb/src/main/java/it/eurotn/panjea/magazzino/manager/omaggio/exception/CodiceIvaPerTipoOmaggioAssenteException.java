package it.eurotn.panjea.magazzino.manager.omaggio.exception;

import it.eurotn.panjea.exception.PanjeaPropertyException;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;

/**
 * @author leonardo
 *
 */
public class CodiceIvaPerTipoOmaggioAssenteException extends Exception implements PanjeaPropertyException {

    private static final long serialVersionUID = -2870900379393184902L;

    private TipoOmaggio tipoOmaggio;

    /**
     * Costruttore.
     */
    public CodiceIvaPerTipoOmaggioAssenteException() {
        super();
    }

    /**
     * Costruttore.
     *
     * @param message
     *            message
     */
    public CodiceIvaPerTipoOmaggioAssenteException(final String message) {
        super(message);
    }

    /**
     * Costruttore.
     *
     * @param message
     *            message
     * @param cause
     *            cause
     */
    public CodiceIvaPerTipoOmaggioAssenteException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruttore.
     *
     * @param cause
     *            cause
     */
    public CodiceIvaPerTipoOmaggioAssenteException(final Throwable cause) {
        super(cause);
    }

    /**
     * Costruttore.
     *
     * @param tipoOmaggio
     *            tipoOmaggio
     */
    public CodiceIvaPerTipoOmaggioAssenteException(final TipoOmaggio tipoOmaggio) {
        super();
        this.tipoOmaggio = tipoOmaggio;
    }

    @Override
    public String[] getPropertyVaules() {
        return new String[] { tipoOmaggio.name() };
    }

    /**
     * @return the tipoOmaggio
     */
    public TipoOmaggio getTipoOmaggio() {
        return tipoOmaggio;
    }

    /**
     * @param tipoOmaggio
     *            the tipoOmaggio to set
     */
    public void setTipoOmaggio(TipoOmaggio tipoOmaggio) {
        this.tipoOmaggio = tipoOmaggio;
    }

}
