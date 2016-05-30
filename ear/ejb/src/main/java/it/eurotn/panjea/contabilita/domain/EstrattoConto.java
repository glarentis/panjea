/**
 *
 */
package it.eurotn.panjea.contabilita.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConto;

/**
 * Contiene i dati di un estratto conto.
 *
 * @author Leonardo
 */
public class EstrattoConto implements Serializable {

    private static final long serialVersionUID = -2985001697013459890L;
    /**
     * @uml.property name="saldoPrecedente"
     */
    private BigDecimal saldoPrecedente = BigDecimal.ZERO;
    /**
     * @uml.property name="saldoFinale"
     */
    private BigDecimal saldoFinale = BigDecimal.ZERO;
    /**
     * @uml.property name="righeEstrattoConto"
     */
    private List<RigaContabileEstrattoConto> righeEstrattoConto = null;

    private ParametriRicercaEstrattoConto parametriRicerca;

    /**
     * Costruttore.
     *
     */
    public EstrattoConto() {
    }

    /**
     * @return the parametriRicerca
     */
    public ParametriRicercaEstrattoConto getParametriRicerca() {
        return parametriRicerca;
    }

    /**
     * @return righeEstrattoConto
     * @uml.property name="righeEstrattoConto"
     */
    public List<RigaContabileEstrattoConto> getRigheEstrattoConto() {
        return righeEstrattoConto;
    }

    /**
     * @return Returns the saldoFinale.
     * @uml.property name="saldoFinale"
     */
    public BigDecimal getSaldoFinale() {
        return saldoFinale;
    }

    /**
     *
     * @return saldo precedente
     */
    public BigDecimal getSaldoPeriodo() {
        return saldoFinale.subtract(saldoPrecedente);
    }

    /**
     * @return the saldoPrecedente
     * @uml.property name="saldoPrecedente"
     */
    public BigDecimal getSaldoPrecedente() {
        return saldoPrecedente;
    }

    /**
     * @param parametriRicerca
     *            the parametriRicerca to set
     */
    public void setParametriRicerca(ParametriRicercaEstrattoConto parametriRicerca) {
        this.parametriRicerca = parametriRicerca;
    }

    /**
     * @param righeEstrattoConto
     *            the righeEstrattoConto to set
     * @uml.property name="righeEstrattoConto"
     */
    public void setRigheEstrattoConto(List<RigaContabileEstrattoConto> righeEstrattoConto) {
        this.righeEstrattoConto = righeEstrattoConto;
    }

    /**
     * @param saldoFinale
     *            The saldoFinale to set.
     * @uml.property name="saldoFinale"
     */
    public void setSaldoFinale(BigDecimal saldoFinale) {
        this.saldoFinale = saldoFinale;
    }

    /**
     * @param saldoPrecedente
     *            the saldoPrecedente to set
     * @uml.property name="saldoPrecedente"
     */
    public void setSaldoPrecedente(BigDecimal saldoPrecedente) {
        this.saldoPrecedente = saldoPrecedente;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SALDO PRECEDENTE:");
        sb.append(saldoPrecedente);
        sb.append("\nSALDO FINALE:");
        sb.append(saldoFinale);
        sb.append("\nRIGHE ESTRATTO CONTO");
        sb.append("\n");
        sb.append(righeEstrattoConto);
        return sb.toString();
    }

}
