package it.eurotn.rich.binding.codice;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;

/**
 * PM creato per gestire anche la proprietà valoreProtocollo.
 *
 * @author fattazzo
 *
 */
public class CodiceDocumentoPM extends CodiceDocumento {

    private static final long serialVersionUID = -5264695912610166514L;

    private boolean protocolloPresente;
    private Integer valoreProtocollo;

    {
        protocolloPresente = false;
        valoreProtocollo = null;
    }

    /**
     * @return the valoreProtocollo
     */
    public Integer getValoreProtocollo() {
        return valoreProtocollo;
    }

    /**
     * @return the protocolloPresente
     */
    public boolean isProtocolloPresente() {
        return protocolloPresente;
    }

    /**
     * @param protocolloPresente
     *            the protocolloPresente to set
     */
    public void setProtocolloPresente(boolean protocolloPresente) {
        this.protocolloPresente = protocolloPresente;
    }

    /**
     * @param valoreProtocollo
     *            the valoreProtocollo to set
     */
    public void setValoreProtocollo(Integer valoreProtocollo) {
        this.valoreProtocollo = valoreProtocollo;
    }
}