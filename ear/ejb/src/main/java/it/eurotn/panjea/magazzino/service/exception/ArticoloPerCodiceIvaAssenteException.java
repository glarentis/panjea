package it.eurotn.panjea.magazzino.service.exception;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.exception.PanjeaPropertyException;

/**
 * @author leonardo
 */
public class ArticoloPerCodiceIvaAssenteException extends Exception implements PanjeaPropertyException {

    private static final long serialVersionUID = 8273054882577853391L;

    private CodiceIva codiceIva = null;

    /**
     * Costruttore.
     * 
     * @param codiceIva
     *            codiceIva
     */
    public ArticoloPerCodiceIvaAssenteException(final CodiceIva codiceIva) {
        super();
        this.codiceIva = codiceIva;
    }

    /**
     * @return the codiceIva
     */
    public CodiceIva getCodiceIva() {
        return codiceIva;
    }

    @Override
    public String[] getPropertyVaules() {
        return new String[] { codiceIva.getCodice() };
    }

    /**
     * @param codiceIva
     *            the codiceIva to set
     */
    public void setCodiceIva(CodiceIva codiceIva) {
        this.codiceIva = codiceIva;
    }

}
