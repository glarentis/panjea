package it.eurotn.panjea.anagrafica.service.exception;

import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita.TipoSede;
import it.eurotn.panjea.exception.PanjeaPropertyException;

/**
 * Exception per segnalare che non e' stato trovato il tipoSedeEntita richiesto.<br/>
 * Cerco il tipoSedeEntita per il {@link TipoSede} .
 *
 * @author Leonardo
 */
public class TipoSedeEntitaNonTrovataException extends Exception implements PanjeaPropertyException {

    private static final long serialVersionUID = -6777255867155474956L;
    /**
     * @uml.property name="tipoSede"
     * @uml.associationEnd
     */
    private TipoSede tipoSede = null;

    /**
     * 
     */
    public TipoSedeEntitaNonTrovataException() {
        super();
    }

    /**
     * Costruttore.
     * 
     * @param message
     *            messaggio
     */
    public TipoSedeEntitaNonTrovataException(final String message) {
        super(message);
    }

    /**
     * Costruttore.
     * 
     * @param message
     *            messaggio
     * @param cause
     *            causa
     */
    public TipoSedeEntitaNonTrovataException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruttore.
     * 
     * @param cause
     *            causa
     */
    public TipoSedeEntitaNonTrovataException(final Throwable cause) {
        super(cause);
    }

    @Override
    public String[] getPropertyVaules() {
        String[] values = new String[] { "" };
        if (tipoSede != null) {
            values = new String[] { tipoSede.name() };
        }
        return values;
    }

    /**
     * @return the tipoSede
     * @uml.property name="tipoSede"
     */
    public TipoSede getTipoSede() {
        return tipoSede;
    }

    /**
     * @param tipoSede
     *            the tipoSede to set
     * @uml.property name="tipoSede"
     */
    public void setTipoSede(TipoSede tipoSede) {
        this.tipoSede = tipoSede;
    }

}
