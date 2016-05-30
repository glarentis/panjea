package it.eurotn.panjea.manutenzioni.manager.areeinstallazioni;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ITableHeader;
import it.eurotn.panjea.anagrafica.util.parametriricerca.TableHeaderObject;
import it.eurotn.panjea.parametriricerca.domain.AbstractParametriRicerca;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;

@Entity
@Table(name = "para_ricerca_area_installazione")
public class ParametriRicercaAreeInstallazione extends AbstractParametriRicerca implements Serializable, ITableHeader {
    private static final long serialVersionUID = 6821579323933324074L;

    private Periodo periodo;

    @ManyToOne(optional = true)
    private EntitaLite entita;

    @ManyToOne(optional = true)
    private SedeEntita sedeEntita;

    /**
     * Costruttore.
     */
    public ParametriRicercaAreeInstallazione() {
        periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.ANNO_CORRENTE);
    }

    /**
     * @return Returns the entita.
     */
    public EntitaLite getEntita() {
        return entita;
    }

    @Override
    public List<TableHeaderObject> getHeaderValues() {
        return null;
    }

    /**
     * @return Returns the periodo.
     */
    public Periodo getPeriodo() {
        return periodo;
    }

    /**
     * @return Returns the sedeEntita.
     */
    public SedeEntita getSedeEntita() {
        return sedeEntita;
    }

    /**
     * @param entita
     *            The entita to set.
     */
    public void setEntita(EntitaLite entita) {
        this.entita = entita;
    }

    /**
     * @param periodo
     *            The periodo to set.
     */
    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    /**
     * @param sedeEntita
     *            The sedeEntita to set.
     */
    public void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

}
