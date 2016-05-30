package it.eurotn.panjea.vending.manager.evadts.rilevazioni;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.vending.domain.Distributore;

@Entity
@Table(name = "para_ricerca_rilevazioni_evadts")
public class ParametriRicercaRilevazioniEvaDts extends AbstractParametriRicerca {

    private static final long serialVersionUID = -1022662301359977334L;

    @Embedded
    private Periodo periodo;

    @ManyToOne(optional = true)
    private EntitaLite entita;

    @ManyToOne(optional = true)
    private SedeEntita sedeEntita;

    @ManyToOne(optional = true)
    private Distributore distributore;

    @ManyToOne(optional = true)
    private Installazione installazione;

    @ManyToOne(optional = true)
    private Operatore operatore;

    /**
     * Costruttore.
     */
    public ParametriRicercaRilevazioniEvaDts() {
        super();
        this.periodo = new Periodo();
        this.periodo.setTipoPeriodo(TipoPeriodo.ANNO_CORRENTE);
    }

    /**
     * @return the distributore
     */
    public Distributore getDistributore() {
        return distributore;
    }

    /**
     * @return the entita
     */
    public EntitaLite getEntita() {
        return entita;
    }

    /**
     * @return the installazione
     */
    public Installazione getInstallazione() {
        return installazione;
    }

    /**
     * @return the operatore
     */
    public Operatore getOperatore() {
        return operatore;
    }

    /**
     * @return the periodo
     */
    public Periodo getPeriodo() {
        return periodo;
    }

    /**
     * @return the sedeEntita
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @param distributore
     *            the distributore to set
     */
    public void setDistributore(Distributore distributore) {
        this.distributore = distributore;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param installazione
     *            the installazione to set
     */
    public void setInstallazione(Installazione installazione) {
        this.installazione = installazione;
    }

    /**
     * @param operatore
     *            the operatore to set
     */
    public void setOperatore(Operatore operatore) {
        this.operatore = operatore;
    }

    /**
     * @param periodo
     *            the periodo to set
     */
    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    /**
     * @param sedeEntita
     *            the sedeEntita to set
     */
    public void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

}
