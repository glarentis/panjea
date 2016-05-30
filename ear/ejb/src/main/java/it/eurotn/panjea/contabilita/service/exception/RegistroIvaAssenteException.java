package it.eurotn.panjea.contabilita.service.exception;

import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.exception.PanjeaPropertyException;

/**
 * @author leonardo
 */
public class RegistroIvaAssenteException extends Exception implements PanjeaPropertyException {

    private static final long serialVersionUID = -8353481821627882789L;

    private TipoRegistro tipoRegistro;
    private Integer numeroRegistro;

    /**
     * Costruttore.
     *
     * @param message
     *            message
     */
    public RegistroIvaAssenteException(final String message) {
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
    public RegistroIvaAssenteException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruttore.
     *
     * @param cause
     *            cause
     */
    public RegistroIvaAssenteException(final Throwable cause) {
        super(cause);
    }

    /**
     * Costruttore.
     *
     * @param tipoRegistro
     *            tipoRegistro
     * @param numeroRegistro
     *            numeroRegistro
     */
    public RegistroIvaAssenteException(final TipoRegistro tipoRegistro, final Integer numeroRegistro) {
        super();
        this.tipoRegistro = tipoRegistro;
        this.numeroRegistro = numeroRegistro;
    }

    /**
     * @return numeroRegistro
     */
    public Integer getNumeroRegistro() {
        return numeroRegistro;
    }

    @Override
    public String[] getPropertyVaules() {
        return new String[] { "<" + (tipoRegistro != null ? tipoRegistro.name() : "<Sconosciuto>") + ">",
                (numeroRegistro != null ? numeroRegistro.toString() : "<Sconosciuto>") };
    }

    /**
     * @return TipoRegistro
     */
    public TipoRegistro getTipoRegistro() {
        return tipoRegistro;
    }

}
