package it.eurotn.panjea.contabilita.service.exception;

import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.exception.PanjeaPropertyException;

/**
 * Eccezione rilanciata quando ContiBase non risultano completi.
 *
 * @author adriano
 * @version 1.0, 29/ago/07
 */
public class ContiBaseException extends Exception implements PanjeaPropertyException {

    private static final long serialVersionUID = -7580638255799753036L;

    private final ETipoContoBase tipoContoBase;

    /**
     * @param tipoContoBase
     *            tipo del conto mancante
     */
    public ContiBaseException(final ETipoContoBase tipoContoBase) {
        super();
        this.tipoContoBase = tipoContoBase;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ContiBaseException)) {
            return false;
        }
        ContiBaseException conti2 = (ContiBaseException) obj;
        return this.getTipoContoBase().equals(conti2.getTipoContoBase());
    }

    @Override
    public String[] getPropertyVaules() {
        String[] values = new String[] { "<Sconosciuto>" };
        if (tipoContoBase != null) {
            values = new String[] { "<" + tipoContoBase.name() + ">" };
        }
        return values;
    }

    /**
     * @return tipoContoBase mancante
     */
    public ETipoContoBase getTipoContoBase() {
        return tipoContoBase;
    }

    @Override
    public int hashCode() {
        if (tipoContoBase != null) {
            String hashStr = this.getClass().getName() + ":" + tipoContoBase.name();
            return hashStr.hashCode();
        }
        return super.hashCode();
    }
}
