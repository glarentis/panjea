package it.eurotn.panjea.ordini.domain.documento.evasione;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;

@Embeddable
public class DatiEvasioneDocumento implements Serializable {

    private static final long serialVersionUID = 5303935621155393099L;

    /**
     * @uml.property name="tipoAreaEvasione"
     * @uml.associationEnd
     */
    @ManyToOne
    private TipoAreaMagazzino tipoAreaEvasione;

    @ManyToOne
    protected CodicePagamento codicePagamento;

    @ManyToOne(optional = true)
    private DepositoLite depositoDestinazione;

    private boolean contoTerzi;

    /**
     * @return the codicePagamento
     */
    public CodicePagamento getCodicePagamento() {
        return codicePagamento;
    }

    /**
     * @return Returns the depositoDestinazione.
     */
    public DepositoLite getDepositoDestinazione() {
        return depositoDestinazione;
    }

    /**
     * @return the tipoAreaEvasione
     */
    public TipoAreaMagazzino getTipoAreaEvasione() {
        return tipoAreaEvasione;
    }

    /**
     * @return Returns the contoTerzi.
     */
    public boolean isContoTerzi() {
        return contoTerzi;
    }

    /**
     * @param codicePagamento
     *            the codicePagamento to set
     */
    public void setCodicePagamento(CodicePagamento codicePagamento) {
        this.codicePagamento = codicePagamento;
    }

    /**
     *
     * @param contoTerzi
     *            contoTerzi to set
     */
    public void setContoTerzi(boolean contoTerzi) {
        this.contoTerzi = contoTerzi;

    }

    /**
     * @param depositoDestinazione
     *            The depositoDestinazione to set.
     */
    public void setDepositoDestinazione(DepositoLite depositoDestinazione) {
        this.depositoDestinazione = depositoDestinazione;
    }

    /**
     * @param tipoAreaEvasione
     *            the tipoAreaEvasione to set
     */
    public void setTipoAreaEvasione(TipoAreaMagazzino tipoAreaEvasione) {
        this.tipoAreaEvasione = tipoAreaEvasione;
        if (tipoAreaEvasione != null) {
            this.depositoDestinazione = tipoAreaEvasione.getDepositoDestinazione();
        }
    }
}
