package it.eurotn.panjea.vending.manager.arearifornimento;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ITableHeader;
import it.eurotn.panjea.anagrafica.util.parametriricerca.TableHeaderObject;
import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.domain.Operatore;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.vending.domain.Distributore;

@Entity
@Table(name = "para_ricerca_area_rifornimento")
public class ParametriRicercaAreeRifornimento extends AbstractParametriRicerca implements Serializable, ITableHeader {

    private static final long serialVersionUID = -4746084916606869241L;

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
    public ParametriRicercaAreeRifornimento() {
        super();
        periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.ANNO_CORRENTE);
    }

    public Distributore getDistributore() {
        return distributore;
    }

    /**
     * @return the entita
     */
    public final EntitaLite getEntita() {
        return entita;
    }

    @Override
    public List<TableHeaderObject> getHeaderValues() {
        return Collections.emptyList();
    }

    public Installazione getInstallazione() {
        return installazione;
    }

    public Operatore getOperatore() {
        return operatore;
    }

    /**
     * @return the periodo
     */
    public final Periodo getPeriodo() {
        return periodo;
    }

    /**
     * @return the sedeEntita
     */
    public final SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    public void setDistributore(Distributore distributore) {
        this.distributore = distributore;
    }

    /**
     * @param entita
     *            the entita to set
     */
    public final void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    public void setInstallazione(Installazione installazione) {
        this.installazione = installazione;
    }

    public void setOperatore(Operatore operatore) {
        this.operatore = operatore;
    }

    /**
     * @param periodo
     *            the periodo to set
     */
    public final void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    /**
     * @param sedeEntita
     *            the sedeEntita to set
     */
    public final void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

}
