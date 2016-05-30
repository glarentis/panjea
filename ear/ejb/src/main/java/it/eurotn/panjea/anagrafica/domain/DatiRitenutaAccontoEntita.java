/**
 *
 */
package it.eurotn.panjea.anagrafica.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.contabilita.domain.Prestazione;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author fattazzo
 *
 */
@Embeddable
public class DatiRitenutaAccontoEntita implements Serializable {

    private static final long serialVersionUID = 511255334568501571L;

    @ManyToOne(fetch = FetchType.LAZY)
    private CausaleRitenutaAcconto causaleRitenutaAcconto;

    private Double percFondoProfessionisti;

    @ManyToOne(fetch = FetchType.LAZY)
    private ContributoPrevidenziale contributoPrevidenziale;

    @ManyToOne(fetch = FetchType.LAZY)
    private Prestazione prestazione;

    {
        percFondoProfessionisti = 0.0;
    }

    /**
     * @return the causaleRitenutaAcconto
     */
    public CausaleRitenutaAcconto getCausaleRitenutaAcconto() {
        return causaleRitenutaAcconto;
    }

    /**
     * @return the contributoPrevidenziale
     */
    public ContributoPrevidenziale getContributoPrevidenziale() {
        return contributoPrevidenziale;
    }

    /**
     * @return the percFondoProfessionisti
     */
    public Double getPercFondoProfessionisti() {
        return PanjeaEJBUtil.roundPercentuale(percFondoProfessionisti);
    }

    /**
     * @return the prestazione
     */
    public Prestazione getPrestazione() {
        return prestazione;
    }

    /**
     * @param causaleRitenutaAcconto
     *            the causaleRitenutaAcconto to set
     */
    public void setCausaleRitenutaAcconto(CausaleRitenutaAcconto causaleRitenutaAcconto) {
        this.causaleRitenutaAcconto = causaleRitenutaAcconto;
    }

    /**
     * @param contributoPrevidenziale
     *            the contributoPrevidenziale to set
     */
    public void setContributoPrevidenziale(ContributoPrevidenziale contributoPrevidenziale) {
        this.contributoPrevidenziale = contributoPrevidenziale;
    }

    /**
     * @param percFondoProfessionisti
     *            the percFondoProfessionisti to set
     */
    public void setPercFondoProfessionisti(Double percFondoProfessionisti) {
        this.percFondoProfessionisti = percFondoProfessionisti;
    }

    /**
     * @param prestazione
     *            the prestazione to set
     */
    public void setPrestazione(Prestazione prestazione) {
        this.prestazione = prestazione;
    }

}
